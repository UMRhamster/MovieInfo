package com.whut.umrhamster.movieinfo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.activity.MovieDetailActivity;
import com.whut.umrhamster.movieinfo.adapter.HotMovieAdapter;
import com.whut.umrhamster.movieinfo.model.Movie;
import com.whut.umrhamster.movieinfo.util.HotMovieUtil;
import com.whut.umrhamster.movieinfo.util.HttpUtil;
import com.whut.umrhamster.movieinfo.util.MovieUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/7/12.
 */

public class BoxMovieFragment extends Fragment {
    View rootView;

    private boolean isLoading = false; //用于控制上拉加载
    private int lastVisibleItem;  //
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Movie> movieList;
    private HotMovieAdapter adapter;

    private List<Movie> movieListTemp; //临时容器，主要用于解决swiperefreshlayout下拉后再上拉崩溃问题，以及避免刷新时的空白问题

    private Handler handler = new Handler();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (rootView != null){
            outState.putInt("y",recyclerView.getScrollY());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null){
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_movie_box,container,false);
        initView(rootView);
        refreshLayout.setRefreshing(true);   //进入是显示刷新图标
        initData();
        initEvent();
        return rootView;
    }
    private void initView(View view){
        refreshLayout = view.findViewById(R.id.movie_box_srl);
        recyclerView = view.findViewById(R.id.movie_box_rv);
        movieList = new ArrayList<>();
        movieListTemp = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HotMovieAdapter(getActivity(),movieList,1);   //类型1 为带有排行
        adapter.setOnItemClickListener(new HotMovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("movie",movieList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void initData(){
//        movieList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String hotJson = HttpUtil.getBoxMovie();
                if (hotJson == null){   //返回为空，一般情况为被封IP
                    return;
                }
                List<String> idList = HotMovieUtil.getBoxMovie(hotJson);
                int count = 0;
                for (String id : idList){
                    String movieJson = HttpUtil.getMovieById(id);
                    movieList.add(MovieUtil.Json2Movie(movieJson));
                    if (++count%4 == 0 || count == idList.size()){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                    if (count == 4 || (idList.size() < 4 && count == idList.size())){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.setRefreshing(false);//取消下拉刷新图标
                            }
                        });
                    }
                }
            }
        }).start();
    }
    //事件处理
    private void initEvent(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (lastVisibleItem +1== adapter.getItemCount() && !isLoading && newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    //当最后一个条目可视，当前不处于加载状态，并且处于手指拖拽状态时
                    adapter.changeShowStatus(0); //提示：继续上拉加载更多
                }
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING && !isLoading && lastVisibleItem+1==adapter.getItemCount()){
                    //当最后一个条目可视，当前不处于加载状态，并且不处于手指拖拽状态时
                    adapter.changeShowStatus(1);  //提示：正在加载
                    isLoading = true;
                    //进行加载处理
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //下拉事件
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String hotJson = HttpUtil.getBoxMovie();
                                if (hotJson == null){   //返回为空，一般情况为被封IP
                                    return;
                                }
                                List<String> idList = HotMovieUtil.getHotMovies(hotJson); //解析出id
                                int count = 0;
                                for (String id : idList){
                                    String movieJson = HttpUtil.getMovieById(id);
                                    count++;
                                    if (count <= 4 ){  //前四条数据放入临时容器
                                        movieListTemp.add(MovieUtil.Json2Movie(movieJson));
                                        if (count == 4 || count == idList.size()){  //放满4条，或数据本身不足4条，已经到达最后
                                            movieList.clear();                //将原来容器的数据清除
                                            movieList.addAll(movieListTemp);  //将临时容器中的数据加入主容器
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                    refreshLayout.setRefreshing(false);  //取消下拉刷新的图标
                                                }
                                            });
                                        }
                                    }else {   //后面的数据走正常流程刷新出来
                                        movieList.add(MovieUtil.Json2Movie(movieJson));
                                        if (count%4 == 0 || count == idList.size()){
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }).start();
                    }
                });
            }
        });
    }

    private void loadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String hotJson = HttpUtil.getBoxMovie();  //上拉加载每次最多请求4条
                if (hotJson == null){   //返回为空，一般情况为被封IP
                    return;
                }
                final List<String> idList = HotMovieUtil.getHotMovies(hotJson); //解析出id
                for (String id : idList){
                    String movieJson = HttpUtil.getMovieById(id);
                    movieList.add(MovieUtil.Json2Movie(movieJson));
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                        if (idList.size() < 4){    //如果请求得到的数据不足4条 说明已经没有更多数据了
                            adapter.changeShowStatus(2);
                        }
                    }
                });
            }
        }).start();
    }
}

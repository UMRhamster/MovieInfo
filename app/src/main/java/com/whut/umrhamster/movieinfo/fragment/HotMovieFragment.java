package com.whut.umrhamster.movieinfo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.activity.MovieDetailActivity;
import com.whut.umrhamster.movieinfo.adapter.HotMovieAdapter;
import com.whut.umrhamster.movieinfo.model.Movie;
import com.whut.umrhamster.movieinfo.util.HotMovieUtil;
import com.whut.umrhamster.movieinfo.util.HttpUtil;
import com.whut.umrhamster.movieinfo.util.MovieUtil;
import com.whut.umrhamster.movieinfo.view.StarBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 12421 on 2018/7/11.
 */

public class HotMovieFragment extends Fragment {
    private static final String TAG = HotMovieFragment.class.getSimpleName();
    View rootView;

    private boolean isLoading = false; //用于控制上拉加载
    private int lastVisibleItem;  //
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Movie> movieList;
    private HotMovieAdapter adapter;

    private Handler handler = new Handler();

    @Override
    public void onSaveInstanceState(Bundle outState) {
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
        rootView = inflater.inflate(R.layout.fragment_movie_hot,container,false);
        initView(rootView);
        initData();
        initEvent();
        return rootView;
    }

    private void initView(View view){
        refreshLayout = view.findViewById(R.id.movie_hot_srl);
        recyclerView = view.findViewById(R.id.movie_hot_rv);
        movieList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HotMovieAdapter(getActivity(),movieList,0);
        adapter.setOnItemClickListener(new HotMovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("movie",movieList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void initData(){
//        movieList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String hotJson = HttpUtil.getHotMovie("长沙",0,10);
                if (hotJson == null){   //返回为空，一般情况为被封IP
                    return;
                }
                List<String> idList = HotMovieUtil.getHotMovies(hotJson); //解析出id
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
                    //进行加载处理
                    //
                    //
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.changeShowStatus(2);
                        }
                    },3000);
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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //处理下拉刷新
                    }
                },2000);
            }
        });
    }
}

package com.whut.umrhamster.movieinfo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.whut.umrhamster.movieinfo.util.MovieUtil;
import com.whut.umrhamster.movieinfo.view.StarBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 12421 on 2018/7/11.
 */

public class HotMovieFragment extends Fragment {
    private static final String TAG = HotMovieFragment.class.getSimpleName();
    View rootView;

    private RecyclerView recyclerView;
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
        return rootView;
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.movie_hot_rv);
        movieList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new HotMovieAdapter(getActivity(),movieList);
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
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://api.douban.com/v2/movie/in_theaters")
                        .build();
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    String json = response.body().string();
                    List<String> idList = HotMovieUtil.getHotMoviews(json);
                    for (String id : idList){
                        Request request1 = new Request.Builder()
                                .url("http://api.douban.com/v2/movie/subject/"+id)
                                .build();
                        Response response1 = null;
                        response1 = okHttpClient.newCall(request1).execute();
                        movieList.add(MovieUtil.Json2Movie(response1.body().string()));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

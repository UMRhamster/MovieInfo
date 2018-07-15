package com.whut.umrhamster.movieinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.adapter.HotMovieAdapter;
import com.whut.umrhamster.movieinfo.model.Movie;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionActivity extends AppCompatActivity {
    @BindView(R.id.ac_collection_tb)
    Toolbar toolbar;
    @BindView(R.id.ac_collection_rv)
    RecyclerView recyclerView;

    private HotMovieAdapter adapter;
    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        initData();
    }

    private void initData(){
        movieList = LitePal.findAll(Movie.class,true); //从本地数据库中获取 收藏的电影信息
        adapter = new HotMovieAdapter(this,movieList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter.setOnItemClickListener(new HotMovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(CollectionActivity.this, MovieDetailActivity.class);
                intent.putExtra("movie",movieList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}

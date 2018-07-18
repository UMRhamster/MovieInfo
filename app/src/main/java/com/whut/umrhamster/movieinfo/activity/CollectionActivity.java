package com.whut.umrhamster.movieinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.adapter.CollectionAdapter;
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
    @BindView(R.id.ac_collection_iv_back)
    ImageView imageViewBack;

    private CollectionAdapter adapter;
    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        initData();
        initEvent();
    }

    private void initEvent() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initData(){
        movieList = LitePal.findAll(Movie.class,true); //从本地数据库中获取 收藏的电影信息
        adapter = new CollectionAdapter(this,movieList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(CollectionActivity.this, MovieDetailActivity.class);
                intent.putExtra("movie",movieList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                startActivityForResult(intent,11);
                overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 22){
            movieList.clear();
            movieList.addAll(LitePal.findAll(Movie.class,true));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fg_back_enter,R.anim.anim_fg_back_out);
    }
}

package com.whut.umrhamster.movieinfo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.adapter.ReviewAdapter;
import com.whut.umrhamster.movieinfo.model.Review;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewActivity extends AppCompatActivity {
    @BindView(R.id.ac_review_rv)
    RecyclerView recyclerView;

    private ReviewAdapter adapter;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        initData();
    }

    private void initData(){
//        reviewList = LitePal.findAll(Review.class);  //从本地数据库中获取自己的评论
        reviewList = new ArrayList<>();
        reviewList.add(new Review("大头儿子，小头爸爸","很棒！","2018-7-11 17:00:00"));
        adapter = new ReviewAdapter(this,reviewList,true);
        adapter = new ReviewAdapter(this,reviewList,true);
        adapter = new ReviewAdapter(this,reviewList,true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}

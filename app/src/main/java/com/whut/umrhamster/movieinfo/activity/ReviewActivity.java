package com.whut.umrhamster.movieinfo.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.adapter.ReviewAdapter;
import com.whut.umrhamster.movieinfo.model.Review;
import com.whut.umrhamster.movieinfo.util.SPUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ReviewActivity extends AppCompatActivity {
    @BindView(R.id.ac_review_rv)
    RecyclerView recyclerView;
    @BindView(R.id.ac_review_tv_title)
    TextView textViewTitle;

    private ReviewAdapter adapter;
    private List<Review> reviewList;

    private String movieId;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        initData();
    }

    private void initData(){
        reviewList = new ArrayList<>();
        movieId = getIntent().getStringExtra("movieId");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (movieId != null){  //说明是从电影详情界面进入，应当前是
            textViewTitle.setText(getIntent().getStringExtra("movieName"));
            adapter = new ReviewAdapter(this,reviewList,false);
            queryFromServer();
        }else {
            adapter = new ReviewAdapter(this,reviewList,true);
            queryFromServerForSelf();
        }
        recyclerView.setAdapter(adapter);
    }


    private void queryFromServer(){
        BmobQuery<Review> queryList = new BmobQuery<>();
        queryList.addWhereEqualTo("movieId",movieId);
        queryList.setLimit(20);  //一次查询20条
        queryList.findObjects(new FindListener<Review>() {
            @Override
            public void done(List<Review> list, BmobException e) {
                if (e == null){
                    reviewList .addAll(list);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    Log.d("ReviewActivity","电影评论查询成功:"+list.size()+"条记录");
                }else {
                    Log.d("ReviewActivity","电影评论查询失败");
                }
            }
        });
    }

    private void queryFromServerForSelf(){
        BmobQuery<Review> queryList = new BmobQuery<>();
        Log.d("ReviewActivity",SPUtil.loadData(ReviewActivity.this,"user","name"));
        queryList.addWhereEqualTo("userId", SPUtil.loadData(ReviewActivity.this,"user","name"));
        queryList.setLimit(20);  //一次查询20条
        queryList.findObjects(new FindListener<Review>() {
            @Override
            public void done(List<Review> list, BmobException e) {
                if (e == null){
                    reviewList.addAll(list);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    Log.d("ReviewActivity","个人评论查询成功:"+list.size()+"条记录");
                }else {
                    Log.d("ReviewActivity","个人评论查询失败");
                }
            }
        });
    }
}

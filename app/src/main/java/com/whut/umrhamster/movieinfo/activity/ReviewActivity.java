package com.whut.umrhamster.movieinfo.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.adapter.ReviewAdapter;
import com.whut.umrhamster.movieinfo.model.Movie;
import com.whut.umrhamster.movieinfo.model.Review;
import com.whut.umrhamster.movieinfo.util.SPUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ReviewActivity extends AppCompatActivity {
    @BindView(R.id.ac_review_rv)
    RecyclerView recyclerView;
    @BindView(R.id.ac_review_tv_title)
    TextView textViewTitle;
    @BindView(R.id.ac_review_iv_back)
    ImageView imageViewBack;
    @BindView(R.id.ac_review_tb_bottom)
    Toolbar toolbarBottom;
    @BindView(R.id.ac_review_tb_bottom_et)
    EditText editTextBottom;
    @BindView(R.id.ac_review_tb_bottom_iv)
    ImageView imageViewSend;

    private ReviewAdapter adapter;
    private List<Review> reviewList;

    private Movie movie;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        initData();
        initEvent();
    }

    private void initEvent() {
        //点击返回键
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //点击发送按钮
        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkReviewInput(editTextBottom.getText().toString())){
                    return;
                }
                Review review = new Review();
                review.setMovieId(movie.getMovieId());    //设置电影ID
                review.setMovieName(movie.getTitle());     //设置电影名
                review.setContent(editTextBottom.getText().toString());   //设置评论内容
                review.setUserId(SPUtil.loadData(ReviewActivity.this,"user","name"));   //设置用户名
                review.setDate(new BmobDate(new Date()));    //设置评论时间
                review.setUserName(SPUtil.loadData(ReviewActivity.this,"user","nickname")); //设置用户昵称
                review.save(new SaveListener<String>() {   //提交评论至云数据库
                    @Override
                    public void done(String s, BmobException e) {
                        //s =objectID
                        if (e == null){
                            Toast.makeText(ReviewActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                            reviewList.clear();
                            queryFromServer();
                        }else {
                            Toast.makeText(ReviewActivity.this,"评论失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void initData(){
        reviewList = new ArrayList<>();
        movie = (Movie) getIntent().getSerializableExtra("movie");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (movie != null){  //说明是从电影详情界面进入，应当显示
            toolbarBottom.setVisibility(View.VISIBLE);
            textViewTitle.setText(movie.getTitle());
            adapter = new ReviewAdapter(this,reviewList,false);
            queryFromServer();
        }else {
            toolbarBottom.setVisibility(View.GONE);
            adapter = new ReviewAdapter(this,reviewList,true);
            queryFromServerForSelf();
        }
        recyclerView.setAdapter(adapter);
    }


    private void queryFromServer(){
        BmobQuery<Review> queryList = new BmobQuery<>();
        queryList.addWhereEqualTo("movieId",movie.getMovieId());
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
    private boolean checkReviewInput(String reviewInput){
        if (SPUtil.loadData(ReviewActivity.this,"user","name").equals("@_@")){
            Toast.makeText(ReviewActivity.this,"登陆之后才能进行此操作",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!reviewInput.equals("")){
            return true;
        }
        Toast.makeText(ReviewActivity.this,"评论不能为空",Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (movie != null){
            overridePendingTransition(R.anim.anim_fg_back_enter,R.anim.anim_fg_back_out);
        }else {
            overridePendingTransition(R.anim.anim_do_nothing,R.anim.anim_fg_back_out);
        }
    }
}

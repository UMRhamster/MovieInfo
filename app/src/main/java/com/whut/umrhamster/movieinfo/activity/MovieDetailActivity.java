package com.whut.umrhamster.movieinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.model.Celebrity;
import com.whut.umrhamster.movieinfo.model.Movie;
import com.whut.umrhamster.movieinfo.model.Rating;
import com.whut.umrhamster.movieinfo.util.CelebrityUtil;
import com.whut.umrhamster.movieinfo.util.MovieUtil;
import com.whut.umrhamster.movieinfo.view.BlurTransformation;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;

public class MovieDetailActivity extends AppCompatActivity {
    private Movie movie;

    private ImageView imageViewBlur;        //模糊背景
    private ImageView imageViewPost;        //海报
    private TextView textViewTile;          //中文名
    private TextView textViewOriginalTitle; //原名
    private RatingBar ratingBar;            //评分（星星）
    private TextView textViewRating;        //评分（数值）
    private TextView textViewDirectors;     //导演
    private TextView textViewYear;          //年份
    private TextView textViewGenres;        //类型
    private TextView textViewCountries;     //国家
    private TextView textViewAka;           //又名
    private TextView textViewSummary;       //简介
    private ImageView imageViewBack;        //返回
    private ImageView imageViewOut;         //外部连接
    private TextView textViewTBTile;        //导航栏上的标题

    private LinearLayout linearLayout;      //用于添加演职人员
    private Context context;

    //底部工具栏
//    private ImageView imageViewShouCang;
//    private TextView textViewShouCang;
//    private TextView textViewPinglun;
    private FloatingActionButton actionButtonPinglun;
    private FloatingActionButton actionButtonShoucang;

    private boolean isCollection = false;  //用于判断收藏，默认为false
    private boolean defaultCollection = false; //原来是否已经收藏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        context = this;
        initView();
        initData();
        initEvent();
        if (LitePal.isExist(Movie.class,"movieId = ?",movie.getMovieId())){
            defaultCollection = true;
            isCollection = true;   ///已经收藏的话，默认持续保持收藏
            actionButtonShoucang.setImageResource(R.drawable.movie_detail_fab_shoucang_yes);
        }
    }

    //初始化界面
    private void initView(){
        imageViewBlur = findViewById(R.id.movie_detail_hot_bg_blur);
        imageViewPost = findViewById(R.id.movie_detail_hot_iv);
        textViewTile = findViewById(R.id.movie_detail_hot_title);
        textViewOriginalTitle = findViewById(R.id.movie_detail_hot_original_title);
        ratingBar = findViewById(R.id.movie_detail_hot_rb);
        textViewRating = findViewById(R.id.movie_detail_hot_rating);
        textViewDirectors = findViewById(R.id.movie_detail_hot_director);
        textViewYear = findViewById(R.id.movie_detail_hot_year);
        textViewGenres = findViewById(R.id.movie_detail_hot_genres);
        textViewCountries = findViewById(R.id.movie_detail_hot_countries);
        textViewAka = findViewById(R.id.movie_detail_hot_aka);
        textViewSummary = findViewById(R.id.movie_detail_hot_summary);
        imageViewBack = findViewById(R.id.movie_detail_hot_tb_back);
        imageViewOut = findViewById(R.id.movie_detail_hot_tb_out);
        textViewTBTile = findViewById(R.id.movie_detail_hot_tb_title);

        linearLayout = findViewById(R.id.movie_detail_hot_celebrities_hsv_ll);

//        imageViewShouCang = findViewById(R.id.movie_detail_hot_tb_bottom_shoucang);
//        textViewShouCang = findViewById(R.id.movie_detail_hot_tb_bottom_shoucang_tv);
//        textViewPinglun = findViewById(R.id.movie_detail_hot_tb_bottom_pinglun_tv);

        actionButtonPinglun = findViewById(R.id.movie_detail_hot_pinglun_fab);
        actionButtonShoucang = findViewById(R.id.movie_detail_hot_shoucang_fab);

        actionButtonPinglun.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.themePurple)));
        actionButtonShoucang.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.themePurple)));
    }
    private void initData(){
        movie = (Movie) getIntent().getSerializableExtra("movie");
        Picasso.with(MovieDetailActivity.this).load(movie.getImages()).transform(new BlurTransformation(this)).into(imageViewBlur);
        Picasso.with(MovieDetailActivity.this).load(movie.getImages()).into(imageViewPost);
        textViewTile.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginal_title());
        ratingBar.setRating(movie.getRating().getAverage()/2);
        textViewRating.setText(String.valueOf(movie.getRating().getAverage()));
        textViewDirectors.setText(String.format(getResources().getString(R.string.hot_directors), CelebrityUtil.List2String(movie.getDirectors())));
        textViewYear.setText(String.format(getResources().getString(R.string.hot_year),movie.getYear()));
        textViewGenres.setText(String.format(getResources().getString(R.string.hot_genres),movie.getGenres()));
        textViewCountries.setText(String.format(getResources().getString(R.string.hot_countries),movie.getCountries()));
        textViewAka.setText(String.format(getResources().getString(R.string.hot_aka),movie.getAka()));
        textViewSummary.setText(movie.getSummary());
        textViewTBTile.setText(movie.getTitle());

        addCelebrityItem(movie.getDirectors(),"导演");
        addCelebrityItem(movie.getCasts(),"主演");

        //还需要判断是否收藏...

    }
    private void addCelebrityItem(List<Celebrity> celebrities, String job){
        for (int i=0;i<celebrities.size();i++){
            final Celebrity celebrity = celebrities.get(i); //
            View view = View.inflate(this,R.layout.celebrity_item,null);
            TextView textViewName = view.findViewById(R.id.celebrity_item_name);
            TextView textViewJob = view .findViewById(R.id.celebrity_item_job);
            ImageView imageViewImg = view.findViewById(R.id.celebrity_item_iv);
            textViewName.setText(celebrity.getName());
            if (!celebrity.getAvatars().equals("无")){
                Picasso.with(MovieDetailActivity.this).load(celebrity.getAvatars()).into(imageViewImg);
            }
            view.setOnClickListener(new View.OnClickListener() {   //演职人员 卡片点击事件，跳转外部浏览器
                @Override
                public void onClick(View view) {
                    if (!celebrity.getAlt().equals("无")){
                        Uri uri = Uri.parse(celebrity.getAlt());
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }else {
                        Toast.makeText(context,"暂无人员信息",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            textViewJob.setText(job);
            linearLayout.addView(view);
        }

    }
    private void initEvent(){
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imageViewOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(movie.getMobile_url());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        //点击收藏
        actionButtonShoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCollection = !isCollection;
                if (isCollection){
                    Toast.makeText(MovieDetailActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                    actionButtonShoucang.setImageResource(R.drawable.movie_detail_fab_shoucang_yes);
                }else {
                    Toast.makeText(MovieDetailActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    actionButtonShoucang.setImageResource(R.drawable.movie_detail_fab_shoucang);
                }
            }
        });
        //点击评论
        actionButtonPinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailActivity.this,ReviewActivity.class);
                intent.putExtra("movie",movie);  //将电影对象传过去，用于获取电影评论
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
    }

    private void saveMovie(){
        movie.getRating().save();                        //存储评分
        for (int i=0;i<movie.getDirectors().size();i++){
            movie.getDirectors().get(i).save();          //存储导演
        }
        for (int i=0;i<movie.getCasts().size();i++){     //存储主演
            movie.getCasts().get(i).save();
        }
        movie.save();
    }
    @Override
    protected void onPause() {  //在此声明周期中确定用于最终对于 电影 是否收藏， 以减少对数据库的频繁操作。
        super.onPause();
        if (isCollection){          //先判断现在是否要收藏
            if (!defaultCollection){   //如果原来没有收藏
                saveMovie();
            }
        }else {
            if (defaultCollection){  //如果原来收藏过，现在不想收藏
                LitePal.deleteAll(Movie.class,"movieId = ?",movie.getMovieId());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!isCollection && defaultCollection){
            setResult(22);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_do_nothing,R.anim.anim_fg_back_out);
    }
}

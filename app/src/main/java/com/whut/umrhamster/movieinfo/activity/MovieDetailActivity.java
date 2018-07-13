package com.whut.umrhamster.movieinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.whut.umrhamster.movieinfo.util.CelebrityUtil;
import com.whut.umrhamster.movieinfo.util.MovieUtil;
import com.whut.umrhamster.movieinfo.view.BlurTransformation;

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

    private LinearLayout linearLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        context = this;
        initView();
        initData();
        initEvent();
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
    }
    private void initData(){
        movie = (Movie) getIntent().getSerializableExtra("movie");
        Picasso.get().load(movie.getImages()).transform(new BlurTransformation(this)).into(imageViewBlur);
        Picasso.get().load(movie.getImages()).into(imageViewPost);
        textViewTile.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginal_title());
        ratingBar.setRating(movie.getRating().getAverage()/2);
        textViewRating.setText(String.valueOf(movie.getRating().getAverage()));
        textViewDirectors.setText(String.format(getResources().getString(R.string.hot_directors), CelebrityUtil.List2String(movie.getDirectors())));
        textViewYear.setText(String.format(getResources().getString(R.string.hot_year),movie.getYear()));
        textViewGenres.setText(String.format(getResources().getString(R.string.hot_genres),MovieUtil.Array2String(movie.getGenres())));
        textViewCountries.setText(String.format(getResources().getString(R.string.hot_countries),MovieUtil.Array2String(movie.getCountries())));
        textViewAka.setText(String.format(getResources().getString(R.string.hot_aka),MovieUtil.Array2String(movie.getAka())));
        textViewSummary.setText(movie.getSummary());
        textViewTBTile.setText(movie.getTitle());

        addCelebrityItem(movie.getDirectors(),"导演");
        addCelebrityItem(movie.getCasts(),"主演");

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
                Picasso.get().load(celebrity.getAvatars()).into(imageViewImg);
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
    }

}

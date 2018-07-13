package com.whut.umrhamster.movieinfo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.util.MovieUtil;
import com.whut.umrhamster.movieinfo.view.BlurTransformation;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 12421 on 2018/7/12.
 */

public class BoxFragment extends Fragment {
    private ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_movie_detail,container,false);
        imageView = view.findViewById(R.id.movie_detail_hot_bg_blur);
        Picasso.get().load("http://img3.doubanio.com/view/photo/s_ratio_poster/public/p494268647.jpg")
                .transform(new BlurTransformation(getActivity()))
                .into(imageView);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient okHttpClient = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url("http://api.douban.com/v2/movie/subject/1764796")
//                        .build();
//                Response response = null;
//                try {
//                    response = okHttpClient.newCall(request).execute();
//                    String json = response.body().string();
//                    MovieUtil.Json2Movie(json);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        return view;
    }
}

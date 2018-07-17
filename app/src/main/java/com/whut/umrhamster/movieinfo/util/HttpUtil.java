package com.whut.umrhamster.movieinfo.util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 12421 on 2018/7/15.
 */

public class HttpUtil {
    private static OkHttpClient okHttpClient =new OkHttpClient();  //全局OKhttp客户端

    //获取正在热映的电影
    //city-城市
    //start-起始
    //count-数量
    //示例 - http://api.douban.com/v2/movie/in_theaters?city=武汉&start=0&count=10
    public static String getHotMovie(String city, int start, int count){
        Request request = new Request.Builder()
                .url("http://api.douban.com/v2/movie/in_theaters?city="+city+"&start="+start+"&count="+count)
                .build(); //默认get请求
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //获取即将上映的电影
    //start-起始
    //count-数量
    //示例 - http://api.douban.com/v2/movie/coming_soon?start=0&count=10
    public static String getSoonMovie(int start, int count){
        Request request = new Request.Builder()
                .url("http://api.douban.com/v2/movie/coming_soon?start="+start+"&count="+count)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //获取Top250的电影
    //start-起始
    //count-数量
    //示例 - http://api.douban.com/v2/movie/top250?start=0&count=3
    public static String getTop250Movie(int start, int count){
        Request request = new Request.Builder()
                .url("http://api.douban.com/v2/movie/top250?start="+start+"&count="+count)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //获取北美票房榜的电影
    //示例 - http://api.douban.com/v2/movie/us_box
    public static String getBoxMovie(){
        Request request = new Request.Builder()
                .url("http://api.douban.com/v2/movie/us_box")
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取电影条目信息
    //id - 电影的id
    public static String getMovieById(String id){
        Request request = new Request.Builder()
                .url("http://api.douban.com/v2/movie/subject/"+id)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //通过搜索获取电影信息
    //
    public static String getMovieSimpleByType(int type, String word){
        Request request = null;
        if (type == 0){
            request = new Request.Builder()
                    .url("http://api.douban.com/v2/movie/search?q="+word)
                    .build();
        }else {
            request = new Request.Builder()
                    .url("http://api.douban.com/v2/movie/search?tag="+word)
                    .build();
        }
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.whut.umrhamster.movieinfo.util;

import android.util.Log;

import com.whut.umrhamster.movieinfo.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/7/12.
 */

public class HotMovieUtil {
    public static List<String> getHotMovies(String json){
        List<String> movieList = new ArrayList<>();
        try {
            if (json == null){
                return null;
            }
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
            for (int i=0;i<jsonArray.length();i++){
                movieList.add(jsonArray.getJSONObject(i).getString("id"));
            }
            if (jsonObject.getString("title").matches("即将上映的电影")){
                MovieCountUtil.soonMovieCount = jsonObject.getInt("total");
            }else if (jsonObject.getString("title").matches("正在上映的电影")){
                MovieCountUtil.hotMovieCount = jsonObject.getInt("total");
            }else if (jsonObject.getString("title").matches("豆瓣电影Top250")){
                MovieCountUtil.topMovieCount = jsonObject.getInt("total");
            }else{
                MovieCountUtil.boxMovieCount = 11;  //固定11
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    public static List<String> getBoxMovie(String json){
        List<String> movieList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
            for (int i=0;i<jsonArray.length();i++){
                movieList.add(jsonArray.getJSONObject(i).getJSONObject("subject").getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

}

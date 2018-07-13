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
    public static List<String> getHotMoviews(String json){
//        Log.d("dsad",json);
        List<String> movieList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
            for (int i=0;i<jsonArray.length();i++){
                movieList.add(jsonArray.getJSONObject(i).getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }
}
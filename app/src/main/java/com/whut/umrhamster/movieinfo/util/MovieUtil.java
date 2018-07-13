package com.whut.umrhamster.movieinfo.util;

import android.util.Log;

import com.whut.umrhamster.movieinfo.model.Celebrity;
import com.whut.umrhamster.movieinfo.model.Movie;
import com.whut.umrhamster.movieinfo.model.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/7/12.
 */

public class MovieUtil {
    //将json转换成Movie对象
    public static Movie Json2Movie(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            Movie movie = new Movie();
            movie.setId(jsonObject.getString("id"));
            movie.setTitle(jsonObject.getString("title"));
            movie.setOriginal_title(jsonObject.getString("original_title"));
            movie.setAka(String2Array(jsonObject.getString("aka")));
            movie.setYear(jsonObject.getString("year"));
            movie.setGenres(String2Array(jsonObject.getString("genres")));
            movie.setSummary(jsonObject.getString("summary"));
            movie.setCountries(String2Array(jsonObject.getString("countries")));
            movie.setImages(jsonObject.getJSONObject("images").getString("small"));
            movie.setAlt(jsonObject.getString("alt"));
            movie.setMobile_url(jsonObject.getString("mobile_url"));
            movie.setRating(getRating(jsonObject.getJSONObject("rating")));
            movie.setRating_count(0);
            movie.setWish_count(jsonObject.getInt("wish_count"));
            movie.setCollect_count(jsonObject.getInt("collect_count"));
            movie.setDirectors(getCelebrity(jsonObject.getJSONArray("directors"),movie.getTitle()));
            movie.setCasts(getCelebrity(jsonObject.getJSONArray("casts"),movie.getTitle()));
            return movie;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String[] String2Array(String str){
        if (str.length() >=4)
        return str.substring(2,str.length()-2).split("\",\"");
        return str.split("");
    }

    public static String Array2String(String[] strings){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<strings.length;i++){
            stringBuilder.append(strings[i])
                    .append("、");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    //从jsonObject中获取Rating评分对象
    public static Rating getRating(JSONObject jsonObject){
        Rating rating = new Rating();
        try {
            rating.setMax(jsonObject.getInt("max"));
            rating.setMin(jsonObject.getInt("min"));
            rating.setAverage((float) jsonObject.getDouble("average"));
            return rating;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    //从jsonArray中生成Celebrity集合，包括导演和主演
    public static List<Celebrity> getCelebrity(JSONArray jsonArray, String title){
        List<Celebrity> celebrityList = new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++){
            Celebrity celebrityTemp = new Celebrity();
            try {
                if (jsonArray.getJSONObject(i).getString("alt") == null){
                    celebrityTemp.setAlt("无");
                }else {
                    celebrityTemp.setAlt(jsonArray.getJSONObject(i).getString("alt"));
                }
//                celebrityTemp.setAlt(jsonArray.getJSONObject(i).getString("alt"));
                if (jsonArray.getJSONObject(i).getJSONObject("avatars") == null){
                    celebrityTemp.setAvatars("无");
                }else {
                    celebrityTemp.setAvatars(jsonArray.getJSONObject(i).getJSONObject("avatars").getString("small"));
                }
//                celebrityTemp.setAvatars(jsonArray.getJSONObject(i).getJSONObject("avatars").getString("small"));
                celebrityTemp.setName(jsonArray.getJSONObject(i).getString("name"));
                celebrityTemp.setId(jsonArray.getJSONObject(i).getInt("id"));
                celebrityList.add(celebrityTemp);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("dasd",i+" "+title);
            }
        }
        return celebrityList;
    }
}

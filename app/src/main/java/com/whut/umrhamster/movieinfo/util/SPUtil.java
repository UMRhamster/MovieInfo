package com.whut.umrhamster.movieinfo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 12421 on 2018/7/12.
 */

public class SPUtil {
    //使用sharePreference存储数据
    //减少请求次数
    //解决豆瓣api请求过快，导致的封ip问题
    public static void saveData(Context context, String name, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    //从sharePreference中读取数据
    public static String loadData(Context context, String name, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
}

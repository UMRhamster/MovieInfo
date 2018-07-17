package com.whut.umrhamster.movieinfo.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.whut.umrhamster.movieinfo.model.User;

/**
 * Created by 12421 on 2018/7/12.
 */

public class SPUtil {
    //使用sharePreference存储数据
    public static void saveData(Context context, String name, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    //从sharePreference中读取数据
    public static String loadData(Context context, String name, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"@_@");
    }

    public static void saveUser(Context context, User user){
        saveData(context,"user","name",user.getName());
        saveData(context,"user","password",user.getPassword());
        saveData(context,"user","nickname",user.getNickname());
        saveData(context,"user","avatars",user.getAvatars());
    }
}

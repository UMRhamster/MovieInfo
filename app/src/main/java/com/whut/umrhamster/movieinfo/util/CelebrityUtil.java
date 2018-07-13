package com.whut.umrhamster.movieinfo.util;

import com.whut.umrhamster.movieinfo.model.Celebrity;

import java.util.List;

/**
 * Created by 12421 on 2018/7/13.
 */

public class CelebrityUtil {
    public static String List2String(List<Celebrity> celebrities){
        StringBuilder stringBuilder = new StringBuilder();
        for (Celebrity celebrity : celebrities){
            stringBuilder.append(celebrity.getName())
                    .append("、");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
}

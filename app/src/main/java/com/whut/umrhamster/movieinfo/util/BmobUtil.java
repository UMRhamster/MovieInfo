package com.whut.umrhamster.movieinfo.util;

import android.os.Handler;
import android.os.Message;

import com.whut.umrhamster.movieinfo.model.Review;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;

/**
 * Created by 12421 on 2018/7/18.
 */

public class BmobUtil {
   static Handler handler=  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Integer integer = (Integer) msg.obj;
        }
    };
    public static void getReviewCountByMoviewId(String movieId){
        int count = 0;
        BmobQuery<Review> reviewQuery = new BmobQuery<>();
        reviewQuery.addWhereEqualTo("movieId",movieId);
        reviewQuery.count(Review.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null){
//                    callback(integer);
                    Message msg = Message.obtain();
                    msg.obj = integer;
                    handler.sendMessage(msg);
                }else {
                    callback(0);
                }
            }
        });
    }

    public static int callback(int count){

        return count;

    }
}

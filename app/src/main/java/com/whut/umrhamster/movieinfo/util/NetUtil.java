package com.whut.umrhamster.movieinfo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;

/**
 * Created by 12421 on 2018/7/19.
 */

public class NetUtil {
    //检查当前网络是否可用
    public static boolean checkNetState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            Network[] net = connectivityManager.getAllNetworks();
            for (int i=0;i<net.length;i++){
                if (connectivityManager.getNetworkInfo(net[i]).isAvailable()){
                    return true;
                }
            }
        }
        return false;
    }
}

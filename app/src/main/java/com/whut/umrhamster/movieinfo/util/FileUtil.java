package com.whut.umrhamster.movieinfo.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by 12421 on 2018/7/12.
 */

public class FileUtil {
    //保存数据到文件中
    //内部存储方式
    public static void saveData(Context context, String name, String data){
        FileOutputStream output = null;
        BufferedWriter writer = null;
        try {
            output = context.openFileOutput(name, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write(data);
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //从文件中读取数据
    //内部存储方式
    public static String loadData(Context context, String name){
        FileInputStream input = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            input = context.openFileInput(name);
            reader = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    //向外部存储写入文件
    public static void setDataExternal(Context context, String name, String data){
        File file = new File(context.getExternalFilesDir("movie"),name);
        if (!file.mkdirs()){
            Log.d("FileUtil","Directory not created");
        }

        try {
            FileOutputStream output = new FileOutputStream(file);
            output.write(data.getBytes());
            output.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从外部存储读取文件
    public static String loadDataExternal(Context context, String name){
        StringBuilder content = new StringBuilder();
        File file = new File(context.getExternalFilesDir("movie"),name);
        FileInputStream input = null;
        BufferedReader reader = null;
        if (!file.mkdirs()){
            Log.d("FileUtil","Directory not created");
        }
        try {
            input = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
    //判断外部存储控件是否可用
    public static boolean sdcardAvailable(int size){
        if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)){  //sd卡已经挂载，且可以访问
            File file = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(file.getPath());

            //每个块的大小
            long blockSize = stat.getBlockSizeLong();
            //公共分为多少个块
            long totalBlocks = stat.getBlockCountLong();
            //共有多少可用的空间，单位为字节
            long availableBlocks = stat.getAvailableBlocksLong();

            if(availableBlocks > size)
            {
                return true;
            }
            return false;
        }
        return false;
    }
}

package com.whut.umrhamster.movieinfo.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
}

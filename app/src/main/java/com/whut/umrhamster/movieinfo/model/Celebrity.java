package com.whut.umrhamster.movieinfo.model;

import org.litepal.LitePal;

import java.io.Serializable;

/**
 * Created by 12421 on 2018/7/9.
 */

public class Celebrity extends LitePal implements Serializable{
    private String alt;     //网页链接
    private String avatars; //影像
    private String name;    //名字
    private int id;         //id

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

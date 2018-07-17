package com.whut.umrhamster.movieinfo.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 12421 on 2018/7/16.
 */

public class User extends BmobObject implements Serializable{
    private int id;  //id
    private String name;
    private String password;
    private String nickname;
    private String avatars;
    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }
}

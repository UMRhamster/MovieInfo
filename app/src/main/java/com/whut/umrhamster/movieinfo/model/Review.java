package com.whut.umrhamster.movieinfo.model;

/**
 * Created by 12421 on 2018/7/15.
 */

public class Review {
    private int id;         //id
    private String movieId; //电影
    private int userId;  //用户
    private String date;    //时间
    private String content; //内容

    public Review(){}

    public Review(String movieId, String content, String date){
        this.movieId = movieId;
        this.content = content;
        this.date = date;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

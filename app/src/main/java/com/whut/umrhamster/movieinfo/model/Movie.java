package com.whut.umrhamster.movieinfo.model;

import org.litepal.LitePal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 12421 on 2018/7/9.
 */

public class Movie extends LitePal implements Serializable{
    private String id;             //条目id
    private String title;          //中文名
    private String original_title; //原名
    private String[] aka;          //又名
    private String year;           //年代
    private String[] genres;       //影片类型
    private String summary;        //简介
    private String[] countries;    //制片国家/地区
    private String images;         //海报
    private String alt;            //原网页
    private String mobile_url;     //移动版原网页
    private Rating rating;         //评分
    private int rating_count;      //评分人数
    private int wish_count;        //想看人数
    private int collect_count;     //看过人数
    private List<Celebrity> directors;   //导演
    private List<Celebrity> casts;       //主演
    private int rankInBox;               //排行（仅在票房榜中使用）

    public Movie(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String[] getAka() {
        return aka;
    }

    public void setAka(String[] aka) {
        this.aka = aka;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String[] getCountries() {
        return countries;
    }

    public void setCountries(String[] countries) {
        this.countries = countries;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public int getRating_count() {
        return rating_count;
    }

    public void setRating_count(int rating_count) {
        this.rating_count = rating_count;
    }

    public int getWish_count() {
        return wish_count;
    }

    public void setWish_count(int wish_count) {
        this.wish_count = wish_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public List<Celebrity> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Celebrity> directors) {
        this.directors = directors;
    }

    public List<Celebrity> getCasts() {
        return casts;
    }

    public void setCasts(List<Celebrity> casts) {
        this.casts = casts;
    }

    public int getRankInBox() {
        return rankInBox;
    }

    public void setRankInBox(int rankInBox) {
        this.rankInBox = rankInBox;
    }
}

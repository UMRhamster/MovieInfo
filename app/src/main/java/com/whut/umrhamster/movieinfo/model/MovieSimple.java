package com.whut.umrhamster.movieinfo.model;

import java.util.List;

/**
 * Created by 12421 on 2018/7/16.
 */

//简化版电影模型   用于搜素
public class MovieSimple {
    private String id;             //id
    private String title;          //中文名
    private String year;           //年代
    private String[] genres;       //影片类型
    private List<Celebrity> directors;   //导演

    public MovieSimple(){}

    public MovieSimple(String title, String year, String[] genres, List<Celebrity> directors){
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.directors = directors;
    }

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

    public List<Celebrity> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Celebrity> directors) {
        this.directors = directors;
    }
}

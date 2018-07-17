package com.whut.umrhamster.movieinfo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 12421 on 2018/7/17.
 */

public class MovieRank implements Serializable{
    private String id;             //id
    private String title;          //中文名
    private String originalTitle;  //原名
    private String year;           //年代
    private String[] genres;       //影片类型
    private int box;               //票房
    private boolean isNew;         //是否新上映
    private String images;         //电影海报

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

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
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

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}

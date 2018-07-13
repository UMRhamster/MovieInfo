package com.whut.umrhamster.movieinfo.model;

import org.litepal.LitePal;

import java.io.Serializable;

/**
 * Created by 12421 on 2018/7/9.
 */

public class Rating extends LitePal implements Serializable{
    private int id;
    private int max;
    private int min;
    private float average;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }
}

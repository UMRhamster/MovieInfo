package com.whut.umrhamster.movieinfo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.whut.umrhamster.movieinfo.R;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.ac_search_ns)
    NiceSpinner niceSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }
    private void initView(){
        List<String> dataSet = new LinkedList<>(Arrays.asList("导演", "类型"));
        niceSpinner.attachDataSource(dataSet);
    }
}

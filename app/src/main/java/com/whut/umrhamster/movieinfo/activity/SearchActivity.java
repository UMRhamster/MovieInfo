package com.whut.umrhamster.movieinfo.activity;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.adapter.SearchHistoryAdapter;
import com.whut.umrhamster.movieinfo.adapter.SearchResultAdapter;
import com.whut.umrhamster.movieinfo.fragment.SearchHistoryFragment;
import com.whut.umrhamster.movieinfo.fragment.SearchResultFragment;
import com.whut.umrhamster.movieinfo.model.MovieSimple;
import com.whut.umrhamster.movieinfo.util.HttpUtil;
import com.whut.umrhamster.movieinfo.util.MovieUtil;
import com.whut.umrhamster.movieinfo.util.SPUtil;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.b.V;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.ac_search_ns)
    NiceSpinner niceSpinner;               //输入导航栏的下拉列表
//    @BindView(R.id.ac_search_rv_history)
//    RecyclerView recyclerViewHistory;      //显示历史记录的列表
//    @BindView(R.id.ac_search_rv_search)
//    RecyclerView recyclerViewResult;       //显示搜索结果的列表
    @BindView(R.id.ac_search_iv_search)
    ImageView imageViewSearch;              //输入导航栏的搜索按钮
    @BindView(R.id.ac_search_et)
    EditText editText;                      //输入导航栏的输入框
    @BindView(R.id.ac_search_et_delete)
    ImageView imageViewDelete;              //输入导航栏的删除按钮
    @BindView(R.id.ac_search_iv_back)
    ImageView imageViewBack;                //输入导航栏的返回按钮

//    private List<String> historyList;
//    private SearchHistoryAdapter adapter;  //历史记录适配器
//    private List<MovieSimple> simpleList;
//    private SearchResultAdapter adapterResult; //搜索结果适配器
//
    private SearchHistoryFragment historyFragment;
    private SearchResultFragment resultFragment;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        initEvent();
        setDefaultFragment();
    }
    private void setDefaultFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        historyFragment = new SearchHistoryFragment();
        transaction.replace(R.id.ac_search_fl,historyFragment);
        transaction.commit();
    }
    private void initView(){
        List<String> dataSet = new LinkedList<>(Arrays.asList("关键字", "标签"));
        niceSpinner.attachDataSource(dataSet);
    }

    private void initEvent(){
        //搜索按钮点击事件
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSearchHistory(editText.getText().toString());//保存记录
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (resultFragment == null){
                    resultFragment = new SearchResultFragment();
                }
                transaction.replace(R.id.ac_search_fl,resultFragment);
                transaction.commit();
                resultFragment.searchMovie(niceSpinner.getSelectedIndex(),editText.getText().toString());
            }
        });
        //监听输入框变化事件
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0){
                    imageViewDelete.setVisibility(View.VISIBLE);  //输入框有文字时，显示删除按钮
                }else {
                    imageViewDelete.setVisibility(View.GONE);     //输入框没有文字时，不显示删除按钮
                }
            }
        });
        //输入导航栏返回按钮监听
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getFragments().get(0) instanceof SearchResultFragment){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    if (historyFragment == null){
                        historyFragment = new SearchHistoryFragment();
                    }
                    transaction.replace(R.id.ac_search_fl,historyFragment);
                    transaction.commit();
                }else {
                    onBackPressed();
                }
            }
        });
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(null);  //清楚输入框内容
                imageViewDelete.setVisibility(View.GONE);
            }
        });
    }
    //保存搜索历史记录
    private void saveSearchHistory(String searchContent){
        List<String> historyList = new ArrayList<>();
        if (searchContent.isEmpty()){ //先判断是否为空
            return;
        }
        String history = SPUtil.loadData(this,"searchHistory","history");
        if (history.equals("@_@")){
            historyList.add(searchContent);
            SPUtil.saveData(this,"searchHistory","history",searchContent);
            return;
        }
        String[] historyTemp = history.split("⊙");
        historyList = new ArrayList<>(Arrays.asList(historyTemp));
        for (int i=0;i<historyList.size();i++){
            if (historyList.get(i).equals(searchContent)){   //如果有和以前相同的，则删除以前的记录
                historyList.remove(i);
                break;
            }
        }
        historyList.add(0,searchContent);   //将新的搜索记录放在最前面，
        if (historyList.size() > 10){
            historyList.remove(10);   //如果历史记录已经有10条，则删除最后一条
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<historyList.size();i++){
            stringBuilder.append(historyList.get(i))
                    .append("⊙");  //使用特殊符号进行拼接
        }
        if (historyList.size() > 0){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);  //删除最后一个特殊符号
        }
        //保存到sharepreference
        SPUtil.saveData(this,"searchHistory","history",stringBuilder.toString());
    }
    public String getEditContent(){
        return editText.getText().toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fg_back_enter,R.anim.anim_fg_back_out);
    }
}

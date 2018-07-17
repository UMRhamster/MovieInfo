package com.whut.umrhamster.movieinfo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.activity.SearchActivity;
import com.whut.umrhamster.movieinfo.adapter.SearchHistoryAdapter;
import com.whut.umrhamster.movieinfo.adapter.SearchResultAdapter;
import com.whut.umrhamster.movieinfo.util.SPUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 12421 on 2018/7/17.
 */

public class SearchHistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchHistoryAdapter adapter;
    private List<String> historyList;

    private Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_history,container,false);
        initView(view);
        return view;
    }
    private void initView(View view){
        recyclerView = view.findViewById(R.id.fragment_search_history_rv);
        historyList = new ArrayList<>();
        getHistory();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SearchHistoryAdapter(getActivity(),historyList);
        adapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == historyList.size()){
                    SPUtil.saveData(getActivity(),"searchHistory","history","@_@"); //清除所有历史记录，存储为默认字符串
                    historyList.clear();
                    adapter.notifyDataSetChanged();
                }else {
//                    ((SearchActivity)getActivity()).getEditContent();
                    EditText editText = getActivity().findViewById(R.id.ac_search_et);
                    editText.setText(historyList.get(position));
//                    searchMovie(historyList.get(position));
                }
            }

            @Override
            public void onItemDelete(int position) {
                deleteItemHistory(position);  //删除单条记录
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void getHistory(){
        String history = SPUtil.loadData(getActivity(),"searchHistory","history");
        String[] historyTemp = history.split("⊙");
        if (historyTemp.length == 1 && historyTemp[0].equals("@_@")){  //如果为默认值
            historyList = new ArrayList<>();
            return;
        }
        historyList = new ArrayList<>(Arrays.asList(historyTemp));
    }

    //删除单条历史记录
    private void deleteItemHistory(int position){
        StringBuilder stringBuilder = new StringBuilder();
        historyList.remove(position);
        if (historyList.size() == 0){   //删除最后一条记录
            stringBuilder.append("@_@");
        }
        for (int i=0;i<historyList.size();i++){
            stringBuilder.append(historyList.get(i))
                    .append("⊙");  //使用特殊符号进行拼接
        }
        if (historyList.size() >0){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);  //删除最后一个特殊符号
        }
        //保存到sharepreference
        SPUtil.saveData(getActivity(),"searchHistory","history",stringBuilder.toString());
        adapter.notifyDataSetChanged();
    }

    //保存搜索历史记录
    public void saveSearchHistory(String searchContent){
        Log.d("SearchHistory",searchContent);
        if (searchContent.isEmpty()){ //先判断是否为空
            return;
        }
        String history = SPUtil.loadData(getActivity(),"searchHistory","history");
        if (history.equals("@_@")){
            historyList = new ArrayList<>();
            historyList.add(searchContent);
            SPUtil.saveData(getActivity(),"searchHistory","history",searchContent);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
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
        SPUtil.saveData(getActivity(),"searchHistory","history",stringBuilder.toString());
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void notifyHistroy(){
        adapter.notifyDataSetChanged();
    }
}

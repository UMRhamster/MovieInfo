package com.whut.umrhamster.movieinfo.fragment;

import android.content.Intent;
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
import android.widget.Toast;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.activity.WebViewActivity;
import com.whut.umrhamster.movieinfo.adapter.SearchResultAdapter;
import com.whut.umrhamster.movieinfo.model.Celebrity;
import com.whut.umrhamster.movieinfo.model.MovieSimple;
import com.whut.umrhamster.movieinfo.util.HttpUtil;
import com.whut.umrhamster.movieinfo.util.MovieUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/7/17.
 */

public class SearchResultFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchResultAdapter adapter;
    private List<MovieSimple> simpleList;

    private Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result,container,false);
        initView(view);
        Log.d("result","d");
        return view;
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.fragment_search_result_rv);
        simpleList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SearchResultAdapter(getActivity(),simpleList);
        adapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                String url = "https://movie.douban.com/subject/"+simpleList.get(position).getId()+"/mobile";
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    public void searchMovie(final int type, final String word){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = HttpUtil.getMovieSimpleByType(type,word);
                simpleList.clear();
                List<MovieSimple> tempList = MovieUtil.Json2SimpleList(json);
                if (tempList != null){
                    simpleList.addAll(tempList);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

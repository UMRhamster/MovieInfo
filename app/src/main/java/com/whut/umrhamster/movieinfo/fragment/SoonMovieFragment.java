package com.whut.umrhamster.movieinfo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.util.FileUtil;

import butterknife.BindView;

/**
 * Created by 12421 on 2018/7/14.
 */

public class SoonMovieFragment extends Fragment {
    Button buttonDU;
    Button buttonXIE;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_soon,container,false);
        buttonDU = view.findViewById(R.id.btn_du);
        buttonXIE = view.findViewById(R.id.btn_xie);
        buttonDU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = FileUtil.loadData(getActivity(),"test");
                Log.d("SoonMovieFragment",string);
            }
        });
        buttonXIE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtil.saveData(getActivity(),"test","test0");
            }
        });
        return view;
    }
}

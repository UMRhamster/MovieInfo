package com.whut.umrhamster.movieinfo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.activity.AboutActivity;
import com.whut.umrhamster.movieinfo.activity.CollectionActivity;
import com.whut.umrhamster.movieinfo.activity.ReInfoActivity;
import com.whut.umrhamster.movieinfo.activity.ReviewActivity;
import com.whut.umrhamster.movieinfo.view.BlurTransformation;
import com.whut.umrhamster.movieinfo.view.CircleImageView;

/**
 * Created by 12421 on 2018/7/11.
 */

public class PersonalFragment extends Fragment {
//    @BindView(R.id.fragment_personal_iv_bg)
    ImageView imageViewBG;
//    @BindView(R.id.fragment_personal_civ)
    CircleImageView circleImageView;
//    @BindView(R.id.fragment_personal_tv_name)
    TextView textViewName;
//    @BindView(R.id.fragment_personal_shoucang)
    RelativeLayout relativeLayoutShouCang;
//    @BindView(R.id.fragment_personal_pinglun)
    RelativeLayout relativeLayoutPinglun;
//    @BindView(R.id.fragment_personal_mima)
    RelativeLayout relativeLayoutMiMa;
//    @BindView(R.id.fragment_personal_zhuxiao)
    RelativeLayout relativeLayoutZhuXiao;
//    @BindView(R.id.fragment_personal_guanyu)
    RelativeLayout relativeLayoutGuanYu;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal,container,false);
        initView(view);
        initEvent();
        Picasso.get().load(R.mipmap.timg).transform(new BlurTransformation(getActivity())).into(imageViewBG);
        return view;
    }

    private void initView(View view){
        imageViewBG = view.findViewById(R.id.fragment_personal_iv_bg);
        circleImageView = view.findViewById(R.id.fragment_personal_civ);
        textViewName = view.findViewById(R.id.fragment_personal_tv_name);
        relativeLayoutShouCang = view.findViewById(R.id.fragment_personal_shoucang);
        relativeLayoutPinglun = view.findViewById(R.id.fragment_personal_pinglun);
        relativeLayoutMiMa = view.findViewById(R.id.fragment_personal_mima);
        relativeLayoutZhuXiao = view.findViewById(R.id.fragment_personal_zhuxiao);
        relativeLayoutGuanYu = view.findViewById(R.id.fragment_personal_guanyu);
    }
    private void initEvent(){
        //进入我的收藏
        relativeLayoutShouCang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CollectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                startActivity(intent);
            }
        });
        //进入我的评论
        relativeLayoutPinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                startActivity(intent);
            }
        });
        //进入修改信息
        relativeLayoutMiMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                startActivity(intent);
            }
        });
        //注销
        relativeLayoutZhuXiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入登陆界面，并清楚sharepreference和数据库中的数据
            }
        });
        //关于
        relativeLayoutGuanYu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                startActivity(intent);
            }
        });
    }
}

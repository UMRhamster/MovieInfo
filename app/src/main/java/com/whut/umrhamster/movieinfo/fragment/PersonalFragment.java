package com.whut.umrhamster.movieinfo.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.whut.umrhamster.movieinfo.activity.LoginActivity;
import com.whut.umrhamster.movieinfo.activity.MainActivity;
import com.whut.umrhamster.movieinfo.activity.ReInfoActivity;
import com.whut.umrhamster.movieinfo.activity.ReviewActivity;
import com.whut.umrhamster.movieinfo.model.User;
import com.whut.umrhamster.movieinfo.util.SPUtil;
import com.whut.umrhamster.movieinfo.view.BlurTransformation;
import com.whut.umrhamster.movieinfo.view.CircleImageView;
import com.youth.banner.loader.ImageLoader;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

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

    private static final int REQUEST_CODE_CHOOSE = 26;

    private List<Uri> mSelected;
    private User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal,container,false);
        initView(view);
        initEvent();
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
                startActivity(new Intent(getActivity(), LoginActivity.class));
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

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPUtil.loadData(getActivity(),"user","name").equals("@_@")){
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }else {
                    //如果存在用户就进行头像上传
                    Matisse.from(getActivity())
                            .choose(MimeType.allOf())
                            .countable(true)
                            .maxSelectable(9)
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new PicassoEngine())
                            .forResult(REQUEST_CODE_CHOOSE);
                }
            }
        });
        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPUtil.loadData(getActivity(),"user","name").equals("@_@")){
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SPUtil.loadData(getActivity(),"user","name").equals("@_@")){
            textViewName.setText(SPUtil.loadData(getActivity(),"user","nickname"));
            Picasso.get().load(SPUtil.loadData(getActivity(),"user","avatars")).transform(new BlurTransformation(getActivity())).into(imageViewBG);

            Bitmap bitmap = BitmapFactory.decodeFile(SPUtil.loadData(getActivity(),"user","local_avatars"));
            circleImageView.setImageBitmap(bitmap);
        }

        Log.d("Fragment","onresume");
    }
}

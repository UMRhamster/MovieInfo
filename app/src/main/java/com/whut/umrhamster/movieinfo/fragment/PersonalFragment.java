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
import android.widget.Toast;

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
                if (permissionCheck()){
                    Intent intent = new Intent(getActivity(), CollectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_fg_out);
                }
            }
        });
        //进入我的评论
        relativeLayoutPinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permissionCheck()){
                    Intent intent = new Intent(getActivity(), ReviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_fg_out);
                }
            }
        });
        //进入修改信息
        relativeLayoutMiMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permissionCheck()){
                    Intent intent = new Intent(getActivity(), ReInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_fg_out);
                }
            }
        });
        //注销
        relativeLayoutZhuXiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入登陆界面，并清楚sharepreference和数据库中的数据
                if (permissionCheck()){
                    SPUtil.deleteUser(getActivity());
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.anim_fg_back_enter,R.anim.anim_fg_back_out);
                }
            }
        });
        //关于
        relativeLayoutGuanYu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //栈顶复用
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_fg_out);
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPUtil.loadData(getActivity(),"user","name").equals("@_@")){
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_fg_out);
                }else {
                    //如果存在用户就进行头像上传
                    Matisse.from(getActivity())
                            .choose(MimeType.of(MimeType.PNG,MimeType.JPEG))
                            .countable(true)
                            .maxSelectable(1)
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .theme(R.style.Matisse_Dracula)
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
                    getActivity().overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_fg_out);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("dsadas","dsa");
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            Log.d("Persnalllll","收到数据啦");
        }else {
            Log.d("Persnalllll","没收到数据");
        }
        if (requestCode == REQUEST_CODE_CHOOSE) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("user-name",SPUtil.loadData(getActivity(),"user","name"));
        if (!SPUtil.loadData(getActivity(),"user","name").equals("@_@")){
            Log.d("PersonalFragment","用户已登录");
            textViewName.setText(SPUtil.loadData(getActivity(),"user","nickname"));
            Picasso.with(getActivity())
                    .load(SPUtil.loadData(getActivity(),"user","avatars"))
                    .placeholder(R.mipmap.default_blur_bg)   //设置默认图片
                    .transform(new BlurTransformation(getActivity()))
                    .into(imageViewBG);
            if (SPUtil.loadData(getActivity(),"user","local_avatars").equals("@_@")){
                circleImageView.setImageResource(R.drawable.default_user_icon);
            }else {
                Bitmap bitmap = BitmapFactory.decodeFile(SPUtil.loadData(getActivity(),"user","local_avatars"));
                circleImageView.setImageBitmap(bitmap);
            }
        }else {
            Log.d("暂未登录","设置默认图片");
            textViewName.setText("点击登录");
            circleImageView.setImageResource(R.drawable.default_user_icon);
            Picasso.with(getActivity())
                    .load(R.mipmap.default_blur_bg)
                    .transform(new BlurTransformation(getActivity()))
                    .into(imageViewBG);
        }

        Log.d("Fragment","onresume");
    }

    //权限验证
    private boolean permissionCheck(){
        if (!SPUtil.loadData(getActivity(),"user","name").equals("@_@")){
            return true;
        }
        Toast.makeText(getActivity(),"请先进行登陆或注册",Toast.LENGTH_SHORT).show();
        return false;
    }
}

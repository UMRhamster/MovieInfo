package com.whut.umrhamster.movieinfo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.adapter.MainFragmentPagerAdapter;
import com.whut.umrhamster.movieinfo.fragment.BoxMovieFragment;
import com.whut.umrhamster.movieinfo.fragment.TopMovieFragment;
import com.whut.umrhamster.movieinfo.fragment.HotMovieFragment;
import com.whut.umrhamster.movieinfo.fragment.PersonalFragment;
import com.whut.umrhamster.movieinfo.fragment.SoonMovieFragment;
import com.whut.umrhamster.movieinfo.model.Celebrity;
import com.whut.umrhamster.movieinfo.model.Rating;
import com.whut.umrhamster.movieinfo.util.MovieCountUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.ac_main_vp)
    ViewPager viewPager;
    @BindView(R.id.ac_main_tabl)
    TabLayout tabLayout;
    @BindView(R.id.ac_main_tb_tv)
    TextView textViewTitle;
    @BindView(R.id.ac_main_tb)
    Toolbar toolbar;
    @BindView(R.id.ac_main_search)
    ImageView imageViewSearch;
    @BindView(R.id.ac_main_tb_location_iv)
    ImageView imageViewLocation;
    @BindView(R.id.ac_main_tb_location_tv)
    TextView textViewLocation;

    public LocationClient locationClient;
    private boolean isLocated = false;
    private String location = "北京";

    private MainFragmentPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private final int[] TAB_TITLES = new int[]{R.string.hot,R.string.late,R.string.top,R.string.tickets,R.string.personal};
    private final int[] TAB_ICONS = new int[]{R.drawable.main_reying_bg,R.drawable.main_jinqi_bg,R.drawable.main_top_bg,
            R.drawable.main_piaofang_bg,R.drawable.main_personal_bg};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LitePal.initialize(this);   //初始化litepal
        Bmob.initialize(this, "8ef4da5b5730f6c7c70dc08e9d6e6373");

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //取消半透明
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initView();
        initEvent();
    }
    //初始化视图
    private void initView(){
        fragmentList = new ArrayList<>();
        fragmentList.add(new HotMovieFragment());
        fragmentList.add(new SoonMovieFragment());
        fragmentList.add(new TopMovieFragment());
        fragmentList.add(new BoxMovieFragment());
        fragmentList.add(new PersonalFragment());
        adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        setTabs(tabLayout,TAB_ICONS);

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        initLocation();
        locationClient.start();

    }
    //初始化百度定位设置
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(3000);
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
    }
    //初始化事件
    private void initEvent(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textViewTitle.setText(TAB_TITLES[position]);
                if (position == 0){
                    imageViewLocation.setVisibility(View.VISIBLE);
                    textViewLocation.setText(location);
                }else {
                    imageViewLocation.setVisibility(View.GONE);
                }
                switch (position){
                    case 1:
                        textViewLocation.setText("共"+ MovieCountUtil.soonMovieCount+"部电影");
                        break;
                    case 2:
                        textViewLocation.setText("共"+ MovieCountUtil.topMovieCount+"部电影");
                        break;
                    case 3:
                        textViewLocation.setText("共"+ MovieCountUtil.boxMovieCount+"部电影");
                        break;
                }
                if (position == 4){
                    toolbar.setVisibility(View.GONE);
                }else {
                    toolbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //搜索按钮点击
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    private void setTabs(TabLayout tabLayout,int[] tabImgs){
        tabLayout.getTabAt(0).setIcon(tabImgs[0]);
        tabLayout.getTabAt(1).setIcon(tabImgs[1]);
        tabLayout.getTabAt(2).setIcon(tabImgs[2]);
        tabLayout.getTabAt(3).setIcon(tabImgs[3]);
        tabLayout.getTabAt(4).setIcon(tabImgs[4]);
    }

    public class MyLocationListener extends BDAbstractLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (!isLocated){
                location = bdLocation.getCity().substring(0,bdLocation.getCity().length()-1);
                textViewLocation.setText(location);
                if (fragmentList.get(0).isVisible()){
                    ((HotMovieFragment)fragmentList.get(0)).initData();
                    isLocated = true;
                }
//                isLocated = true; //只定位一次
            }
        }
    }
    public String getTitleBelow(){
        return textViewTitle.getText().toString();
    }
}

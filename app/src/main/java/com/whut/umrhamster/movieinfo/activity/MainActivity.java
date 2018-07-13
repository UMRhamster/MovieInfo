package com.whut.umrhamster.movieinfo.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.adapter.MainFragmentPagerAdapter;
import com.whut.umrhamster.movieinfo.fragment.BoxFragment;
import com.whut.umrhamster.movieinfo.fragment.CardFragment;
import com.whut.umrhamster.movieinfo.fragment.HotMovieFragment;
import com.whut.umrhamster.movieinfo.fragment.PersonalFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.ac_main_vp)
    ViewPager viewPager;
    @BindView(R.id.ac_main_tabl)
    TabLayout tabLayout;
    @BindView(R.id.ac_main_tb_tv)
    TextView textViewTitle;

    private MainFragmentPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private final int[] TAB_TITLES = new int[]{R.string.hot,R.string.late,R.string.top,R.string.tickets,R.string.personal};
    private final int[] TAB_ICONS = new int[]{R.drawable.main_reying_bg,R.drawable.main_jinqi_bg,R.drawable.main_top_bg,
            R.drawable.main_piaofang_bg,R.drawable.main_personal_bg};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initEvent();
    }
    //初始化视图
    private void initView(){
        fragmentList = new ArrayList<>();
        fragmentList.add(new HotMovieFragment());
        fragmentList.add(new CardFragment());
        fragmentList.add(new CardFragment());
        fragmentList.add(new BoxFragment());
        fragmentList.add(new PersonalFragment());
        adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        setTabs(tabLayout,TAB_ICONS);

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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
}

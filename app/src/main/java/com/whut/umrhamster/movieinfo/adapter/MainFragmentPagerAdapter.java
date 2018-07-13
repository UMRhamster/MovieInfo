package com.whut.umrhamster.movieinfo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 12421 on 2018/7/10.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private final String[] TAB_TITLES = new String[]{"热映","近期","TOP","票房榜","我的"};

    private List<Fragment> fragmentList;
    public MainFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList){
        super(fragmentManager);
        this.fragmentList = fragmentList;
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}

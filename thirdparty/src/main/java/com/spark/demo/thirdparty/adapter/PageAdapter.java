//package com.spark.demo.thirdparty.adapter;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//
//import com.spark.baselib.ui.fragment.AbsFragment;
//
//import java.util.List;
//
///**
// * Created by spark on 2016/6/30.
// */
//public class PageAdapter extends FragmentPagerAdapter {
//    private List<String> mTitles;
//    private List<AbsFragment> mFragments;
//
//    public PageAdapter(FragmentManager fm, List<String> titles, List<AbsFragment> fragments) {
//        super(fm);
//        this.mTitles = titles;
//        this.mFragments = fragments;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return mFragments != null ? mFragments.get(position) : null;
//    }
//
//    @Override
//    public int getCount() {
//        return mFragments != null ? mFragments.size() : 0;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mTitles != null ? mTitles.get(position) : "";
//    }
//}

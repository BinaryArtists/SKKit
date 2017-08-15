package com.spark.demo.androiddemo.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.spark.demo.androiddemo.R;
import com.spark.framework.component.autoviewpager.IconPageIndicator;
import com.spark.framework.component.autoviewpager.adapter.IconLoopPagerAdapter;
import com.spark.framework.component.autoviewpager.page.Page;
import com.spark.framework.component.autoviewpager.viewpager.AutoSlidePager;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播控件 demo
 * Created by chenwei on 2017/7/25.
 */

public class ViewPagerDemoActivity extends AppCompatActivity {
    private static final String TAG = "ViewPagerDemoActivity";

    private AutoSlidePager mViewPager;
    private IconLoopPagerAdapter mAdapter;
    private List<Page> mPageList = new ArrayList<>();
    private IconPageIndicator mIconPageIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_demo);
        mViewPager = (AutoSlidePager) findViewById(R.id.view_pager);
        mIconPageIndicator  = (IconPageIndicator) findViewById(R.id.icon_indicator);
        mAdapter  = new IconLoopPagerAdapter(mPageList) {
            @Override
            public ImageView getIndicatorIcon(Context context, ViewGroup parent) {
                return (ImageView) LayoutInflater.from(context).inflate(R.layout.view_indicator_black_icon, parent, false);

            }
        };
        mViewPager.setAdapter(mAdapter);
        mIconPageIndicator.setViewPager(mViewPager);
    }


    public void refreshViewData(View view){
        mPageList.clear();
        for (int i = 0; i < 5; i++){
            DemoPage item  = new DemoPage("这是第 " + (i+1) +"项");
            mPageList.add(item);
        }
        mAdapter.notifyDataSetChanged();
        mIconPageIndicator.setViewPager(mViewPager);
    }
}

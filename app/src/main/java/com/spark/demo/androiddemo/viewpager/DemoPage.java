package com.spark.demo.androiddemo.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spark.demo.androiddemo.R;
import com.spark.framework.component.autoviewpager.page.Page;

/**
 * demo
 * Created by chenwei on 2017/7/25.
 */

public class DemoPage implements Page {
    @Override
    public View createPage(Context context, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.view_demo_page, parent, false);
    }

    @Override
    public void initPage(Context context, View page) {
        TextView mtvInfo = (TextView) page.findViewById(R.id.tv_info);
        if (mtvInfo!= null){
            mtvInfo.setText(mInfo);
        }
    }

    private String mInfo;

    public DemoPage(String info) {
        this.mInfo  = info;
    }
}

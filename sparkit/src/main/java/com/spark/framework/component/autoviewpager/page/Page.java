package com.spark.framework.component.autoviewpager.page;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


public interface Page {
    public View createPage(Context context, ViewGroup parent);
    
    public void initPage(Context context, View page);
    // public void destory();
}

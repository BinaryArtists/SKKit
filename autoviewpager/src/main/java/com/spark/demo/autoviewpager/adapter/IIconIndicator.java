package com.spark.demo.autoviewpager.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

public interface IIconIndicator {
    public int getItemCount();
    public ImageView getIndicatorIcon(Context context, ViewGroup parent);
}

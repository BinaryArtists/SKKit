package com.spark.demo.autoviewpager.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.spark.demo.autoviewpager.page.Page;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    
    List<Page> pages;
    
    OnPageClickListener pageListener;
    
    public ViewPagerAdapter(List<Page> pages) {
        this.pages = pages;
    }
    
    public ViewPagerAdapter setOnPageClickListener(OnPageClickListener l) {
        pageListener = l;
        return this;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            View child = (View) object;
            if (container.indexOfChild(child) >= 0) {
                container.removeView(child);
                child.setTag(null);
                child.setOnClickListener(null);
            }
        }
    }
    
    @Override
    public int getCount() {
        return pages != null ? pages.size() : 0;
    }
    
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        int size = pages != null ? pages.size() : 0;
        if (position >= 0 && position < size) {
            final Context context = container.getContext();
            final Page page = pages.get(position);
            View child = page.createPage(context, container);
            child.setTag(page);
            container.addView(child);
            page.initPage(context, child);
            child.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    if (pageListener != null) {
                        pageListener.onPageClick(v, position, page);
                    }
                }
            });
            return child;
        }
        return null;
    }
    
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    
    @Override
    public int getItemPosition(Object object) {
        if (!(object instanceof View)) {
            return POSITION_NONE;
        }
        View child = (View) object;
        Object tag = child.getTag();
        int index = POSITION_NONE;
        if ((tag instanceof Page) && pages != null) {
            Page page = (Page) tag;
            index = pages.indexOf(page);
        }
        return index;
    }
    
    public int getRealCount() {
        return pages != null ? pages.size() : 0;
    }
    
    public interface OnPageClickListener {
        public void onPageClick(View pageView, int position, Page page);
    }
    
}

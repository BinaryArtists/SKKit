package com.spark.demo.thirdparty.calender;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePagerAdapter<T extends View> extends PagerAdapter {
    
    private SparseArray<T> mViews = new SparseArray<T>();
    private List<T> mRemovedViews = new ArrayList<T>();
    
    protected Context mContext;
    
    public BasePagerAdapter(Context context) {
        mContext = context;
    }
    
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int count = getCount();
        if (position >= 0 && position < count) {
            T view = createView(container, position);
            update(view, position);
            container.addView(view);
            mViews.put(position, view);
            return view;
        }
        return super.instantiateItem(container, position);
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            T child = (T) object;
            if (container.indexOfChild(child) >= 0) {
                container.removeView(child);
                mViews.remove(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int getItemPosition(Object object) {
        try {
            T child = (T) object;
            int index = mViews.indexOfValue(child);
            if (index >= 0 && index < mViews.size()) {
                return mViews.keyAt(index);
            }
        } catch (Exception e) {
            
        }
        return POSITION_NONE;
    }
    
    public T getViewByPos(int pos) {
        return mViews.get(pos);
    }
    
    public abstract T createView(ViewGroup container, int position);
    
    public abstract void update(T view, int position);
    
}

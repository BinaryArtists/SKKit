package com.spark.demo.thirdparty.calender;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.spark.demo.thirdparty.adapter.BaseAdapter;

import java.util.List;

/**
 * @author huangming
 * @date 2015-7-28
 */
public class DayGridAdapter extends BaseAdapter<Day> {
    
    public DayGridAdapter(Context context, List<Day> list) {
        super(context, list);
    }
    
    @Override
    public View createView(Context context, ViewGroup parent) {
        return new DayView(context);
    }
    
    @Override
    public BaseAdapter.ViewHolder<Day> createViewHolder() {
        return new DayViewHolder();
    }
    
    public static class DayViewHolder extends BaseAdapter.ViewHolder<Day> {
        
        DayView dayView;
        
        @Override
        public void init(Context context, View convertView) {
            if (convertView instanceof DayView) {
                dayView = (DayView) convertView;
            }
        }
        
        @Override
        public void update(Context context, Day data) {
            if (dayView != null) {
                dayView.update(data);
            }
        }
    }
    
}

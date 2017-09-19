package com.spark.demo.thirdparty.calender;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * @author huangming
 * @date 2015-7-28
 */
public abstract class DayPagerAdapter extends BasePagerAdapter<GridView> {
    
    public DayPagerAdapter(Context context) {
        super(context);
    }
    
    @Override
    public GridView createView(ViewGroup container, int position) {
        return createGridView(container, position);
    }
    
    @Override
    public void update(GridView view, int position) {
        view.setAdapter(createGridAdapter(mContext, createDays(position)));
    }
    
    @Deprecated
    public GridView getGridViewByPos(int pos) {
        return getViewByPos(pos);
    }

    @Deprecated
    public GridView createGridView(ViewGroup container, int position) {
        return new GridView(container.getContext());
    }
    
    public DayGridAdapter createGridAdapter(Context context, List<Day> days) {
        return new DayGridAdapter(context, days);
    }
    
    public abstract List<Day> createDays(int position);
    
}

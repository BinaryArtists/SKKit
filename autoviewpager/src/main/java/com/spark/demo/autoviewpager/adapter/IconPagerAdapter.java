package com.spark.demo.autoviewpager.adapter;

import com.spark.demo.autoviewpager.page.Page;

import java.util.List;

public abstract class IconPagerAdapter extends IconLoopPagerAdapter implements IIconIndicator {

    private boolean mMustRefresh = false;

    public IconPagerAdapter(List<Page> pages) {
        super(pages);
    }

    public IconPagerAdapter setMustRefresh(boolean mustRefresh){
        this.mMustRefresh  = mustRefresh;
        return this;
    }

    @Override
    public int getItemPosition(Object object) {
        if (mMustRefresh) return POSITION_NONE;  // TODO: 2016/8/24  可能会导致系统开销过大， 后期优化
        return super.getItemPosition(object);
    }

    @Override
    public int getItemCount() {
        return pages != null ? pages.size() : 0;
    }
    
}

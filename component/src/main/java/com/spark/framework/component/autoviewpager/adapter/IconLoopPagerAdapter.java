package com.spark.framework.component.autoviewpager.adapter;


import com.spark.framework.component.autoviewpager.page.Page;

import java.util.List;

public abstract class IconLoopPagerAdapter extends LoopPagerAdapter implements
        IIconIndicator {
    
    public IconLoopPagerAdapter(List<Page> pages) {
        super(pages);
    }
    
    @Override
    public int getCount() {
        int itemCount = getItemCount();
        return itemCount > 1 ? Integer.MAX_VALUE : itemCount;
    } 
    
    @Override
    public int getItemCount() {
        return pages != null ? pages.size() : 0;
    }
    
}

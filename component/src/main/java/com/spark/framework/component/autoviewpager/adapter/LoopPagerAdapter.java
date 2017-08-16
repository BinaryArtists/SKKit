package com.spark.framework.component.autoviewpager.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.spark.framework.component.autoviewpager.page.Page;

import java.util.List;

/**
 * 可以无限循环的ViewPager
 *
 */
public class LoopPagerAdapter extends ViewPagerAdapter {
    
    public LoopPagerAdapter(List<Page> pages) {
        super(pages);
    }

    @Override
    public int getCount() {
        int itemCount = pages.size();
        return itemCount > 1 ? Integer.MAX_VALUE : itemCount;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        int realSize = pages != null ? pages.size() : 0;
        if (realSize <= 0) {
            return null;
        }
        final int realPos = position % realSize;
        final Context context = container.getContext();
        final Page page = pages.get(realPos);
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
    
    @Override
    public int getItemPosition(Object object) {
        return POSITION_UNCHANGED;
    }

}

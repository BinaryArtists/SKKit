package com.spark.framework.component.autoviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.spark.framework.component.autoviewpager.adapter.IIconIndicator;
import com.spark.framework.component.autoviewpager.viewpager.PageIndicator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class IconPageIndicator extends HorizontalScrollView implements PageIndicator {
    private static final String TAG = "ViewPagerDemoActivity";
    private final LinearLayout mIconsLayout;
    
    private ViewPager mViewPager;
    private OnPageChangeListener mListener;
    private Runnable mIconSelector;
    private int mSelectedIndex;
    
    public IconPageIndicator(Context context) {
        this(context, null);
    }
    
    public IconPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        
        mIconsLayout = new LinearLayout(context);
        addView(mIconsLayout,
                new LayoutParams(WRAP_CONTENT, MATCH_PARENT, Gravity.CENTER));
    }
    
    private void animateToIcon(final int position) {
        final View iconView = mIconsLayout.getChildAt(position);
        if (mIconSelector != null) {
            removeCallbacks(mIconSelector);
        }
        mIconSelector = new Runnable() {
            public void run() {
                final int scrollPos = iconView.getLeft()
                        - (getWidth() - iconView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mIconSelector = null;
            }
        };
        post(mIconSelector);
    }
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mIconSelector != null) {
            // Re-post the selector we saved
            post(mIconSelector);
        }
    }
    
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mIconSelector != null) {
            removeCallbacks(mIconSelector);
        }
    }
    
    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }
    
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }
    
    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        Log.i(TAG, "222 onPageSelected");
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
    }
    
    @Override
    public void setViewPager(ViewPager view) {
//        if (mViewPager == view) {
//            return;
//        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }
    
    public void notifyDataSetChanged() {
        mIconsLayout.removeAllViews();
        IIconIndicator iIconIndicator = (IIconIndicator) mViewPager.getAdapter();
        int count = iIconIndicator.getItemCount();
        for (int i = 0; i < count; i++) {
            ImageView view = iIconIndicator.getIndicatorIcon(getContext(), mIconsLayout);
            mIconsLayout.addView(view);
        }
        if (mSelectedIndex > count) {
            mSelectedIndex = count - 1;
        }
        setCurrentItem(mSelectedIndex);
        requestLayout();
    }
    
    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }
    
    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        IIconIndicator iIconIndicator = (IIconIndicator) mViewPager.getAdapter();
        int itemCount = iIconIndicator.getItemCount();
        mSelectedIndex = itemCount <= 0 ? 0 : item % itemCount;
        mViewPager.setCurrentItem(item);
        
        int tabCount = mIconsLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            View child = mIconsLayout.getChildAt(i);
            boolean isSelected = (i == mSelectedIndex);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToIcon(mSelectedIndex);
            }
        }
    }
    
    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }
    
    @Override
    public int getCurrentItem() {
        return mSelectedIndex;
    }
}

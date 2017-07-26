package com.spark.demo.autoviewpager.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以自动轮播的 ViewPager
 *
 *  @author spark
 */
public class AutoSlidePager extends ViewPager {
    // 最小的间隔时间
    private static final long MIN_INTERVAL = 1000;
    
    private static final int MSG_AUTO_SLIDE = 1;

    // 间隔时间
    private long mInterval = 2000;
    // 是否停止自动轮播
    private boolean mIsEndAuto = true;
    
    public AutoSlidePager(Context context) {
        this(context, null);
    }
    
    public AutoSlidePager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        commonKey(ev);
        return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        commonKey(ev);
        return super.onTouchEvent(ev);
    }
    
    private void commonKey(MotionEvent ev) {
        if(mIsEndAuto) {
            return;
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 如果用户有操作，就暂停自动轮播
                pauseAutoSlide();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startAutoSlide(mInterval);
                break;
            default:
                break;
        }
    }
    
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_AUTO_SLIDE) {
                if (getAdapter() != null) {
                    int count = getAdapter().getCount();
                    int index = getCurrentItem();
                    if (index >= 0 && index < count - 1) {
                        setCurrentItem(index + 1);
                        mHandler.sendEmptyMessageDelayed(MSG_AUTO_SLIDE, mInterval);
                    }
                }
            }
            
        }
    };

    /**
     * 开启自动轮播
     * @param interval  自己轮播的间隔时间
     */
    public void startAutoSlide(long interval) {
        mIsEndAuto = false;
        mHandler.removeMessages(MSG_AUTO_SLIDE);
        mInterval = Math.max(MIN_INTERVAL, interval);
        mHandler.sendEmptyMessageDelayed(MSG_AUTO_SLIDE, mInterval);
    }

    /**
     * 停止自动轮播
     */
    public void endAutoSlide() {
        mIsEndAuto = true;
        mHandler.removeMessages(MSG_AUTO_SLIDE);
    }

    /**
     * 暂停自动轮播
     */
    private void pauseAutoSlide() {
        mHandler.removeMessages(MSG_AUTO_SLIDE);
    }
    
}

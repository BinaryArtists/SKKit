package com.spark.demo.thirdparty.calender;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author huangming
 * @date 2015-7-28
 */
public class DayView extends RelativeLayout {

    protected Day mDay;

    public DayView(Context context) {
        this(context, null);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    public void update(Day day) {
        if (day != null) {
            mDay = day;
            Drawable bg = getBackground();
            if (bg != null) {
                bg.setLevel(day.isSelected() ? 1 : 0);
            }
//            for (int i = 0; i < getChildCount(); i++) {
//                Drawable bgChild = getChildAt(i).getBackground();
//                if (bgChild != null) {
//                    bgChild.setLevel(day.isSelected() ? 1 : 0);
//                }
//            }

        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDay != null) {
            mDay.destory();
        }
        mDay = null;
    }

}

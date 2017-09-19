package com.spark.demo.thirdparty.calender;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;


import com.spark.demo.thirdparty.R;

import java.util.Calendar;


public class CourseDayView extends DayView {

    private ImageView mRemainImg;
    private TextView mDayTv;
    private TextView mHasClassTv;

    public CourseDayView(Context context) {
        this(context, null);
    }

    public CourseDayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseDayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRemainImg = (ImageView) findViewById(R.id.img_remain);
        mDayTv = (TextView) findViewById(R.id.tv_course_day);
        mHasClassTv = (TextView) findViewById(R.id.tv_has_class);
    }

    @Override
    public void update(Day day) {
        super.update(day);

        if ((day instanceof CourseDay) && mRemainImg != null && mDayTv != null
                && mHasClassTv != null) {

            final Resources res = getResources();

//            Day today = new Day(NetworkTime.currentTimeMillis());
            Day today = new Day(Calendar.getInstance().getTime());

            CourseDay courseDay = (CourseDay) day;
            final int flag = courseDay.getFlag();

            // -- CourseDay.FLAG_HAS_CLASS  --> 有课
            // -- CourseDay.FLAG_HAS_CLASS_COMPLETE --> 已结课
            // -- CourseDay.FLAG_HAS_CLASS_ATTENTIONS --> 上课中
            // -- CourseDay.FLAG_NONE  --> 没课

//             boolean inWeek = (flag & CourseDay.FLAG_IN_WEEK) != 0;
            boolean inWeek = (courseDay.getmWeekMonthFlag() == CourseDay.FLAG_IN_WEEK);
            //boolean inMonth = (flag & CourseDay.FLAG_IN_MONTH) != 0;
            boolean inMonth = !inWeek;
            //boolean hasClass = (flag & CourseDay.FLAG_HAS_CLASS) != 0;
            boolean hasClass = flag == CourseDay.FLAG_HAS_CLASS;
            //boolean hasClassComplete = (flag & CourseDay.FLAG_HAS_CLASS_COMPLETE) != 0;
            boolean hasClassComplete = flag == CourseDay.FLAG_HAS_CLASS_COMPLETE;
            //boolean isRemain = (flag & CourseDay.FLAG_HAS_CLASS_ATTENTIONS) != 0;
            boolean isRemain = flag == CourseDay.FLAG_HAS_CLASS_ATTENTIONS;
            boolean isToday = Day.isSameDay(courseDay, today);

            boolean isAfaterToday = Day.isAfterToday(courseDay, today);

            boolean isCurrentMonth = courseDay.isCurrent();

            int dayTextColor;
            int hasClassTextColor = !hasClassComplete ? res
                    .getColor(R.color.teacher_blue) : res.getColor(R.color.noMonth);


            if (inMonth) {
                if (isToday) {
                    dayTextColor = res.getColor(R.color.teacher_blue);
                } else if (isAfaterToday && isCurrentMonth) {
                    dayTextColor = res.getColor(R.color.ToDayText);
                } else {
                    dayTextColor = res.getColor(R.color.noMonth);
                }

                if (day.isSelected()) {
                    dayTextColor = res.getColor(R.color.white);
                    mDayTv.setBackgroundResource(R.drawable.calendar_item_selected_v2);
                } else {
                    mDayTv.setBackgroundResource(0);
                }

//                mRemainImg.setVisibility(View.GONE);
//                if ((hasClass || isRemain) && isCurrentMonth) {
//                    if (hasClass) {
//                        mHasClassTv.setText("有课");
//                    }
//                    if (hasClassComplete){
//                        mHasClassTv.setText("已结课");
//                    }
//                    if (isRemain){
//                        mHasClassTv.setText("上课中");
//                    }
//                    mHasClassTv.setVisibility(View.VISIBLE);
//                }
//                else {
//                    mHasClassTv.setVisibility(View.GONE);
//                }

//                if (isRemain && isCurrentMonth) {
////                    mRemainImg.setVisibility(View.VISIBLE);
//                    mRemainImg.setVisibility(View.GONE);
//                    mHasClassTv.setText("上课中");
//                    mHasClassTv.setVisibility(VISIBLE);
//                }
//                else {
//                    mRemainImg.setVisibility(View.GONE);
//                    mHasClassTv.setVisibility(GONE);
//                }

            } else {
                if (isToday) {
                    dayTextColor = res.getColor(R.color.teacher_blue);
                } else if (isAfaterToday) {
                    dayTextColor = res.getColor(R.color.ToDayText);
                } else {
                    dayTextColor = res.getColor(R.color.noMonth);
                }
                if (day.isSelected()) {
                    dayTextColor = res.getColor(R.color.white);
                    mDayTv.setBackgroundResource(R.drawable.calendar_item_selected_v2);
                } else {
                    mDayTv.setBackgroundResource(0);
                }

//                mRemainImg.setVisibility(View.GONE);
//                if (hasClass || isRemain) {
//                    if (hasClass) {
//                        mHasClassTv.setText("有课");
//                    }
//                    if (hasClassComplete){
//                        mHasClassTv.setText("已结课");
//                    }
//                    if (isRemain){
//                        mHasClassTv.setText("上课中");
//                    }
//                    mHasClassTv.setVisibility(View.VISIBLE);
//                }
//                else {
//                    mHasClassTv.setVisibility(View.GONE);
//                }

//                if (isRemain) {
////                    mRemainImg.setVisibility(View.VISIBLE);
//                    mRemainImg.setVisibility(View.GONE);
//                }
//                else {
//                    mRemainImg.setVisibility(View.GONE);
//                }
            }

            mDayTv.setText("" + courseDay.getDayOfMonth());
            mDayTv.setTextColor(dayTextColor);
            String hasClassText = "";
            mRemainImg.setVisibility(GONE);
            if (hasClass) {
                hasClassText = "有课";
                mHasClassTv.setVisibility(VISIBLE);
            } else if (hasClassComplete) {
                hasClassText = "已结";
                mHasClassTv.setVisibility(VISIBLE);
            } else if (isRemain) {
                hasClassText = "上课中";
                mHasClassTv.setVisibility(VISIBLE);
            } else {
                mHasClassTv.setVisibility(GONE);
            }
            mHasClassTv.setText(hasClassText);
            mHasClassTv.setTextColor(hasClassTextColor);
//            mHasClassTv.setCompoundDrawablesWithIntrinsicBounds(hasClassDrawable, null,
//                    null, null);
        }
    }

}

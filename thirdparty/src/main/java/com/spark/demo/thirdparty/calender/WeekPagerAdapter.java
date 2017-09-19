package com.spark.demo.thirdparty.calender;

import android.content.Context;

import java.util.List;

/**
 * @author huangming
 * @date 2015-7-27
 */
public class WeekPagerAdapter extends DayPagerAdapter {
    
    public WeekPagerAdapter(Context context) {
        super(context);
    }
    
    @Override
    public int getCount() {
        return CalendarController.WEEK_COUNT;
    }
    
    @Override
    public List<Day> createDays(int position) {
        return CalendarController.getWeekDaysByIndex(position);
    }
    
}

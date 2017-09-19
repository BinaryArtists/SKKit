package com.spark.demo.thirdparty.calender;

import android.content.Context;

import java.util.List;

/**
 * @author huangming
 * @date 2015-7-27
 */
public class MonthPagerAdapter extends DayPagerAdapter {
    
    public MonthPagerAdapter(Context context) {
        super(context);
    }
    
    @Override
    public int getCount() {
        return CalendarController.MONTH_COUNT;
    }
    
    @Override
    public List<Day> createDays(int position) {
        return CalendarController.getMonthDaysByIndex(position);
    }
    
}

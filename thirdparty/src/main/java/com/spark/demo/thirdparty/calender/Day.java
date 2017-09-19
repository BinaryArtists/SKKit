package com.spark.demo.thirdparty.calender;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author huangming
 * @date 2015-7-28
 */
public class Day {
    
    private int mKey;
    
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private long mMilliSeconds;
    private int mDayOfWeek;
    private boolean mSelected;
    private boolean mCurrent = true;
    
    public Day(Day day) {
        setMilliseconds(day.getMilliSeconds());
        setCurrent(day.isCurrent());
        setSelected(day.isSelected());
    }
    
    public Day(long milliseconds) {
        setMilliseconds(milliseconds);
    }
    
    public Day(Date date) {
        setMilliseconds(date.getTime());
    }
    
    public Day(int year, int month, int dayOfMonth) {
        setDate(year, month, dayOfMonth);
    }
    
    public void setMilliseconds(long milliseconds) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.setTimeInMillis(milliseconds);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        mDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        mMilliSeconds = milliseconds;
        mKey = CalendarController.getKeyByDate(mYear, mMonth, mDayOfMonth);
    }
    
    public void setDate(int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mYear = year;
        mMonth = month;
        mDayOfMonth = dayOfMonth;
        mDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        mMilliSeconds = c.getTimeInMillis();
        mKey = CalendarController.getKeyByDate(mYear, mMonth, mDayOfMonth);
    }
    
    public boolean isToday() {
        return false;
    }
    
    public int getYear() {
        return mYear;
    }
    
    public int getMonth() {
        return mMonth;
    }
    
    public int getDayOfMonth() {
        return mDayOfMonth;
    }
    
    public int getDayOfWeek() {
        return mDayOfWeek;
    }
    
    public long getMilliSeconds() {
        return mMilliSeconds;
    }
    
    public int getKey() {
        return mKey;
    }
    
    public void setSelected(boolean selected) {
        mSelected = selected;
    }
    
    public boolean isSelected() {
        return mSelected;
    }
    
    public boolean isCurrent() {
        return mCurrent;
    }
    
    public void setCurrent(boolean current) {
        mCurrent = current;
    }
    
    public void destory() {
        // nothing
    }
    
    public static boolean isSameDay(Day day1, Day day2) {
        return day1 != null && day2 != null && day1.getYear() == day2.getYear()
                && day1.getMonth() == day2.getMonth()
                && day1.getDayOfMonth() == day2.getDayOfMonth();
    }
    
    public static boolean isAfterToday(Day day, Day today) {
        return day.getYear() > today.getYear()
                || (day.getYear() == today.getYear() && day.getMonth() > today.getMonth())
                || (day.getYear() == today.getYear()
                        && day.getMonth() == today.getMonth() && day.getDayOfMonth() > today
                        .getDayOfMonth());
    }
    
    @Override
    public String toString() {
        String res  = mYear+"-";
        if (mMonth >0&& mMonth <=9){
            res += "0";
        }
        res+= mMonth+"-";
        if (mDayOfMonth >0 && mDayOfMonth <=9){
            res += "0";
        }
        res+= mDayOfMonth;
//        return mYear + "-" + mMonth + "-" + mDayOfMonth;
        return  res;
    }
    
}

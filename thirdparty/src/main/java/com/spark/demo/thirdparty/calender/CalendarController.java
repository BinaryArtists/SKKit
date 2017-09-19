package com.spark.demo.thirdparty.calender;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author huangming
 * @date 2015-7-28
 */
public class CalendarController {
    
    public static final long ONE_MIN = 60 * 1000;
    
    public static final long ONE_HOUR = 60 * ONE_MIN;
    
    public static final long ONE_DAY = 24 * ONE_HOUR;
    
    public static final long ONE_WEEK = 7 * ONE_DAY;
    
    public static final int MONTH_COUNT_OF_YEAR = 12;
    public static final int DAY_COUNT_OF_WEEK = 7;
    public static final int WEEK_MAX_COUNT_OF_MONTH = 6;
    
    public static final int MIN_CALENDAR_YEAR = 1970;
    public static final int MAX_CALENDAR_YEAR = 2050;
    public static final int MIN_CALENDAR_WEEK = 0;
    public static final int MAX_CALENDAR_WEEK = getTotalWeekFromMinYear(
            MAX_CALENDAR_YEAR, 12, 31); // weeks
    
    public static final int MAX_CALENDAR_DAY = getTotalDaysOfYear(MIN_CALENDAR_YEAR,
            MAX_CALENDAR_YEAR); // days
    // between
    // 1/1/1970
    // and 12/31/2050
    
    public static final int WEEK_COUNT = MAX_CALENDAR_WEEK - MIN_CALENDAR_WEEK;
    
    public static final int MONTH_COUNT = MAX_CALENDAR_YEAR * MONTH_COUNT_OF_YEAR
            - MIN_CALENDAR_YEAR * MONTH_COUNT_OF_YEAR;
    
    public static final long START_TIME_MILLIS = getTimeInMillis(MIN_CALENDAR_YEAR, 1, 1);

    private static  final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public static long getTimeInMillis(int year, int month, int day) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTimeInMillis();
    }
    
    public static long getTimeInMillis(int year, int month, int day, int field, int value) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(field, value);
        return c.getTimeInMillis();
    }

    public static String[] getStartEndDateString (int year, int month, int day) {
        String[] arr  = new String[2];
        Calendar c  = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.MONTH, -1);
        arr[0]= sdf.format(c.getTime());
        c.add(Calendar.MONTH, 3);
        arr[1] = sdf.format(c.getTime());
        return arr;
    }
    
    /**
     * 指定日期为当年第几周
     */
    public static int getWeekOfYear(int year, int month, int day) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * 获取指定月份的总天数
     */
    public static int getCurrentMonthDays(int month) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.set(Calendar.MONTH, month);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 获取指定年份有多少周
     */
    public static int getTotalWeekOfYear(int year) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        return c.getActualMaximum(Calendar.WEEK_OF_YEAR);
    }
    
    public static int getTotalDaysOfYear(int startYear, int endYear) {
        int totalDays = 0;
        for (int i = startYear; i <= endYear; i++) {
            totalDays += getTotalDaysOfYear(i);
        }
        return totalDays;
    }
    
    /**
     * 获取指定年份有多少天
     */
    public static int getTotalDaysOfYear(int year) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        return c.getActualMaximum(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * 判断是否是当前月
     */
    public static boolean isCurrentMonth(int year, int month) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        return c.get(Calendar.MONTH) == month - 1 && year == c.get(Calendar.YEAR);
    }
    
    /**
     * 判断是否是今天
     */
    public static boolean isToday(int year, int month, int day) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        return day == c.get(Calendar.DAY_OF_MONTH) && month == c.get(Calendar.MONTH)
                && year == c.get(Calendar.YEAR);
    }
    
    /**
     * 计算指定的月份共有多少天
     */
    public static int getTotalDaysOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    public static List<Day> getMonthDays(int year, int month) {
        // 1970-1-1为第一个月
        return getMonthDaysByIndex(getMonthIndex(year, month));
    }
    
    public static List<Day> getMonthDaysByIndex(int index) {
        // 1970-1-1为第一个月
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, MIN_CALENDAR_YEAR + index / MONTH_COUNT_OF_YEAR);
        c.set(Calendar.MONTH, index % MONTH_COUNT_OF_YEAR);
        c.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfFirstWeek = c.get(Calendar.DAY_OF_WEEK);
        int realDayOfFirstWeek = getRealDayOfWeek(dayOfFirstWeek);
        int daysOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        long curMillis = c.getTimeInMillis();
        // long startMillis = dayOfFirstWeek > Calendar.SUNDAY ? curMillis -
        // ONE_DAY
        // * (dayOfFirstWeek - 1) : curMillis;
        List<Day> days = new ArrayList<Day>();
        for (int i = 1; i <= DAY_COUNT_OF_WEEK * WEEK_MAX_COUNT_OF_MONTH; i++) {
            Day day = new Day(curMillis + (i - realDayOfFirstWeek) * ONE_DAY);
            day.setCurrent(i >= realDayOfFirstWeek
                    && i <= daysOfMonth - 1 + realDayOfFirstWeek);
            days.add(day);
        }
        // for (int i = 0; i < DAY_COUNT_OF_WEEK * WEEK_MAX_COUNT_OF_MONTH; i++)
        // {
        // //
        // Day day = new Day(startMillis + i * ONE_DAY);
        // // 判断是否在当月
        // day.setCurrent(i >= dayOfFirstWeek - 1
        // && i < dayOfFirstWeek - 1 + daysOfMonth);
        // days.add(day);
        // }
        return days;
    }
    
    public static int getMonthIndex(int year, int month) {
        return (year - MIN_CALENDAR_YEAR) * MONTH_COUNT_OF_YEAR + month - 1;
    }
    
    public static List<Day> getWeekDays(int year, int month, int dayOfMonth) {
        // add by hm
        return getWeekDaysByIndex(getWeekIndex(year, month, dayOfMonth));
        
        // modify by hm
        // Calendar c = Calendar.getInstance(Locale.getDefault());
        // c.clear();
        // c.set(Calendar.YEAR, year);
        // c.set(Calendar.MONTH, month - 1);
        // c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        // long curMillis = c.getTimeInMillis();
        // long startMillis = dayOfWeek > Calendar.SUNDAY ? curMillis - ONE_DAY
        // * (dayOfWeek - 1) : curMillis;
        //
        // List<Day> days = new ArrayList<Day>();
        // for (int i = 0; i < DAY_COUNT_OF_WEEK; i++) {
        // Day day = new Day(startMillis + i * ONE_DAY);
        // days.add(day);
        // }
        // return days;
    }
    
    public static int getDayIndex(int year, int month, int dayOfMonth) {
        int totalDays = 0;
        for (int i = MIN_CALENDAR_YEAR; i < year; i++) {
            totalDays += getTotalDaysOfYear(i);
        }
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        totalDays += c.get(Calendar.DAY_OF_YEAR);
        return totalDays - 1;
    }
    
    public static List<Day> getWeekDaysByIndex(int index) {
        // 1970-1-1为第一周，index=0
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.setTimeInMillis(START_TIME_MILLIS + index * ONE_WEEK);
        
        // add by hm
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int realDayOfWeek = getRealDayOfWeek(dayOfWeek);
        long curMillis = c.getTimeInMillis();
        // long startMillis = dayOfWeek > Calendar.SUNDAY ? curMillis - ONE_DAY
        // * (dayOfWeek - 1) : curMillis;
        
        List<Day> days = new ArrayList<Day>();
        for (int i = 1; i <= DAY_COUNT_OF_WEEK; i++) {
            Day day = new Day(curMillis + (i - realDayOfWeek) * ONE_DAY);
            days.add(day);
        }
        return days;
    }
    
    public static int getWeekIndex(int year, int month, int dayOfMonth) {
        return getTotalWeekFromMinYear(year, month, dayOfMonth) - 1;
        // Calendar c = Calendar.getInstance(Locale.getDefault());
        // c.clear();
        // c.set(Calendar.YEAR, year);
        // c.set(Calendar.MONTH, month - 1);
        // c.set(Calendar.DAY_OF_MONTH, day);
        // int weekOfYear = c.get(Calendar.WEEK_OF_YEAR);
        // if (weekOfYear == 1 && month == MONTH_COUNT_OF_YEAR) {
        // return getTotalWeekFromMinYear(year);
        // }
        // else {
        // return getTotalWeekFromMinYear(year - 1) + weekOfYear - 1;
        // }
    }
    
    // 适配：周显示从星期一开始，以前是从星期天开始
    public static int getTotalWeekFromMinYear(int curYear, int curMonth, int curDayOfMonth) {
        // int totalWeek = 0;
        // for (int i = MIN_CALENDAR_YEAR; i <= curYear; i++) {
        // totalWeek += getTotalWeekOfYear(i);
        // }
        int totalWeeks = 0;
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(MIN_CALENDAR_YEAR, 0, 1);
        long startTime = c.getTimeInMillis();
        int startDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int startRealDayOfWeek = getRealDayOfWeek(startDayOfWeek);
        
        c.clear();
        c.set(curYear, curMonth - 1, curDayOfMonth);
        long curTime = c.getTimeInMillis();
        // int curDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        // int curRealDayOfWeek = getRealDayOfWeek(curDayOfWeek);
        if (curTime >= startTime) {
            int totalDays = (int) ((curTime - startTime) / ONE_DAY);
            int weeks = totalDays / DAY_COUNT_OF_WEEK;
            // 求余
            int modDays = totalDays % DAY_COUNT_OF_WEEK;
            totalWeeks = weeks + 1
                    + ((modDays + startRealDayOfWeek > DAY_COUNT_OF_WEEK) ? 1 : 0);
        }
        return totalWeeks;
    }
    
    public static int getRealDayOfWeek(int dayOfWeek) {
        int reelDayIndex = dayOfWeek - 1;
        return reelDayIndex >= 1 ? reelDayIndex : 7;
    }
    
    public static Day getDayByIndexAndWeek(int index, int dayOfWeek) {
        // 1970-1-1为第一周，index=0
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.setTimeInMillis(START_TIME_MILLIS + index * ONE_WEEK);
        
        long startMillis = c.getTimeInMillis();
        
        int startDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        // add by hm
        int startRealDayOfWeek = getRealDayOfWeek(startDayOfWeek);
        
        int curRealDayOfWeek = getRealDayOfWeek(dayOfWeek);
        
        return new Day(startMillis + (curRealDayOfWeek - startRealDayOfWeek) * ONE_DAY);
        
        // int otherDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        // long curMillis = c.getTimeInMillis();
        // long startMillis = otherDayOfWeek > Calendar.SUNDAY ? curMillis -
        // ONE_DAY
        // * (otherDayOfWeek - 1) : curMillis;
        //
        // return new Day(startMillis + (dayOfWeek - 1) * ONE_DAY);
    }
    
    public static Day getDayByIndexAndMonth(int index, int dayOfMonth) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, MIN_CALENDAR_YEAR + index / MONTH_COUNT_OF_YEAR);
        c.set(Calendar.MONTH, index % MONTH_COUNT_OF_YEAR);
        int daysOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, Math.min(Math.max(1, dayOfMonth), daysOfMonth));
        return new Day(c.getTimeInMillis());
    }
    
    public static Day getDayByIndex(int index) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, MIN_CALENDAR_YEAR);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DATE, index);
        return new Day(c.getTimeInMillis());
    }
    
    public static int getKeyByTimeInMillis(long timeMillis) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.setTimeInMillis(timeMillis);
        return getKeyByDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
    }
    
    public static long getStartTime(long timeMillis) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.setTimeInMillis(timeMillis);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return c.getTimeInMillis();
    }
    
    public static int getKeyByDate(int year, int month, int day) {
        return year * 10000 + month * 100 + day;
    }

    /**
     * 通过字符串获取key
     * @param ymdStr  必须是 yyyy-MM-dd 格式的
     * @return
     */
    public static int getKeyByDateString (String ymdStr){
        if (TextUtils.isEmpty(ymdStr)) return -1;
        String[] arr =  ymdStr.split("-");
        try {
            return Integer.valueOf(arr[0]) * 10000  + Integer.valueOf(arr[1]) * 100 + Integer.valueOf(arr[2]) ;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return -1;
        }
    }
    
}

package com.spark.demo.thirdparty.datepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.spark.demo.thirdparty.R;
import com.spark.demo.thirdparty.log.Logger;
import com.spark.demo.thirdparty.wheelview.LoopView;
import com.spark.demo.thirdparty.wheelview.OnItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 时间选择View
 *
 * @author spark
 * @createtime 2016/11/30
 */
public class DatePickerV2 extends LinearLayout {


    private static final String TAG = "DatePickerV2";

    private static SimpleDateFormat sWeekFormat = new SimpleDateFormat("E");

    enum DateType {
        YEAR, MONTH, DAY
    }

    private LoopView mYearPicker;
    private LoopView mMonthPicker;
    private LoopView mDayPicker;

    private int mYear;
    private int mMonth;
    private int mDay;

    private boolean mYearVisible = true;
    private boolean mMonthVisible = true;
    private boolean mDayVisible = true;

    private int mMinYear;
    private int mMaxYear;

    private int mMinMonth;
    private int mMaxMonth;

    private int mCurrentMinMonth;
    private int mCurrentMaxMonth;

    private int mMinDay;
    private int mMaxDay;

    private int mCurrentMinDay;
    private int mCurrentMaxDay;

//    private List<Integer> mDayDatas = new ArrayList<Integer>();

    private List<String> mYears = new ArrayList<>();     // 年数据
    private List<String> mMonths = new ArrayList<>();     // 月数据
    private List<String> mDays = new ArrayList<>();      // 日数据
    private List<String> mApms = new ArrayList<>();      // 上午、下午等数据

    private View yearView, monthView, dayView, apmView;


    public DatePickerV2(Context context) {
        this(context, null);
    }

    public DatePickerV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        final LayoutInflater inflater = LayoutInflater.from(context);
        LayoutParams lp;

        yearView = inflater.inflate(R.layout.view_item_date_picker, this, false);
        mYearPicker = (LoopView) yearView.findViewById(R.id.lv_item);
        lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        addView(yearView, lp);
        mYearPicker.setListener(mYearSelectedListener);

        monthView = inflater.inflate(R.layout.view_item_date_picker, this, false);
        mMonthPicker = (LoopView) monthView.findViewById(R.id.lv_item);
        lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        addView(monthView, lp);
        mMonthPicker.setListener(mMonthSelectedListener);

        dayView = inflater.inflate(R.layout.view_item_date_picker, this, false);
        mDayPicker = (LoopView) dayView.findViewById(R.id.lv_item);
        lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        addView(dayView, lp);
        mDayPicker.setListener(mDaySelectedListener);

        setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void setDate(int year, int minYear, int maxYear) {
        setDate(year, 1, 1, minYear, maxYear);
    }

    public void setDate(int year, int month, int day, int minYear, int maxYear) {
        if (maxYear < minYear) {
            throw new RuntimeException("maxYear < minYear");
        }
        mMaxYear = maxYear;
        mMinYear = minYear;
        if (year < minYear) {
            mYear = minYear;
        } else if (year > maxYear) {
            mYear = maxYear;
        } else {
            mYear = year;
        }

        mMonth = month;
        mMinMonth = 1;
        mMaxMonth = 12;

        mDay = day;
        mMinDay = 1;
        mMaxDay = 31;

        upadteYearData();
//        updateMonthData();
//        updateDaysData(year, month);

    }

    public void setDate(int year, int minYear, int maxYear,
                        int month, int minMonth, int maxMonth,
                        int day, int minDay, int maxDay) {
        if (maxYear < minYear) {
            throw new RuntimeException("maxYear < minYear");
        }
//        if (maxMonth < minMonth) {
//            throw new RuntimeException("maxMonth < minMonth");
//        }
//        if (maxDay < minDay) {
//            throw new RuntimeException("maxDay < minDay");
//        }

        mMaxYear = maxYear;
        mMinYear = minYear;
        mMaxMonth = maxMonth;
        mMinMonth = minMonth;
        mMinDay = minDay;
        mMaxDay = maxDay;

        if (year < minYear) {
            mYear = minYear;
        } else if (year > maxYear) {
            mYear = maxYear;
        } else {
            mYear = year;
        }

        this.mMonth = month;
        this.mDay = day;

//        if (month < minMonth) {
//            mMonth = minMonth;
//        } else if (month > maxMonth) {
//            mMonth = maxMonth;
//        } else {
//            mMonth = month;
//        }

//        if (day < minDay) {
//            mDay = minDay;
//        } else if (day > maxDay) {
//            mDay = maxDay;
//        } else {
//            mDay = day;
//        }

        upadteYearData();
//        updateMonthData();
//        updateDaysData(year, month);

    }


    /**
     * 更新年数据
     */
    private void upadteYearData() {
        mYears.clear();
        int index = 0;
        for (int i = mMinYear; i <= mMaxYear; i++) {
            mYears.add(i + "年");
            if (mYear == i) {
                index = i - mMinYear;
            }
        }
        mYearPicker.setItems(mYears);
        mYearPicker.setInitPosition(index);

        // 更新月列表
        // mYear  = mMinYear;
        updateMonthData();
    }

    /**
     * 更新月数据
     */
    private void updateMonthData() {
        mMonths.clear();
        mCurrentMinMonth = 1;
        mCurrentMaxMonth = 12;
        if (mYear == mMinYear) {
            mCurrentMinMonth = mMinMonth;
        }
        if (mYear == mMaxYear) {
            mCurrentMaxMonth = mMaxMonth;
        }
        int index = 0;
        for (int i = mCurrentMinMonth; i <= mCurrentMaxMonth; i++) {
            mMonths.add(i + "月");
            if (mMonth == i) {
                index = i - mCurrentMinMonth;
            }
        }
        mMonthPicker.setItems(mMonths);
        mMonthPicker.setInitPosition(index);

        // 更新日列表
//        mMonth = mMinMonth;
        updateDaysData(mYear, mMonth);
    }

    /**
     * 更新日数据
     */
    private void updateDaysData(int year, int month) {
        mDays.clear();
        mCurrentMinDay = 1;
        mCurrentMaxDay = getMonthDays(year, month);
        if (mYear == mMinYear && month == mMinMonth) {
            mCurrentMinDay = mMinDay;
        }
        if (mYear == mMaxYear && month == mMaxMonth) {
            mCurrentMaxDay = mMaxDay;
        }

        int index = 0;
        for (int i = mCurrentMinDay; i <= mCurrentMaxDay; i++) {
            mDays.add(i + "日");
            if (index == mDay) {
                index = i - mCurrentMinDay;
            }
        }
        mDayPicker.setItems(mDays);
        mDayPicker.setInitPosition(index);

//        mDay = mCurrentMinDay;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public void setDateVisible(boolean yearVisible, boolean monthVisible,
                               boolean dayVisible) {
        mYearVisible = yearVisible;
        mMonthVisible = monthVisible;
        mDayVisible = dayVisible;

        yearView.setVisibility(mYearVisible ? VISIBLE : GONE);
        monthView.setVisibility(mMonthVisible ? VISIBLE : GONE);
        dayView.setVisibility(mDayVisible ? VISIBLE : GONE);
    }

    private OnItemSelectedListener mYearSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(int index) {
            Logger.d(TAG, "mYearSelectedListener---index:" + index);
            mYear = mMinYear + index;

            updateMonthData();

//            upadteDaysData(mYear, mMonth);
        }
    };

    private OnItemSelectedListener mMonthSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(int index) {
            Logger.d(TAG, "mMonthSelectedListener---index:" + index);

            mMonth = mCurrentMinMonth + index;
            updateDaysData(mYear, mMonth);
        }
    };

    private OnItemSelectedListener mDaySelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(int index) {
            Logger.d(TAG, "mDaySelectedListener---index:" + index);

            mDay = mCurrentMinDay + index;
        }
    };

    public static long getTime(int year, int month, int day) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTimeInMillis();
    }

    public static int getMonthDays(int year, int month) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}

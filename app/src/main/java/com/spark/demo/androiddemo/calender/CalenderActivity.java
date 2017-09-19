package com.spark.demo.androiddemo.calender;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.spark.demo.androiddemo.R;
import com.spark.demo.thirdparty.calender.CalendarContainer;
import com.spark.demo.thirdparty.calender.CalendarController;
import com.spark.demo.thirdparty.calender.CourseDay;
import com.spark.demo.thirdparty.calender.Day;
import com.spark.demo.thirdparty.calender.DayGridAdapter;
import com.spark.demo.thirdparty.calender.DayPagerAdapter;
import com.spark.demo.thirdparty.calender.MonthPagerAdapter;
import com.spark.demo.thirdparty.calender.WeekPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2017/9/14.
 */

public class CalenderActivity extends AppCompatActivity {

    private static final String TAG = "CalenderActivity";
    private CalendarContainer mCalendarContainer;
    private ViewPager mMonthPager;
    private ViewPager mWeekPager;
    private ListView mListView;

    private DayPagerAdapter mMonthAdapter;
    private DayPagerAdapter mWeekAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        initView();


        mMonthAdapter = new MonthPagerAdapter(this) {
            @Override
            public GridView createGridView(ViewGroup container, int position) {
                GridView gridView = (GridView) LayoutInflater
                        .from(container.getContext()).inflate(R.layout.view_day_grid,
                                container, false);
                gridView.setNumColumns(CalendarController.DAY_COUNT_OF_WEEK);
                //gridView.setOnItemClickListener(mMonthGridCilckListener);
                return gridView;
            }

            @Override
            public List<Day> createDays(int position) {
                return getCourseDays(super.createDays(position), CourseDay.FLAG_IN_MONTH);
            }

            @Override
            public DayGridAdapter createGridAdapter(Context context, List<Day> days) {
                return new DayGridAdapter(context, days) {
                    @Override
                    public View createView(Context context, ViewGroup parent) {
                        return LayoutInflater.from(context).inflate(
                                R.layout.item_course_day, parent, false);
                    }
                };
            }
        };
        mMonthPager.setAdapter(mMonthAdapter);
//        mWeekPager.setOnPageChangeListener(mWeekPageListener);
        mWeekAdapter = new WeekPagerAdapter(this) {
            @Override
            public GridView createGridView(ViewGroup container, int position) {
                GridView gridView = super.createGridView(container, position);
                gridView.setNumColumns(CalendarController.DAY_COUNT_OF_WEEK);
//                gridView.setOnItemClickListener(mWeekGridCilckListener);
                return gridView;
            }

            @Override
            public List<Day> createDays(int position) {
                return getCourseDays(super.createDays(position), CourseDay.FLAG_IN_WEEK);
            }

            @Override
            public DayGridAdapter createGridAdapter(Context context, List<Day> days) {
                return new DayGridAdapter(context, days) {
                    @Override
                    public View createView(Context context, ViewGroup parent) {
                        return LayoutInflater.from(context).inflate(
                                R.layout.item_course_day, parent, false);
                    }
                };
            }

        };
        mWeekPager.setAdapter(mWeekAdapter);
    }

    private void initView() {
        mCalendarContainer = (CalendarContainer) findViewById(R.id.view_calendar_container);
//        mCalendarContainer.setOnCalendarTypeChangeListener(mOnCalendarTypeChangeListener);

        mMonthPager = mCalendarContainer.getMonthPager();
        mWeekPager = mCalendarContainer.getWeekPager();
        mListView = mCalendarContainer.getListView();


    }


    private List<Day> getCourseDays(List<Day> days, int flag) {
        List<Day> courseDays = new ArrayList<Day>();
        for (Day day : days) {
            int courseFlag = CourseDay.FLAG_NONE;
            courseFlag |= flag;

//            CourseDayStatus status = mLoadTeacherLessonListPresenter.getCourseStatusSparse().get(day.getKey());
//            if (status != null) {
//                // courseFlag |= CourseDay.FLAG_HAS_CLASS;
//                switch (status) {
//                    case HAVE_CLASS:
////                        courseFlag |= CourseDay.FLAG_HAS_CLASS;
//                        courseFlag = CourseDay.FLAG_HAS_CLASS;
//                        break;
//                    case CLASS_FINISHED:
////                        courseFlag |= CourseDay.FLAG_HAS_CLASS_COMPLETE;
//                        courseFlag = CourseDay.FLAG_HAS_CLASS_COMPLETE;
//                        break;
//                    case CLASS_GOING:
////                        courseFlag |= CourseDay.FLAG_HAS_CLASS_ATTENTIONS;
//                        courseFlag = CourseDay.FLAG_HAS_CLASS_ATTENTIONS;
//                        break;
//                    case NO_CLASS:
////                        courseFlag |= CourseDay.FLAG_NONE;
//                        courseFlag = CourseDay.FLAG_NONE;
//                        break;
//                    default:
//                        break;
//                }
//            }
            CourseDay courseDay = new CourseDay(day);
            courseDay.setFlag(courseFlag);
            // courseDay.setSelectedDay(mSelectedDay);
            courseDays.add(courseDay);
        }
        return courseDays;
    }


    private CalendarContainer.OnCalendarTypeChangeListener mOnCalendarTypeChangeListener = new CalendarContainer.OnCalendarTypeChangeListener() {

        @Override
        public void onCalendarTypeChange(CalendarContainer.CalendarType type) {
            Log.i(TAG, "mOnCalendarTypeChangeListener");
            if (type == CalendarContainer.CalendarType.Month) {
//                updateMonthGridByDay(mCurDay);
                refreshGridView(mMonthAdapter.getGridViewByPos(mMonthPager
                        .getCurrentItem()));
            } else if (type == CalendarContainer.CalendarType.Week) {
//                updateWeekGridByDay(mCurDay);
                refreshGridView(mWeekAdapter
                        .getGridViewByPos(mWeekPager.getCurrentItem()));
            }
        }
    };

    private void refreshGridView(GridView view) {
        if (view == null) {
            return;
        }
        if (view.getAdapter() instanceof android.widget.BaseAdapter) {
            android.widget.BaseAdapter adapter = (android.widget.BaseAdapter) view
                    .getAdapter();
            int count = adapter.getCount();
            for (int i = 0; i < count; i++) {
                Object obj = adapter.getItem(i);
                if (obj instanceof CourseDay) {
                    CourseDay day = (CourseDay) obj;
                    int courseFlag = CourseDay.FLAG_NONE;
                    int oldFlag = day.getFlag();
                    int weekMonthFlag = 0;
                    if ((oldFlag & CourseDay.FLAG_IN_WEEK) != 0) {
                        weekMonthFlag = CourseDay.FLAG_IN_WEEK;
                    }
                    if ((oldFlag & CourseDay.FLAG_IN_MONTH) != 0) {
                        weekMonthFlag = CourseDay.FLAG_IN_MONTH;
                    }

//                    CourseDayStatus status = mLoadTeacherLessonListPresenter.getCourseStatusSparse().get(day.getKey());
//                    if (status != null) {
//                        // courseFlag |= CourseDay.FLAG_HAS_CLASS;
//                        switch (status) {
//                            case HAVE_CLASS:
//                                // courseFlag |= CourseDay.FLAG_HAS_CLASS;
//                                courseFlag = CourseDay.FLAG_HAS_CLASS;
//                                break;
//                            case CLASS_FINISHED:
//                                //courseFlag |= CourseDay.FLAG_HAS_CLASS_COMPLETE;
//                                courseFlag = CourseDay.FLAG_HAS_CLASS_COMPLETE;
//                                break;
//                            case CLASS_GOING:
//                                //courseFlag |= CourseDay.FLAG_HAS_CLASS_ATTENTIONS;
//                                courseFlag = CourseDay.FLAG_HAS_CLASS_ATTENTIONS;
//                                break;
//                            case NO_CLASS:
//                                //courseFlag |= CourseDay.FLAG_NONE;
//                                courseFlag = CourseDay.FLAG_NONE;
//                                break;
//                            default:
//                                break;
//                        }
//                    }
                    day.setFlag(courseFlag);
                    day.setmWeekMonthFlag(weekMonthFlag);
                }
            }

//            if (mLoadTeacherLessonListPresenter.getTeacherLessonList().size() > 0){
//                mEmptyView.setVisibility(View.GONE);
//            } else{
//                mEmptyView.setVisibility(View.VISIBLE);
//            }

            adapter.notifyDataSetChanged();
        }

    }


}
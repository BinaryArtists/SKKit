package com.spark.demo.thirdparty.dlg;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.spark.demo.thirdparty.R;
import com.spark.demo.thirdparty.datepicker.DatePickerV2;


/**
 * 选择时间
 * <p>
 * Created by spark on 2016/11/09.
 */
public class SelectTimeDlg extends BaseDlg {

    protected SelectTimeDlg(Context context) {
        super(context);
    }

    protected SelectTimeDlg(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置全屏显示
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    @Override
    protected void setValue(DialogController.DialogParams p) {
        super.setValue(p);
    }

    public static class Builder {
        private DialogController.DialogParams P;
        private int mTheme;
        private SelectTimeDlg dlg = null;
        private int mYear, mMonth, mDay, mMinYear, mMaxYear, mMinMonth, mMaxMonth, mMinDay, mMaxDay;
        private boolean mIsYearVisible = true, mIsMonthVisible = true, mIsDayVisible = true;

        public interface SelectTimeListner {
            public void onConfirm(int year, int month, int day);
        }

        private SelectTimeListner mSelectTimeListner = null;

        public Builder setSelectTimeListener(SelectTimeListner selectTimeListner) {
            this.mSelectTimeListner = selectTimeListner;
            return this;
        }

        public Builder(Context context) {
            this(context, 0);
        }

        public Builder(Context context, int themeId) {
            mTheme = themeId;
            P = new DialogController.DialogParams(context);
        }

        /**
         * 值对应与Gravity中的几个常量
         *
         * @param gravity Gravity.TOP, Gravity.BOTTOM等
         * @return
         */
        public Builder setGravity(int gravity) {
            P.mGravity = gravity;
            return this;
        }

        /**
         * 设置要数据
         *
         * @param year    年
         * @param month   月
         * @param day     日
         * @param minYear 最小可选的年
         * @param maxYear 最大可选的年
         * @return
         */
        public Builder setData(int year, int month, int day, int minYear, int maxYear ){
            this.mYear = year;
            this.mMonth = month;
            this.mDay = day;
            this.mMinYear = minYear;
            this.mMaxYear = maxYear;
            this.mMinMonth =  1;
            this.mMaxMonth =  12;
            this.mMinDay =  1;
            this.mMaxDay = 31;
            return this;
        }

        public Builder setData(int year, int minYear , int maxYear,
                               int month, int minMonth, int maxMonth,
                               int day, int minDay, int maxDay ){
            this.mYear = year;
            this.mMonth = month;
            this.mDay = day;
            this.mMinYear = minYear;
            this.mMaxYear = maxYear;
            this.mMinMonth =  minMonth;
            this.mMaxMonth =  maxMonth;
            this.mMinDay =  minDay;
            this.mMaxDay = maxDay;
            return this;
        }

        /**
         * 设置年、月、日的显示情况
         * @param isYearVisible 年是否显示
         * @param isMonthVisible 月是否显示
         * @param isDayVisible 日是否显示
         * @return
         */
        public Builder setItemVisible(boolean isYearVisible, boolean isMonthVisible, boolean isDayVisible) {
            this.mIsYearVisible = isYearVisible;
            this.mIsMonthVisible = isMonthVisible;
            this.mIsDayVisible = isDayVisible;
            return this;
        }

        public SelectTimeDlg create() {
            dlg = new SelectTimeDlg(P.mContext, mTheme > 0 ? mTheme : 0);

            View view = LayoutInflater.from(P.mContext).inflate(R.layout.dlg_bottom_select_time_view, null);
//            Calendar c = Calendar.getInstance();
//            int currentMonth = c.get(Calendar.MONTH);
//            int currentYear = c.get(Calendar.YEAR);
//            int currentDay = c.get(Calendar.DAY_OF_MONTH);
            final DatePickerV2 datePicker = new DatePickerV2(P.mContext);
            datePicker.setDate(mYear, mMinYear, mMaxYear, mMonth,mMinMonth, mMaxMonth, mDay, mMinDay, mMaxDay);
            datePicker.setDateVisible(mIsYearVisible, mIsMonthVisible, mIsDayVisible);

            LinearLayout llMain = (LinearLayout) view.findViewById(R.id.ll_main);
            llMain.removeAllViews();
            llMain.addView(datePicker, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dlg != null && dlg.isShowing()) {
                        dlg.dismissDlg();
                    }
                }
            });
            view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dlg != null && dlg.isShowing()) {
                        dlg.dismissDlg();
                    }
                    if (mSelectTimeListner != null) {
                        mSelectTimeListner.onConfirm(datePicker.getYear(), datePicker.getMonth(), datePicker.getDay());
                    }
                }
            });

            P.mView = view;
            P.mShowContentDivider = false;
            P.mShowBtnDivider = false;
            P.mShowTitleDivider = false;
            P.mCancelable = true;
            dlg.setValue(P);
            return dlg;
        }


    }
}

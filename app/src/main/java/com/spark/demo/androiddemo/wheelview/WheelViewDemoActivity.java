package com.spark.demo.androiddemo.wheelview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.spark.demo.androiddemo.R;
import com.spark.demo.thirdparty.dlg.SelectTimeDlg;
import com.spark.demo.thirdparty.wheelview.LoopView;

import java.util.ArrayList;
import java.util.List;

/**
 * 滚轮控件
 * <p>
 * Created by mac on 2017/9/11.
 */

public class WheelViewDemoActivity extends AppCompatActivity {


    private LoopView mLoopView;
    private TextView mtvTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheelview);
        mLoopView = (LoopView) findViewById(R.id.view_loop);
        mtvTextView = (TextView) findViewById(R.id.tv_select_time);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add((i + 1) + "月");
        }

        mLoopView.setInitPosition(9);
        mLoopView.setItems(list);

    }

    public void viewDlgClick(View v) {
        SelectTimeDlg dlg  =new SelectTimeDlg.Builder(this, R.style.dialog_bottom)
                .setGravity(Gravity.BOTTOM)
                .setData(2017, 2000, 2050,9, 1, 12,11, 1, 31)
                .setItemVisible(true, true, true)
                .setSelectTimeListener(new SelectTimeDlg.Builder.SelectTimeListner() {
                    @Override
                    public void onConfirm(int year, int month, int day) {
                        mtvTextView.setText(year+"-"+month+"-"+day);
                    }
                }).create();
        dlg.show();
    }
}

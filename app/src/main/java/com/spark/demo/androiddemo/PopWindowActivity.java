package com.spark.demo.androiddemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by mac on 2017/8/9.
 */

public class PopWindowActivity extends Activity {


    PopupWindow popupWindow  = null;
    View btn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popwindow);
        btn1  = findViewById(R.id.tv_show);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(btn1, 0, 0);
            }
        });

        popupWindow = new PopupWindow(this);
        popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.view_popwindow_test, null));

    }

}

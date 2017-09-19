package com.spark.demo.androiddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import com.spark.demo.androiddemo.calender.CalenderActivity;
import com.spark.demo.androiddemo.dlg.DialogActivity;
import com.spark.demo.androiddemo.pay.PayActivity;
import com.spark.demo.androiddemo.share.ShareActivity;
import com.spark.demo.androiddemo.viewpager.ViewPagerDemoActivity;
import com.spark.demo.androiddemo.wheelview.WheelViewDemoActivity;
import com.spark.demo.thirdparty.shake.ShakeShowActivity;
import com.spark.demo.thirdparty.zxing.activity.CaptureActivity;

/**
 * 测试功能菜单
 *
 */
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void view1Click(View v){
        // 轮播控件
        startActivity(new Intent(MenuActivity.this, ViewPagerDemoActivity.class));
    }

    public void view3Click(View v){
        // 支付
        startActivity(new Intent(MenuActivity.this, PayActivity.class));
    }

    public void richScanClick (View v){
        // 扫一扫
        startActivity(new Intent(MenuActivity.this, CaptureActivity.class));
    }

    public void shakeClick (View v){
        // 摇一摇
        startActivity(new Intent(MenuActivity.this, ShakeShowActivity.class));
    }

    public void dialogClick(View v){
        // 常见的dialog
        startActivity(new Intent(MenuActivity.this, DialogActivity.class));
    }

    public void shareClick (View v){
        // 分享
        startActivity(new Intent(MenuActivity.this, ShareActivity.class));
    }

    public void wheelClick (View v){
        // 滚轮控件
        startActivity(new Intent(MenuActivity.this, WheelViewDemoActivity.class));
    }

    public void popwindowClick(View v){
        startActivity(new Intent(MenuActivity.this, PopWindowActivity.class));
    }

    public void calenderClick (View v){
        startActivity(new Intent(MenuActivity.this, CalenderActivity.class));
    }

}

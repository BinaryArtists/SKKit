package com.spark.demo.androiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.spark.demo.androiddemo.pay.PayActivity;
import com.spark.demo.androiddemo.viewpager.ViewPagerDemoActivity;
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

}

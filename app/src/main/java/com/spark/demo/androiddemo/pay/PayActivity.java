package com.spark.demo.androiddemo.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.spark.demo.androiddemo.R;

/**
 * 支付
 *
 * Created by chenwei on 2017/7/26.
 */

public class PayActivity extends AppCompatActivity {
    private static final String TAG = "PayActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }

    public void alipayClick (View v){
        Log.i(TAG, "alipayClick")  ;
    }
    public void wxClick (View v){
        Log.i(TAG, "wxClick")  ;
    }

}

package com.spark.demo.androiddemo.pay;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.spark.demo.androiddemo.R;
import com.spark.demo.thirdparty.pay.IPayPrepareParams;
import com.spark.demo.thirdparty.pay.PayListener;
import com.spark.demo.thirdparty.pay.PayManager;

/**
 * 支付
 *
 * Created by chenwei
 */

public class PayActivity extends AppCompatActivity implements
        IPayPrepareParams, PayListener{
    private static final String TAG = "PayActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        // 初始wx
        PayManager.INSTANCE().init(this);

    }

    /**
     * 支付宝支付
     * @param v
     */
    public void alipayClick (View v){
        String  orderInfo  ="";
        PayManager.INSTANCE().payOrderByAlipay(this, orderInfo, this);
    }

    /**
     * 微信支付
     * @param v
     */
    public void wxClick (View v){
        PayManager.INSTANCE().payOrderByWX();
    }

    // -- 微信支付需要的各种信息参数 start

    @Override
    public String getWXAppKey() {
        return "aaabbbccc111222333";
    }

    @Override
    public String getAppId() {
        return null;
    }

    @Override
    public String getPartnerId() {
        return null;
    }

    @Override
    public String getPrepayId() {
        return null;
    }

    @Override
    public String getNoncestr() {
        return null;
    }

    @Override
    public String getTimestamp() {
        return null;
    }

    @Override
    public String getPackage() {
        return null;
    }

    @Override
    public String getSign() {
        return null;
    }

    @Override
    public String getExtData() {
        return null;
    }

    @Override
    public Context getActivity() {
        return this;
    }

    @Override
    public void wxFailed(String errorMsg) {
        Toast.makeText(this, errorMsg , Toast.LENGTH_LONG).show();
    }

    // -- 微信支付需要的各种信息参数 end





    // -- 支付宝支付 -- start
    @Override
    public void onResult(String code, String result, int subCode) {
        // 9000 --成功   6001 -- 用户取消   其他 -失败
        if ("9000".equals(code)){
            Toast.makeText(this, "支付成功！", Toast.LENGTH_LONG).show();
        } else if ("6001".equals(code)){
            Toast.makeText(this, "用户取消！", Toast.LENGTH_LONG).show();
        } else  {
            Toast.makeText(this, "支付失败！", Toast.LENGTH_LONG).show();
        }
    }
    // -- 支付宝支付 -- end

}

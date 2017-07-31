package com.spark.demo.thirdparty.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.spark.demo.thirdparty.R;
import com.spark.demo.thirdparty.pay.PayManager;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    /**
     * 微信支付返回的错误代码
     */
    public final static String PAY_WX_ERRORCODE = "PAY_WX_ERRORCODE";
    public final static String STUDENT_INFO="---STUDENT_INFO";
    /**
     * 微信支付的返回
     */
    public final static String ACTION_WX_PAY_RESULT = "com.alliance.student.wxpay_result";

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    private String sWXAppKey= "wx8384902348029";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, sWXAppKey);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        // 0 -- 成功   -1  错误    -2 用户取消
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            PayManager.INSTANCE().mWXPayResultCode  = resp.errCode;
            Intent intent = new Intent();
            intent.putExtra(PAY_WX_ERRORCODE, resp.errCode);
            intent.setAction(ACTION_WX_PAY_RESULT);
            sendBroadcast(intent);// 发送广播
            finish();
        }
    }
}
package com.spark.demo.thirdparty.wxapi;


import com.alliance.student.R;
import com.alliance.student.core.manager.PayManager;
import com.spark.baselib.BaseApplication;
import com.spark.baselib.core.Constants;
import com.spark.baselib.core.ConstantsKey;
import com.spark.baselib.log.Logger;
import com.spark.baselib.utils.ToastWrapper;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    private String sWXAppKey;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);


        prepareThirdInfo(BaseApplication.USER_TYPE);

        api = WXAPIFactory.createWXAPI(this, sWXAppKey);
        api.handleIntent(getIntent(), this);
    }

    private void prepareThirdInfo(int type) {

        if (!TextUtils.isEmpty(sWXAppKey))
            return;

        switch (type) {
            case 1: // 学生
                sWXAppKey = "wx8a167c0d7f84fdd9";
                break;
            case 2: // 老师
                sWXAppKey = "wxb496f04e28f4c724";
                break;
        }
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
//        ToastWrapper.show("onPayFinish, errCode = " + resp.errCode);
//        Logger.i(TAG, "onPayFinish, errCode = " + resp.errCode+"---"+ConstantsAPI.COMMAND_PAY_BY_WX);

        // 0 -- 成功   -1  错误    -2 用户取消

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.toast_title_tips);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//			builder.show();

            PayManager.INSTANCE().mWXPayResultCode  = resp.errCode;
            Intent intent = new Intent();
            intent.putExtra(ConstantsKey.PAY_WX_ERRORCODE, resp.errCode);
            intent.setAction(ConstantsKey.ACTION_WX_PAY_RESULT);
            sendBroadcast(intent);// 发送广播
            finish();
        }
    }
}
package com.spark.demo.thirdparty.pay;

/**
 * 支付管理
 *
 * Created by spark
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

/**
 * 支付管理
 * <p>
 */
public class PayManager {
    private static final String TAG = "PayManager";
    private IPayPrepareParams mPayPrepareParams;
    private static PayManager sInstance;
    private Context mActivity;
    private String mOrderId;
    public static final int THIRD_PAY_SUCCESS = 20000;
    public static final int THIRD_PAY_CANCEL = -2;
    public static final int THIRD_PAY_FAIL = -1;
    public static final int DEFAULT_SUBCODE = 0;

    private int MAXRETRYCOUNT = 3;

    // 微信支付
    private IWXAPI mWXAPI = null;
    private static String sWXAppKey;
    /**
     * 微信支付的返回  【 0：成功   -1：错误   -2：用户取消 】
     */
    public   int mWXPayResultCode = 5555;

    /**
     * 请求数据回调接口
     */
    public interface ReqListener {
        void onReqDone(boolean ret, String info);
    }

    private PayListener payListener = null;

    /**
     * 设置支付宝回调监听
     *
     * @param payListener
     */
    public void setPayListener(PayListener payListener) {
        this.payListener = payListener;
    }

    public void init (IPayPrepareParams payPrepareParams){
        this.mPayPrepareParams  =  payPrepareParams;
        sWXAppKey = mPayPrepareParams.getWXAppKey();
    }

    /**
     * 实例化支付管理对象
     *
     * @return
     */
    public static PayManager INSTANCE() {
        if (sInstance == null) {
            synchronized (PayManager.class) {
                if (sInstance == null) {
                    sInstance = new PayManager();
                    // prepareThirdInfo(BaseApplication.USER_TYPE);
                }
            }
        }
        return sInstance;
    }

    // -- 支付宝支付
    private final int TAG_ALIPAY_RESULT = 11;
    // -- 微信支付
    private final int TAG_WEIXIN_RESULT = 22;

    /**
     * 支付宝支付
     * 获取PayTask支付对象调用支付（支付行为需要在独立的非ui线程中执行）
     * @param activity
     * @param info 订单信息
     */
    public void payOrderByAlipay(final Activity activity, String info, PayListener payListener) {
        this.payListener = payListener;
        final String orderInfo = info;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask payTask = new PayTask(activity);
                Map<String, String> result = payTask.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = TAG_ALIPAY_RESULT;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 微信支付
     */
    public void payOrderByWX() {
        if (mPayPrepareParams != null) {
            this.mActivity = mPayPrepareParams.getActivity();
            if (prepareWXInfo()) {
                PayReq req = new PayReq();
                req.appId = mPayPrepareParams.getAppId();
                req.partnerId = mPayPrepareParams.getPartnerId();
                req.prepayId = mPayPrepareParams.getPrepayId();
                req.nonceStr = mPayPrepareParams.getNoncestr();
                req.timeStamp = mPayPrepareParams.getTimestamp();
                req.packageValue = mPayPrepareParams.getPackage();
                req.sign = mPayPrepareParams.getSign();
                req.extData = mPayPrepareParams.getExtData();
                mWXAPI.sendReq(req);
            }
        }
    }

    /**
     * 初始化微信支付的基础信息
     * @return
     */
    private boolean prepareWXInfo() {
        if (mWXAPI == null) {
            // 加载分享设置 ，传入微信id
            mWXAPI = WXAPIFactory.createWXAPI(mActivity, sWXAppKey);
            // 注册
            mWXAPI.registerApp(sWXAppKey);
        }
        if (!mWXAPI.isWXAppInstalled()) {
            mPayPrepareParams.wxFailed("请确认您的手机是否已安装了微信");
            return false;
        }
        return true;
    }

    /**
     * 支付结果的处理
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TAG_ALIPAY_RESULT: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 9000 --成功   6001 -- 用户取消   其他 -失败
                    if (payListener != null) {
                        payListener.onResult(resultStatus, resultInfo, 0);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}

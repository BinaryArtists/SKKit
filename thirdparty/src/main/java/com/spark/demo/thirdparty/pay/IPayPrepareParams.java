package com.spark.demo.thirdparty.pay;

import android.app.Activity;
import android.content.Context;

/**
 * Created by chenwei on 2017/7/26.
 */

public interface IPayPrepareParams {


    // -- WX start -- //
    /**
     * 获取 微信的appKey
     * @return
     */
    public String getWXAppKey();

    public String getAppId ();

    public String getPartnerId() ;

    public String getPrepayId();

    public String getNoncestr();

    public String getTimestamp();

    public String getPackage();

    public String getSign ();

    public String getExtData ();

    public Context getActivity ();

    public void wxFailed  (String errorMsg);

    // -- WX end -- //



}

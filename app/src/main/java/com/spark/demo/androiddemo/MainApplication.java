package com.spark.demo.androiddemo;

import android.app.Application;

import com.spark.demo.thirdparty.utils.AbsUtil;
import com.spark.demo.thirdparty.utils.UtilsPackage;


/**
 * Created by chenwei on 2017/7/25.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AbsUtil.init(getApplicationContext());
    }
}

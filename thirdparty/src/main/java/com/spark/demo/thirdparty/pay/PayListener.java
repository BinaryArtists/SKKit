package com.spark.demo.thirdparty.pay;

/**
 * 支付宝支付结果监听
 *
 * Created by chenwei on 2017/7/31.
 */

public interface PayListener {

   public void onResult(String code, String result, int subCode);
}


package com.spark.demo.thirdparty.dlg;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * 基础dialog的参数
 *
 * Created by dell-pc on 2016/5/23.
 */
public class DialogController {

    public static class DialogParams {

        public final Context mContext;
        public String mTitle;
        public String mMessage;
        public String mBtn1Info;
        public String mBtn2Info;
        public DialogInterface.OnClickListener mBtn1ClickListener;
        public DialogInterface.OnClickListener mBtn2ClickListener;
        public View mView;
        public boolean mShowTitleDivider = true;
        public boolean mShowContentDivider = true;
        public boolean mShowBtnDivider = true;
        public boolean mCancelable = true;
        public  boolean mIsFullScreen = false; // 是否全屏显示
        /**
         * 这个值对应与Gravity里的常量 Gravity.TOP,Gravity.BOTTOM等, 0 表示没有设置方向。
         */
        public int mGravity = 0;

        public DialogParams(Context context) {
            this.mContext = context;
            mCancelable = true;
        }
    }
}

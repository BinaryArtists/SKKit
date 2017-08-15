package com.spark.demo.thirdparty.dlg;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.demo.thirdparty.R;

/**
 * Created by spark
 */
public class BaseDlg extends Dialog {
    private static final String TAG = "BaseDlg";
    protected TextView mtvTitle; // 标题
    protected FrameLayout mdlgContainer; // 主要显示内容的区域
    protected TextView mtvMsg, mtvBtn1, mtvBtn2;   // 单条显示的msg, 默认的两个button
    protected View mTitleDivider; // 标题的分割线
    protected View mContainerDivider; // 内容的分割线
    protected View mBtnDivider; // button的分割线
    protected View mDlgView;
    protected View mContainerView;
    protected int mAnimStyle = -1; // 弹出、消失动画
    protected boolean mIsFullScreen = false; // 是否全屏显示

    public void dismissDlg() {
        if (getContext() != null && isShowing()) {
            dismiss();
        }
    }

    // 默认按钮的默认点击事件
    protected DialogInterface.OnClickListener mDefaultClickListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    protected BaseDlg(Context context) {
        super(context);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置全屏显示
        if (mIsFullScreen) {
            WindowManager m = getWindow().getWindowManager();
            Display d = m.getDefaultDisplay();
            WindowManager.LayoutParams p = getWindow().getAttributes();
            p.width = d.getWidth();
            getWindow().setAttributes(p);
        }
    }

    protected BaseDlg(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected BaseDlg(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    protected void setValue(final DialogController.DialogParams p) {
        if (p != null) {
            // 标题
            if (TextUtils.isEmpty(p.mTitle)) {
                mtvTitle.setVisibility(View.GONE);
                p.mShowTitleDivider = false; // 如果没有标题就不显示分割线
            } else {
                mtvTitle.setVisibility(View.VISIBLE);
                mtvTitle.setText(p.mTitle);
            }
            // 主要显示的内容区域
            if (p.mView != null) {
                mdlgContainer.removeAllViews();
                mtvMsg = null;
                mdlgContainer.addView(p.mView);
            } else {
                if (!TextUtils.isEmpty(p.mMessage) && mtvMsg != null) {
                    mtvMsg.setVisibility(View.VISIBLE);
                    mtvMsg.setText(p.mMessage);
                }
            }
            // 按钮区域
            boolean isShowBtn1 = false;
            if (TextUtils.isEmpty(p.mBtn1Info)) {
                mtvBtn1.setVisibility(View.GONE);
                p.mShowBtnDivider = false;
                isShowBtn1 = false;
            } else {
                isShowBtn1 = true;
                mtvBtn1.setVisibility(View.VISIBLE);
                mtvBtn1.setText(p.mBtn1Info);
                mtvBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getContext() != null && isShowing()) {
                            BaseDlg.this.dismiss();
                        }
                        if (p.mBtn1ClickListener != null) {
                            p.mBtn1ClickListener.onClick(BaseDlg.this, 0);
                        } else {
                            // default
                            mDefaultClickListener.onClick(BaseDlg.this, 0);
                        }
                    }
                });

            }
            boolean isShowBtn2 = false;
            if (TextUtils.isEmpty(p.mBtn2Info)) {
                mtvBtn2.setVisibility(View.GONE);
                p.mShowBtnDivider = false;
                isShowBtn2 = false;
            } else {
                isShowBtn2 = true;
                mtvBtn2.setVisibility(View.VISIBLE);
                mtvBtn2.setText(p.mBtn2Info);
                mtvBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getContext() != null && isShowing()) {
                            BaseDlg.this.dismiss();
                        }
                        if (p.mBtn2ClickListener != null) {
                            p.mBtn2ClickListener.onClick(BaseDlg.this, 0);
                        } else {
                            // default
                            mDefaultClickListener.onClick(BaseDlg.this, 0);
                        }
                    }
                });
            }
            // 分割线
            // 标题与内容之间的分割线
            if (p.mShowTitleDivider) {
                mTitleDivider.setVisibility(View.VISIBLE);
            } else {
                mTitleDivider.setVisibility(View.GONE);
            }
            // 内容与按钮区域之间的分割线
            if (p.mShowContentDivider && (isShowBtn1 || isShowBtn2)) {
                mContainerDivider.setVisibility(View.VISIBLE);
            } else {
                mContainerDivider.setVisibility(View.GONE);
            }
            // 按钮之间的分割线
            if (p.mShowBtnDivider && !TextUtils.isEmpty(p.mBtn1Info) && !TextUtils.isEmpty(p.mBtn2Info)) {
                mBtnDivider.setVisibility(View.VISIBLE);
            } else {
                mBtnDivider.setVisibility(View.GONE);
            }

            // 设置显示的方向  TOP,BOTTOM 等
            if (p.mGravity > 0) {
                getWindow().setGravity(p.mGravity);
            }

            // 是否是全屏
            if (p.mIsFullScreen) {
                mIsFullScreen = true;
            } else {
                mIsFullScreen = false;
            }
        }
    }

    protected void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDlgView = getDlgView(context);
        if (mDlgView != null) {
            mtvTitle = (TextView) mDlgView.findViewById(R.id.dlg_title);
            mtvMsg = (TextView) mDlgView.findViewById(R.id.tv_msg);
            mdlgContainer = (FrameLayout) mDlgView.findViewById(R.id.dlg_container);
            mtvBtn1 = (TextView) mDlgView.findViewById(R.id.dlg_btn1);
            mtvBtn2 = (TextView) mDlgView.findViewById(R.id.dlg_btn2);
            mTitleDivider = mDlgView.findViewById(R.id.dlg_title_divider);
            mContainerDivider = mDlgView.findViewById(R.id.dlg_content_divider);
            mBtnDivider = mDlgView.findViewById(R.id.dlg_btn_divider);

            super.setContentView(mDlgView);
        }
    }

    protected View getDlgView(Context context) {
        return LayoutInflater.from(context).inflate(
                R.layout.dlg_base, null);
    }

    @Override
    public void setContentView(int layoutResID) {
//        super.setContentView(layoutResID);
        this.setContentView(null);
    }

    @Override
    public void setContentView(View view) {
//        super.setContentView(view);
        this.setContentView(null, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
//        super.setContentView(view, params);
        Log.e(TAG, "please  use  the setCustomView() to  instead it."); 
    }


    public static class Builder {
        private DialogController.DialogParams P;
        private int mTheme;
        private boolean mCancelable = true;

        public Builder(Context context) {
            this(context, 0);
        }

        public Builder(Context context, int themeId) {
            mTheme = themeId;
            P = new DialogController.DialogParams(context);
        }

        public Builder setTitleDividerLineVisible(boolean isVisible) {
            P.mShowTitleDivider = isVisible;
            return this;
        }

        public Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        public Builder setMessage(String message) {
            P.mMessage = message;
            return this;
        }

        public Builder setCustomView(View view) {
            P.mView = view;
            return this;
        }

        public Builder isFullScreen(boolean isFullScreen) {
            P.mIsFullScreen = isFullScreen;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder setBtn1(String btnInfo, DialogInterface.OnClickListener clickListener) {
            P.mBtn1Info = btnInfo;
            P.mBtn1ClickListener = clickListener;
            return this;
        }

        public Builder setBtn2(String btnInfo, DialogInterface.OnClickListener clickListener) {
            P.mBtn2Info = btnInfo;
            P.mBtn2ClickListener = clickListener;
            return this;
        }

        /**
         * 值对应与Gravity中的几个常量
         *
         * @param gravity Gravity.TOP, Gravity.BOTTOM等
         * @return
         */
        public Builder setGravity(int gravity) {
            P.mGravity = gravity;
            return this;
        }

        public BaseDlg create() {
            BaseDlg dlg = null;
            if (mTheme > 0) {
                dlg = new BaseDlg(P.mContext, mTheme);
            } else {
                dlg = new BaseDlg(P.mContext);
            }
            dlg.setValue(P);
            dlg.setCancelable(mCancelable);
            return dlg;
        }

    }


}

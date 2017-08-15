package com.spark.demo.androiddemo.dlg;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.spark.demo.androiddemo.R;
import com.spark.demo.thirdparty.dlg.BaseDlg;

/**
 * Dialog demo
 * <p>
 * Created by spark on 2017/8/1.
 */

public class DialogActivity extends Activity {

    private BaseDlg mWithThreeBtnDlg, mShareDlg;

    public void dialogClick1(View v) {
        BaseDlg dlg = new BaseDlg.Builder(this, R.style.dialog_common)
                .setTitle("通知")
                .setBtn1("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("您有一个新的订单，请注意查收。")
                .create();
        dlg.show();
    }

    public void dialogClick2(View v) {
        BaseDlg dlg = new BaseDlg.Builder(this, R.style.dialog_common)
                .setTitle("通知")
                .setBtn1("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setBtn2("去查看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("您有一个新的订单，请注意查收。")
                .create();
        dlg.show();
    }

    public void dialogClick3(View v) {
        View view = LayoutInflater.from(this).inflate(R.layout.dlg_with_three_btn, null);
        view.findViewById(R.id.dlg_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWithThreeBtnDlg != null && mWithThreeBtnDlg.isShowing()) {
                    mWithThreeBtnDlg.dismiss();
                }
            }
        });
        view.findViewById(R.id.dlg_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWithThreeBtnDlg != null && mWithThreeBtnDlg.isShowing()) {
                    mWithThreeBtnDlg.dismiss();
                }
            }
        });
        view.findViewById(R.id.dlg_btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWithThreeBtnDlg != null && mWithThreeBtnDlg.isShowing()) {
                    mWithThreeBtnDlg.dismiss();
                }
            }
        });
        mWithThreeBtnDlg = new BaseDlg.Builder(this, R.style.dialog_common)
                .setTitle("通知")
                .setCustomView(view)
                .create();
        mWithThreeBtnDlg.show();
    }

    public void dialogClick4(View v) {
        View view = LayoutInflater.from(this).inflate(R.layout.dlg_cotent_share, null);
        ShareClick shareClick = new ShareClick();
        view.findViewById(R.id.ll_share_weichat).setOnClickListener(shareClick);
        view.findViewById(R.id.ly_share_wei_friend).setOnClickListener(shareClick);
        view.findViewById(R.id.ll_share_qq).setOnClickListener(shareClick);
        view.findViewById(R.id.ll_share_sina_weibo).setOnClickListener(shareClick);
        mShareDlg = new BaseDlg.Builder(this, R.style.dialog_common)
                .setCustomView(view).create();
        mShareDlg.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        setTitle("常见的dialog");
    }

    private class ShareClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mShareDlg != null && mShareDlg.isShowing()) {
                mShareDlg.dismiss();
            }
            switch (v.getId()) {
                case R.id.ll_share_weichat:
                    Toast.makeText(DialogActivity.this, "分享到微信", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ly_share_wei_friend:
                    Toast.makeText(DialogActivity.this, "分享到朋友圈", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ll_share_qq:
                    Toast.makeText(DialogActivity.this, "分享到QQ", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ll_share_sina_weibo:
                    Toast.makeText(DialogActivity.this, "分享到微博", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}

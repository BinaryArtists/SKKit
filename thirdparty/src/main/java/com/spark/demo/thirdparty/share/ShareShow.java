//package com.spark.demo.thirdparty.share;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.widget.Toast;
//
//import com.facebook.common.executors.CallerThreadExecutor;
//import com.facebook.common.references.CloseableReference;
//import com.facebook.datasource.DataSource;
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.imagepipeline.core.ImagePipeline;
//import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
//import com.facebook.imagepipeline.image.CloseableImage;
//import com.facebook.imagepipeline.request.ImageRequest;
//import com.facebook.imagepipeline.request.ImageRequestBuilder;
//import com.spark.demo.thirdparty.R;
//import com.spark.demo.thirdparty.utils.UtilsImage;
//import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.sdk.modelmsg.WXImageObject;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//
//
///**
// * Created by spark
// * <p/>
// * 分享界面
// * <p/>
// * 支持多种形式的分享
// */
//public final class ShareShow {
//
//    private static final String TAG = "ShareShow";
//    private static final int WX_THUMB_SIZE = 90;// 微信的头像大小【分享头像的大小不能超过32k】
//
//    private Activity mActivity;
////    private CompatDialog mShowDialog;
//
//
//    private String mShareLinkUrl = "http://www.zoomforkids.cn";
//    private String mShareTitle = "最爱儿童联盟";
//    private String mShareIconUrl;
//    private String mShareContent = "最爱儿童联盟";
//    private String mShareImagePath = null; // 分享的图片地址
//    private String mEmailShareContent;// 邮件的内容
//    private String mShowTitle = "分享给";
//    private String mCancelString = "取消";
//
//    private int mDefaultShareIcon;// 默认Share icon
//    private int mLastDefaultShareIcon; // 上一次设置的默认 share icon
//
//    private static File sDefaultShareIconFile;// 默认的地址
//    private static byte[] sDefaultShareIconData;
//
//    // ------------------微信相关
//    private IWXAPI mWXAPI = null;
//    private WXMediaMessage.IMediaObject mWXWebpageObj;
//    //    private WXImageObject mWXImageObj;
//    private WXMediaMessage mWXMediaMsg;
//    private byte[] mIconData;
//    private boolean mIsIconFetchDone = true;
//    private static String sWXAppKey;
//    private Bitmap sShareBitmap;
//
//    private boolean mIsSharePhoto = false;
//    // -------------------QQ相关
////    private Tencent mTencent;
//    private static String sQQAppId;
////    private String shareType;
//
//
//    public ShareShow(Activity activity) {
//        mActivity = activity;
//        prepareThirdInfo(BaseApplication.USER_TYPE);
//
//    }
//
//
//    public ShareShow setShowTitle(String title) {
//        if (!TextUtils.isEmpty(title))
//            mShowTitle = title;
//        return this;
//    }
//
//    public ShareShow setDefaultShareIcon(int id) {
//        mLastDefaultShareIcon = mDefaultShareIcon;
//        mDefaultShareIcon = id;
//
//        return this;
//    }
//
//    public ShareShow setCancelString(String str) {
//        if (!TextUtils.isEmpty(str))
//            mCancelString = str;
//        return this;
//    }
//
//    public ShareShow setShareLink(String linkUrl) {
//        mShareLinkUrl = linkUrl;
////        if (!TextUtils.isEmpty(linkUrl)) {
////            // 4.8 增加分享渠道
////            String param = getShareAppParam();
////            if (!TextUtils.isEmpty(param))
////                mShareLinkUrl = UtilsHttp.addParamToUrl(linkUrl, "chnid", param);
////            else
////                mShareLinkUrl = linkUrl;
////        }
//        return this;
//    }
//
////    /**
////     * @param linkUrl 要分享的url
////     * @param chnid   渠道 【如果设置的chnid为空，就取默认的】
////     * @return
////     */
////    public ShareShow setShareLink(String linkUrl, String chnid) {
////        if (!TextUtils.isEmpty(linkUrl)) {
////
////            // 4.8 增加分享渠道
////            String param;
////            if (TextUtils.isEmpty(chnid)) {
////                param = getShareAppParam();
////            } else {
////                param = chnid;
////            }
////
////            if (!TextUtils.isEmpty(param))
////                mShareLinkUrl = UtilsHttp.addParamToUrl(linkUrl, "chnid", param);
////            else
////                mShareLinkUrl = linkUrl;
////        }
////        return this;
////    }
//
//
////    private static String getShareAppParam() {
////        AppCommon.AppType appType = DefaultDataCache.INSTANCE().getAppType();
////        switch (appType) {
////            case qingqing_student:
////                return "h5_stu_share";
////            case qingqing_teacher:
////                return "h5_tr_share";
////            case qingqing_ta:
////                return "h5_ta_share";
////        }
////
////        return null;
////    }
//
//    public ShareShow setShareTitle(String title) {
//        if (!TextUtils.isEmpty(title))
//            mShareTitle = title;
//        return this;
//    }
//
//    public ShareShow setShareIcon(String iconUrl) {
//
//        mIsIconFetchDone = true;
//        if (!TextUtils.isEmpty(iconUrl) && !iconUrl.equals(mShareIconUrl)) {
//
//            // mShareIconUrl = ImageUrlUtilV2.removeSuffixFormat(iconUrl);
//            ImageRequest imageRequest = ImageRequestBuilder
//                    .newBuilderWithSource(Uri.parse(mShareIconUrl))
//                    .setProgressiveRenderingEnabled(true).build();
//
//            mIsIconFetchDone = false;
//            ImagePipeline imagePipeline = Fresco.getImagePipeline();
//            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline
//                    .fetchDecodedImage(imageRequest, mActivity);
//
//            dataSource.subscribe(new BaseBitmapDataSubscriber() {
//                @Override
//                protected void onNewResultImpl(Bitmap bitmap) {
//                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, WX_THUMB_SIZE,
//                            WX_THUMB_SIZE, true);
//                    mIconData = UtilsImage.getBytesFromBitmap(thumbBmp);
//                    thumbBmp.recycle();
//                    mIsIconFetchDone = true;
//                }
//
//                @Override
//                protected void onFailureImpl(
//                        DataSource<CloseableReference<CloseableImage>> closeableReferenceDataSource) {
//                    mIsIconFetchDone = true;
//                }
//            }, CallerThreadExecutor.getInstance());
//        }
//
//        return this;
//    }
//
//    public ShareShow setShareContent(String content) {
//        if (!TextUtils.isEmpty(content))
//            mShareContent = content;
//        return this;
//    }
//
//    public ShareShow setShareImage(String imgPath) {
//        if (!TextUtils.isEmpty(imgPath))
//            this.mShareImagePath = imgPath;
//        return this;
//    }
//
//    public ShareShow setEmailShareContent(String content) {
//        if (!TextUtils.isEmpty(content))
//            mEmailShareContent = content;
//        return this;
//    }
//
//    public ShareShow setShareBitMap(Bitmap bitMap) {
//        if (bitMap != null)
//            sShareBitmap = bitMap;
//        return this;
//    }
//
//    public ShareShow setIsSharePhoto(boolean isSharePhoto) {
//        mIsSharePhoto = isSharePhoto;
//        return this;
//    }
//
//    /**
//     * 弹出分享弹框
//     */
//    public void show() {
//
//        prepareDefaultShareIcon();
//
//        ShareDlg shareDlg = new ShareDlg.Builder(mActivity, R.style.dialog_bottom)
//                .setGravity(Gravity.BOTTOM)
//                .setShareListener(new ShareDlg.Builder.ShareListener() {
//                    @Override
//                    public void shareClick(int type) {
//                        switch (type) {
//                            case ShareDlg.TYPE_SHARE_TO_WEIXIN_FRIEND:
//                                shareWithWX(SendMessageToWX.Req.WXSceneTimeline);
//                                break;
//                            case ShareDlg.TYPE_SHARE_TO_QQ:
//                                // TODO: 2016/6/6
//                                break;
//                            case ShareDlg.TYPE_SHARE_TO_WEIXIN:
//                                // TODO: 2016/6/6
//                                shareWithWX(SendMessageToWX.Req.WXSceneSession);
//                                break;
//                            case ShareDlg.TYPE_SHARE_TO_WEIBO:
//                                // TODO: 2016/6/6
//                                break;
//                        }
//                    }
//                }).create();
//        shareDlg.show();
//    }
//
//    private boolean prepareWXShare() {
//
//        if (mWXAPI == null) {
//            // 加载分享设置 ，传入微信id
//            mWXAPI = WXAPIFactory.createWXAPI(mActivity, sWXAppKey);
//            // 注册
//            mWXAPI.registerApp(sWXAppKey);
//        }
//
//        if (!mWXAPI.isWXAppInstalled()) {
//            Toast.makeText(mActivity,R.string.toast_base_wechat_api_invalid, Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//        if (TextUtils.isEmpty(mShareImagePath)&!mIsSharePhoto) {
//            // 分享url
//            if (mWXWebpageObj == null) {
//                mWXWebpageObj = new WXWebpageObject();
//                ((WXWebpageObject) mWXWebpageObj).webpageUrl = mShareLinkUrl;
//            }
//        } else {
//            // 分享图片
//            if (mWXWebpageObj == null) {
//                if (sShareBitmap != null) {
//                    mWXWebpageObj = new WXImageObject(sShareBitmap);
//                } else {
//                    Bitmap bmp = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.share_icon_qq);
//                    mWXWebpageObj = new WXImageObject(bmp);
//                }
//            }
////            ( (WXImageObject) mWXWebpageObj).setImagePath(mShareImagePath);
//        }
//
//        if (mWXMediaMsg == null) {
//            mWXMediaMsg = new WXMediaMessage(mWXWebpageObj);
//        }
//
//        if (mIsSharePhoto) {
//            Bitmap bitmap = Bitmap.createScaledBitmap(sShareBitmap, WX_THUMB_SIZE, WX_THUMB_SIZE, true);
//            mWXMediaMsg.thumbData = bmpToByteArray(bitmap, true);
//            mWXMediaMsg.mediaObject = mWXWebpageObj;
//
//            return true;
//        } else {
//            mWXMediaMsg.title = mShareTitle;
//            mWXMediaMsg.description = mShareContent;
//            mWXMediaMsg.mediaObject = mWXWebpageObj;
//            if (mIconData == null && mIsIconFetchDone) {
//                mIconData = sDefaultShareIconData;
//            }
//
//            mWXMediaMsg.thumbData = mIconData;
//            return true;
//        }
//    }
//
//    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
//        if (needRecycle) {
//            bmp.recycle();
//        }
//
//        byte[] result = output.toByteArray();
//        try {
//            output.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    private void prepareDefaultShareIcon() {
//
//        if (sDefaultShareIconFile == null || mDefaultShareIcon != mLastDefaultShareIcon || !sDefaultShareIconFile.exists()
//                ) {
//            Resources res = mActivity.getResources();
//            if (mDefaultShareIcon <= 0)
//                mDefaultShareIcon = R.drawable.weicut1;
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(
//                    BitmapFactory.decodeResource(res, mDefaultShareIcon), WX_THUMB_SIZE,
//                    WX_THUMB_SIZE, true);
//
//            sDefaultShareIconFile = new File(mActivity.getExternalCacheDir(),
//                    "default_share");
//            UtilsImage.saveBitmapToFileWithCompress(
//                    sDefaultShareIconFile.getAbsolutePath(), thumbBmp, 30 * 1024);
//            if (mDefaultShareIcon != mLastDefaultShareIcon || sDefaultShareIconData == null) {
//                sDefaultShareIconData = UtilsImage.getBytesFromBitmap(thumbBmp);
//            }
//            thumbBmp.recycle();
//        }
//    }
//
//    private void shareWithWX(int shareScene) {
//        if (prepareWXShare()) {
//            // 构造一个Req
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//
//            if (TextUtils.isEmpty(mShareImagePath)) {
//                req.transaction = "webpage" + System.currentTimeMillis();// transaction字段用于唯一标识一个请求
//            } else {
//                req.transaction = "img" + System.currentTimeMillis();
//            }
//            req.message = mWXMediaMsg;
//            req.scene = shareScene;
//            // 调用api接口发送数据到微信
//            mWXAPI.sendReq(req);
//        }
//    }
//
//    private void prepareThirdInfo(int type) {
//
//        if (!TextUtils.isEmpty(sWXAppKey))
//            return;
//
//        switch (type) {
//            case 1: // 学生
//                sWXAppKey = "wx8a167c0d7f84fdd9";
//                sQQAppId = "1105235087";
//                break;
//            case 2: // 老师
//                sWXAppKey = "wxb496f04e28f4c724";
//                sQQAppId = "1105284383";
//                break;
//        }
//    }
//
//    private void shareWithCopyLink() {
//        UtilsDevice.copyToClipBoard(mShareLinkUrl);
//        ToastWrapper.show("链接已复制");
//    }
//
//    private void shareWithEmail() {
//        Uri uri = Uri.parse("mailto:");
//        // String[] email = {"3802**92@qq.com"};
//        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//        // intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
//        intent.putExtra(Intent.EXTRA_SUBJECT, mShareTitle); // 主题
//        if (TextUtils.isEmpty(mEmailShareContent)) {
//            mEmailShareContent = mShareContent;
//        }
//
//        if (mEmailShareContent != null) {
//            mEmailShareContent += "\n" + mShareLinkUrl;
//        }
//
//        intent.putExtra(Intent.EXTRA_TEXT, mEmailShareContent); // 正文
//        mActivity.startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
//    }
//
//    private void shareWithSMS() {
//        Uri smsToUri = Uri.parse("smsto:");
//        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
//        intent.putExtra("sms_body", mShareTitle + "\n"
//                + mShareLinkUrl + "【麻辣爸妈家教】");
//        mActivity.startActivity(intent);
//    }
//
////    private void shareWithQQ() {
////        final Bundle params = new Bundle();
////        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
////        params.putString(QQShare.SHARE_TO_QQ_TITLE, mShareTitle);
////        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mShareContent);
////        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mShareLinkUrl);
////        if (TextUtils.isEmpty(mShareIconUrl)) {
////            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
////                    sDefaultShareIconFile.getAbsolutePath());
////        } else {
////            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareIconUrl);
////        }
////        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "返回" + UtilsPackage.getLabel());
////        // params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
////        mTencent.shareToQQ(mActivity, params, new IUiListener() {
////            @Override
////            public void onComplete(Object o) {
////                ToastWrapper.show(R.string.weibo_share_success);
////            }
////
////            @Override
////            public void onError(UiError uiError) {
////
////            }
////
////            @Override
////            public void onCancel() {
////
////            }
////        });
////    }
//
////    private void shareWithQzone() {
////        // 分享类型
////        final Bundle params = new Bundle();
////
////        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
////                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
////        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mShareTitle);// 必填
////        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mShareContent);// 选填
////        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, mShareLinkUrl);// 必填
////        ArrayList<String> imgUrlList = new ArrayList<String>();
////        if (!TextUtils.isEmpty(mShareIconUrl)) {
////            imgUrlList.add(mShareIconUrl);
////        } else {
////            imgUrlList.add(sDefaultShareIconFile.getAbsolutePath());
////        }
////        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList);
////
////        mTencent.shareToQzone(mActivity, params, new IUiListener() {
////            @Override
////            public void onComplete(Object o) {
////                ToastWrapper.show(R.string.weibo_share_success);
////            }
////
////            @Override
////            public void onError(UiError uiError) {
////
////            }
////
////            @Override
////            public void onCancel() {
////
////            }
////        });
////    }
////
////    private void shareWithWeibo() {
////        Intent it = new Intent(mActivity, WBShareActivity.class);
////        it.putExtra(WBShareActivity.PARAM_SHARE_TITLE, mShareTitle);
////        it.putExtra(WBShareActivity.PARAM_SHARE_CONTENT, mShareContent);
////        it.putExtra(WBShareActivity.PARAM_SHARE_LINK, mShareLinkUrl);
////        if (mIconData == null && mIsIconFetchDone) {
////            mIconData = sDefaultShareIconData;
////        }
////
////        it.putExtra(WBShareActivity.PARAM_SHARE_ICON, mIconData);
////        mActivity.startActivity(it);
////    }
//
//}

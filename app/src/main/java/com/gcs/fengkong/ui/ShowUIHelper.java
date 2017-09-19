package com.gcs.fengkong.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.atys.LoginActivity;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.bean.SimpleBackPage;
import com.gcs.fengkong.ui.frags.subfrags.BrowserFragment;
import com.gcs.fengkong.ui.widget.SimplexToast;
import com.gcs.fengkong.utils.AppOperator;
import com.gcs.fengkong.utils.MyLog;


/**
 * 界面帮助类
 *
 * @author lyw
 * @version 创建时间：2014年10月10日 下午3:33:36
 */
public class ShowUIHelper {



    /**
     * 显示登录界面
     *
     * @param context
     */
    public static void showLoginActivity(Context context) {
        LoginActivity.show(context);
    }





    public static void showSimpleBack(Context context, SimpleBackPage page) {

        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page,
                                      Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }




    /**
     * 显示设置界面
     *
     * @param context
     */
    public static void showSetting(Context context) {
        showSimpleBack(context, SimpleBackPage.SETTING);
    }

    /**
     * 显示关于界面
     *
     * @param context
     */
    public static void showAboutGCS(Context context) {
       showSimpleBack(context, SimpleBackPage.ABOUT_GCS);
    }

    /**
     * 显示意见反馈
     *
     * @param context
     */
    public static void showFeedBack(Context context) {
        showSimpleBack(context, SimpleBackPage.FEEDBACK_GCS);
    }

    /**
     * 显示个人资料
     *
     * @param context
     */
    public static void showPersonalDATA(Context context) {
        showSimpleBack(context, SimpleBackPage.PERSONAL_DATA);
    }

    /**
     * 显示重置密码
     *
     * @param context
     */
    public static void showResetPassword(Context context) {
        showSimpleBack(context, SimpleBackPage.RESET_PASSWORD);
    }

    /**
     * 显示身份信息输入
     *
     * @param context
     */
    public static void showIdentityAuth(Context context) {
        showSimpleBack(context, SimpleBackPage.IDENTITY_AUTH);
    }

    /**
     * 显示银行卡输入
     *
     * @param context
     */
    public static void showBankAuth(Context context) {
        showSimpleBack(context, SimpleBackPage.BANK_AUTH);
    }

    /**
     * 显示芝麻信用授权
     *
     * @param context
     */
    public static void showZhimaAuth(Context context) {
        showSimpleBack(context, SimpleBackPage.ZHIMA_AUTH);
    }

    /**
     * 显示支付宝授权
     *
     * @param context
     */
    public static void showAlipayAuth(Context context) {
        showSimpleBack(context, SimpleBackPage.ALIPAY_AUTH);
    }

    /**
     * 显示淘宝输入
     *
     * @param context
     *
     */
    public static void showTaobaoAuth(Context context) {
        showSimpleBack(context, SimpleBackPage.TAOBAO_AUTH);
    }
    /**
     * 显示京东输入
     *
     * @param context
     */
    public static void showJdAuth(Context context) {
        showSimpleBack(context, SimpleBackPage.JD_AUTH);
    }
    /**
     * 显示运营商输入
     *
     * @param context
     */
    public static void showOperatorAuth(Context context) {
        showSimpleBack(context, SimpleBackPage.OPERATOR_AUTH);
    }



    /**
     * 打开内置浏览器
     *
     * @param context
     * @param url
     */
    public static void openInternalBrowser(Context context, String url) {
        try {
            Bundle bundle = new Bundle();

            bundle.putString(BrowserFragment.BROWSER_KEY, url);

            showSimpleBack(context, SimpleBackPage.BROWSER, bundle);
        } catch (Exception e) {
            e.printStackTrace();
           openExternalBrowser(context, url);
        }
    }

    /**
     * 打开外置的浏览器
     *
     * @param context
     * @param url
     */
    public static void openExternalBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(Intent.createChooser(intent, "选择打开的应用"));
    }

    /**
     * 清除app缓存
     */
    public static void clearAppCache(boolean showToast) {
        final Handler handler = showToast ? new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    SimplexToast.showMyToast("缓存清除成功",GlobalApplication.getContext());
                } else {
                    SimplexToast.showMyToast("缓存清除失败",GlobalApplication.getContext());
                }
            }
        } : null;
        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    GlobalApplication.getInstance().clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                if (handler != null)
                    handler.sendMessage(msg);
            }
        });
    }





}

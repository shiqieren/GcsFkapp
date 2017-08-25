package com.gcs.fengkong.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.ui.account.atys.LoginActivity;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.bean.SimpleBackPage;
import com.gcs.fengkong.ui.frags.subfrags.BrowserFragment;


/**
 * 界面帮助类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
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
         //111外置浏览器   openExternalBrowser(context, url);
        }
    }

    /**
     * 清除app缓存
     */
    public static void clearAppCache(boolean showToast) {
        final Handler handler = showToast ? new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    GlobalApplication.showToastShort("缓存清除成功");
                } else {
                    GlobalApplication.showToastShort("缓存清除失败");
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

package com.gcs.jyfk;

import android.content.Context;
import android.os.Looper;

import com.gcs.jyfk.ui.atys.ErrorActivity;
import com.gcs.jyfk.ui.widget.SimplexToast;


/**
 * 不能被捕获的异常线程类
 * 单例
 * Created by lyw on 2017/7/23.
 */
public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static AppCrashHandler INSTANCE = new AppCrashHandler();
    private Context mContext;

    private AppCrashHandler() {
    }

    public static AppCrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;

        Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (exceptionHandler == this)
            return;
        mDefaultHandler = exceptionHandler;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //错误信息展示activity
        ErrorActivity.show(mContext, ex.getMessage());
        if (mDefaultHandler != null && (BuildConfig.DEBUG || (!handleException(ex)))) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();


        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                SimplexToast.showMyToast("OSC异常；正准备重启！！",GlobalApplication.getContext());
                Looper.loop();
            }
        }.start();

        GlobalApplication.getInstance().clearAppCache();


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return true;
    }
}

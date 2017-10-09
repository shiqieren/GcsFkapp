package com.gcs.jyfk.utils;

import android.text.TextUtils;
import android.util.Log;


/**
 * Created by Administrator on 0001 9-1.
 */

public class MyLog {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    //均不显示
    public static final int NOTHING = 6;

    public static final int level = NOTHING;


    public static void v(String tag, String log) {

        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log) && level<=VERBOSE) Log.v(tag, log);
    }

    public static void d(String tag, String log) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log)&& level<=DEBUG) Log.d(tag, log);
    }

    public static void i(String tag, String log) {

        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log) && level<=INFO) Log.i(tag, log);
    }

    public static void w(String tag, String log) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log) && level<=WARN) Log.w(tag, log);
    }

    public static void e(String tag, String log) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log) && level<=ERROR) Log.e(tag, log);
    }


}

package com.gcs.jyfk.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.gcs.jyfk.GlobalApplication;


/**
 * Created by lyw on 2017/7/25.
 */

public class UIUtils {
    public static Context getContext() {
        return GlobalApplication.getContext();
    }

    public static Handler getHandler() {
        return GlobalApplication.getHandler();
    }

    public static int getMainThreadId() {
        return GlobalApplication.getMainThreadId();
    }

    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    public static int getColor(int id) {
        return getContext().getResources().getColor(id);
    }

    public static ColorStateList getColorStateList(int id) {
        return getContext().getResources().getColorStateList(id);
    }

    public static int getDimen(int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }


    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    public static boolean isRunOnUIThread() {
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId()) {
            return true;
        }

        return false;
    }

    public static void runOnUIThread(Runnable r) {
        if (isRunOnUIThread()) {
            r.run();
        } else {
            getHandler().post(r);
        }
    }


}

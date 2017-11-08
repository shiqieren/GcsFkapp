package com.gcs.jyfk.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;


import com.gcs.jyfk.GlobalApplication;

import java.io.File;


/**
 * Created by lyw on 2017/7/23.
 */

public class VersionUtil {
    //是否有网络
    public static boolean hasInternet() {
        ConnectivityManager cm = (ConnectivityManager) GlobalApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.isConnected();
    }

    //是否打开wifi
    public static boolean isWifiOpen() {
        ConnectivityManager cm = (ConnectivityManager) GlobalApplication
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        if (!info.isAvailable() || !info.isConnected()) return false;
        if (info.getType() != ConnectivityManager.TYPE_WIFI) return false;
        return true;
    }

    //获得版本号
    public static int getVersionCode() {
        return getVersionCode(GlobalApplication.getContext().getPackageName());
    }

    //根据包名获得版本号
    public static int getVersionCode(String packageName) {
        try {
            return GlobalApplication.getContext()
                    .getPackageManager()
                    .getPackageInfo(packageName, 0)
                    .versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            return 0;
        }
    }

    //获得版本名称
    public static String getVersionName() {
        try {
            return GlobalApplication.getContext()
                    .getPackageManager()
                    .getPackageInfo(GlobalApplication.getContext().getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            return "undefined version name";
        }
    }

    //安装app
    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists())
            return;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean openAppActivity(Context context,
                                          String packageName,
                                          String activityName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName cn = new ComponentName(packageName, activityName);
        intent.setComponent(cn);
        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}

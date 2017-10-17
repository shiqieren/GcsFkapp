package com.gcs.jyfk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.TextUtils;

import com.bqs.crawler.cloud.sdk.BqsParams;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.VersionUtil;


/**
 * Created by lyw on 2017/7/23.
 */

public final class Setting {

    //服务url地址
    private static final String KEY_SEVER_URL = "serverUrl";
    //版本号
    private static final String KEY_VERSION_CODE = "versionCode";
    //客户端唯一标识
    public static final String KEY_APP_UNIQUE_ID = "appUniqueID";

    private static final String KEY_LOCATION_INFO = "locationInfo";
    private static final String KEY_LOCATION_PERMISSION = "locationPermission";
    private static final String KEY_LOCATION_APP_CODE = "locationAppCode";
    //白骑士授权参数
    public static BqsParams bqsParams;

    public static SharedPreferences getSettingPreferences(Context context) {
        return context.getSharedPreferences(Setting.class.getName(), Context.MODE_PRIVATE);
    }

    public static boolean checkIsNewVersion(Context context) {
        int saveVersionCode = getSaveVersionCode(context);
        int currentVersionCode = VersionUtil.getVersionCode();
        if (saveVersionCode < currentVersionCode) {
            updateSaveVersionCode(context, currentVersionCode);
            return true;
        }
        return false;
    }

    public static int getSaveVersionCode(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        return sp.getInt(KEY_VERSION_CODE, 0);
    }
    //更新版本号
    private static int updateSaveVersionCode(Context context, int version) {
        SharedPreferences sp = getSettingPreferences(context);
        Editor editor = sp.edit().putInt(KEY_VERSION_CODE, version);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
        return version;
    }

    public static String getServerUrl(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        String url = sp.getString(KEY_SEVER_URL, null);
        if (TextUtils.isEmpty(url)) {
            String[] urls = BuildConfig.API_SERVER_URL.split(";");
            if (urls.length > 0) {
                url = urls[0];
            } else {
                url = "http://www.guanyunsz.com/";
            }
            updateServerUrl(context, url);
        }

        MyLog.i("GCS","当前服务器连接url固定地址"+url);
        return url;
    }

    public static void updateServerUrl(Context context, String url) {
        SharedPreferences sp = getSettingPreferences(context);
        Editor editor = sp.edit().putString(KEY_SEVER_URL, url);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }


    public static void updateLocationInfo(Context context, boolean hasLocation) {
        SharedPreferences sp = getSettingPreferences(context);
        SharedPreferences.Editor editor = sp.edit().putBoolean(KEY_LOCATION_INFO, hasLocation);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static boolean hasLocation(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        return sp.getBoolean(KEY_LOCATION_INFO, false);
    }

    public static void updateLocationPermission(Context context, boolean hasPermission) {
        SharedPreferences sp = getSettingPreferences(context);
        SharedPreferences.Editor editor = sp.edit().putBoolean(KEY_LOCATION_PERMISSION, hasPermission);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static boolean hasLocationPermission(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        return sp.getBoolean(KEY_LOCATION_PERMISSION, false);
    }

    public static void updateLocationAppCode(Context context, int appCode) {
        SharedPreferences sp = getSettingPreferences(context);
        SharedPreferences.Editor editor = sp.edit().putInt(KEY_LOCATION_APP_CODE, appCode);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static int hasLocationAppCode(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        return sp.getInt(KEY_LOCATION_APP_CODE, 0);
    }


}


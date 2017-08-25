package com.gcs.fengkong;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.TextUtils;

import com.gcs.fengkong.utils.VersionUtil;


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
                url = "https://www.oschina.net/";
            }
            updateServerUrl(context, url);
        }

        GlobalApplication.showToast(url);
        return url;
    }

    public static void updateServerUrl(Context context, String url) {
        SharedPreferences sp = getSettingPreferences(context);
        Editor editor = sp.edit().putString(KEY_SEVER_URL, url);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }



}


package com.gcs.jyfk.ui.api;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.SharedPreferencesCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.Setting;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.MACgetUtil;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.NetWorkUtils;
import com.gcs.jyfk.utils.UIUtils;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.UUID;

import okhttp3.Call;

import static android.content.Context.TELEPHONY_SERVICE;

public class ApiClientHelper {


    /**
     * 获得请求的服务端数据的userAgent
     * 客户端唯一标识
     *
     * @param appContext
     * @return
     */
    public static void getUserAgent(Application appContext) {

        //全局手机管理器
        TelephonyManager tm = (TelephonyManager) UIUtils.getContext().getSystemService(TELEPHONY_SERVICE);
        //// 获取智能设备唯一编号
        String deviceid = tm.getDeviceId() + "";
        MyLog.i("GCS", "deviceid:" + deviceid);
        // 获得SIM卡的序号
        String imei = tm.getSimSerialNumber() + "";

        MyLog.i("GCS", "imei:" + imei);
        //国际移动用户识别码-用户Id
        String imsi = tm.getSubscriberId() + "";

        MyLog.i("GCS", "imsi:" + imsi);
        //MAC
        String mac = MACgetUtil.getAdresseMAC(UIUtils.getContext()) + "";

        MyLog.i("GCS", "mac:" + mac);
        //ip
        String ip = NetWorkUtils.getIPAddress(GlobalApplication.getContext()) + "";

        MyLog.i("GCS", "ip:" + ip);
        //本机号码
        String phone = tm.getLine1Number() + "";

        MyLog.i("GCS", "phone:" + phone);
        // WebSettings.getDefaultUserAgent(appContext)
        //版本号
        int vCode = getPackageInfo(appContext).versionCode;
        //发布
        String version = Build.VERSION.RELEASE; // "1.0" or "3.4b5"
        String osVer = version.length() > 0 ? version : "1.0";

        String model = Build.MODEL;
        MyLog.i("客户端唯一标识Build.MODEL>>>", "getBuild.MODEL:" + model);
        String id = Build.ID; // "MASTER" or "M4-rc20"
        MyLog.i("客户端唯一标识Build.ID>>>", "getBuild.ID:" + id);
        if (id.length() > 0) {
            model += " Build/" + id;
        }

        String format = "GCSfk.NET/1.0 (gcsapp; %s; Android %s; %s; %s)";
        // String ua = String.format(format, vCode, osVer, model, getAppId(appContext));
        if (AccountHelper.isLogin()) {
            User user = AccountHelper.getUser();
            user.getMore().setImei(imei);
            user.getMore().setImsi(imsi);
            user.getMore().setMac(mac);
            user.getMore().setIpnum(ip);
           /*关于通过代码获取手机号
           if (phone !=null){
                MyLog.i("GCS","通过后台代码获取到使用者当前手机号");
                user.setPhone(phone);
            }*/


            //地理位置
            String myaddress = user.getMore().getAddress() + "";
            //token
            String token = user.getToken().toString() + "";
            AccountHelper.updateUserCache(user);
            MyApi.sendUserAgent(imei, imsi, ip, mac, myaddress, token, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    MyLog.i("GCS", "平台信息上传响应返回Exception" + e.toString());
                }

                @Override
                public void onResponse(String response, int id) {
                    MyLog.i("GCS", "平台信息上传响应返回" + response.toString());
                    try {
                        Type type = new TypeToken<ResultBean>() {
                        }.getType();
                        ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                        int code = resultBean.getCode();
                        if (code == 200) {
                            MyLog.i("GCS", "平台信息上传200状态码");
                        } else {
                            String message = resultBean.getMessage();
                            if (code == 500) {
                                MyLog.i("GCS", "状态码500" + message);
                            } else if (code == 300) {
                                MyLog.i("GCS", "状态码300" + message);
                            }

                            //更新失败应该是不进行任何的本地操作
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {

        }
    }

    public static String getDefaultUserAgent() {
        StringBuilder result = new StringBuilder(64);
        result.append("Dalvik/");
        result.append(System.getProperty("java.vm.version")); // such as 1.1.0
        result.append(" (Linux; U; Android ");

        String version = Build.VERSION.RELEASE; // "1.0" or "3.4b5"
        result.append(version.length() > 0 ? version : "1.0");

        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }
        String id = Build.ID; // "MASTER" or "M4-rc20"
        if (id.length() > 0) {
            result.append(" Build/");
            result.append(id);
        }
        result.append(")");
        return result.toString();
    }

    private static String getAppId(Application context) {
        if (context != null) {
            SharedPreferences sp = Setting.getSettingPreferences(context);
            String uniqueID = sp.getString(Setting.KEY_APP_UNIQUE_ID, null);
            if (TextUtils.isEmpty(uniqueID)) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sp.edit().putString(Setting.KEY_APP_UNIQUE_ID, uniqueID);
                SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
            }
            return uniqueID;
        }
        return UUID.randomUUID().toString();
    }

    private static PackageInfo getPackageInfo(Application context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

}

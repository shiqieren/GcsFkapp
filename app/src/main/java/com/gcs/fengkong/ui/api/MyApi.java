package com.gcs.fengkong.ui.api;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.Setting;
import com.gcs.fengkong.utils.MyLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.internal.http2.Header;

public class MyApi {


    public static final int CATALOG_ALL = 0;

    public static final String REGISTER_INTENT = "1";
    public static final String RESET_PWD_INTENT = "2";

    public static final String CATALOG_NEWS_DETAIL = "news";



   /*
    Map<String, String> params = new HashMap<>();
        params.put("username", "张鸿洋");
        params.put("password", "123");

    Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");


                .url(url)//
                .params(params)//
                .headers(headers)//
                .build()//
                .execute(new MyStringCallback());
        */

    /**
     * 初始化网络请求，包括Cookie的初始化
     *
     * @param context AppContext
     *
     *
            dispatcher = new Dispatcher();
            protocols = DEFAULT_PROTOCOLS;
            connectionSpecs = DEFAULT_CONNECTION_SPECS;
            eventListenerFactory = EventListener.factory(EventListener.NONE);
            proxySelector = ProxySelector.getDefault();
            cookieJar = CookieJar.NO_COOKIES;
            socketFactory = SocketFactory.getDefault();
            hostnameVerifier = OkHostnameVerifier.INSTANCE;
            certificatePinner = CertificatePinner.DEFAULT;
            proxyAuthenticator = Authenticator.NONE;
            authenticator = Authenticator.NONE;
            connectionPool = new ConnectionPool();
            dns = Dns.SYSTEM;
            followSslRedirects = true;
            followRedirects = true;
            retryOnConnectionFailure = true;
            connectTimeout = 10_000;
            readTimeout = 10_000;
            writeTimeout = 10_000;
            pingInterval = 0;
     *
     *
     */
    public static void init(Application context) {
        //获取存储在sp中的cookie
        //PersistentCookieStore //持久化cookie
        //SerializableHttpCookie //持久化cookie
       // MemoryCookieStore //cookie信息存在内存中
      // ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(GlobalApplication.getContext()));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //          .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置固定设置值
                .cookieJar(cookieJar)
                // 校验安全
                /*.hostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        return true;
                    }
                })*/

       /* c.setUserAgent(ApiClientHelper.getUserAgent(AppContext.getInstance()));*/
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 销毁当前AsyncHttpClient 并重新初始化网络参数，初始化Cookie等信息
     *
     * @param appContext AppContext
     */
    public static void destroyAndRestore(Application appContext) {
        cleanCookie();
        init(appContext);
    }

    public static void cleanCookie(){
        if (OkHttpUtils.getInstance().getOkHttpClient()!= null) {
            CookieJar cookieJar = OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
            MyLog.i("GCS","未添加清除本地存储代码");
            if (cookieJar instanceof CookieJarImpl) {
                ((CookieJarImpl) cookieJar).getCookieStore().removeAll();
            }
        }

    }





    //根url通过build.gradle配置到setting的sp文件中，并获取得到
    public static String getAbsoluteApiUrl(String partUrl) {
        String url = partUrl;
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            url = Setting.getServerUrl(GlobalApplication.getContext())+partUrl;
        }
        MyLog.i("url","request:" + url);
        return url;
    }

    /*
    * 检查更新
    * */
    public static void checkUpdate(StringCallback callback) {
        MyLog.i("GCS","app检查更新");
        /*Map<String, String> params = new HashMap<>();
        params.put("appId", "1");
        params.put("catalog", "1");
        params.put("all", "false");
        OkHttpUtils.get().url(getAbsoluteApiUrl("action/apiv2/product_version")).params(params).build().execute(callback);
        */
        // Set AppToken  加密后的用户登录状态......addHeader("AppToken", Verifier.getPrivateToken(application));加密后
        //客户端唯一标识UserAgent uuid
    }


    /**
     * login account
     *
     * @param username username
     * @param pwd      pwd
     */
    public static void login(String username, String pwd, StringCallback callback) {
        Map<String, String> params = new HashMap<>();
       // params.put("appId", "1");
       // params.put("catalog", "1");
       // params.put("all", "false");
        params.put("name", username);
        params.put("password", pwd);
        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/login.do")).params(params).build().execute(callback);
        MyLog.i("GCS","登录url:"+getAbsoluteApiUrl("wind-phone/phone/login.do"));
    }

    /**
     * login account
     *
     * @param username username
     * @param pwd      pwd
     */
    public static void loginbysms(String username, String pwd,String jsessionid, StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        // params.put("appId", "1");
        // params.put("catalog", "1");
        // params.put("all", "false");
        params.put("name", username);
        params.put("code", pwd);
        params.put("JSESSIONID",jsessionid);
        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/smsLogin.do")).params(params).build().execute(callback);
        MyLog.i("GCS","短信登录url:"+getAbsoluteApiUrl("wind-phone/phone/smsLogin.do"));
    }

    public static void sendRegisterSmsCode(String phone, StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        //  params.put("appId", "1");
        //  params.put("token", "1");
        params.put("name", phone);
        //  params.put("intent", intent);

        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/registerSendMsg.do")).params(params).build().execute(callback);
        MyLog.i("GCS","请求发送短信验证码url:"+getAbsoluteApiUrl("wind-phone/phone/registerSendMsg.do"));
    }


    public static void getverifyStatus(String token, StringCallback callback) {
        Map<String, String> params = new HashMap<>();
      //  params.put("appId", "1");
      //  params.put("token", "1");
        params.put("token", token);
      //  params.put("intent", intent);

        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/verifyStatus.do")).params(params).build().execute(callback);
        MyLog.i("GCS","请求发送短信验证码url:"+getAbsoluteApiUrl("wind-phone/phone/verifyStatus.do"));
    }

    public static void sendSmsCode(String phone, StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        //  params.put("appId", "1");
        //  params.put("token", "1");
        params.put("name", phone);
        //  params.put("intent", intent);

        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/smsSend.do")).params(params).build().execute(callback);
        MyLog.i("GCS","请求发送短信验证码url:"+getAbsoluteApiUrl("wind-phone/phone/smsSend.do"));
    }
    /**
     *
     *
     * @param phoneNumber phoneNumber
     * @param smsCode     smsCode
     * @param pwd      pwd
     */
    public static void register(String phoneNumber, String smsCode, String pwd,String jsessionid,StringCallback callback) {
        Map<String, String> params = new HashMap<>();
       // params.put("appId", "1");
       /// params.put("phone", phoneNumber);
        params.put("name",phoneNumber);
        params.put("password",pwd);
        params.put("code",smsCode);
        params.put("JSESSIONID",jsessionid);
        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/register.do")).params(params).build().execute(callback);
        MyLog.i("GCS","注册账户url:"+getAbsoluteApiUrl("wind-phone/phone/register.do"));
        MyLog.i("GCS","短信验证码："+smsCode);

      //  OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/register.do?"+"name="+phoneNumber+"&password="+pwd+"&code="+smsCode)).build().execute(callback);
    }

    /**
     * validate and get phone token
     *
     * @param token token
     *
     */
    public static void sendUserAgent(String imei,String imsi,String ip,String mac,String address,String token,StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        // params.put("appId", "1");
        /// params.put("phone", phoneNumber);
        params.put("imei",imei);
        params.put("imsi",imsi);
        params.put("ip",ip);
        params.put("mac",mac);
        params.put("address", address);
        params.put("platform","ANDROID");
        params.put("token",token);
        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/map/statistical.do")).params(params).build().execute(callback);
        MyLog.i("GCS","发送平台信息url:"+getAbsoluteApiUrl("wind-phone/phone/map/statistical.do"));


        //  OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/register.do?"+"name="+phoneNumber+"&password="+pwd+"&code="+smsCode)).build().execute(callback);
    }

    /**
     * reset pwd
     *
     * @param phoneNumber phoneNumber
     * @param smsCode     smsCode
     * @param pwd      pwd
     */
    public static void resetPwd(String phoneNumber, String smsCode, String pwd,String jsessionid,StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("name",phoneNumber);
        params.put("password",pwd);
        params.put("code",smsCode);
        params.put("JSESSIONID",jsessionid);
        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/updatePass.do")).params(params).build().execute(callback);
    }

    /**
     * zhima auth
     *
     */
    public static void authzhima(String token, String mobileno, String certno,String name,String openid,StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("token",token);
        params.put("mobileno",mobileno);
        params.put("certno",certno);
        params.put("name",name);
        //params.put("openid ",openid);
        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/zhima/authOpenId.do")).params(params).build().execute(callback);
    }

    /**
     * reset password
     *
     */
    public static void updatePasswd(String token, String opasswd, String npasswd,String vpasswd,StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("token",token);
        params.put("opasswd",opasswd);
        params.put("npasswd",npasswd);
        params.put("vpasswd",vpasswd);
        //params.put("openid ",openid);
        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/updatePasswd.do")).params(params).build().execute(callback);
        MyLog.i("GCS","更新密码url:"+getAbsoluteApiUrl("wind-phone/phone/updatePasswd.do"));
    }

    /**
     * feedback
     *
     */
    public static void feedBack(String token, String message,StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("token",token);
        params.put("message",message);
        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/feedBackMess.do")).params(params).build().execute(callback);
        MyLog.i("GCS","意见反馈url:"+getAbsoluteApiUrl("wind-phone/phone/feedBackMess.do"));
    }

    /**
     * changestatus
     *
     */
    public static void changestatus(String token, String type,String status,StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("token",token);
        params.put("type",type);
        params.put("status",status);
        OkHttpUtils.post().url(getAbsoluteApiUrl("wind-phone/phone/changeStatus.do")).params(params).build().execute(callback);
        MyLog.i("GCS","更改状态url:"+getAbsoluteApiUrl("wind-phone/phone/changeStatus.do"));
    }
}

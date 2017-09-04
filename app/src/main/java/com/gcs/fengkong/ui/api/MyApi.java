package com.gcs.fengkong.ui.api;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.Setting;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

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
      //  CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(GlobalApplication.getContext()));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //          .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置固定设置值
                //.cookieJar(cookieJar)
               // AccountHelper.getCookie();
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




    //根url通过build.gradle配置到setting的sp文件中，并获取得到
    public static String getAbsoluteApiUrl(String partUrl) {
        String url = partUrl;
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            url = Setting.getServerUrl(GlobalApplication.getContext())+partUrl;
        }
        Log.i("url","request:" + url);
        return url;
    }

    /*
    * 检查更新
    * */
    public static void checkUpdate(StringCallback callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", "1");
        params.put("catalog", "1");
        params.put("all", "false");
        OkHttpUtils.get().url(getAbsoluteApiUrl("action/apiv2/product_version")).params(params).build().execute(callback);

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
        params.put("appId", "1");
        params.put("catalog", "1");
        params.put("all", "false");
        params.put("account", username);
        params.put("password", pwd);
        OkHttpUtils.get().url(getAbsoluteApiUrl("action/apiv2/account_login")).params(params).build().execute(callback);

    }

    public static void sendSmsCode(String phone, String intent,StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", "1");
        params.put("token", "1");
        params.put("phone", phone);
        params.put("intent", intent);

        OkHttpUtils.post().url(getAbsoluteApiUrl("action/apiv2/account_login")).params(params).build().execute(callback);
    }

    /**
     * validate and get phone token
     *
     * @param phoneNumber phoneNumber
     * @param smsCode     smsCode
     * @param pwd      pwd
     */
    public static void register(String phoneNumber, String smsCode, String pwd,StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", "1");
        params.put("phone", phoneNumber);
        params.put("code", smsCode);
        params.put("password", pwd);
        OkHttpUtils.post().url(getAbsoluteApiUrl("action/apiv2/account_login")).params(params).build().execute(callback);

    }


    /**
     * reset pwd
     *
     * @param phoneNumber phoneNumber
     * @param smsCode     smsCode
     * @param pwd      pwd
     */
    public static void resetPwd(String phoneNumber, String smsCode, String pwd,StringCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", "1");
        params.put("phone", phoneNumber);
        params.put("code", smsCode);
        params.put("password", pwd);
        OkHttpUtils.post().url(getAbsoluteApiUrl("action/apiv2/account_login")).params(params).build().execute(callback);
    }

}

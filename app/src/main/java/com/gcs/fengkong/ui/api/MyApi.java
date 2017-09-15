package com.gcs.fengkong.ui.api;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.Setting;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.bean.User;
import com.gcs.fengkong.utils.MyLog;
import com.gcs.fengkong.utils.SharedPreferencesHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.store.CookieStore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.internal.http2.Header;

public class MyApi {


    public static final int CATALOG_ALL = 0;

    public static final String REGISTER_INTENT = "1";
    public static final String RESET_PWD_INTENT = "2";

    public static final String CATALOG_NEWS_DETAIL = "news";



    private static OkHttpClient okHttpClient;
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

        okHttpClient = new OkHttpClient.Builder()
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
    /*设置头cookie*/
    public static void setCookieHeader(String cookie) {
        if (!TextUtils.isEmpty(cookie))
           // CLIENT.addHeader("Cookie", cookie);
        MyLog.i("GCS","setCookieHeader:" + cookie);
    }
    /**
     * 销毁当前AsyncHttpClient 并重新初始化网络参数，初始化Cookie等信息
     *
     * @param appContext AppContext
     */
    public static void destroyAndRestore(Application appContext) {
        cleanCookie();
        okHttpClient = null;
        init(appContext);
    }

    public static void cleanCookie() {
        // first clear store
        // new PersistentCookieStore(AppContext.getInstance()).clear();
        // clear header
        /*if (client != null) {
            HttpContext httpContext = client.getHttpContext();
            CookieStore cookies = (CookieStore) httpContext
                    .getAttribute(HttpClientContext.COOKIE_STORE);
            // 清理Async本地存储
            if (cookies != null) {
                cookies.clear();
            }
            // 清理当前正在使用的Cookie
            client.removeHeader("Cookie");
        }*/
        MyLog.i("GCS","网络请求的  cleanCookie");
    }

    /**
     * 从okhttpClient自带缓存中获取CookieString
     *
     * @param
     * @return CookieString
     */
    private static String getClientCookie(OkHttpClient client) {
        String cookie = "";
        if (client != null) {
            //获得cookie
            /*HttpContext httpContext = client.getHttpContext();
            CookieStore cookies = (CookieStore) httpContext
                    .getAttribute(HttpClientContext.COOKIE_STORE);*/

            /*if (cookies != null && cookies.getCookies() != null && cookies.getCookies().size() > 0) {
                for (Cookie c : cookies.getCookies()) {
                    cookie += (c.getName() + "=" + c.getValue()) + ";";
                }
            }*/
        }
        MyLog.i("GCS","getClientCookie:" + cookie);
        return cookie;
    }

    /**
     * 得到当前的网络请求Cookie，
     * 登录后触发
     *
     * @param headers Header
     */
    public static String getCookie(Header[] headers) {
        String cookie = getClientCookie(okHttpClient);
        /*if (TextUtils.isEmpty(cookie)) {
            cookie = "";
            if (headers != null) {
                for (Header header : headers) {
                    String key = header.getName();
                    String value = header.getValue();
                    if (key.contains("Set-Cookie"))
                        cookie += value + ";";
                }
                if (cookie.length() > 0) {
                    cookie = cookie.substring(0, cookie.length() - 1);
                }
            }
        }*/

        MyLog.i("GCS","getCookie:" + cookie);
        return cookie;
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
}

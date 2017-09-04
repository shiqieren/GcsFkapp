package com.gcs.fengkong;

/**
 * Created by lyw on 2017/7/20.
 */

import android.content.Context;
import android.os.Environment;


import com.gcs.fengkong.utils.StreamUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 *
 * 应用程序配置类
 * 用于保存用户相关信息及设置
 * 键值对形式
 *
 * @author lyw
 *	获取单例
 */
public class AppConfig {
    private final static String APP_CONFIG = "config";
    //是否检查更新设置参数
    public static final String KEY_CHECK_UPDATE = "KEY_CHECK_UPDATE";
    //是否设置双击退出
    public static final String KEY_DOUBLE_CLICK_EXIT = "KEY_DOUBLE_CLICK_EXIT";

    //登录相关——用户切换和退出
    public static final String INTENT_ACTION_USER_CHANGE = "net.gcsfk.action.USER_CHANGE";
    public static final String INTENT_ACTION_LOGOUT = "net.gcsfk.action.LOGOUT";
    public static final String HOLD_ACCOUNT = "hold_account";
    public static final String RETRIEVE_PWD_URL = "https://www.oschina.net/home/reset-pwd";

    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "liyiwei"
            + File.separator + "lyw_img" + File.separator;
    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "liyiwei"
            + File.separator + "download" + File.separator;

    private Context mContext;
    private static AppConfig appConfig;

    ///获取单例
    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fis);
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fos);
        }
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);

        //R.string.releaseUrl
        //BuildConfig.API_SERVER_URL

        //BuildConfig.API_SERVER_URL
    }




}

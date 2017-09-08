package com.gcs.fengkong.ui.atys;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.Setting;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.bean.User;
import com.gcs.fengkong.utils.AppOperator;


/**
 * @author lyw
 * 欢迎界面，版本检测
 *
 */
public class LaunchActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.app_start;
    }
    @Override
    protected void initData() {

        //配置文件生成并记录app当前进入时间点
        //  AppConfig.getAppConfig(LaunchActivity.this).set("system_time", "初始化");


        super.initData();
        // 在这里我们检测是否是新版本安装，如果是则进行老版本数据迁移工作
        // 该工作可能消耗大量时间所以放在自线程中执行
        //线程池执行
        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                Log.i("GCS","线程池开启线程，异步检查新版本的数据迁移工作");
                doMerge();
            }
        });
    }
    //版本检查

    private void doMerge() {


        // 判断是否是新版本
        if (Setting.checkIsNewVersion(this)) {
            Log.i("GCS","新版本更新后进行cookie搬移操作,全局中app_config中获取cookie赋值给user");
            String cookie = GlobalApplication.getInstance().getContextAppConfigValue("cookie");
            //判断是否为空cookie
            if (!TextUtils.isEmpty(cookie)) {
                GlobalApplication.getInstance().removeContextAppConfigValue("cookie");
                User user = AccountHelper.getUser();
                user.setCookie(cookie);
                Log.i("GCS","app更新后自动登录和更新缓存");
                AccountHelper.updateUserCache(user);
                GlobalApplication.reInit();
            }
        }

        // Delay...
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 完成后进行跳转操作
        redirectTo();
    }

    private void redirectTo() {
        Log.i("GCS","通常登录,非重新安装状态，先进入mainactivity再判断是否登录");
        /*if (!AccountHelper.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }else {

        }*/
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

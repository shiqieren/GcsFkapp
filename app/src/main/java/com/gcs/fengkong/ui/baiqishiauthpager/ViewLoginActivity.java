package com.gcs.fengkong.ui.baiqishiauthpager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bqs.crawler.cloud.sdk.ServiceId;
import com.bqs.crawler.cloud.sdk.view.LoginView;
import com.bqs.crawler.cloud.sdk.view.OnLoginViewListener;
import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.bean.User;
import com.gcs.fengkong.ui.atys.BaseActivity;
import com.gcs.fengkong.ui.widget.statusbar.StatusBarCompat;
import com.gcs.fengkong.utils.DialogUtil;
import com.gcs.fengkong.utils.MyLog;
import com.gcs.fengkong.utils.UIUtils;

/**
 * Desction:授权成功操作
 * Author:lyw
 * Date:2016/10/19 下午10:56
 */
public class ViewLoginActivity extends BaseActivity implements OnLoginViewListener, View.OnClickListener {

    public static final String TAG = ViewLoginActivity.class.getSimpleName();

    public static final String PARAMS_DATA_TYPE = "params_data_type";

    private LoginView loginView;
    private TextView tvTitle;
    private TextView tvLoginType;
    private ProgressDialog mDialog;
    private int serviceId;

    @Override
    protected int getContentView() {
        return R.layout.activity_webview_login;
    }


    @Override
    protected void initWindow() {
        super.initWindow();
        StatusBarCompat.setStatusBarColor(this, UIUtils.getColor(R.color.base_app_color));
        String message = getResources().getString(R.string.progress_holdon);
        if (mDialog == null) {
            mDialog = DialogUtil.getProgressDialog(this, message, false);//DialogHelp.getWaitDialog(this, message);
        }


        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLoginType = (TextView)findViewById(R.id.tv_login_type);
        tvLoginType.setOnClickListener(this);

        Intent intent = getIntent();
        serviceId = intent.getIntExtra(PARAMS_DATA_TYPE, 0);
        String s = "";
        if (serviceId == ServiceId.JD_SERVICE_ID) {
            s = "京东";
        } else if (serviceId == ServiceId.TB_SERVICE_ID) {
            s = "淘宝";
        } else if (serviceId == ServiceId.ALIPAY_SERVICE_ID) {
            s = "支付宝";
        } else if (serviceId == ServiceId.LINKEDIN_SERVICE_ID) {
            s = "领英";
        } else if (serviceId == ServiceId.QZONE_SERVICE_ID) {
            s = "QQ";
        } else if (serviceId == ServiceId.WEIBO_SERVICE_ID) {
            s = "微博";
        } else if (serviceId == ServiceId.QQ_EMAIL_BILL_SERVICE_ID) {
            s = "QQ邮箱";
        } else if (serviceId == ServiceId._163_EMAIL_BILL_SERVICE_ID) {
            s = "163邮箱";
        } else if (serviceId == ServiceId._126_EMAIL_BILL_SERVICE_ID) {
            s = "126邮箱";
        } else if (serviceId == ServiceId._139_EMAIL_BILL_SERVICE_ID) {
            s = "139邮箱";
        } else if (serviceId == ServiceId.ALI_EMAIL_BILL_SERVICE_ID) {
            s = "阿里邮箱";
        } else if (serviceId == ServiceId.SOHU_EMAIL_BILL_SERVICE_ID) {
            s = "搜狐邮箱";
        } else if (serviceId == ServiceId.SINA_EMAIL_BILL_SERVICE_ID) {
            s = "新浪邮箱";
        } else if (serviceId == ServiceId.HOTMAIL_EMAIL_BILL_SERVICE_ID) {
            s = "Hotmail邮箱";
        }
        tvTitle.setText(s + "授权");


        loginView = (LoginView) findViewById(R.id.login_view);
        loginView.setOnLoginViewListener(this);

        LoginView.LoginType defaultLoginType;
        if(serviceId == ServiceId.TB_SERVICE_ID || serviceId == ServiceId.JD_SERVICE_ID){
            tvLoginType.setVisibility(View.VISIBLE);
            defaultLoginType = LoginView.LoginType.QR_CODE_LOGIN;
        } else {
            tvLoginType.setVisibility(View.GONE);
            defaultLoginType = LoginView.LoginType.WEBVIEW_LOGIN;
        }
        loginView.setLoginType(defaultLoginType, serviceId);
    }

    @Override
    public void onLoginSuccess(int serviceId) {
        //AppCacheUtils.getInstance(ViewLoginActivity.this).put(serviceId + "", true);
        User user = AccountHelper.getUser();
        String s = "";
        if (serviceId == ServiceId.JD_SERVICE_ID) {
            //设置该用户京东授权状态
            user.getAuthstate().setAuth_jd(true);
            s = "京东";
        } else if (serviceId == ServiceId.TB_SERVICE_ID) {
            //设置该用户淘宝授权状态
            user.getAuthstate().setAuth_taobao(true);
            s = "淘宝";
        } else if (serviceId == ServiceId.ALIPAY_SERVICE_ID) {
            //设置该用户支付宝授权状态
            user.getAuthstate().setAuth_alipay(true);
            s = "支付宝";
        } else if (serviceId == ServiceId.LINKEDIN_SERVICE_ID) {
            s = "LinkedIn";
        } else if (serviceId == ServiceId.QZONE_SERVICE_ID) {
            s = "QQ";
        } else if (serviceId == ServiceId.WEIBO_SERVICE_ID) {
            s = "微博";
        }
        //Toast.makeText(getBaseContext(), "\"" + s + "\"授权成功", Toast.LENGTH_SHORT).show();
        GlobalApplication.showToast(s+"认证成功",0,0, Gravity.CENTER);
        AccountHelper.updateUserCache(user);
        MyLog.i("GCS","更新sp中user的认证状态值");
        finish();
    }
    
    @Override
    public void onLoginFailure(String resultCode, String resultDesc, int serviceId) {
        Toast.makeText(getBaseContext(), resultDesc, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadUrlProgress(int progress) {
        Log.d(TAG, "onLoadUrlProgress"+progress);
    }

    @Override
    public void onLoadUrlStart() {
        Log.d(TAG, "onLoadUrlStart");
    }

    @Override
    public void onLoadUrlFinish() {
        Log.d(TAG, "onLoadUrlFinish");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_login_type) {
            if(serviceId == ServiceId.JD_SERVICE_ID || serviceId == ServiceId.TB_SERVICE_ID){
                LoginView.LoginType newType;

                if (loginView.getLoginType() == LoginView.LoginType.QR_CODE_LOGIN) {
                    tvLoginType.setText("二维码登录");
                    newType = LoginView.LoginType.WEBVIEW_LOGIN;
                } else {
                    tvLoginType.setText("账号登录");
                    newType = LoginView.LoginType.QR_CODE_LOGIN;
                }

                loginView.setLoginType(newType, serviceId);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && loginView.canGoBack()) {
            if(mDialog != null) {
                mDialog.dismiss();
            }
            loginView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDialog != null) {
            mDialog.dismiss();
        }
        loginView.destroy();
    }
}

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
import com.gcs.fengkong.ui.api.MyApi;
import com.gcs.fengkong.ui.atys.BaseActivity;
import com.gcs.fengkong.ui.bean.base.ResultBean;
import com.gcs.fengkong.ui.widget.SimplexToast;
import com.gcs.fengkong.ui.widget.statusbar.StatusBarCompat;
import com.gcs.fengkong.utils.AppOperator;
import com.gcs.fengkong.utils.DialogUtil;
import com.gcs.fengkong.utils.MyLog;
import com.gcs.fengkong.utils.UIUtils;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

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
            defaultLoginType = LoginView.LoginType.WEBVIEW_LOGIN;
        } else {
            tvLoginType.setVisibility(View.GONE);
            defaultLoginType = LoginView.LoginType.ONEKEY_LOGIN;
        }
        loginView.setLoginType(defaultLoginType, serviceId);
    }

    @Override
    public void onLoginSuccess(int serviceId) {
        //AppCacheUtils.getInstance(ViewLoginActivity.this).put(serviceId + "", true);
        if (AccountHelper.isLogin()&&AccountHelper.isAuth()){
            User user = AccountHelper.getUser();
            String s = "";
            if (serviceId == ServiceId.JD_SERVICE_ID) {
                //设置该用户京东授权状态
                user.getAuthstate().setAuth_jd("1");
                MyApi.changestatus(user.getToken(), "jd", "1", new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Type type = new TypeToken<ResultBean>() {}.getType();
                            ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                            //注册结果返回该用户User
                            int code = resultBean.getCode();
                            String msg = resultBean.getMessage();
                            SimplexToast.showMyToast(msg,GlobalApplication.getContext());
                            switch (code) {
                                case 200://
                                    SimplexToast.showMyToast("京东成功登录"+msg,GlobalApplication.getContext());
                                    break;
                                case 500://
                                    SimplexToast.showMyToast("京东已经登录成功,,但是服务器状态值未上传成功，失败信息："+msg,GlobalApplication.getContext());
                                    break;

                                default:
                                    break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
                s = "京东";
            } else if (serviceId == ServiceId.TB_SERVICE_ID) {
                //设置该用户淘宝授权状态
                user.getAuthstate().setAuth_taobao("1");
                MyApi.changestatus(user.getToken(), "taobao", "1", new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Type type = new TypeToken<ResultBean>() {}.getType();
                            ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                            //注册结果返回该用户User
                            int code = resultBean.getCode();
                            String msg = resultBean.getMessage();
                            SimplexToast.showMyToast(msg,GlobalApplication.getContext());
                            switch (code) {
                                case 200://
                                    SimplexToast.showMyToast("淘宝成功登录"+msg,GlobalApplication.getContext());
                                    break;
                                case 500://
                                    SimplexToast.showMyToast("淘宝已经登录成功,,但是服务器状态值未上传成功，失败信息："+msg,GlobalApplication.getContext());
                                    break;

                                default:
                                    break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
                s = "淘宝";
            } else if (serviceId == ServiceId.ALIPAY_SERVICE_ID) {
                //设置该用户支付宝授权状态
                user.getAuthstate().setAuth_alipay("1");
                MyApi.changestatus(user.getToken(), "alipay", "1", new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Type type = new TypeToken<ResultBean>() {}.getType();
                            ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                            //注册结果返回该用户User
                            int code = resultBean.getCode();
                            String msg = resultBean.getMessage();
                            SimplexToast.showMyToast(msg,GlobalApplication.getContext());
                            switch (code) {
                                case 200://
                                    SimplexToast.showMyToast("支付宝成功登录"+msg,GlobalApplication.getContext());
                                    break;
                                case 500://
                                    SimplexToast.showMyToast("支付宝已经登录成功,,但是服务器状态值未上传成功，失败信息："+msg,GlobalApplication.getContext());
                                    break;

                                default:
                                    break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
                s = "支付宝";
            } else if (serviceId == ServiceId.LINKEDIN_SERVICE_ID) {
                s = "LinkedIn";
            } else if (serviceId == ServiceId.QZONE_SERVICE_ID) {
                s = "QQ";
            } else if (serviceId == ServiceId.WEIBO_SERVICE_ID) {
                s = "微博";
            }
            SimplexToast.showMyToast(s+"认证成功",GlobalApplication.getContext());
            AccountHelper.updateUserCache(user);
            MyLog.i("GCS","更新sp中user的认证状态值");
            finish();
        }
    }
    
    @Override
    public void onLoginFailure(String resultCode, String resultDesc, int serviceId) {
        SimplexToast.showMyToast(resultDesc,getBaseContext());
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

                if (loginView.getLoginType() == LoginView.LoginType.ONEKEY_LOGIN) {
                    tvLoginType.setText("一键登录");
                    newType = LoginView.LoginType.WEBVIEW_LOGIN;
                } else {
                    tvLoginType.setText("账号登录");
                    newType = LoginView.LoginType.ONEKEY_LOGIN;
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

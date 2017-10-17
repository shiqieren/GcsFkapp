package com.gcs.jyfk.ui.baiqishiauthpager;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bqs.crawler.cloud.sdk.mno.MnoAuthAction;
import com.bqs.crawler.cloud.sdk.mno.OnMnoAuthListener;
import com.bqs.crawler.cloud.sdk.mno.OnMnoSendSmsListener;
import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.atys.BaseActivity;
import com.gcs.jyfk.ui.atys.SimpleBackActivity;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.ui.widget.TimerButton;
import com.gcs.jyfk.utils.ActivityManager;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.DialogUtil;
import com.gcs.jyfk.utils.MyLog;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by lyw on 2016/11/16.
 */
public class MnoAuthActivity extends BaseActivity implements OnMnoAuthListener,OnMnoSendSmsListener {

    private ProgressDialog mDialog;
    private EditText etSmscode;
    private TextView tvTips;
    private LinearLayout llTips;
    private TimerButton btnTimer;

    private MnoAuthAction authAction = new MnoAuthAction();

    @Override
    protected void initWindow() {
        super.initWindow();
        String message = getResources().getString(R.string.progress_holdon);
        if (mDialog == null) {
            mDialog = DialogUtil.getProgressDialog(this, message, false);//DialogHelp.getWaitDialog(this, message);
        }
        etSmscode = (EditText)findViewById(R.id.et_smscode);
        tvTips = (TextView)findViewById(R.id.tv_tips);
        llTips = (LinearLayout)findViewById(R.id.ll_tips);
        tvTips = (TextView)findViewById(R.id.tv_tips);
        btnTimer = (TimerButton)findViewById(R.id.btn_timer);
        btnTimer.startTimer();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitClick();
            }
        });

        findViewById(R.id.btn_timer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendAuthSmsBtnClick();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_mno_auth;
    }

    public void onSubmitClick() {
        String smsCode = etSmscode.getText().toString();
        if (TextUtils.isEmpty(smsCode)) {
            etSmscode.setError("请输入短信验证码");
            return;
        }
        mDialog.show();
        authAction.verifyAuthSmsCode(smsCode, this);
    }

    public void onSendAuthSmsBtnClick() {
        mDialog.show();
        authAction.sendAuthSmsCode(this);
    }

    //mark OnMnoAuthListener
    @Override
    public void onAuthSuccess() {
        SimplexToast.showMyToast("运营商授权成功", GlobalApplication.getContext());
        if (AccountHelper.isLogin()){
            //登录成功
            User user = AccountHelper.getUser();
            //设置该用户运营商授权状态
            user.getAuthstate().setAuth_operator("1");
            MyApi.changestatus(user.getToken(), "operator", "1", new StringCallback() {
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
                                SimplexToast.showMyToast("运营商成功登录"+msg,GlobalApplication.getContext());
                                break;
                            case 500://
                                SimplexToast.showMyToast("运营商已经登录成功,,但是服务器状态值未上传成功，失败信息："+msg,GlobalApplication.getContext());
                                break;

                            default:
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
            AccountHelper.updateUserCache(user);
            MyLog.i("GCS","更新sp中user的认证状态值");
            mDialog.hide();
            ActivityManager.getActivityManager().finishActivity(this);
            //此处可能关闭不必要的activity，后期优化改为结果启动和结果返回来辨别
            ActivityManager.getActivityManager().finishActivity(SimpleBackActivity.class);
        }

    }

    @Override
    public void onAuthFailure(String resultCode, String resultDesc) {
        setTips(resultDesc);
        mDialog.hide();
    }

    //mark OnMnoSendSmsListener
    @Override
    public void onSendSmsSuccess() {
        setTips("短信发送成功");
        mDialog.hide();
        btnTimer.startTimer();
    }

    @Override
    public void onSendSmsFailure(String resultCode, String resultDesc) {
        setTips(resultDesc);
        mDialog.hide();
    }

    public void setTips(String tips) {
        if (TextUtils.isEmpty(tips)) {
            llTips.setVisibility(View.GONE);
            return;
        }
        llTips.setVisibility(View.VISIBLE);
        tvTips.setText(tips);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        authAction.destroy();
    }
}

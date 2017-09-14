package com.gcs.fengkong.ui.baiqishiauthpager;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bqs.crawler.cloud.sdk.mno.MnoAuthAction;
import com.bqs.crawler.cloud.sdk.mno.OnMnoAuthListener;
import com.bqs.crawler.cloud.sdk.mno.OnMnoSendSmsListener;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.atys.BaseActivity;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.widget.TimerButton;
import com.gcs.fengkong.utils.ActivityManager;
import com.gcs.fengkong.utils.DialogUtil;

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
        Toast.makeText(this, "运营商授权成功", Toast.LENGTH_SHORT).show();
        mDialog.hide();
        ActivityManager.getActivityManager().finishActivity(this);
        //此处可能关闭不必要的activity，后期优化改为结果启动和结果返回来辨别
        ActivityManager.getActivityManager().finishActivity(SimpleBackActivity.class);
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

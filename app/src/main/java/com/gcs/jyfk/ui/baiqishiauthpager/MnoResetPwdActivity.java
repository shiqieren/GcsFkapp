package com.gcs.jyfk.ui.baiqishiauthpager;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bqs.crawler.cloud.sdk.mno.MnoResetPwdAction;
import com.bqs.crawler.cloud.sdk.mno.OnMnoResetPwdListener;
import com.bqs.crawler.cloud.sdk.mno.OnMnoSendSmsListener;
import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.Setting;
import com.gcs.jyfk.ui.atys.BaseActivity;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.ui.widget.TimerButton;
import com.gcs.jyfk.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 重置运营商服务密码
 * Date:2017/7/7 下午11:51
 */
public class MnoResetPwdActivity extends BaseActivity implements OnMnoResetPwdListener, OnMnoSendSmsListener {

    private ProgressDialog mDialog;
    private EditText etSmscode;
    private TextView tvTips;
    private LinearLayout llTips;
    private TimerButton btnTimer;

    private EditText etNewPwd;
    private TextView tvNewPwdTips;

    private EditText etContacts;
    private TextView tvContactsTips;
    private LinearLayout llSmsCode;
    private
    EditText etMobile;
    private boolean smsCodeFlag, newPwdFlag, contactsFlag;
    private int contactsCount;

    MnoResetPwdAction resetPwdAction = new MnoResetPwdAction();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mDialog != null) {
                mDialog.dismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected int getContentView() {
        return R.layout.activity_mno_reset_pwd;
    }

    @Override
    protected void initWindow() {
        super.initWindow();
        String message = getResources().getString(R.string.progress_holdon);
        if (mDialog == null) {
            mDialog = DialogUtil.getProgressDialog(this, message, false);//DialogHelp.getWaitDialog(this, message);
        }
        //ActivityManager.getActivityManager().addActivity(this);

        etSmscode = (EditText)findViewById(R.id.et_smscode);
        etMobile = (EditText)findViewById(R.id.et_mobile);
        etMobile.setText(Setting.bqsParams.getMobile());
        tvTips = (TextView)findViewById(R.id.tv_tips);
        llTips = (LinearLayout)findViewById(R.id.ll_tips);
        tvTips = (TextView)findViewById(R.id.tv_tips);
        btnTimer = (TimerButton)findViewById(R.id.btn_timer);
        btnTimer.startTimer();
        etNewPwd = (EditText) findViewById(R.id.et_service_pwd);
        tvNewPwdTips = (TextView) findViewById(R.id.tv_pwd_tips);
        etContacts = (EditText) findViewById(R.id.et_contacts);
        tvContactsTips = (TextView) findViewById(R.id.tv_contacts_tips);
        llSmsCode = (LinearLayout) findViewById(R.id.ll_smscode);

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
                sendSmsCode();
            }
        });

        mDialog.show();
        resetPwdAction.resetPwd(this);
    }

    public void onSubmitClick() {
        String smsCode = null;
        if(smsCodeFlag) {
            smsCode = etSmscode.getText().toString();
            if (TextUtils.isEmpty(smsCode)) {
                etSmscode.setError("请输入短信验证码");
                return;
            }
        }

        String newPwd = null;
        if(newPwdFlag) {
            newPwd = etNewPwd.getText().toString();
            if (TextUtils.isEmpty(newPwd)) {
                etNewPwd.setError("请输入新密码");
                return;
            }
        }

        List<String> contactsList = null;
        if(contactsFlag) {
            contactsList = new ArrayList<>();
            String[]s = etContacts.getText().toString().split(",");
            if (s == null || s.length == 0 || s.length != contactsCount) {
                etContacts.setError(String.format(Locale.CHINA, "请输入%d个联系人号码, 以\",\"隔开", contactsCount));
                return;
            }
        }

        mDialog.show();
        resetPwdAction.resetPwd(smsCode, newPwd, contactsList, this);
    }

    private void sendSmsCode(){
        mDialog.show();
        resetPwdAction.sendResetPwdSmsCode(this);
    }

    //mark OnMnoResetPwdListener
    @Override
    public void onResetPwdSuccess() {
        mDialog.hide();
        SimplexToast.showMyToast("密码修改成功", GlobalApplication.getContext());
        finish();
    }

    @Override
    public void onResetPwdInputExtraData(boolean smsCodeFlag, boolean newPwdFlag, String newPwdTips, boolean contactsFlag, int contactsCount, String contactsTips, String resultDesc) {
        setTips(resultDesc);
        mDialog.hide();
        this.smsCodeFlag = smsCodeFlag;
        this.newPwdFlag = newPwdFlag;
        this.contactsFlag = contactsFlag;
        this.contactsCount = contactsCount;

        if(smsCodeFlag){
            llSmsCode.setVisibility(View.VISIBLE);
            btnTimer.startTimer();
        }

        if(newPwdFlag){
            etNewPwd.setVisibility(View.VISIBLE);
            tvNewPwdTips.setVisibility(View.VISIBLE);
            tvNewPwdTips.setText(newPwdTips);
        }

        if(contactsFlag){
            etContacts.setVisibility(View.VISIBLE);
            tvContactsTips.setVisibility(View.VISIBLE);
            tvContactsTips.setText(contactsTips);
        }
    }

    @Override
    public void onResetPwdFailure(String resultCode, String resultDesc) {
        setTips(resultDesc);
        mDialog.hide();
    }

    //mark OnMnoSendSmsListener
    @Override
    public void onSendSmsSuccess() {
        mDialog.hide();
        setTips("短信验证码发送成功");
        btnTimer.startTimer();
    }

    @Override
    public void onSendSmsFailure(String resultCode, String resultDesc) {
        mDialog.hide();
        setTips(resultDesc);
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
        resetPwdAction.destroy();
        if(mDialog != null) {
            mDialog.dismiss();
        }
    }


}

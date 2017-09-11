package com.gcs.fengkong.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bqs.crawler.cloud.sdk.mno.MnoLoginAction;
import com.bqs.crawler.cloud.sdk.mno.OnMnoLoginListener;
import com.gcs.fengkong.R;
import com.gcs.fengkong.Setting;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.atys.MnoResetPwdActivity;
import com.gcs.fengkong.ui.account.bean.User;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.frags.BaseFragment;
import com.gcs.fengkong.utils.DialogUtil;

/**
 * Created by Administrator on 0029 8-29.
 */
@SuppressLint("NewApi")
public class OperatorAuthFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener,OnMnoLoginListener {

    private CheckBox mCbAgreeAuthbook;
    private LinearLayout mLlAuthUsername;
    private LinearLayout mLlAuthPassword;
    private EditText mEtAuthUsername;
    private EditText mEtAuthPassword;
    private Button mBtAuthSubmit;
    private TextView mTvResetPwd;
    MnoLoginAction loginAction = new MnoLoginAction();

    private ProgressDialog mDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_operator_auth;
    }
    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.operator_string);
        view.findViewById(R.id.traceroute_rootview).setOnClickListener(this);
        mCbAgreeAuthbook = view.findViewById(R.id.cb_agree_authbook);
        mLlAuthUsername = view.findViewById(R.id.ll_auth_username);
        mLlAuthPassword = view.findViewById(R.id.ll_auth_password);
        mEtAuthUsername = view.findViewById(R.id.et_auth_username);
        mEtAuthPassword = view.findViewById(R.id.et_auth_password);
        mBtAuthSubmit = view.findViewById(R.id.bt_auth_submit);
        mTvResetPwd= view.findViewById(R.id.tv_ResetPwd);
        //自动获取当前手机号码值
        mEtAuthUsername.setText(Setting.bqsParams.getMobile());
        setListener();
        mEtAuthUsername.setOnFocusChangeListener(this);

        mEtAuthUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                String username = s.toString().trim();

                String name = mEtAuthUsername.getText().toString().trim();
                String pwd = mEtAuthPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(pwd)) {
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*/
                } else {
                  /*  mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                }

            }
        });

        mEtAuthPassword.setOnFocusChangeListener(this);
        mEtAuthPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();

                String pwd = mEtAuthPassword.getText().toString().trim();
                String name = mEtAuthUsername.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(name)&& mCbAgreeAuthbook.isChecked()) {
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*/
                }else {
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                }
            }
        });
    }

    private void setListener() {
        mLlAuthUsername.setOnClickListener(this);
        mLlAuthPassword.setOnClickListener(this);
        mEtAuthUsername.setOnClickListener(this);
        mEtAuthPassword.setOnClickListener(this);
        mBtAuthSubmit.setOnClickListener(this);
        mTvResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MnoResetPwdActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.traceroute_rootview:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.et_auth_username:
                mEtAuthPassword.clearFocus();
                mEtAuthUsername.setFocusableInTouchMode(true);
                mEtAuthUsername.requestFocus();
                break;
            case R.id.et_auth_password:
                mEtAuthUsername.clearFocus();
                mEtAuthPassword.setFocusableInTouchMode(true);
                mEtAuthPassword.requestFocus();
                break;

            case R.id.bt_auth_submit:
                AuthRequest();
                break;

            case R.id.iv_auth_username_del:
                mEtAuthUsername.setText(null);
                break;
            case R.id.iv_auth_password_del:
                mEtAuthPassword.setText(null);
                break;
            default:
                break;
        }

    }

    private void AuthRequest() {

        if(!mCbAgreeAuthbook.isChecked()){
            Toast.makeText(getActivity(),"尚未勾选授权协议书",Toast.LENGTH_SHORT).show();
        }else {
            String servicePwd = mEtAuthPassword.getText().toString();
            if (TextUtils.isEmpty(servicePwd) || servicePwd.length() < 6) {
                Toast.makeText(getActivity(), "请输入有效的服务密码", Toast.LENGTH_SHORT).show();
                return;
            }
            String message = getResources().getString(R.string.progress_submit);
            if (mDialog == null) {
                mDialog = DialogUtil.getProgressDialog(getActivity(), message, false);//DialogHelp.getWaitDialog(this, message);
            }
            mDialog.show();

            loginAction.login(servicePwd, "", this);
        }


    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void onInputAuthSmsCode() {

    }

    @Override
    public void onInputLoginSmsCode() {

    }

    @Override
    public void onLoginSuccess() {
        //登录成功
        Toast.makeText(getActivity(), "运营商授权成功", Toast.LENGTH_SHORT).show();
        User user = AccountHelper.getUser();
        //设置该用户运营商授权状态
        user.getAuthstate().setAuth_operator(true);
        AccountHelper.updateUserCache(user);
        Log.i("GCS","更新sp中user的认证状态值");
        getActivity().finish();
        mDialog.hide();
    }

    @Override
    public void onLoginFailure(String s, String s1) {
        mDialog.hide();
        Toast.makeText(getActivity(), "失败："+s1, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loginAction.destroy();
    }
}

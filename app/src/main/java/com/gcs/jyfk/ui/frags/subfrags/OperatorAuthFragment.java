package com.gcs.jyfk.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bqs.crawler.cloud.sdk.mno.MnoLoginAction;
import com.bqs.crawler.cloud.sdk.mno.OnMnoLoginListener;
import com.bqs.crawler.cloud.sdk.mno.OnMnoSendSmsListener;
import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.Setting;
import com.gcs.jyfk.ui.ShowUIHelper;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.RichTextParser;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.baiqishiauthpager.MnoAuthActivity;
import com.gcs.jyfk.ui.baiqishiauthpager.MnoResetPwdActivity;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.atys.SimpleBackActivity;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.ui.frags.BaseFragment;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.ui.widget.TimerButton;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.DialogUtil;
import com.gcs.jyfk.utils.MyLog;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by Administrator on 0029 8-29.
 */
@SuppressLint("NewApi")
public class OperatorAuthFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener,OnMnoLoginListener, OnMnoSendSmsListener {

    private CheckBox mCbAgreeAuthbook;
    private LinearLayout mLlAuthUsername;
    private LinearLayout mLlAuthPassword;
    private EditText mEtAuthUsername;
    private EditText mEtAuthPassword;
    private Button mBtAuthSubmit;
    private TextView mTvauthbook;
    private TextView mTvResetPwd;
    private ImageView mIvAuthUsernaneDel;
    private ImageView mIvAuthPasswordDel;
    private EditText etSmscode;
    private LinearLayout llSmscode;
    private TimerButton btnTimer;

    MnoLoginAction loginAction = new MnoLoginAction();

    private ProgressDialog mDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_operator_auth;
    }
    @Override
    protected void initView(View view) {
        super.initView(view);
        String message = getResources().getString(R.string.progress_submit);
        if (mDialog == null) {
            mDialog = DialogUtil.getProgressDialog(getActivity(), message, false);//DialogHelp.getWaitDialog(this, message);
        }

        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.operator_string);
        view.findViewById(R.id.traceroute_rootview).setOnClickListener(this);
        mCbAgreeAuthbook = view.findViewById(R.id.cb_agree_authbook);
        mLlAuthUsername = view.findViewById(R.id.ll_auth_username);
        mLlAuthPassword = view.findViewById(R.id.ll_auth_password);
        mEtAuthUsername = view.findViewById(R.id.et_auth_username);
        mEtAuthPassword = view.findViewById(R.id.et_auth_password);
        mTvauthbook = view.findViewById(R.id.tv_authbook);
        mBtAuthSubmit = view.findViewById(R.id.bt_auth_submit);
        mIvAuthUsernaneDel = view.findViewById(R.id.iv_auth_username_del);
        mIvAuthPasswordDel = view.findViewById(R.id.iv_auth_password_del);
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
                if (username.length() > 0) {
                    //   mLlIdentityName.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvAuthUsernaneDel.setVisibility(View.VISIBLE);
                } else {
                    //   mLlIdentityName.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvAuthUsernaneDel.setVisibility(View.GONE);
                }
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
                if (length > 0) {
                    // mLlIdentityNumber.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvAuthPasswordDel.setVisibility(View.VISIBLE);
                } else {
                    mIvAuthPasswordDel.setVisibility(View.GONE);
                }
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

        btnTimer = (TimerButton)view.findViewById(R.id.btn_timer);
        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendSmsClick();
            }
        });
        llSmscode = (LinearLayout)view.findViewById(R.id.ll_smscode);
        etSmscode = (EditText)view.findViewById(R.id.et_smscode);

    }

    private void setListener() {
        mLlAuthUsername.setOnClickListener(this);
        mLlAuthPassword.setOnClickListener(this);
        mEtAuthUsername.setOnClickListener(this);
        mEtAuthPassword.setOnClickListener(this);
        mTvauthbook.setOnClickListener(this);
        mIvAuthUsernaneDel.setOnClickListener(this);
        mIvAuthPasswordDel.setOnClickListener(this);
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
            case R.id.tv_authbook:
                ShowUIHelper.openInternalBrowser(getActivity(), "http://www.jinyanfk.com/wind-phone/authOperatorAgreement.jsp");
                break;
            default:
                break;
        }

    }

    private void AuthRequest() {
       String username =  mEtAuthUsername.getText().toString();
        String smscode = etSmscode.getText().toString();
        if (RichTextParser.machPhoneNum(username)){
            if(!mCbAgreeAuthbook.isChecked()){
                SimplexToast.showMyToast("需勾选授权协议哦!",GlobalApplication.getContext());
            }else {
                String servicePwd = mEtAuthPassword.getText().toString();
                if (TextUtils.isEmpty(servicePwd) || servicePwd.length() < 6||servicePwd.length() > 18) {

                    SimplexToast.showMyToast("请输入有效的服务密码", GlobalApplication.getContext());
                    return;
                }

                if (llSmscode.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(smscode)) {
                        SimplexToast.showMyToast("请输入短信验证码", GlobalApplication.getContext());
                       // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                mDialog.show();

                loginAction.login(servicePwd,smscode, this);
            }
        }else {
            SimplexToast.showMyToast("手机号码有误.请重新填写!",GlobalApplication.getContext());
        }



    }

    public void onSendSmsClick() {
        mDialog.show();
        loginAction.sendLoginSmsCode(this);
    }

    @Override
    public void onLoginFailure(String s, String s1) {
            SimplexToast.showMyToast("认证失败："+s1,GlobalApplication.getContext());
            mDialog.hide();
    }

    @Override
    public void onLoginSuccess() {

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
            getActivity().finish();
            mDialog.hide();
        }


    }
    @Override
    public void onInputAuthSmsCode() {
        mDialog.hide();
        //可使用带结果返回的启动方式，便于关闭相对的simplebackactivity
        Intent intent = new Intent(getActivity(), MnoAuthActivity.class);
        startActivity(intent);
        MyLog.i("GCS","");
    }

    @Override
    public void onInputLoginSmsCode() {
        mDialog.hide();
        llSmscode.setVisibility(View.VISIBLE);
        btnTimer.startTimer();
        SimplexToast.showMyToast("请输入登录短信验证码", GlobalApplication.getContext());
        MyLog.i("GCS","请输入登录短信验证码");
    }



    @Override
    public void onSendSmsSuccess() {
        mDialog.hide();
        btnTimer.startTimer();
        SimplexToast.showMyToast("动态码发送成功", GlobalApplication.getContext());
        MyLog.i("GCS","动态码发送成功");
    }

    @Override
    public void onSendSmsFailure(String s, String resultDesc) {
        mDialog.hide();
        SimplexToast.showMyToast(resultDesc, GlobalApplication.getContext());
        MyLog.i("GCS",resultDesc);
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();

        if (id == R.id.et_auth_username) {
            if (hasFocus) {
                mLlAuthUsername.setActivated(true);
                mLlAuthPassword.setActivated(false);
                mIvAuthUsernaneDel.setVisibility(View.VISIBLE);
                mIvAuthPasswordDel.setVisibility(View.GONE);
            }
        } else {
            if (hasFocus) {
                mLlAuthUsername.setActivated(true);
                mLlAuthPassword.setActivated(false);
                mIvAuthUsernaneDel.setVisibility(View.GONE);
                mIvAuthPasswordDel.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loginAction.destroy();
    }


}

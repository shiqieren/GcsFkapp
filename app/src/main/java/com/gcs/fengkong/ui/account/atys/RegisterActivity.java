package com.gcs.fengkong.ui.account.atys;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.RichTextParser;
import com.gcs.fengkong.ui.account.bean.PhoneToken;
import com.gcs.fengkong.ui.api.MyApi;
import com.gcs.fengkong.ui.bean.base.ResultBean;
import com.gcs.fengkong.utils.AppOperator;
import com.gcs.fengkong.utils.TDevice;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;


/**
 * Created by fei
 * on 2016/10/14.
 * desc:
 */

public class RegisterActivity extends AccountBaseActivity implements View.OnClickListener, View.OnFocusChangeListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    private LinearLayout mLayBackBar;
    private ImageButton mIb_navigation_back;
    private LinearLayout mRegister_one_container;
    private ImageView mIvLogo;
    private LinearLayout mLlRegisterPhone;
    private EditText mEtRegisterUsername;
    private ImageView mIvRegisterDel;
    private LinearLayout mLlRegisterSmsCode;
    private EditText mEtRegisterPwd;
    private LinearLayout mLlRegisterTwoPwd;
    private EditText mEtRegisterAuthCode;
    private TextView mTvRegisterSmsCall;
    private Button mBtRegisterSubmit;

    private boolean mMachPhoneNum;

    private CountDownTimer mTimer;

    private int mRequestType = 1;//1. 请求发送验证码  2.请求phoneToken



    /*private TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {


        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {


        }
    };*/
    private int mLogoHeight;
    private int mLogoWidth;

    /**
     * show the register activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_register_step_one;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initViews();
        setListener();
        TextView label = (TextView) mLayBackBar.findViewById(R.id.tv_navigation_label);
        label.setText(R.string.login_register_hint);
        mEtRegisterUsername.setOnFocusChangeListener(this);
        mEtRegisterUsername.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int length = s.length();
                        if (length > 0) {
                            mIvRegisterDel.setVisibility(View.VISIBLE);
                        } else {
                            mIvRegisterDel.setVisibility(View.INVISIBLE);
                        }
                    }

                    @SuppressWarnings("deprecation")
                    @Override
                    public void afterTextChanged(Editable s) {
                        int length = s.length();
                        String input = s.toString();
                        mMachPhoneNum = RichTextParser.machPhoneNum(input);

                        if (mMachPhoneNum) {
                            String smsCode = mEtRegisterAuthCode.getText().toString().trim();
                            String pwd = mEtRegisterPwd.getText().toString().trim();
                            if (!TextUtils.isEmpty(smsCode)&&!TextUtils.isEmpty(pwd)) {
                                mBtRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                                mBtRegisterSubmit.setTextColor(getResources().getColor(R.color.white));
                            } else {
                                mBtRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                                mBtRegisterSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                            }
                        } else {
                            mBtRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                            mBtRegisterSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                        }

                        if (length > 0 && length < 11) {
                            mLlRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                            mTvRegisterSmsCall.setAlpha(0.4f);
                        } else if (length == 11) {
                            if (mMachPhoneNum) {
                                mLlRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_ok);
                                if (mTvRegisterSmsCall.getTag() == null) {
                                    mTvRegisterSmsCall.setAlpha(1.0f);
                                } else {
                                    mTvRegisterSmsCall.setAlpha(0.4f);
                                }
                            } else {
                                mLlRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                                showToastForKeyBord(R.string.hint_username_ok);
                                mTvRegisterSmsCall.setAlpha(0.4f);
                            }
                        } else if (length > 11) {
                            mTvRegisterSmsCall.setAlpha(0.4f);
                            mLlRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                        } else if (length <= 0) {
                            mTvRegisterSmsCall.setAlpha(0.4f);
                            mLlRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_ok);
                        }


                    }
                }

        );
        mEtRegisterAuthCode.setOnFocusChangeListener(this);
        mEtRegisterAuthCode.addTextChangedListener(new TextWatcher() {
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
                String pwd = mEtRegisterPwd.getText().toString().trim();
                if (length > 0 && mMachPhoneNum && !TextUtils.isEmpty(pwd)) {
                    mBtRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtRegisterSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mBtRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtRegisterSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
                mLlRegisterSmsCode.setBackgroundResource(R.drawable.bg_login_input_ok);
            }
        });

        mEtRegisterPwd.setOnFocusChangeListener(this);
        mEtRegisterPwd.addTextChangedListener(new TextWatcher() {
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
                if (length < 6) {
                    mLlRegisterTwoPwd.setBackgroundResource(R.drawable.bg_login_input_error);
                } else {
                    mLlRegisterTwoPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                }
                String username = mEtRegisterUsername.getText().toString().trim();
                String authcode = mEtRegisterAuthCode.getText().toString().trim();
                if (!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(authcode)) {
                    mBtRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtRegisterSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mBtRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtRegisterSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });

    }

    private void initViews() {
        mLayBackBar = (LinearLayout) findViewById(R.id.ly_retrieve_bar);
        mIb_navigation_back = (ImageButton) findViewById(R.id.ib_navigation_back);
        mRegister_one_container= (LinearLayout) findViewById(R.id.lay_register_one_container);
        mIvLogo= (ImageView) findViewById(R.id.iv_login_logo);
        mLlRegisterTwoPwd= (LinearLayout) findViewById(R.id.ll_register_two_pwd);
        mEtRegisterPwd= (EditText) findViewById(R.id.et_register_pwd_input);
        mLlRegisterPhone= (LinearLayout) findViewById(R.id.ll_register_phone);
        mEtRegisterUsername= (EditText) findViewById(R.id.et_register_username);
        mIvRegisterDel= (ImageView) findViewById(R.id.iv_register_username_del);
        mLlRegisterSmsCode= (LinearLayout) findViewById(R.id.ll_register_sms_code);
        mEtRegisterAuthCode= (EditText) findViewById(R.id.et_register_auth_code);
        mTvRegisterSmsCall= (TextView) findViewById(R.id.tv_register_sms_call);
        mBtRegisterSubmit= (Button) findViewById(R.id.bt_register_submit);
    }


    @Override
    protected void initData() {
        super.initData();//必须要调用,用来注册本地广播
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLayBackBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
        mLayBackBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    private void setListener(){
        mLayBackBar.setOnClickListener(this);
        mIb_navigation_back.setOnClickListener(this);
        mIvLogo.setOnClickListener(this);
        mLlRegisterPhone.setOnClickListener(this);
        mEtRegisterUsername.setOnClickListener(this);
        mIvRegisterDel.setOnClickListener(this);
        mLlRegisterSmsCode.setOnClickListener(this);
        mEtRegisterAuthCode.setOnClickListener(this);
        mTvRegisterSmsCall.setOnClickListener(this);
        mBtRegisterSubmit.setOnClickListener(this);
        mRegister_one_container.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.et_register_username:
                mEtRegisterAuthCode.clearFocus();
                mEtRegisterPwd.clearFocus();
                mEtRegisterUsername.setFocusableInTouchMode(true);
                mEtRegisterUsername.requestFocus();
                break;
            case R.id.et_register_auth_code:
                mEtRegisterUsername.clearFocus();
                mEtRegisterPwd.clearFocus();
                mEtRegisterAuthCode.setFocusableInTouchMode(true);
                mEtRegisterAuthCode.requestFocus();
                break;
            case R.id.et_register_pwd_input:
                mEtRegisterUsername.clearFocus();
                mEtRegisterAuthCode.clearFocus();
                mEtRegisterPwd.setFocusableInTouchMode(true);
                mEtRegisterPwd.requestFocus();
                break;
            case R.id.iv_register_username_del:
                mEtRegisterUsername.setText(null);
                break;
            case R.id.tv_register_sms_call:
                requestSmsCode();
                break;
            case R.id.bt_register_submit:
               requestRegister();
           //111     RegisterStepTwoActivity.show(this,null);
               Toast.makeText(RegisterActivity.this,"提交资料",Toast.LENGTH_SHORT).show();

                break;
            case R.id.lay_register_one_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            default:
                break;
        }

    }

    private void requestRegister() {

        String smsCode = mEtRegisterAuthCode.getText().toString().trim();
        String pwd = mEtRegisterPwd.getText().toString().trim();
        if (!mMachPhoneNum || TextUtils.isEmpty(smsCode)|| TextUtils.isEmpty(pwd)) {
            //showToastForKeyBord(R.string.hint_username_ok);
            return;
        }

        if (!TDevice.hasInternet()) {
            showToastForKeyBord(R.string.tip_network_error);
            return;
        }

        mRequestType = 2;
        String phoneNumber = mEtRegisterUsername.getText().toString().trim();
       //111 OSChinaApi.validateRegisterInfo(phoneNumber, smsCode, mHandler);注册信息提交的api
        MyApi.register(phoneNumber, smsCode, getSha1(pwd), new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                showWaitDialog(R.string.progress_submit);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                hideWaitDialog();
            }


            @Override
            public void onError(Call call, Exception e, int id) {
                if (mRequestType == 1) {
                    if (mTimer != null) {
                        mTimer.onFinish();
                        mTimer.cancel();
                    }
                }
                requestFailureHint(e);
            }

            @Override
            public void onResponse(String response, int id) {

                Type phoneType = new TypeToken<ResultBean<PhoneToken>>() {
                }.getType();

                ResultBean<PhoneToken> phoneTokenResultBean = AppOperator.createGson().fromJson(response, phoneType);
                int smsCode = phoneTokenResultBean.getCode();
                switch (smsCode) {
                    case 1://注册成功,进行用户信息填写
                        if (phoneTokenResultBean.isSuccess()) {
                            //获得phoneToken
                            PhoneToken phoneToken = phoneTokenResultBean.getResult();
                            if (phoneToken != null) {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                Toast.makeText(RegisterActivity.this,"进入资料填写",Toast.LENGTH_LONG).show();
                                //1111  RegisterStepTwoActivity.show(RegisterActivity.this, phoneToken);
                            }
                        } else {
                            showToastForKeyBord(phoneTokenResultBean.getMessage());
                        }
                        break;
                    case 215://注册失败,手机验证码错误
                        mLlRegisterSmsCode.setBackgroundResource(R.drawable.bg_login_input_error);
                        showToastForKeyBord(phoneTokenResultBean.getMessage());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void requestSmsCode() {
        if (!mMachPhoneNum) {
            //showToastForKeyBord(R.string.hint_username_ok);
            return;
        }
        if (!TDevice.hasInternet()) {
            showToastForKeyBord(R.string.tip_network_error);
            return;
        }

        if (mTvRegisterSmsCall.getTag() == null) {
            mRequestType = 1;
            mTvRegisterSmsCall.setAlpha(0.6f);
            mTvRegisterSmsCall.setTag(true);
            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    mTvRegisterSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    mTvRegisterSmsCall.setTag(null);
                    mTvRegisterSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    mTvRegisterSmsCall.setAlpha(1.0f);
                }
            }.start();
            String phoneNumber = mEtRegisterUsername.getText().toString().trim();
          //1111  OSChinaApi.sendSmsCode(phoneNumber, OSChinaApi.REGISTER_INTENT, mHandler);发送短信的api
            MyApi.sendSmsCode(phoneNumber, MyApi.REGISTER_INTENT, new StringCallback() {
                @Override
                public void onBefore(Request request, int id) {
                    super.onBefore(request, id);
                    showWaitDialog(R.string.progress_submit);
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    hideWaitDialog();
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    if (mRequestType == 1) {
                        if (mTimer != null) {
                            mTimer.onFinish();
                            mTimer.cancel();
                        }
                    }
                    requestFailureHint(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    Type type = new TypeToken<ResultBean>() {
                    }.getType();
                    ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                    int code = resultBean.getCode();
                    switch (code) {
                        case 1:
                            //发送验证码成功,请求进入下一步
                            //意味着我们可以进行第二次请求了,获取phoneToken
                            //mRequestType = 2;
                            GlobalApplication.showToast(R.string.send_sms_code_success_hint);
                            mEtRegisterAuthCode.setText(null);
                            break;
                        case 218:
                            //手机号已被注册,提示重新输入
                            mLlRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                            showToastForKeyBord(resultBean.getMessage());
                            break;
                        case 0:
                            //异常错误，发送验证码失败,回收timer,需重新请求发送验证码
                            if (mTimer != null) {
                                mTimer.onFinish();
                                mTimer.cancel();
                            }
                            showToastForKeyBord(resultBean.getMessage());
                            break;
                        default:
                            break;
                    }
                }
            });
        } else {
            GlobalApplication.showToast(getResources().getString(R.string.register_sms_wait_hint), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id) {
            case R.id.et_register_username:
                if (hasFocus) {
                    mLlRegisterPhone.setActivated(true);
                    mLlRegisterSmsCode.setActivated(false);
                    mLlRegisterTwoPwd.setActivated(false);
                }
                break;
            case R.id.et_register_auth_code:
                if (hasFocus) {
                    mLlRegisterPhone.setActivated(false);
                    mLlRegisterSmsCode.setActivated(true);
                    mLlRegisterTwoPwd.setActivated(false);
                }
                break;
            case R.id.et_register_pwd_input:
                if (hasFocus) {
                    mLlRegisterPhone.setActivated(false);
                    mLlRegisterSmsCode.setActivated(false);
                    mLlRegisterTwoPwd.setActivated(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGlobalLayout() {

        final ImageView ivLogo = this.mIvLogo;

        Rect keypadRect = new Rect();

        mLayBackBar.getWindowVisibleDisplayFrame(keypadRect);

        int screenHeight = mLayBackBar.getRootView().getHeight();

        int keypadHeight = screenHeight - keypadRect.bottom;
        if (keypadHeight > 0) {
            updateKeyBoardActiveStatus(true);
        } else {
            updateKeyBoardActiveStatus(false);
        }

        if (keypadHeight > 0 && ivLogo.getTag() == null) {
            final int height = ivLogo.getHeight();
            final int width = ivLogo.getWidth();
            this.mLogoHeight = height;
            this.mLogoWidth = width;
            ivLogo.setTag(true);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivLogo.getLayoutParams();

                    layoutParams.height = (int) (height * animatedValue);
                    layoutParams.width = (int) (width * animatedValue);
                    ivLogo.requestLayout();
                    ivLogo.setAlpha(animatedValue);
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();


        } else if (keypadHeight == 0 && ivLogo.getTag() != null) {
            final int height = mLogoHeight;
            final int width = mLogoWidth;
            ivLogo.setTag(null);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivLogo.getLayoutParams();
                    layoutParams.height = (int) (height * animatedValue);
                    layoutParams.width = (int) (width * animatedValue);
                    ivLogo.requestLayout();
                    ivLogo.setAlpha(animatedValue);
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();

        }

    }
}

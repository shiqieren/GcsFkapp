package com.gcs.fengkong.ui.account.atys;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
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

import com.gcs.fengkong.AppConfig;
import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.Setting;
import com.gcs.fengkong.ui.account.RichTextParser;
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

public class ResetPwdActivity extends AccountBaseActivity implements View.OnClickListener, View.OnFocusChangeListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    private LinearLayout mLlRetrieveBar;
    private ImageButton mIb_navigation_back;
    private LinearLayout mLlRetrievecontainer;
    private LinearLayout mLlRetrieveTel;
    private EditText mEtRetrieveTel;
    private ImageView mIvRetrieveTelDel;
    private LinearLayout mLlRetrieveCode;
    private EditText mEtRetrieveCodeInput;
    private TextView mTvRetrieveSmsCall;
    private Button mBtRetrieveSubmit;
    private TextView mTvRetrieveLabel;
    private boolean mMachPhoneNum;

    private LinearLayout mLlResetPwd;
    private EditText mEtResetPwd;
    private ImageView mIvResetPwdDel;

    private CountDownTimer mTimer;

    private int mRequestType;
   /* private TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {

        @Override
        public void onStart() {
            super.onStart();
            showWaitDialog(R.string.progress_submit);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }

        @Override
        public void onCancel() {
            super.onCancel();
            hideWaitDialog();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            //请求失败,比如服务器连接超时,回收timer,需重新请求发送验证码
            if (mRequestType == 1) {
                if (mTimer != null) {
                    mTimer.onFinish();
                    mTimer.cancel();
                }
            }
            requestFailureHint(throwable);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {

            try {
                switch (mRequestType) {
                    //第一步请求发送验证码
                    case 1:
                        Type type = new TypeToken<ResultBean>() {
                        }.getType();
                        ResultBean resultBean = AppOperator.createGson().fromJson(responseString, type);
                        int code = resultBean.getCode();
                        switch (code) {
                            case 1:
                                //发送验证码成功,请求进入下一步
                                //mRequestType = 2;
                                mEtRetrieveCodeInput.setText(null);
                                AppContext.showToast(R.string.send_sms_code_success_hint, Toast.LENGTH_SHORT);
                                break;
                            case 218:
                                //手机号已被注册,提示重新输入
                                mLlRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_error);
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

                        break;
                    //第二步请求进行重置密码
                    case 2:

                        Type phoneType = new TypeToken<ResultBean<PhoneToken>>() {
                        }.getType();

                        ResultBean<PhoneToken> phoneTokenResultBean = AppOperator.createGson().fromJson(responseString, phoneType);

                        int smsCode = phoneTokenResultBean.getCode();
                        switch (smsCode) {
                            case 1:
                                if (phoneTokenResultBean.isSuccess()) {
                                    PhoneToken phoneToken = phoneTokenResultBean.getResult();
                                    if (phoneToken != null) {
                                        if (mTimer != null) {
                                            mTimer.onFinish();
                                            mTimer.cancel();
                                        }
                                        ResetPwdActivity.show(ResetPwdActivity.this, phoneToken);
                                    }
                                } else {
                                    showToastForKeyBord(phoneTokenResultBean.getMessage());
                                }
                                break;
                            case 215:
                                mLlRetrieveCode.setBackgroundResource(R.drawable.bg_login_input_error);
                                showToastForKeyBord(phoneTokenResultBean.getMessage());
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }

        }
    };*/
    private int mTopMargin;

    /**
     * show the retrieve activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, ResetPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_retrieve_pwd;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initViews();
        setListener();
        TextView tvLabel = (TextView) mLlRetrieveBar.findViewById(R.id.tv_navigation_label);
        tvLabel.setText(R.string.retrieve_pwd_label);
        mEtRetrieveTel.setOnFocusChangeListener(this);
        mEtRetrieveTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length > 0) {
                    mIvRetrieveTelDel.setVisibility(View.VISIBLE);
                } else {
                    mIvRetrieveTelDel.setVisibility(View.INVISIBLE);
                }

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                String input = s.toString();
                mMachPhoneNum = RichTextParser.machPhoneNum(input);

                //对提交控件的状态判定
                if (mMachPhoneNum) {
                    String smsCode = mEtRetrieveCodeInput.getText().toString().trim();
                    String resetpwd = mEtResetPwd.getText().toString().trim();
                    if (!TextUtils.isEmpty(smsCode)&&!TextUtils.isEmpty(resetpwd)) {
                        mBtRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                        mBtRetrieveSubmit.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        mBtRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        mBtRetrieveSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    }
                } else {
                    mBtRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtRetrieveSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }

                if (length > 0 && length < 11) {
                    mLlRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_error);
                    mTvRetrieveSmsCall.setAlpha(0.4f);
                } else if (length == 11) {
                    if (mMachPhoneNum) {
                        mLlRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_ok);
                        if (mTvRetrieveSmsCall.getTag() == null) {
                            mTvRetrieveSmsCall.setAlpha(1.0f);
                        } else {
                            mTvRetrieveSmsCall.setAlpha(0.4f);
                        }
                    } else {
                        mLlRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_error);
                        showToastForKeyBord(R.string.hint_username_ok);
                        mTvRetrieveSmsCall.setAlpha(0.4f);
                    }
                } else if (length > 11) {
                    mTvRetrieveSmsCall.setAlpha(0.4f);
                    mLlRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_error);
                } else if (length <= 0) {
                    mTvRetrieveSmsCall.setAlpha(0.4f);
                    mLlRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_ok);
                }

            }
        });
        mEtRetrieveCodeInput.setOnFocusChangeListener(this);
        mEtRetrieveCodeInput.addTextChangedListener(new TextWatcher() {
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
                String resetpwd = mEtResetPwd.getText().toString().trim();

                if (length > 0 && mMachPhoneNum && !TextUtils.isEmpty(resetpwd)) {
                    mBtRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtRetrieveSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mBtRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtRetrieveSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
                mLlRetrieveCode.setBackgroundResource(R.drawable.bg_login_input_ok);
            }
        });

        mEtResetPwd.setOnFocusChangeListener(this);
        mEtResetPwd.addTextChangedListener(new TextWatcher() {
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
                String smsCode = mEtRetrieveCodeInput.getText().toString().trim();
                String resetpwd = mEtResetPwd.getText().toString().trim();
                if (length >= 6 && mMachPhoneNum && mMachPhoneNum && !TextUtils.isEmpty(smsCode) && mMachPhoneNum && !TextUtils.isEmpty(resetpwd)) {
                    mIvResetPwdDel.setVisibility(View.VISIBLE);
                    mLlResetPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mBtRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtRetrieveSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    if (length <= 0) {
                        mIvResetPwdDel.setVisibility(View.GONE);
                        mLlResetPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                    } else {
                        mIvResetPwdDel.setVisibility(View.VISIBLE);
                        mLlResetPwd.setBackgroundResource(R.drawable.bg_login_input_error);
                    }
                    mBtRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtRetrieveSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }

            }
        });
    }


    private void initViews() {
        mLlRetrieveBar = (LinearLayout) findViewById(R.id.ly_retrieve_bar);
        mIb_navigation_back = (ImageButton) findViewById(R.id.ib_navigation_back);
        mLlRetrievecontainer = (LinearLayout) findViewById(R.id.lay_retrieve_container);
        mLlRetrieveTel= (LinearLayout) findViewById(R.id.ll_retrieve_tel);
        mEtRetrieveTel = (EditText) findViewById(R.id.et_retrieve_tel);
        mIvRetrieveTelDel= (ImageView) findViewById(R.id.iv_retrieve_tel_del);
        mLlRetrieveCode= (LinearLayout) findViewById(R.id.ll_retrieve_code);
        mEtRetrieveCodeInput= (EditText) findViewById(R.id.et_retrieve_code_input);
        mTvRetrieveSmsCall= (TextView) findViewById(R.id.retrieve_sms_call);
        mBtRetrieveSubmit= (Button) findViewById(R.id.bt_retrieve_submit);
        mTvRetrieveLabel= (TextView) findViewById(R.id.tv_retrieve_label);

        mLlResetPwd= (LinearLayout) findViewById(R.id.ll_reset_pwd);
        mEtResetPwd= (EditText) findViewById(R.id.et_reset_pwd);
        mIvResetPwdDel= (ImageView) findViewById(R.id.iv_reset_pwd_del);
    }

    private void setListener(){
        mLlRetrieveBar.setOnClickListener(this);
        mIb_navigation_back.setOnClickListener(this);
        mLlRetrievecontainer.setOnClickListener(this);
        mLlRetrieveTel.setOnClickListener(this);
        mEtRetrieveTel.setOnClickListener(this);
        mIvRetrieveTelDel.setOnClickListener(this);
        mLlRetrieveCode.setOnClickListener(this);
        mEtRetrieveCodeInput.setOnClickListener(this);
        mTvRetrieveSmsCall.setOnClickListener(this);
        mBtRetrieveSubmit.setOnClickListener(this);
        mTvRetrieveLabel.setOnClickListener(this);
        mIvResetPwdDel.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();//必须要调用,用来注册本地广播
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLlRetrieveBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
        mLlRetrieveBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.et_retrieve_tel:
                mEtResetPwd.clearFocus();
                mEtRetrieveCodeInput.clearFocus();
                mEtRetrieveTel.setFocusableInTouchMode(true);
                mEtRetrieveTel.requestFocus();
                break;
            case R.id.et_retrieve_code_input:
                mEtResetPwd.clearFocus();
                mEtRetrieveTel.clearFocus();
                mEtRetrieveCodeInput.setFocusableInTouchMode(true);
                mEtRetrieveCodeInput.requestFocus();
                break;
            case R.id.et_reset_pwd:
                mEtRetrieveCodeInput.clearFocus();
                mEtRetrieveTel.clearFocus();
                mEtResetPwd.setFocusableInTouchMode(true);
                mEtResetPwd.requestFocus();
            case R.id.iv_retrieve_tel_del:
                mEtRetrieveTel.setText(null);
                break;
            case R.id.iv_reset_pwd_del:
                mEtResetPwd.setText(null);
                break;
            case R.id.retrieve_sms_call:
                //获取验证码
                requestSmsCode();

                break;
            case R.id.bt_retrieve_submit:
                Toast.makeText(ResetPwdActivity.this,"修改密码",Toast.LENGTH_SHORT).show();
                //根据验证码获取phoneToken
               //111 requestRetrievePwd();
                //requestResetPwd();
                break;
            case R.id.tv_retrieve_label:

                //打开web进入邮箱找回密码

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                // intent.setAction(Intent.CATEGORY_BROWSABLE);
                Uri content_url = Uri.parse(AppConfig.RETRIEVE_PWD_URL);
                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.lay_retrieve_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            default:
                break;
        }

    }

    private void requestRetrievePwd() {
        String tempPwd = mEtResetPwd.getText().toString().trim();
        String smsCode = mEtRetrieveCodeInput.getText().toString().trim();
        if (!mMachPhoneNum || TextUtils.isEmpty(smsCode)||TextUtils.isEmpty(tempPwd) || tempPwd.length() < 6) {
            // showToastForKeyBord(R.string.hint_username_ok);
            showToastForKeyBord(R.string.reset_pwd_hint);
            return;
        }

        if (!TDevice.hasInternet()) {
            showToastForKeyBord(R.string.tip_network_error);
            return;
        }
        mRequestType = 2;
        String phoneNumber = mEtRetrieveTel.getText().toString().trim();
       //111 OSChinaApi.validateRegisterInfo(phoneNumber, smsCode, mHandler);
        MyApi.resetPwd(phoneNumber, smsCode, getSha1(tempPwd), new StringCallback() {
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
                        GlobalApplication.showToast(getResources().getString(R.string.reset_success_hint), Toast.LENGTH_SHORT);
                        LoginActivity.show(ResetPwdActivity.this);
                        finish();
                        break;
                    case 216:
                        showToastForKeyBord(resultBean.getMessage());
                        finish();
                        break;
                    case 219:
                        mLlResetPwd.setBackgroundResource(R.drawable.bg_login_input_error);
                        showToastForKeyBord(resultBean.getMessage());
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

        if (mTvRetrieveSmsCall.getTag() == null) {
            mRequestType = 1;
            mTvRetrieveSmsCall.setAlpha(0.6f);
            mTvRetrieveSmsCall.setTag(true);
            mTimer = new CountDownTimer(AppConfig.SMSCODE_TIME_OUT * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    mTvRetrieveSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    mTvRetrieveSmsCall.setTag(null);
                    mTvRetrieveSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    mTvRetrieveSmsCall.setAlpha(1.0f);
                }
            }.start();
            String phoneNumber = mEtRetrieveTel.getText().toString().trim();
         //1111   OSChinaApi.sendSmsCode(phoneNumber, OSChinaApi.RESET_PWD_INTENT, mHandler);
            MyApi.sendSmsCode(phoneNumber, new StringCallback() {
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
                            //mRequestType = 2;
                            mEtRetrieveCodeInput.setText(null);
                            GlobalApplication.showToast(R.string.send_sms_code_success_hint, Toast.LENGTH_SHORT);
                            break;
                        case 218:
                            //手机号已被注册,提示重新输入
                            mLlRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_error);
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
            case R.id.et_retrieve_tel:
                if (hasFocus) {
                    mLlRetrieveTel.setActivated(true);
                    mLlRetrieveCode.setActivated(false);
                    mLlResetPwd.setActivated(false);
                }
                break;
            case R.id.et_retrieve_code_input:
                if (hasFocus) {
                    mLlRetrieveTel.setActivated(false);
                    mLlRetrieveCode.setActivated(true);
                    mLlResetPwd.setActivated(false);
                }
                break;
            case R.id.et_reset_pwd:
                if (hasFocus) {
                    mLlRetrieveTel.setActivated(false);
                    mLlRetrieveCode.setActivated(false);
                    mLlResetPwd.setActivated(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGlobalLayout() {
        final LinearLayout kayResetPwd = this.mLlResetPwd;
        final LinearLayout layRetrieveTel = this.mLlRetrieveTel;
        Rect KeypadRect = new Rect();

        mLlRetrieveBar.getWindowVisibleDisplayFrame(KeypadRect);

        int screenHeight = mLlRetrieveBar.getRootView().getHeight();

        int keypadHeight = screenHeight - KeypadRect.bottom;

        if (keypadHeight > 0) {
            updateKeyBoardActiveStatus(true);
        } else {
            updateKeyBoardActiveStatus(false);
        }

        if (keypadHeight > 0 && layRetrieveTel.getTag() == null) {
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layRetrieveTel.getLayoutParams();
            final int topMargin = layoutParams.topMargin;
            this.mTopMargin = topMargin;
            layRetrieveTel.setTag(true);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    layoutParams.topMargin = (int) (topMargin * animatedValue);
                    layRetrieveTel.requestLayout();
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();


        } else if (keypadHeight == 0 && layRetrieveTel.getTag() != null) {
            final int topMargin = mTopMargin;
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layRetrieveTel.getLayoutParams();
            layRetrieveTel.setTag(null);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    layoutParams.topMargin = (int) (topMargin * animatedValue);
                    layRetrieveTel.requestLayout();
                }
            });
            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();
        }
    }
}

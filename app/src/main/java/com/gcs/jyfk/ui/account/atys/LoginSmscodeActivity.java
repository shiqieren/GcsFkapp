package com.gcs.jyfk.ui.account.atys;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcs.jyfk.AppConfig;
import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.RichTextParser;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.atys.MainActivity;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.TDevice;
import com.gcs.jyfk.utils.VibratorUtil;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;


/**
 * Created by lyw on 2016/10/14.
 * desc:
 */

public class LoginSmscodeActivity extends AccountBaseActivity implements View.OnClickListener, View.OnFocusChangeListener, ViewTreeObserver.OnGlobalLayoutListener {
    public static final String HOLD_USERNAME_KEY = "holdUsernameKey";

    private LinearLayout mLayBackBar;
    private ImageButton mIb_navigation_back;
    private LinearLayout mLayLoginContains;
    private ImageView mIvLoginLogo;
    private LinearLayout mLlLoginUsername;
    private EditText mEtLoginUsername;
    private ImageView mIvLoginUsernameDel;
    private LinearLayout mLlLoginPwd;
    private EditText mEtLoginPwd;
    private ImageView mIvHoldPwd;
    private TextView mTvLoginForgetPwd;
    private Button mBtLoginSubmit;
    private TextView mTvRegisterSmsCall;
    private TextView mTvLoginRegister;
    private boolean mMachPhoneNum;
    private CountDownTimer mTimer;
    private String Jsessionid;
    //第三方接入的handler登录接收器callback

    private boolean mKeyBoardIsActive;
    //第三方接入的handler登录接收器callback

    /**
     * update keyBord active status
     *
     * @param isActive isActive
     */
    protected void updateKeyBoardActiveStatus(boolean isActive) {
        this.mKeyBoardIsActive = isActive;
    }

    private void logSucceed() {
        View view;
        if ((view = getCurrentFocus()) != null) {
            hideKeyBoard(view.getWindowToken());
        }
        SimplexToast.showMyToast(R.string.login_success_hint, this);
        setResult(RESULT_OK);
        //发送关闭登录界面的广播
        sendLocalReceiver();
        //后台异步同步数据-同步认证状态信息
        MyLog.i("GCS", "同步认证状态");
        //ContactsCacheManager.sync();
        holdAccount();
    }

    private int mLogoHeight;
    private int mLogoWidth;

    /**
     * hold account information
     */
    private void holdAccount() {
        String username = mEtLoginUsername.getText().toString().trim();
        if (!TextUtils.isEmpty(username)) {
            SharedPreferences sp = getSharedPreferences(AppConfig.HOLD_ACCOUNT, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(HOLD_USERNAME_KEY, username);
            SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
        }
    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, LoginSmscodeActivity.class);
        context.startActivity(intent);
    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Activity context, int requestCode) {
        Intent intent = new Intent(context, LoginSmscodeActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * show the login activity
     *
     * @param fragment fragment
     */
    public static void show(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), LoginSmscodeActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_smscode_login;
    }


    private void initViews() {

        mLayBackBar = (LinearLayout) findViewById(R.id.ly_retrieve_bar);
        mIb_navigation_back = (ImageButton) findViewById(R.id.ib_navigation_back);

        mLayLoginContains = (LinearLayout) findViewById(R.id.lay_login_container);

        mIvLoginLogo = (ImageView) findViewById(R.id.iv_login_logo);

        mLlLoginUsername = (LinearLayout) findViewById(R.id.ll_login_username);

        mEtLoginUsername = (EditText) findViewById(R.id.et_login_username);

        mIvLoginUsernameDel = (ImageView) findViewById(R.id.iv_login_username_del);

        mLlLoginPwd = (LinearLayout) findViewById(R.id.ll_login_pwd);

        mEtLoginPwd = (EditText) findViewById(R.id.et_login_pwd);


        mIvHoldPwd = (ImageView) findViewById(R.id.iv_login_hold_pwd);

        mTvLoginForgetPwd = (TextView) findViewById(R.id.tv_login_forget_pwd);

        mTvRegisterSmsCall = (TextView) findViewById(R.id.tv_register_sms_call);

        mBtLoginSubmit = (Button) findViewById(R.id.bt_login_submit);

        mTvLoginRegister = (TextView) findViewById(R.id.tv_login_register);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        initViews();
        setListener();
        TextView tvLabel = (TextView) mLayBackBar.findViewById(R.id.tv_navigation_label);
        tvLabel.setText(R.string.login_btn);

        /*隐藏
        TextView label = (TextView) mLayBackBar.findViewById(R.id.tv_navigation_label);
        label.setVisibility(View.INVISIBLE);*/
        mEtLoginUsername.setOnFocusChangeListener(this);
        mEtLoginUsername.addTextChangedListener(new TextWatcher() {
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
                String input = s.toString().trim();
                mMachPhoneNum = RichTextParser.machPhoneNum(input);
                if (mMachPhoneNum) {
                    String smsCode = mEtLoginPwd.getText().toString().trim();
                    if (!TextUtils.isEmpty(smsCode)) {
                        mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                        mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    }
                } else {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }

                if (length > 0 && length < 11) {
                    mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_error);
                    mTvRegisterSmsCall.setAlpha(0.4f);
                } else if (length == 11) {
                    if (mMachPhoneNum) {
                        mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_ok);
                        if (mTvRegisterSmsCall.getTag() == null) {
                            mTvRegisterSmsCall.setAlpha(1.0f);
                        } else {
                            mTvRegisterSmsCall.setAlpha(0.4f);
                        }
                    } else {
                        mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_error);
                        SimplexToast.showToastForKeyBord(R.string.hint_username_ok, GlobalApplication.getContext(), mKeyBoardIsActive);
                        mTvRegisterSmsCall.setAlpha(0.4f);
                    }
                } else if (length > 11) {
                    mTvRegisterSmsCall.setAlpha(0.4f);
                    mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_error);
                } else if (length <= 0) {
                    mTvRegisterSmsCall.setAlpha(0.4f);
                    mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_ok);
                }
               /* if (input.length() > 0) {
                    mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvLoginUsernameDel.setVisibility(View.VISIBLE);
                } else {
                    mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvLoginUsernameDel.setVisibility(View.INVISIBLE);
                }

                String pwd = mEtLoginPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }*/

            }
        });

        mEtLoginPwd.setOnFocusChangeListener(this);
        mEtLoginPwd.addTextChangedListener(new TextWatcher() {
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
                String pwd = mEtLoginPwd.getText().toString().trim();
                if (length > 0 && mMachPhoneNum && !TextUtils.isEmpty(pwd)) {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
                mLlLoginPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                /*int length = s.length();
                if (length > 0) {
                    mLlLoginPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvLoginPwdDel.setVisibility(View.VISIBLE);
                } else {
                    mIvLoginPwdDel.setVisibility(View.INVISIBLE);
                }
                String username = mEtLoginUsername.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    showToastForKeyBord(R.string.usernull);
                }
                String pwd = mEtLoginPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {

                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                }else {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }*/
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();//必须要,用来注册本地广播

        //初始化控件状态数据
        SharedPreferences sp = getSharedPreferences(AppConfig.HOLD_ACCOUNT, Context.MODE_PRIVATE);
        String holdUsername = sp.getString(HOLD_USERNAME_KEY, null);
        //String holdPwd = sp.getString(HOLD_PWD_KEY, null);
        //int holdStatus = sp.getInt(HOLD_PWD_STATUS_KEY, 0);//0第一次默认/1用户设置保存/2用户设置未保存

        // mEtLoginUsername.setText(holdUsername);

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

    private void setListener() {
        mLayBackBar.setOnClickListener(this);
        mIb_navigation_back.setOnClickListener(this);
        mIvLoginLogo.setOnClickListener(this);
        mLlLoginUsername.setOnClickListener(this);
        mEtLoginUsername.setOnClickListener(this);
        mIvLoginUsernameDel.setOnClickListener(this);
        mLlLoginPwd.setOnClickListener(this);
        mEtLoginPwd.setOnClickListener(this);
        mIvHoldPwd.setOnClickListener(this);
        mTvLoginForgetPwd.setOnClickListener(this);
        mBtLoginSubmit.setOnClickListener(this);
        mTvRegisterSmsCall.setOnClickListener(this);
        mTvLoginRegister.setOnClickListener(this);
        mLayLoginContains.setOnClickListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.et_login_username:
                mEtLoginPwd.clearFocus();
                mEtLoginUsername.setFocusableInTouchMode(true);
                mEtLoginUsername.requestFocus();
                break;
            case R.id.et_login_pwd:
                mEtLoginUsername.clearFocus();
                mEtLoginPwd.setFocusableInTouchMode(true);
                mEtLoginPwd.requestFocus();
                break;
            case R.id.tv_login_forget_pwd:
                //忘记密码
                LoginActivity.show(LoginSmscodeActivity.this);
                finish();
                break;
            case R.id.tv_register_sms_call:
                requestSmsCode();
                break;
            case R.id.bt_login_submit:
                loginRequest();
                break;
            case R.id.iv_login_hold_pwd:
                //记住密码
            case R.id.tv_login_register:
                RegisterActivity.show(LoginSmscodeActivity.this);
                break;
            case R.id.iv_login_username_del:
                mEtLoginUsername.setText(null);
                break;
            case R.id.lay_login_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            default:
                break;
        }

    }


    private void requestSmsCode() {

        if (!mMachPhoneNum) {
            //showToastForKeyBord(R.string.hint_username_ok);
            return;
        }
        if (!TDevice.hasInternet()) {
            SimplexToast.showToastForKeyBord(R.string.tip_network_error, GlobalApplication.getContext(), mKeyBoardIsActive);
            return;
        }

        if (mTvRegisterSmsCall.getTag() == null) {
            mTvRegisterSmsCall.setAlpha(0.6f);
            mTvRegisterSmsCall.setTag(true);
            mTimer = new CountDownTimer(AppConfig.SMSCODE_TIME_OUT * 1000, 1000) {

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
            //加密
            String phoneNumber = mEtLoginUsername.getText().toString().trim();

            MyLog.i("GCS", "加密前的手机:" + phoneNumber);
            //1111  OSChinaApi.sendRegisterSmsCode(phoneNumber, OSChinaApi.REGISTER_INTENT, mHandler);发送短信的api
            MyApi.sendSmsCode(getAES(phoneNumber), new StringCallback() {
                @Override
                public void onBefore(Request request, int id) {
                    super.onBefore(request, id);
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    MyLog.i("GCS", "发送短信验证码返回Exception：" + e.toString());
                    if (mTimer != null) {
                        mTimer.onFinish();
                        mTimer.cancel();
                    }

                    SimplexToast.requestFailureHint(e, LoginSmscodeActivity.this);
                }

                @Override
                public void onResponse(String response, int id) {
                    MyLog.i("GCS", "发送短信验证码返回response：" + response);
                    Type type = new TypeToken<ResultBean>() {
                    }.getType();
                    ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                    if (resultBean.getResult() != null) {
                        Jsessionid = resultBean.getResult().toString();
                    }

                    int code = resultBean.getCode();
                    switch (code) {
                        case 200:
                            //发送验证码成功,请求进入下一步
                            //意味着我们可以进行第二次请求了,获取phoneToken
                            //mRequestType = 2;
                            SimplexToast.showToastForKeyBord(R.string.send_sms_code_success_hint, GlobalApplication.getContext(), mKeyBoardIsActive);
                            mEtLoginPwd.setText(null);
                            break;
                        case 400:
                            //手机号已被注册,提示重新输入
                            if (mTimer != null) {
                                mTimer.onFinish();
                                mTimer.cancel();
                            }
                            SimplexToast.showToastForKeyBord(resultBean.getMessage(), GlobalApplication.getContext(), mKeyBoardIsActive);
                            break;
                        case 500:
                            //异常错误，发送验证码失败,回收timer,需重新请求发送验证码
                            if (mTimer != null) {
                                mTimer.onFinish();
                                mTimer.cancel();
                            }
                            SimplexToast.showToastForKeyBord(resultBean.getMessage(), GlobalApplication.getContext(), mKeyBoardIsActive);
                            break;
                        default:
                            break;
                    }
                }
            });

        } else {
            SimplexToast.showMyToast(R.string.register_sms_wait_hint, GlobalApplication.getContext());
        }
    }


    @SuppressWarnings("ConstantConditions")
    private void loginRequest() {

        String tempUsername = mEtLoginUsername.getText().toString().trim();
        String tempPwd = mEtLoginPwd.getText().toString().trim();

        if (!TextUtils.isEmpty(tempPwd) && !TextUtils.isEmpty(tempUsername)) {

            //登录成功,请求数据进入用户个人中心页面

            if (TDevice.hasInternet()) {
                requestLogin(tempUsername, tempPwd);
            } else {
                SimplexToast.showToastForKeyBord(R.string.footer_type_net_error, GlobalApplication.getContext(), mKeyBoardIsActive);
            }

        } else {
            //手机震动
            VibratorUtil.Vibrate(this, 100);
            if (TextUtils.isEmpty(tempUsername)) {
                mEtLoginPwd.setFocusableInTouchMode(false);
                mEtLoginPwd.clearFocus();
                mEtLoginUsername.requestFocus();
                mEtLoginUsername.setFocusableInTouchMode(true);
                mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_error);
                SimplexToast.showToastForKeyBord(R.string.login_input_username_hint_error, GlobalApplication.getContext(), mKeyBoardIsActive);
            } else if (TextUtils.isEmpty(tempPwd)) {
                mEtLoginUsername.setFocusableInTouchMode(false);
                mEtLoginUsername.clearFocus();
                mEtLoginPwd.requestFocus();
                mEtLoginPwd.setFocusableInTouchMode(true);
                mLlLoginPwd.setBackgroundResource(R.drawable.bg_login_input_error);
                SimplexToast.showToastForKeyBord(R.string.login_password_hint, GlobalApplication.getContext(), mKeyBoardIsActive);
            }

        }

    }

    private void requestLogin(String tempUsername, String tempPwd) {
        MyLog.i("GCS", "加密前用户名：" + tempUsername + ",加密前密码：" + tempPwd);
        MyApi.loginbysms(getAES(tempUsername), tempPwd, Jsessionid, new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                showFocusWaitDialog();
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                hideWaitDialog();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                hideWaitDialog();
                MyLog.i("GCS", "短信登录返回Exception：" + e.toString());
                SimplexToast.requestFailureHint(e, LoginSmscodeActivity.this);
            }

            @Override
            public void onResponse(String response, int id) {
                MyLog.i("GCS", "短信登录成功返回response：" + response);
                try {
                    Type type = new TypeToken<ResultBean<User>>() {
                    }.getType();
                    ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                    int code = resultBean.getCode();
                    if (code == 200) {
                        MyLog.i("GCS", "短信登录");
                        User user = (User) resultBean.getResult();
                        //模拟用户登录cookie添加
                        String netcookie = "gcs test login test add cookie2222222" + System.currentTimeMillis();
                        user.setId(Long.valueOf(user.getUserid()));
                        MyLog.i("GCS", "短信登录2");
                        if (AccountHelper.login(user, netcookie)) {
                            // MyLog.i("GCS","短信登录logsucced（）");
                            new Handler(new Handler.Callback() {
                                //处理接收到的消息的方法
                                @Override
                                public boolean handleMessage(Message arg0) {
                                    //实现页面跳转
                                    Intent intent = new Intent(LoginSmscodeActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    logSucceed();
                                    return false;
                                }
                            }).sendEmptyMessageDelayed(0, 2000); //表示延时三秒进行任务的执行


                        } else {
                            SimplexToast.showToastForKeyBord("登录异常", GlobalApplication.getContext(), mKeyBoardIsActive);
                        }
                    } else {

                        String message = resultBean.getMessage();
                        if (code == 500) {
                            mEtLoginUsername.setFocusableInTouchMode(false);
                            mEtLoginUsername.clearFocus();
                            mEtLoginPwd.requestFocus();
                            mEtLoginPwd.setFocusableInTouchMode(true);
                            message += "," + getResources().getString(R.string.message_pwd_error);
                            mLlLoginPwd.setBackgroundResource(R.drawable.bg_login_input_error);
                        } else if (code == 400) {

                        }
                        SimplexToast.showToastForKeyBord(message, GlobalApplication.getContext(), mKeyBoardIsActive);
                        //更新失败应该是不进行任何的本地操作
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        int id = v.getId();

        if (id == R.id.et_login_username) {
            if (hasFocus) {
                mLlLoginUsername.setActivated(true);
                mLlLoginPwd.setActivated(false);
            }
        } else {
            if (hasFocus) {
                mLlLoginPwd.setActivated(true);
                mLlLoginUsername.setActivated(false);
            }
        }
    }


    @Override
    public void onGlobalLayout() {

        final ImageView ivLogo = this.mIvLoginLogo;
        Rect KeypadRect = new Rect();

        mLayBackBar.getWindowVisibleDisplayFrame(KeypadRect);

        int screenHeight = mLayBackBar.getRootView().getHeight();

        int keypadHeight = screenHeight - KeypadRect.bottom;

        if (keypadHeight > 0) {
            updateKeyBoardActiveStatus(true);
        } else {
            updateKeyBoardActiveStatus(false);
        }
        if (keypadHeight > 200 && ivLogo.getTag() == null) {
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
                    ViewGroup.LayoutParams layoutParams = ivLogo.getLayoutParams();
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


        } else if (keypadHeight <= 200 && ivLogo.getTag() != null) {
            final int height = mLogoHeight;
            final int width = mLogoWidth;
            ivLogo.setTag(null);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = ivLogo.getLayoutParams();
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

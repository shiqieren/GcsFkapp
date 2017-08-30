package com.gcs.fengkong.ui.account.atys;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
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
import android.widget.Toast;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.UserConstants;
import com.gcs.fengkong.ui.atys.MainActivity;
import com.gcs.fengkong.utils.TDevice;


/**
 * Created by fei on 2016/10/14.
 * desc:
 */

public class LoginActivity extends AccountBaseActivity implements View.OnClickListener, View.OnFocusChangeListener, ViewTreeObserver.OnGlobalLayoutListener {

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
    private ImageView mIvLoginPwdDel;
    private  ImageView mIvHoldPwd;
    private TextView mTvLoginForgetPwd;
    private Button mBtLoginSubmit;
    private TextView mTvLoginRegister;



    /*private TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {

        @Override
        public void onStart() {
            super.onStart();
            showFocusWaitDialog();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            requestFailureHint(throwable);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {

            Type type = new TypeToken<ResultBean<User>>() {
            }.getType();

            GsonBuilder gsonBuilder = new GsonBuilder();
            ResultBean<User> resultBean = gsonBuilder.create().fromJson(responseString, type);
            if (resultBean.isSuccess()) {
                User user = resultBean.getResult();
                if (AccountHelper.login(user, headers)) {
                    logSucceed();
                } else {
                    showToastForKeyBord("登录异常");
                }
            } else {
                showToastForKeyBord(resultBean.getMessage());
            }
        }
    };*/

    private void logSucceed() {
        View view;
        if ((view = getCurrentFocus()) != null) {
            hideKeyBoard(view.getWindowToken());
        }
        GlobalApplication.showToast(R.string.login_success_hint);
        setResult(RESULT_OK);
        sendLocalReceiver();
        //后台异步同步数据
      //  ContactsCacheManager.sync();
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
            SharedPreferences sp = getSharedPreferences(UserConstants.HOLD_ACCOUNT, Context.MODE_PRIVATE);
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
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Activity context, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * show the login activity
     *
     * @param fragment fragment
     */
    public static void show(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), LoginActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_login;
    }


    private  void initViews(){

        mLayBackBar = (LinearLayout) findViewById(R.id.ly_retrieve_bar);
        mIb_navigation_back = (ImageButton) findViewById(R.id.ib_navigation_back);

        mLayLoginContains= (LinearLayout) findViewById(R.id.lay_login_container);

        mIvLoginLogo = (ImageView) findViewById(R.id.iv_login_logo);

        mLlLoginUsername = (LinearLayout) findViewById(R.id.ll_login_username);

        mEtLoginUsername = (EditText) findViewById(R.id.et_login_username);

        mIvLoginUsernameDel = (ImageView) findViewById(R.id.iv_login_username_del);

        mLlLoginPwd = (LinearLayout) findViewById(R.id.ll_login_pwd);

        mEtLoginPwd = (EditText) findViewById(R.id.et_login_pwd);

        mIvLoginPwdDel = (ImageView) findViewById(R.id.iv_login_pwd_del);

        mIvHoldPwd = (ImageView) findViewById(R.id.iv_login_hold_pwd);

        mTvLoginForgetPwd = (TextView) findViewById(R.id.tv_login_forget_pwd);

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
                String username = s.toString().trim();
                if (username.length() > 0) {
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
                }

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
                } else {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();//必须要,用来注册本地广播

        //初始化控件状态数据
        SharedPreferences sp = getSharedPreferences(UserConstants.HOLD_ACCOUNT, Context.MODE_PRIVATE);
        String holdUsername = sp.getString(HOLD_USERNAME_KEY, null);
        //String holdPwd = sp.getString(HOLD_PWD_KEY, null);
        //int holdStatus = sp.getInt(HOLD_PWD_STATUS_KEY, 0);//0第一次默认/1用户设置保存/2用户设置未保存

        mEtLoginUsername.setText(holdUsername);

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
    private void setListener(){
        mLayBackBar.setOnClickListener(this);
        mIb_navigation_back.setOnClickListener(this);
        mIvLoginLogo.setOnClickListener(this);
        mLlLoginUsername.setOnClickListener(this);
        mEtLoginUsername.setOnClickListener(this);
        mIvLoginUsernameDel.setOnClickListener(this);
        mLlLoginPwd.setOnClickListener(this);
        mEtLoginPwd.setOnClickListener(this);
        mIvLoginPwdDel.setOnClickListener(this);
        mIvHoldPwd.setOnClickListener(this);
        mTvLoginForgetPwd.setOnClickListener(this);
        mBtLoginSubmit.setOnClickListener(this);
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
                RetrieveActivity.show(LoginActivity.this);
                break;
            case R.id.bt_login_submit:

               loginRequest();
                break;
            case R.id.iv_login_hold_pwd:
                //记住密码
            case R.id.tv_login_register:
                RegisterStepOneActivity.show(LoginActivity.this);
                break;
            case R.id.iv_login_username_del:
                mEtLoginUsername.setText(null);
                break;
            case R.id.iv_login_pwd_del:
                mEtLoginPwd.setText(null);
                break;
            case R.id.lay_login_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            default:
                break;
        }

    }






    @SuppressWarnings("ConstantConditions")
    private void loginRequest() {

        String tempUsername = mEtLoginUsername.getText().toString().trim();
        String tempPwd = mEtLoginPwd.getText().toString().trim();


        if (!TextUtils.isEmpty(tempPwd) && !TextUtils.isEmpty(tempUsername)) {


            //登录成功,请求数据进入用户个人中心页面

            if (TDevice.hasInternet()) {
                //用户登录
                Toast.makeText(LoginActivity.this,"登录",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
              //111  requestLogin(tempUsername, tempPwd);
            } else {
                showToastForKeyBord(R.string.footer_type_net_error);
            }

        } else {
            showToastForKeyBord(R.string.login_input_username_hint_error);
        }

    }

    /*private void requestLogin(String tempUsername, String tempPwd) {
        OSChinaApi.login(tempUsername, getSha1(tempPwd), new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                showFocusWaitDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                requestFailureHint(throwable);
            }

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    Type type = new TypeToken<ResultBean<User>>() {
                    }.getType();

                    ResultBean<User> resultBean = AppOperator.createGson().fromJson(responseString, type);
                    if (resultBean.isSuccess()) {
                        User user = resultBean.getResult();
                        if (AccountHelper.login(user, headers)) {
                            logSucceed();
                        } else {
                            showToastForKeyBord("登录异常");
                        }
                    } else {
                        int code = resultBean.getCode();
                        String message = resultBean.getMessage();
                        if (code == 211) {
                            mEtLoginPwd.setFocusableInTouchMode(false);
                            mEtLoginPwd.clearFocus();
                            mEtLoginUsername.requestFocus();
                            mEtLoginUsername.setFocusableInTouchMode(true);
                            mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_error);
                        } else if (code == 212) {
                            mEtLoginUsername.setFocusableInTouchMode(false);
                            mEtLoginUsername.clearFocus();
                            mEtLoginPwd.requestFocus();
                            mEtLoginPwd.setFocusableInTouchMode(true);
                            message += "," + getResources().getString(R.string.message_pwd_error);
                            mLlLoginPwd.setBackgroundResource(R.drawable.bg_login_input_error);
                        }
                        showToastForKeyBord(message);
                        //更新失败应该是不进行任何的本地操作
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
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
        });
    }*/






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

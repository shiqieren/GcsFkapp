package com.gcs.fengkong.ui.account.atys;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.bean.PhoneToken;
import com.gcs.fengkong.utils.TDevice;


/**
 * Created by fei
 * on 2016/10/14.
 * desc:
 */

public class ResetPwdActivity extends AccountBaseActivity implements View.OnClickListener, View.OnFocusChangeListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    private LinearLayout mLlResetBar;
    private ImageButton mIb_navigation_back;
    private LinearLayout mLlResetcontainer;
    private LinearLayout mLlResetPwd;
    private EditText mEtResetPwd;
    private ImageView mIvResetPwdDel;
    private Button mBtResetSubmit;
    private PhoneToken mPhoneToken;
    /*private TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {

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
            requestFailureHint(throwable);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {

            Type type = new TypeToken<ResultBean>() {
            }.getType();
            ResultBean resultBean = AppOperator.createGson().fromJson(responseString, type);
            int code = resultBean.getCode();

            switch (code) {
                case 1:
                    AppContext.showToast(getResources().getString(R.string.reset_success_hint), Toast.LENGTH_SHORT);
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
    };*/
    private int mTopMargin;

    /**
     * show the resetPwdActivity
     *
     * @param context context
     */
    public static void show(Context context, PhoneToken phoneToken) {
        Intent intent = new Intent(context, ResetPwdActivity.class);
        intent.putExtra(RegisterStepTwoActivity.PHONE_TOKEN_KEY, phoneToken);
        context.startActivity(intent);
    }
    //测试跳转方法
    public static void show(Context context) {
        Intent intent = new Intent(context, ResetPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_reset_pwd;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initViews();
        setListener();
        TextView tvLabel = (TextView) mLlResetBar.findViewById(R.id.tv_navigation_label);
        tvLabel.setText(R.string.reset_pwd_label);
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
                if (length >= 6) {
                    mIvResetPwdDel.setVisibility(View.VISIBLE);
                    mLlResetPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mBtResetSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtResetSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    if (length <= 0) {
                        mIvResetPwdDel.setVisibility(View.GONE);
                        mLlResetPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                    } else {
                        mIvResetPwdDel.setVisibility(View.VISIBLE);
                        mLlResetPwd.setBackgroundResource(R.drawable.bg_login_input_error);
                    }
                    mBtResetSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtResetSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }

            }
        });
    }

    private void initViews() {
        mLlResetBar = (LinearLayout) findViewById(R.id.ly_reset_bar);
        mIb_navigation_back = (ImageButton) findViewById(R.id.ib_navigation_back);
        mLlResetcontainer = (LinearLayout) findViewById(R.id.lay_reset_container);
        mLlResetPwd= (LinearLayout) findViewById(R.id.ll_reset_pwd);
        mEtResetPwd= (EditText) findViewById(R.id.et_reset_pwd);
        mIvResetPwdDel= (ImageView) findViewById(R.id.iv_reset_pwd_del);
        mBtResetSubmit= (Button) findViewById(R.id.bt_reset_submit);
    }


    @Override
    protected void initData() {
        super.initData();//必须要调用,用来注册本地广播
        Intent intent = getIntent();
        mPhoneToken = (PhoneToken) intent.getSerializableExtra(RegisterStepTwoActivity.PHONE_TOKEN_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLlResetBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
        mLlResetBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }


    @SuppressWarnings("ConstantConditions")
    private void setListener(){
        mLlResetBar.setOnClickListener(this);
        mIb_navigation_back.setOnClickListener(this);
        mIvResetPwdDel.setOnClickListener(this);
        mBtResetSubmit.setOnClickListener(this);
        mLlResetcontainer.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.iv_reset_pwd_del:
                mEtResetPwd.setText(null);
                break;
            case R.id.bt_reset_submit:
                requestResetPwd();
                break;
            case R.id.lay_reset_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            default:
                break;
        }

    }

    private void requestResetPwd() {
        String tempPwd = mEtResetPwd.getText().toString().trim();
        if (TextUtils.isEmpty(tempPwd) || tempPwd.length() < 6) {
            //showToastForKeyBord(R.string.reset_pwd_hint);
            return;
        }
        if (!TDevice.hasInternet()) {
            showToastForKeyBord(R.string.tip_network_error);
            return;
        }

       //111 OSChinaApi.resetPwd(getSha1(tempPwd), mPhoneToken.getToken(), mHandler);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mLlResetPwd.setActivated(true);
        }
    }

    @Override
    public void onGlobalLayout() {

        final LinearLayout kayResetPwd = this.mLlResetPwd;
        Rect keypadRect = new Rect();

        mLlResetBar.getWindowVisibleDisplayFrame(keypadRect);

        int screenHeight = mLlResetBar.getRootView().getHeight();

        int keypadHeight = screenHeight - keypadRect.bottom;

        if (keypadHeight > 0) {
            updateKeyBoardActiveStatus(true);
        } else {
            updateKeyBoardActiveStatus(false);
        }

        if (keypadHeight > 0 && kayResetPwd.getTag() == null) {
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) kayResetPwd.getLayoutParams();
            final int topMargin = layoutParams.topMargin;
            this.mTopMargin = topMargin;
            kayResetPwd.setTag(true);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    layoutParams.topMargin = (int) (topMargin * animatedValue);
                    kayResetPwd.requestLayout();
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();


        } else if (keypadHeight == 0 && kayResetPwd.getTag() != null) {
            final int topMargin = mTopMargin;
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) kayResetPwd.getLayoutParams();
            kayResetPwd.setTag(null);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    layoutParams.topMargin = (int) (topMargin * animatedValue);
                    kayResetPwd.requestLayout();
                }
            });
            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();

        }
    }
}

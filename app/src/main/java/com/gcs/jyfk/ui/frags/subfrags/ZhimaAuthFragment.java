package com.gcs.jyfk.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.ShowUIHelper;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.atys.SimpleBackActivity;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.ui.frags.BaseFragment;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.VibratorUtil;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 0029 8-29.
 */
@SuppressLint("NewApi")
public class ZhimaAuthFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private LinearLayout mLlAuthUsername;
    private LinearLayout mLlAuthPassword;
    private EditText mEtAuthUsername;
    private EditText mEtAuthPassword;
    private ImageView mIvAuthUsernaneDel;
    private ImageView mIvAuthPasswordDel;
    private Button mBtAuthSubmit;
    private Boolean mIsZHname = false;
    private Boolean mIsIDcardnumber = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zhima_auth;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity) getActivity()).setToolBarTitle(R.string.zhima_string);
        view.findViewById(R.id.traceroute_rootview).setOnClickListener(this);
        mLlAuthUsername = view.findViewById(R.id.ll_auth_username);
        mLlAuthPassword = view.findViewById(R.id.ll_auth_password);
        mEtAuthUsername = view.findViewById(R.id.et_auth_username);
        mEtAuthPassword = view.findViewById(R.id.et_auth_password);
        mIvAuthUsernaneDel = view.findViewById(R.id.iv_auth_username_del);
        mIvAuthPasswordDel = view.findViewById(R.id.iv_auth_password_del);
        mBtAuthSubmit = view.findViewById(R.id.bt_auth_submit);

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
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
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
                if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(name)) {
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*/
                } else {
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                }
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        if (AccountHelper.isLogin()) {
            User user = AccountHelper.getUser();
            if (user.getName() != null) {
                mEtAuthUsername.setText(unAES(user.getName()));
            }
            if (user.getCertno() != null) {
                mEtAuthPassword.setText(unAES(user.getCertno()));
            }
        }
    }

    private void setListener() {
        mLlAuthUsername.setOnClickListener(this);
        mLlAuthPassword.setOnClickListener(this);
        mEtAuthUsername.setOnClickListener(this);
        mEtAuthPassword.setOnClickListener(this);
        mIvAuthUsernaneDel.setOnClickListener(this);
        mIvAuthPasswordDel.setOnClickListener(this);
        mBtAuthSubmit.setOnClickListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
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
                AuthIdentityRequest();
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

    private void AuthIdentityRequest() {
        String tempUsername = mEtAuthUsername.getText().toString().trim();
        String tempIdcard = mEtAuthPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(tempUsername)) {
            if (mIsIDcardnumber) {
                VibratorUtil.Vibrate(getActivity(), 100);
                mEtAuthUsername.setFocusableInTouchMode(false);
                mEtAuthUsername.clearFocus();
                mEtAuthPassword.requestFocus();
                mEtAuthPassword.setFocusableInTouchMode(true);
                mEtAuthPassword.setBackgroundResource(R.drawable.bg_login_input_error);
                SimplexToast.showMyToast("身份证格式有误", GlobalApplication.getContext());
                return;
            }
        } else {
            SimplexToast.showMyToast("姓名不能为空", GlobalApplication.getContext());
            return;
        }

        if (!TextUtils.isEmpty(tempUsername) && AccountHelper.isLogin() && !TextUtils.isEmpty(tempIdcard)) {

            //登录成功,请求数据进入用户个人中心页面
            User user = AccountHelper.getUser();
            String token = user.getToken();
            String phone = user.getPhone();
            MyApi.authzhima(token, getAES(phone), getAES(tempIdcard), tempUsername, "", new StringCallback() {
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
                    MyLog.i("GCS", "身份认证返回Exception：" + e);
                }

                @Override
                public void onResponse(String response, int id) {
                    hideWaitDialog();
                    MyLog.i("GCS", "登录返回response：" + response);
                    try {
                        Type type = new TypeToken<ResultBean>() {
                        }.getType();
                        ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                        int code = resultBean.getCode();
                        switch (code) {

                            case 200://授权url获取成功
                                MyLog.i("GCS", "跳转到回调url：" + response);
                                String url = resultBean.getResult().toString();
                                MyLog.i("GCS", "授权按钮点击，打开webview：" + url);
                                ShowUIHelper.openInternalBrowser(getActivity(), url);

                                break;
                            case 600://已经授权过,成功更新了用户芝麻数据
                                SimplexToast.showMyToast(resultBean.getMessage(), GlobalApplication.getContext());
                                getActivity().finish();
                                break;
                            case 300://账户问题
                                SimplexToast.showMyToast(resultBean.getMessage(), GlobalApplication.getContext());

                                break;
                            case 500://失败
                                SimplexToast.showMyToast(resultBean.getMessage(), GlobalApplication.getContext());
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // requestLoginno(tempUsername, tempPwd);
        }

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
}

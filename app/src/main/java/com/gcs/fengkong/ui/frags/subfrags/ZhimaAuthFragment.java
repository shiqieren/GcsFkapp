package com.gcs.fengkong.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.ShowUIHelper;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.bean.User;
import com.gcs.fengkong.ui.api.MyApi;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.bean.base.ResultBean;
import com.gcs.fengkong.ui.frags.BaseFragment;
import com.gcs.fengkong.utils.AppOperator;
import com.gcs.fengkong.utils.MyLog;
import com.gcs.fengkong.utils.TDevice;
import com.gcs.fengkong.utils.VibratorUtil;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by Administrator on 0029 8-29.
 */
@SuppressLint("NewApi")
public class ZhimaAuthFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener{

    private LinearLayout mLlAuthUsername;
     private LinearLayout mLlAuthPassword;
    private EditText mEtAuthUsername;
    private EditText mEtAuthPassword;
    private ImageView mIvAuthUsernaneDel;
    private ImageView mIvAuthPasswordDel;
    private Button mBtAuthSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zhima_auth;
    }
    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.zhima_string);
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
                if (!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(name)) {
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

        String tempUsername = mEtAuthUsername.getText().toString().trim();
        String tempIdcard = mEtAuthPassword.getText().toString().trim();
        MyLog.i("GCS","点击返回："+tempUsername);
        if (!TextUtils.isEmpty(tempUsername)&&AccountHelper.isLogin()&&!TextUtils.isEmpty(tempIdcard)) {

            //登录成功,请求数据进入用户个人中心页面
            User user = AccountHelper.getUser();
            String token =  user.getToken();
            MyLog.i("GCS","token："+token);
            MyLog.i("GCS","idcard："+tempIdcard);
            MyLog.i("GCS","tempUsername："+tempUsername);

                MyApi.authzhima(token, getAES(tempUsername), getAES(tempIdcard), "李毅伟", "", new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyLog.i("GCS","Exception："+e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyLog.i("GCS","登录返回response："+response);
                        try {
                            Type type = new TypeToken<ResultBean>() {}.getType();
                            ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                            int code = resultBean.getCode();

                            if (code == 200) {
                                MyLog.i("GCS","跳转到回调url："+response);
                               String url =  resultBean.getResult().toString();
                                MyLog.i("GCS","授权按钮点击，打开webview："+url);
                                ShowUIHelper.openInternalBrowser(getActivity(), url);
                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                // requestLoginno(tempUsername, tempPwd);


        } else {

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

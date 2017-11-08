package com.gcs.jyfk.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.atys.SimpleBackActivity;
import com.gcs.jyfk.ui.frags.BaseFragment;
import com.gcs.jyfk.ui.widget.SimplexToast;

/**
 * Created by Administrator on 0029 8-29.
 */
@SuppressLint("NewApi")
public class AlipayAuthFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private CheckBox mCbAgreeAuthbook;
    private LinearLayout mLlAuthUsername;
    private LinearLayout mLlAuthPassword;
    private EditText mEtAuthUsername;
    private EditText mEtAuthPassword;
    private ImageView mIvAuthUsernaneDel;
    private ImageView mIvAuthPasswordDel;
    private Button mBtAuthSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alipay_auth;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity) getActivity()).setToolBarTitle(R.string.alipay_string);
        view.findViewById(R.id.traceroute_rootview).setOnClickListener(this);
        mCbAgreeAuthbook = view.findViewById(R.id.cb_agree_authbook);
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
                    mIvAuthUsernaneDel.setVisibility(View.INVISIBLE);
                }

                String name = mEtAuthUsername.getText().toString().trim();
                String pwd = mEtAuthPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
                    mBtAuthSubmit.setEnabled(true);
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*/
                } else {
                    mBtAuthSubmit.setEnabled(false);
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
                    mIvAuthPasswordDel.setVisibility(View.INVISIBLE);
                }

                String pwd = mEtAuthPassword.getText().toString().trim();
                String name = mEtAuthUsername.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(name) && mCbAgreeAuthbook.isChecked()) {
                    mBtAuthSubmit.setEnabled(true);
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*/
                } else {
                    mBtAuthSubmit.setEnabled(false);
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
        if (!mCbAgreeAuthbook.isChecked()) {
            SimplexToast.showMyToast("尚未勾选授权协议书", GlobalApplication.getContext());
        } else {

        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }
}

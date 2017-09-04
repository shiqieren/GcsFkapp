package com.gcs.fengkong.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.atys.LoginActivity;
import com.gcs.fengkong.ui.account.atys.RegisterActivity;
import com.gcs.fengkong.ui.account.atys.ResetPwdActivity;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.frags.BaseFragment;

/**
 * Created by Administrator on 0029 8-29.
 */
@SuppressLint("NewApi")
public class IdentityAuthFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener{

    private LinearLayout mLlIdentityName;
    private LinearLayout mLlIdentityNumber;
    private ImageView mIvIdentityNameDel;
    private ImageView mIvIdentityNumberDel;
    private EditText mEtIdentityName;
    private EditText mEtIdentityNumber;
    private Button mBtIdentitySubmit;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_identity_auth;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.identity_string);
        mLlIdentityName = view.findViewById(R.id.ll_identity_name);
        mLlIdentityNumber = view.findViewById(R.id.ll_identity_number);
        mIvIdentityNameDel = (ImageView) view.findViewById(R.id.iv_identity_name_del);
        mIvIdentityNumberDel = (ImageView) view.findViewById(R.id.iv_identity_number_del);
       mEtIdentityName = view.findViewById(R.id.et_identity_name);
       mEtIdentityNumber = view.findViewById(R.id.et_identity_number);
        mBtIdentitySubmit = view.findViewById(R.id.bt_identity_submit);
        setListener();
        mEtIdentityName.setOnFocusChangeListener(this);
        mEtIdentityName.addTextChangedListener(new TextWatcher() {
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
                    mIvIdentityNameDel.setVisibility(View.VISIBLE);
                } else {
                 //   mLlIdentityName.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvIdentityNameDel.setVisibility(View.INVISIBLE);
                }

                String identitynumber = mEtIdentityNumber.getText().toString().trim();
                String pwd = mEtIdentityNumber.getText().toString().trim();
                if (!TextUtils.isEmpty(identitynumber)&&!TextUtils.isEmpty(pwd)) {
                    mBtIdentitySubmit.setEnabled(true);
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*/
                } else {
                    mBtIdentitySubmit.setEnabled(false);
                  /*  mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                }

            }
        });

        mEtIdentityNumber.setOnFocusChangeListener(this);
        mEtIdentityNumber.addTextChangedListener(new TextWatcher() {
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
                    mIvIdentityNumberDel.setVisibility(View.VISIBLE);
                } else {
                    mIvIdentityNumberDel.setVisibility(View.INVISIBLE);
                }

                String pwd = mEtIdentityNumber.getText().toString().trim();
                String identitynumber = mEtIdentityNumber.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(identitynumber)) {
                    mBtIdentitySubmit.setEnabled(true);
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*/
                }else {
                    mBtIdentitySubmit.setEnabled(false);
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                }
            }
        });
    }

    private void setListener() {
        mLlIdentityName.setOnClickListener(this);
        mEtIdentityName.setOnClickListener(this);
        mIvIdentityNameDel.setOnClickListener(this);
        mLlIdentityNumber.setOnClickListener(this);
        mEtIdentityNumber.setOnClickListener(this);
        mIvIdentityNumberDel.setOnClickListener(this);
        mBtIdentitySubmit.setOnClickListener(this);
    }
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.et_identity_name:
                mEtIdentityNumber.clearFocus();
                mEtIdentityName.setFocusableInTouchMode(true);
                mEtIdentityName.requestFocus();
                break;
            case R.id.et_identity_number:
                mEtIdentityName.clearFocus();
                mEtIdentityNumber.setFocusableInTouchMode(true);
                mEtIdentityNumber.requestFocus();
                break;

            case R.id.bt_identity_submit:
                AuthIdentityRequest();
                break;

            case R.id.iv_identity_name_del:
                mEtIdentityName.setText(null);
                break;
            case R.id.iv_identity_number_del:
                mEtIdentityNumber.setText(null);
                break;
            default:
                break;
        }
    }

    private void AuthIdentityRequest() {
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();

        if (id == R.id.et_identity_name) {
            if (hasFocus) {
                mLlIdentityName.setActivated(true);
                mLlIdentityNumber.setActivated(false);
            }
        } else {
            if (hasFocus) {
                mLlIdentityNumber.setActivated(true);
                mLlIdentityName.setActivated(false);
            }
        }
    }
}

package com.gcs.fengkong.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gcs.fengkong.AppConfig;
import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.RichTextParser;
import com.gcs.fengkong.ui.api.MyApi;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.bean.base.ResultBean;
import com.gcs.fengkong.ui.frags.BaseFragment;
import com.gcs.fengkong.ui.widget.SimplexToast;
import com.gcs.fengkong.utils.AppOperator;
import com.gcs.fengkong.utils.RegexUtils;
import com.gcs.fengkong.utils.TDevice;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 0029 8-29.
 */
@SuppressLint("NewApi")
public class BankAuthFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener{

    private LinearLayout mLlBankcardName;
    private LinearLayout mLlBankcardNumber;
    private LinearLayout mLlBankcardPhone;
    private EditText mEtBankcardName;
    private EditText mEtBankcardNumber;
    private EditText mEtBankcardPhone;
    private ImageView mIvBankcardNameDel;
    private ImageView mIvBankcardNumberDel;
    private ImageView mIvBankcardPhoneDel;
    private TextView mTvBankcardSmsCall;
    private LinearLayout mLlBankcardSmsCode;
    private EditText mEtBankcardSmsCode;
    private Button mBtBankcardSubmit;

    private boolean mMachPhoneNum;
    private boolean mBankcardNum;
    private CountDownTimer mTimer;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bankcard_auth;
    }
    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.bank_string);
        view.findViewById(R.id.traceroute_rootview).setOnClickListener(this);
        mLlBankcardName  = view.findViewById(R.id.ll_bankcard_name);
        mLlBankcardNumber  = view.findViewById(R.id.ll_bankcard_number);
        mLlBankcardPhone  = view.findViewById(R.id.ll_bankcard_phone);

        mEtBankcardName =  view.findViewById(R.id.et_bankcard_name);
        mEtBankcardNumber =  view.findViewById(R.id.et_bankcard_number);
        mEtBankcardPhone =  view.findViewById(R.id.et_bankcard_phone);

        mIvBankcardNameDel =  view.findViewById(R.id.iv_bankcard_name_del);
        mIvBankcardNumberDel =  view.findViewById(R.id.iv_bankcard_number_del);
        mIvBankcardPhoneDel =  view.findViewById(R.id.iv_bankcard_phone_del);

        mBtBankcardSubmit =  view.findViewById(R.id.bt_bankcard_submit);

        mTvBankcardSmsCall= view.findViewById(R.id.tv_bankcard_sms_call);
        mLlBankcardSmsCode= view.findViewById(R.id.ll_bankcard_sms_code);
        mEtBankcardSmsCode=  view.findViewById(R.id.et_bankcard_sms_code);

        setListener();
        mEtBankcardName.setOnFocusChangeListener(this);
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!RichTextParser.isChinese(source.charAt(i))) {
                       // SimplexToast.showMyToast("请输入中文字符",GlobalApplication.getContext());
                        return "";
                    }
                }
                return null;
            }
        };
        mEtBankcardName.setFilters(new InputFilter[]{filter});
        mEtBankcardName.addTextChangedListener(new TextWatcher() {
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
                    mIvBankcardNameDel.setVisibility(View.VISIBLE);
                } else {
                    //   mLlIdentityName.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvBankcardNameDel.setVisibility(View.GONE);
                }
                String phone = mEtBankcardPhone.getText().toString().trim();
                String identitynumber = mEtBankcardNumber.getText().toString().trim();
                String pwd = mEtBankcardNumber.getText().toString().trim();
                if (!TextUtils.isEmpty(identitynumber)&&!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(phone)) {
                    mBtBankcardSubmit.setEnabled(true);
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*/
                } else {
                    mBtBankcardSubmit.setEnabled(false);
                  /*  mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                }

            }
        });

        mEtBankcardNumber.setOnFocusChangeListener(this);


        mEtBankcardNumber.addTextChangedListener(new TextWatcher() {
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
                String input = s.toString();
                if (length > 0) {
                    // mLlIdentityNumber.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvBankcardNumberDel.setVisibility(View.VISIBLE);
                } else {
                    mIvBankcardNumberDel.setVisibility(View.GONE);
                }
                mBankcardNum = RegexUtils.isBankCard(input);

                if (mBankcardNum) {
                    mLlBankcardNumber.setBackgroundResource(0);
                    String name = mEtBankcardName.getText().toString().trim();
                    String phone = mEtBankcardPhone.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(phone)) {
                        mBtBankcardSubmit.setEnabled(true);
                      //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                      //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        mBtBankcardSubmit.setEnabled(false);
                      //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                      //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    }
                } else {
                    mLlBankcardNumber.setBackgroundResource(R.drawable.bg_login_input_error);
                    mBtBankcardSubmit.setEnabled(false);
                  //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                  //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }

            }
        });

        mEtBankcardPhone.setOnFocusChangeListener(this);
        mEtBankcardPhone.addTextChangedListener(new TextWatcher() {
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
                String input = s.toString();
                if (length > 0) {
                    // mLlIdentityNumber.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvBankcardPhoneDel.setVisibility(View.VISIBLE);
                } else {
                    mIvBankcardPhoneDel.setVisibility(View.GONE);
                }
                mMachPhoneNum = RichTextParser.machPhoneNum(input);

                if (mMachPhoneNum) {
                    String smsCode = mEtBankcardSmsCode.getText().toString().trim();
                    String pwd = mEtBankcardNumber.getText().toString().trim();
                    if (!TextUtils.isEmpty(smsCode)&&!TextUtils.isEmpty(pwd)) {
                        mBtBankcardSubmit.setEnabled(true);
                        /*mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                        mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.white));*/
                    } else {
                        mBtBankcardSubmit.setEnabled(false);
                      /*  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                    }
                } else {
                    mBtBankcardSubmit.setEnabled(false);
                    /*mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                }

                if (length > 0 && length < 11) {
                    mLlBankcardPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                    mTvBankcardSmsCall.setAlpha(0.4f);
                } else if (length == 11) {
                    if (mMachPhoneNum) {
                        mLlBankcardPhone.setBackgroundResource(0);
                        if (mTvBankcardSmsCall.getTag() == null) {
                            mTvBankcardSmsCall.setAlpha(1.0f);
                        } else {
                            mTvBankcardSmsCall.setAlpha(0.4f);
                        }
                    } else {
                        mLlBankcardPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                        //showToastForKeyBord(R.string.hint_username_ok);
                        mTvBankcardSmsCall.setAlpha(0.4f);
                    }
                } else if (length > 11) {
                    mTvBankcardSmsCall.setAlpha(0.4f);
                    mLlBankcardPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                } else if (length <= 0) {
                    mTvBankcardSmsCall.setAlpha(0.4f);
                    mLlBankcardPhone.setBackgroundResource(0);
                }
            }
        });
    }

    private void setListener() {
        mLlBankcardName.setOnClickListener(this);
        mEtBankcardName.setOnClickListener(this);
        mIvBankcardNameDel.setOnClickListener(this);
        mLlBankcardPhone.setOnClickListener(this);
        mEtBankcardPhone.setOnClickListener(this);
        mIvBankcardPhoneDel.setOnClickListener(this);
        mTvBankcardSmsCall.setOnClickListener(this);
        mLlBankcardNumber.setOnClickListener(this);
        mEtBankcardNumber.setOnClickListener(this);
        mIvBankcardNumberDel.setOnClickListener(this);
        mBtBankcardSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.traceroute_rootview:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.et_bankcard_name:
                mEtBankcardNumber.clearFocus();
                mEtBankcardPhone.clearFocus();
                mEtBankcardName.setFocusableInTouchMode(true);
                mEtBankcardName.requestFocus();
                break;
            case R.id.et_bankcard_number:
                mEtBankcardName.clearFocus();
                mEtBankcardPhone.clearFocus();
                mEtBankcardNumber.setFocusableInTouchMode(true);
                mEtBankcardNumber.requestFocus();
                break;
            case R.id.et_bankcard_phone:
                mEtBankcardName.clearFocus();
                mEtBankcardNumber.clearFocus();
                mEtBankcardPhone.setFocusableInTouchMode(true);
                mEtBankcardPhone.requestFocus();
                break;
            case R.id.tv_bankcard_sms_call:
               // requestSmsCode();
                break;
            case R.id.bt_bankcard_submit:
                AuthBankcardRequest();
                break;

            case R.id.iv_bankcard_name_del:
                mEtBankcardName.setText(null);
                break;
            case R.id.iv_bankcard_number_del:
                mEtBankcardNumber.setText(null);
                break;
            case R.id.iv_bankcard_phone_del:
                mEtBankcardPhone.setText(null);
                break;
            default:
                break;
        }
    }



    private void AuthBankcardRequest() {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id){
            case R.id.et_bankcard_name:
                if (hasFocus) {
                    mLlBankcardName.setActivated(true);
                    mLlBankcardNumber.setActivated(false);
                    mLlBankcardPhone.setActivated(false);
                    mIvBankcardNameDel.setVisibility(View.VISIBLE);
                    mIvBankcardNumberDel.setVisibility(View.GONE);
                    mIvBankcardPhoneDel.setVisibility(View.GONE);
                }
                break;
            case  R.id.et_bankcard_number:
                if (hasFocus) {
                    mLlBankcardNumber.setActivated(true);
                    mLlBankcardName.setActivated(false);
                    mLlBankcardPhone.setActivated(false);
                    mIvBankcardNameDel.setVisibility(View.GONE);
                    mIvBankcardNumberDel.setVisibility(View.VISIBLE);
                    mIvBankcardPhoneDel.setVisibility(View.GONE);
                }
                break;
            case  R.id.et_bankcard_phone:
                if (hasFocus) {
                    mLlBankcardNumber.setActivated(false);
                    mLlBankcardName.setActivated(false);
                    mLlBankcardPhone.setActivated(true);
                    mIvBankcardNameDel.setVisibility(View.GONE);
                    mIvBankcardNumberDel.setVisibility(View.GONE);
                    mIvBankcardPhoneDel.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }

    }
}

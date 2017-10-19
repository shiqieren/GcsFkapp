package com.gcs.jyfk.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gcs.jyfk.AppConfig;
import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.ShowUIHelper;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.RichTextParser;
import com.gcs.jyfk.ui.account.atys.RegisterActivity;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.adapter.MyWebListPopuAdapter;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.atys.MainActivity;
import com.gcs.jyfk.ui.atys.SimpleBackActivity;
import com.gcs.jyfk.ui.bean.BankType;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.ui.frags.BaseFragment;
import com.gcs.jyfk.ui.widget.MyOptionBottomDialog;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.utils.ActivityManager;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.DialogUtil;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.RegexUtils;
import com.gcs.jyfk.utils.TDevice;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

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
    private LinearLayout mLlBanktype;
    private TextView mEtBankcardName;
    private EditText mEtBankcardNumber;
    private EditText mEtBankcardPhone;
    private ImageView mIvBankcardNameDel;
    private ImageView mIvBankcardNumberDel;
    private ImageView mIvBankcardPhoneDel;
    private TextView mTvBankcardSmsCall;
    private LinearLayout mLlBankcardSmsCode;
    private EditText mEtBankcardSmsCode;
    private Button mBtBankcardSubmit;
    private TextView mTvBanktype;
    private TextView mTvBankcode;
    private String trade_no_x;
    private boolean mMachPhoneNum;
    private boolean mBankcardNum;
    private List<BankType> mOptions;
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
        mLlBanktype  = view.findViewById(R.id.ll_banktype_select);

        mEtBankcardName =  view.findViewById(R.id.et_bankcard_name);
        mEtBankcardNumber =  view.findViewById(R.id.et_bankcard_number);
        mEtBankcardPhone =  view.findViewById(R.id.et_bankcard_phone);
        mTvBanktype = view.findViewById(R.id.tv_banktype);
        mTvBankcode = view.findViewById(R.id.tv_banktype_code);

        mIvBankcardNameDel =  view.findViewById(R.id.iv_bankcard_name_del);
        mIvBankcardNumberDel =  view.findViewById(R.id.iv_bankcard_number_del);
        mIvBankcardPhoneDel =  view.findViewById(R.id.iv_bankcard_phone_del);

        mBtBankcardSubmit =  view.findViewById(R.id.bt_bankcard_submit);

        mTvBankcardSmsCall= view.findViewById(R.id.tv_bankcard_sms_call);
        mLlBankcardSmsCode= view.findViewById(R.id.ll_bankcard_sms_code);
        mEtBankcardSmsCode=  view.findViewById(R.id.et_bankcard_sms_code);


        setListener();
      //  mEtBankcardName.setOnFocusChangeListener(this);
       /* InputFilter filter = new InputFilter() {
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
                   // mBtBankcardSubmit.setEnabled(true);
                   *//* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*//*
                } else {
                  //  mBtBankcardSubmit.setEnabled(false);
                  *//*  mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*//*
                }

            }
        });*/

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
                mLlBankcardNumber.setBackgroundResource(0);
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
                      //  mBtBankcardSubmit.setEnabled(true);
                      //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                      //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.white));
                    } else {
                      //  mBtBankcardSubmit.setEnabled(false);
                      //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                      //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    }
                } else {
                  //  mLlBankcardNumber.setBackgroundResource(R.drawable.bg_login_input_error);
                  //  mBtBankcardSubmit.setEnabled(false);
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
                       // mBtBankcardSubmit.setEnabled(true);
                        /*mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                        mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.white));*/
                    } else {
                      //  mBtBankcardSubmit.setEnabled(false);
                      /*  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                    }
                } else {
                   // mBtBankcardSubmit.setEnabled(false);
                    /*mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                }

                if (length > 0 && length < 11) {
                    mLlBankcardPhone.setBackgroundResource(0);
                  //  mLlBankcardPhone.setBackgroundResource(R.drawable.bg_login_input_error);
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
                       // mLlBankcardPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                        //showToastForKeyBord(R.string.hint_username_ok);
                        mTvBankcardSmsCall.setAlpha(0.4f);
                    }
                } else if (length > 11) {
                    mTvBankcardSmsCall.setAlpha(0.4f);
                  //  mLlBankcardPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                } else if (length <= 0) {
                    mTvBankcardSmsCall.setAlpha(0.4f);
                    mLlBankcardPhone.setBackgroundResource(0);
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if (AccountHelper.isLogin()){
            User user = AccountHelper.getUser();
            if(user.getName()!=null){ mEtBankcardName.setText(unAES(user.getName()));}
        }
        //获取银行选择列表
        spGetBanktype();
    }

    private void spGetBanktype() {
        MyLog.i("GCS","获取银行下拉选择列表");
        MyApi.bankCodeList(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyLog.i("GCS","银行下拉选择列表返回Exception："+e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                MyLog.i("GCS","银行下拉选择列表返回response："+response);
                try {
                    Type type = new TypeToken<ResultBean<List<BankType>>>() {
                    }.getType();
                    ResultBean<List<BankType>> resultBean = AppOperator.createGson().fromJson(response, type);
                    int code = resultBean.getCode();
                    if (code == 200) {
                        mOptions = resultBean.getResult();

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setListener() {
        mLlBankcardName.setOnClickListener(this);
      //  mEtBankcardName.setOnClickListener(this);
        mIvBankcardNameDel.setOnClickListener(this);
        mLlBankcardPhone.setOnClickListener(this);
        mEtBankcardPhone.setOnClickListener(this);
        mIvBankcardPhoneDel.setOnClickListener(this);
        mTvBankcardSmsCall.setOnClickListener(this);
        mLlBankcardNumber.setOnClickListener(this);
        mEtBankcardNumber.setOnClickListener(this);
        mIvBankcardNumberDel.setOnClickListener(this);
        mBtBankcardSubmit.setOnClickListener(this);
        mTvBanktype.setOnClickListener(this);
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
                requestSmsCode();
                break;
            case R.id.bt_bankcard_submit:
               // showAuthbookconfirm("认证成功");
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
            case R.id.tv_banktype:
                InputMethodManager imms = (InputMethodManager) GlobalApplication.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if ( imms.isActive( ) ) {
                    imms.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

                }
                String webtitileName = "请选择银行";
                if (mOptions.size() != 0){
                    MyWebListPopuAdapter mWebAdapter = new MyWebListPopuAdapter(mOptions);
                    setPopuWindow(mOptions,webtitileName,mTvBanktype,mWebAdapter);
                }else {
                    SimplexToast.showMyToast("为获取到银行列表",GlobalApplication.getContext());
                }

                break;
            default:
                break;
        }
    }


    //银行弹出框
    private void setPopuWindow(List<BankType> stringList, String
            titileName, final TextView tv, MyWebListPopuAdapter adapter){
        final MyOptionBottomDialog optionBottomDialog = new MyOptionBottomDialog(getContext(), stringList,titileName,adapter);
        optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankType thisobj = (BankType) parent.getItemAtPosition(position);
                //隐藏值
                mTvBankcode.setText(thisobj.getBankCode().toString());
                // (SelectCustomersItem)parent.getItemAtPosition(position)
                tv.setText(thisobj.getBankName().toString());
                optionBottomDialog.dismiss();
            }
        });
    }



    private void requestSmsCode() {
            if (!mMachPhoneNum) {
                mLlBankcardPhone.setActivated(true);
                mLlBankcardPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                SimplexToast.showMyToast(R.string.login_input_username_hint_error, GlobalApplication.getContext());
                return;
            }
            if (!TDevice.hasInternet()) {
                SimplexToast.showMyToast(R.string.tip_network_error, GlobalApplication.getContext());
                return;
            }

             if (mTvBankcardSmsCall.getTag() == null) {
                mTvBankcardSmsCall.setAlpha(0.6f);
                mTvBankcardSmsCall.setTag(true);
                mTimer = new CountDownTimer(AppConfig.SMSCODE_TIME_OUT * 1000, 1000) {

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTvBankcardSmsCall.setText(String.format("%s%s%d%s",
                                getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                    }

                    @Override
                    public void onFinish() {
                        mTvBankcardSmsCall.setTag(null);
                        mTvBankcardSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                        mTvBankcardSmsCall.setAlpha(1.0f);
                    }
                }.start();
                //加密
                 String bankcardName = mEtBankcardName.getText().toString().trim();
                 String bankcardNumber = mEtBankcardNumber.getText().toString().trim();
                 String phoneNumber = mEtBankcardPhone.getText().toString().trim();
                 String pay_code = mTvBankcode.getText().toString();
                 String token = AccountHelper.getUser().getToken();
                 String identitynumber = unAES(AccountHelper.getUser().getCertno());
                 MyLog.i("GCS","name="+bankcardName);
                 MyLog.i("GCS","bankcard="+bankcardNumber);
                 MyLog.i("GCS","phone="+phoneNumber);
                 MyLog.i("GCS","idcard="+identitynumber);
                 MyLog.i("GCS","pay_code="+pay_code);
                if (TextUtils.isEmpty(pay_code)){
                    SimplexToast.showMyToast("请选择银行",GlobalApplication.getContext());
                }else {
                //1111  OSChinaApi.sendRegisterSmsCode(phoneNumber, OSChinaApi.REGISTER_INTENT, mHandler);发送短信的api
                MyApi.bankCardSendMsg(token,pay_code,bankcardName,identitynumber,bankcardNumber,phoneNumber, new StringCallback() {
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
                        MyLog.i("GCS","发送银行卡短信验证码返回Exception："+e.toString());
                        if (mTimer != null) {
                            mTimer.onFinish();
                            mTimer.cancel();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyLog.i("GCS","发送银行卡短信验证码返回response："+response);
                        Type type = new TypeToken<ResultBean>() {}.getType();
                        ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                        if(resultBean.getResult()!= null){
                            trade_no_x = resultBean.getResult().toString();
                        }

                        int code = resultBean.getCode();
                        switch (code) {
                            case 200:
                                //发送验证码成功,请求进入下一步
                                //意味着我们可以进行第二次请求了,获取phoneToken
                                //mRequestType = 2;
                                SimplexToast.showMyToast(R.string.send_sms_code_success_hint,GlobalApplication.getContext());
                                mEtBankcardSmsCode.setText(null);
                                break;
                            case 300:
                                //失败token验证失败
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                SimplexToast.showMyToast(resultBean.getMessage(),GlobalApplication.getContext());
                                break;
                            case 500:
                                //异常错误，发送验证码失败,回收timer,需重新请求发送验证码
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                SimplexToast.showMyToast(resultBean.getMessage(),GlobalApplication.getContext());
                                break;
                            default:
                                break;
                        }
                    }
                });
            }

        } else {
            SimplexToast.showMyToast(R.string.register_sms_wait_hint,GlobalApplication.getContext());
        }
    }


    private void AuthBankcardRequest() {
        String bankcardName = mEtBankcardName.getText().toString().trim();
        String bankcardNumber = mEtBankcardNumber.getText().toString().trim();
        String phoneNumber = mEtBankcardPhone.getText().toString().trim();
        String smscode = mEtBankcardSmsCode.getText().toString().trim();
        String token = AccountHelper.getUser().getToken();
        String identitynumber = AccountHelper.getUser().getCertno();
        if (TextUtils.isEmpty(trade_no_x)){
            SimplexToast.showMyToast("请先进行手机短信验证",GlobalApplication.getContext());
            return;
        }
        if (TextUtils.isEmpty(smscode)){
            SimplexToast.showMyToast("请输入手机短信验证码",GlobalApplication.getContext());
            return;
        }
        if (TextUtils.isEmpty(bankcardName)&&!mBankcardNum&&!mMachPhoneNum) {
            //showToastForKeyBord(R.string.hint_username_ok);
            SimplexToast.showMyToast("身份输入信息有误",GlobalApplication.getContext());
            return;
        }

        if (!TDevice.hasInternet()) {
            SimplexToast.showMyToast(R.string.tip_network_error,GlobalApplication.getContext());
            return;
        }

        //111 OSChinaApi.validateRegisterInfo(phoneNumber, smsCode, mHandler);注册信息提交的api
        MyApi.bankCardVerifyMsg(token,bankcardName,identitynumber,bankcardNumber,phoneNumber ,trade_no_x,smscode, new StringCallback() {
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
                MyLog.i("GCS","注册返回Exception："+e.toString());
                hideWaitDialog();
                    if (mTimer != null) {
                        mTimer.onFinish();
                        mTimer.cancel();
                    }
                showAuthbookconfirm("认证失败","重新认证");
            }

            @Override
            public void onResponse(String response, int id) {
                MyLog.i("GCS",">>注册返回成功response："+response);
                try {
                    Type type = new TypeToken<ResultBean>() {}.getType();
                    ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                    int code = resultBean.getCode();
                    switch (code) {
                        case 200://成功
                         //   SimplexToast.showMyToast(resultBean.getMessage(),GlobalApplication.getContext());
                            if (mTimer != null) {
                                mTimer.onFinish();
                                mTimer.cancel();
                            }
                            showAuthbookconfirm("认证成功","确认");
                            break;
                        case 500://失败
                            showAuthbookconfirm("认证失败","重新认证");
                            break;
                        case 300://失败token验证失败
                            showAuthbookconfirm("认证失败","重新认证");
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAuthbookconfirm(String tip, final String btnstr) {
       /* TextView title = new TextView(getContext());
        title.setText("通讯录授权");
        title.setPadding(0, 0, 0, 0);
        title.setGravity(Gravity.CENTER);
        // title.setTextColor(getResources().getColor(R.color.greenBG));
        title.setTextSize(18);*/

        View dialogview = View.inflate(getActivity(),R.layout.custom_dialog,null);
        TextView tv_title = dialogview.findViewById(R.id.dialog_tip);
        tv_title.setText(tip);
        TextView tv_link = dialogview.findViewById(R.id.read_authbook_link);
        tv_link.setVisibility(View.INVISIBLE);
        Button bt_cancle = dialogview.findViewById(R.id.btn_cancel);
        bt_cancle.setText(btnstr);
        /*final AlertDialog dlgShowBack = DialogUtil.getDialog(getContext()).setView(dialogview).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), PhoneAdressActivity.class);
                startActivity(intent);
            }
        }).create();*/
        final AlertDialog dlgShowBack = DialogUtil.getDialog(getContext()).setView(dialogview).setCancelable(true).create();

        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgShowBack.dismiss();
                if (btnstr.equals("确认")){
                    getActivity().finish();
                }

            }
        });
        dlgShowBack.show();
        dlgShowBack.getWindow().setBackgroundDrawableResource(R.drawable.rounded_search_text);
        WindowManager.LayoutParams params = dlgShowBack.getWindow().getAttributes();
        params.width = (int) TDevice.dp2px(270);
        params.height = (int) TDevice.dp2px(122);
        dlgShowBack.getWindow().setAttributes(params);
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

package com.gcs.jyfk.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.Setting;
import com.gcs.jyfk.ui.ShowUIHelper;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.RichTextParser;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.atys.SimpleBackActivity;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.ui.frags.BaseFragment;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.DialogUtil;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.RegexUtils;
import com.gcs.jyfk.utils.TDevice;
import com.gcs.jyfk.utils.VibratorUtil;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 0024 10-24.
 */
@SuppressLint("NewApi")
public class DriverCardAuthFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {


    private LinearLayout mLlIdentityName;
    private LinearLayout mLlIdentityNumber;
    private LinearLayout mLlDrivercardNumber;
    private ImageView mIvIdentityNameDel;
    private ImageView mIvIdentityNumberDel;
    private ImageView mIvDrivercardDel;
    private EditText mEtIdentityName;
    private EditText mEtIdentityNumber;
    private EditText mEtDrivercardnumber;
    private Button mBtIdentitySubmit;

    private Boolean mIsZHname = false;
    private Boolean mIsIDcardnumber = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_drivercard_auth;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity) getActivity()).setToolBarTitle(R.string.driver_card);
        view.findViewById(R.id.traceroute_rootview).setOnClickListener(this);
        mLlIdentityName = view.findViewById(R.id.ll_identity_name);
        mLlIdentityNumber = view.findViewById(R.id.ll_identity_number);
        mLlDrivercardNumber = view.findViewById(R.id.ll_drivercard_number);
        mIvIdentityNameDel = view.findViewById(R.id.iv_identity_name_del);
        mIvIdentityNumberDel = view.findViewById(R.id.iv_identity_number_del);
        mIvDrivercardDel = view.findViewById(R.id.iv_drivercard_number_del);
        mEtIdentityName = view.findViewById(R.id.et_identity_name);
        mEtIdentityNumber = view.findViewById(R.id.et_identity_number);
        mEtDrivercardnumber = view.findViewById(R.id.et_drivercard_number);
        mBtIdentitySubmit = view.findViewById(R.id.bt_identity_submit);
        setListener();
        mEtIdentityName.setOnFocusChangeListener(this);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!RichTextParser.isChinese(source.charAt(i))) {
                        //SimplexToast.showMyToast("请输入中文字符",GlobalApplication.getContext());
                        return "";
                    }
                }
                return null;
            }
        };
        mEtIdentityName.setFilters(new InputFilter[]{filter});
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
                mIsZHname = RichTextParser.checkIsZH(username);
                if (mIsZHname) {

                }
                if (username.length() > 0) {
                    //   mLlIdentityName.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvIdentityNameDel.setVisibility(View.VISIBLE);
                } else {
                    //   mLlIdentityName.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvIdentityNameDel.setVisibility(View.GONE);
                }

                String identitynumber = mEtIdentityName.getText().toString().trim();
                String pwd = mEtIdentityNumber.getText().toString().trim();
                if (!TextUtils.isEmpty(identitynumber) && !TextUtils.isEmpty(pwd)) {
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*/
                } else {
                  /*  mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));*/
                }

            }
        });

        mEtIdentityNumber.setOnFocusChangeListener(this);

        mEtIdentityNumber.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'X', 'Y', 'Z'};
                return numberChars;
            }
        });
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
                mLlIdentityNumber.setBackgroundResource(R.drawable.bg_login_input_ok);
                /*int length = s.length();
                String input = s.toString();
                if (length > 0) {
                    // mLlIdentityNumber.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvIdentityNumberDel.setVisibility(View.VISIBLE);
                } else {
                    mIvIdentityNumberDel.setVisibility(View.GONE);
                }
                try {
                    mIsIDcardnumber = IDCardUtil.IDCardValidate(input);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (mIsIDcardnumber) {
                    mLlIdentityNumber.setBackgroundResource(0);
                    String name = mEtIdentityName.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)) {
                        mLlIdentityName.setBackgroundResource(0);
                        //mBtBankcardSubmit.setEnabled(true);
                        //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                        //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        mLlIdentityName.setBackgroundResource(R.drawable.bg_login_input_error);
                       // mBtBankcardSubmit.setEnabled(false);
                        //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    }
                } else {
                    mLlIdentityNumber.setBackgroundResource(R.drawable.bg_login_input_error);
                  //  mBtBankcardSubmit.setEnabled(false);
                    //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }*/

            }
        });
        mEtDrivercardnumber.setOnFocusChangeListener(this);

        mEtDrivercardnumber.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'X', 'Y', 'Z'};
                return numberChars;
            }
        });
        mEtDrivercardnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                mLlDrivercardNumber.setBackgroundResource(R.drawable.bg_login_input_ok);
                /*int length = s.length();
                String input = s.toString();
                if (length > 0) {
                    // mLlIdentityNumber.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvIdentityNumberDel.setVisibility(View.VISIBLE);
                } else {
                    mIvIdentityNumberDel.setVisibility(View.GONE);
                }
                try {
                    mIsIDcardnumber = IDCardUtil.IDCardValidate(input);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (mIsIDcardnumber) {
                    mLlIdentityNumber.setBackgroundResource(0);
                    String name = mEtIdentityName.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)) {
                        mLlIdentityName.setBackgroundResource(0);
                        //mBtBankcardSubmit.setEnabled(true);
                        //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                        //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        mLlIdentityName.setBackgroundResource(R.drawable.bg_login_input_error);
                       // mBtBankcardSubmit.setEnabled(false);
                        //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    }
                } else {
                    mLlIdentityNumber.setBackgroundResource(R.drawable.bg_login_input_error);
                  //  mBtBankcardSubmit.setEnabled(false);
                    //  mBtBankcardSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    //  mBtBankcardSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }*/

            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        if (AccountHelper.isLogin()) {
            User user = AccountHelper.getUser();
            if (user.getName() != null) {
                mEtIdentityName.setText(unAES(user.getName()));
            }
            if (user.getCertno() != null) {
                mEtIdentityNumber.setText(unAES(user.getCertno()));
            }
        }
    }

    private void setListener() {
        mLlIdentityName.setOnClickListener(this);
        mEtIdentityName.setOnClickListener(this);
        mIvIdentityNameDel.setOnClickListener(this);
        mLlIdentityNumber.setOnClickListener(this);
        mEtIdentityNumber.setOnClickListener(this);
        mIvIdentityNumberDel.setOnClickListener(this);
        mLlDrivercardNumber.setOnClickListener(this);
        mIvDrivercardDel.setOnClickListener(this);
        mEtDrivercardnumber.setOnClickListener(this);
        mBtIdentitySubmit.setOnClickListener(this);
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
            case R.id.et_identity_name:
                mEtIdentityNumber.clearFocus();
                mEtDrivercardnumber.clearFocus();
                mEtIdentityName.setFocusableInTouchMode(true);
                mEtIdentityName.requestFocus();
                break;
            case R.id.et_identity_number:
                mEtIdentityName.clearFocus();
                mEtDrivercardnumber.clearFocus();
                mEtIdentityNumber.setFocusableInTouchMode(true);
                mEtIdentityNumber.requestFocus();
                break;
            case R.id.et_drivercard_number:
                mEtIdentityName.clearFocus();
                mEtIdentityNumber.clearFocus();
                mEtDrivercardnumber.setFocusableInTouchMode(true);
                mEtDrivercardnumber.requestFocus();
                break;
            case R.id.bt_identity_submit:
                AuthCreditCardRequest();
                break;

            case R.id.iv_identity_name_del:
                mEtIdentityName.setText(null);
                break;
            case R.id.iv_identity_number_del:
                mEtIdentityNumber.setText(null);
                break;
            case R.id.iv_drivercard_number_del:
                mEtDrivercardnumber.setText(null);
                break;
            default:
                break;
        }
    }

    private void AuthCreditCardRequest() {

        String tempUsername = mEtIdentityName.getText().toString().trim();
        String tempIdcard = mEtIdentityNumber.getText().toString().trim();
        String tempDrivercard = mEtDrivercardnumber.getText().toString().trim();
        if (!TextUtils.isEmpty(mEtIdentityName.getText().toString().trim())) {
            if (mIsIDcardnumber) {
                VibratorUtil.Vibrate(getActivity(), 100);
                mEtIdentityName.setFocusableInTouchMode(false);
                mEtIdentityName.clearFocus();
                mEtIdentityNumber.requestFocus();
                mEtIdentityNumber.setFocusableInTouchMode(true);
                mLlIdentityNumber.setBackgroundResource(R.drawable.bg_login_input_error);
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
            MyApi.driverCard(token, tempUsername, getAES(tempIdcard), tempDrivercard, new StringCallback() {
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
                    MyLog.i("GCS", "驾驶证Exception：" + e);
                }

                @Override
                public void onResponse(String response, int id) {
                    hideWaitDialog();
                    MyLog.i("GCS", "驾驶证response：" + response);
                    try {
                        Type type = new TypeToken<ResultBean>() {
                        }.getType();
                        ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                        int code = resultBean.getCode();
                        switch (code) {

                            case 200://授权url获取成功
                                MyLog.i("GCS", "跳转到回调url：" + response);

                                showAuthbookconfirm("认证成功", "确认");
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

    private void showAuthbookconfirm(String tip, final String btnstr) {
       /* TextView title = new TextView(getContext());
        title.setText("通讯录授权");
        title.setPadding(0, 0, 0, 0);
        title.setGravity(Gravity.CENTER);
        // title.setTextColor(getResources().getColor(R.color.greenBG));
        title.setTextSize(18);*/

        View dialogview = View.inflate(getActivity(), R.layout.custom_dialog, null);
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
                if (btnstr.equals("确认")) {
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
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();
        switch (id) {
            case R.id.et_identity_name:
                if (hasFocus) {
                    mLlIdentityName.setActivated(true);
                    mLlIdentityNumber.setActivated(false);
                    mLlDrivercardNumber.setActivated(false);
                    mIvIdentityNameDel.setVisibility(View.VISIBLE);
                    mIvIdentityNumberDel.setVisibility(View.GONE);
                    mIvDrivercardDel.setVisibility(View.GONE);
                }
                break;
            case R.id.et_identity_number:
                if (hasFocus) {
                    mLlIdentityNumber.setActivated(true);
                    mLlIdentityName.setActivated(false);
                    mLlDrivercardNumber.setActivated(false);
                    mIvIdentityNameDel.setVisibility(View.GONE);
                    mIvDrivercardDel.setVisibility(View.GONE);
                    mIvIdentityNumberDel.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.et_drivercard_number:
                if (hasFocus) {
                    mLlIdentityName.setActivated(false);
                    mLlIdentityNumber.setActivated(false);
                    mLlDrivercardNumber.setActivated(true);
                    mIvDrivercardDel.setVisibility(View.VISIBLE);
                    mIvIdentityNameDel.setVisibility(View.GONE);
                    mIvIdentityNumberDel.setVisibility(View.GONE);
                }
                break;
        }
    }

}

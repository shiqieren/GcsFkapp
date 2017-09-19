package com.gcs.fengkong.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.ShowUIHelper;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.RichTextParser;
import com.gcs.fengkong.ui.account.bean.User;
import com.gcs.fengkong.ui.api.MyApi;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.bean.base.ResultBean;
import com.gcs.fengkong.ui.frags.BaseFragment;
import com.gcs.fengkong.ui.widget.SimplexToast;
import com.gcs.fengkong.utils.AppOperator;
import com.gcs.fengkong.utils.IDCardUtil;
import com.gcs.fengkong.utils.MyLog;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.text.ParseException;

import okhttp3.Call;

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

    private Boolean mIsZHname = false;
    private Boolean mIsIDcardnumber = false;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_identity_auth;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.identity_string);
        view.findViewById(R.id.traceroute_rootview).setOnClickListener(this);
        mLlIdentityName = view.findViewById(R.id.ll_identity_name);
        mLlIdentityNumber = view.findViewById(R.id.ll_identity_number);
        mIvIdentityNameDel = (ImageView) view.findViewById(R.id.iv_identity_name_del);
        mIvIdentityNumberDel = (ImageView) view.findViewById(R.id.iv_identity_number_del);
       mEtIdentityName = view.findViewById(R.id.et_identity_name);
       mEtIdentityNumber = view.findViewById(R.id.et_identity_number);
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
                if(mIsZHname){

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
                if (!TextUtils.isEmpty(identitynumber)&&!TextUtils.isEmpty(pwd)) {
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
                char[] numberChars = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'X','Y','Z' };
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

                int length = s.length();
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
                }

            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        if (AccountHelper.isLogin()){
            User user = AccountHelper.getUser();
            mEtIdentityName.setText(user.getName());
           mEtIdentityNumber.setText(user.getCertno());
        }
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
            case R.id.traceroute_rootview:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
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
        String tempUsername = mEtIdentityName.getText().toString().trim();
        String tempIdcard = mEtIdentityNumber.getText().toString().trim();
        if(!TextUtils.isEmpty(mEtIdentityName.getText().toString().trim())){
            if (!mIsIDcardnumber){
                SimplexToast.showMyToast("身份证格式有误", GlobalApplication.getContext());
            }
        }else {
            SimplexToast.showMyToast("姓名不能为空", GlobalApplication.getContext());
        }

        if (!TextUtils.isEmpty(tempUsername)&&AccountHelper.isLogin()&&!TextUtils.isEmpty(tempIdcard)){

            //登录成功,请求数据进入用户个人中心页面
            User user = AccountHelper.getUser();
            String token =  user.getToken();
            String phone = user.getPhone();
            MyApi.authzhima(token, getAES("18018746184"), getAES(tempIdcard), tempUsername, "", new StringCallback() {
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
        }

    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();

        if (id == R.id.et_identity_name) {
            if (hasFocus) {
                mLlIdentityName.setActivated(true);
                mLlIdentityNumber.setActivated(false);
                mIvIdentityNameDel.setVisibility(View.VISIBLE);
                mIvIdentityNumberDel.setVisibility(View.GONE);
            }
        } else {
            if (hasFocus) {
                mLlIdentityNumber.setActivated(true);
                mLlIdentityName.setActivated(false);
                mIvIdentityNameDel.setVisibility(View.GONE);
                mIvIdentityNumberDel.setVisibility(View.VISIBLE);
            }
        }
    }
}

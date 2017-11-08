package com.gcs.jyfk.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.View;
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
public class CreditCardAuthFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private CheckBox mCbAgreeAuthbook;
    private LinearLayout mLlAuthUsername;
    private LinearLayout mLlAuthPassword;
    private EditText mEtAuthUsername;
    private EditText mEtAuthPassword;
    private Button mBtAuthSubmit;
    private TextView mTvauthbook;
    private ImageView mIvAuthUsernaneDel;
    private ImageView mIvAuthPasswordDel;
    private ProgressDialog mDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_creditcard_auth;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        String message = getResources().getString(R.string.progress_submit);
        if (mDialog == null) {
            mDialog = DialogUtil.getProgressDialog(getActivity(), message, false);//DialogHelp.getWaitDialog(this, message);
        }

        ((SimpleBackActivity) getActivity()).setToolBarTitle(R.string.credit_card);
        view.findViewById(R.id.traceroute_rootview).setOnClickListener(this);
        mLlAuthUsername = view.findViewById(R.id.ll_auth_username);
        mLlAuthPassword = view.findViewById(R.id.ll_auth_password);
        mEtAuthUsername = view.findViewById(R.id.et_auth_username);
        mEtAuthPassword = view.findViewById(R.id.et_auth_password);
        mTvauthbook = view.findViewById(R.id.tv_authbook);
        mBtAuthSubmit = view.findViewById(R.id.bt_auth_submit);
        mIvAuthUsernaneDel = view.findViewById(R.id.iv_auth_username_del);
        mIvAuthPasswordDel = view.findViewById(R.id.iv_auth_password_del);
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
                if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(name) && mCbAgreeAuthbook.isChecked()) {
                   /* mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));*/
                } else {
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
        mTvauthbook.setOnClickListener(this);
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
            case R.id.tv_authbook:
                ShowUIHelper.openInternalBrowser(getActivity(), Setting.getServerUrl(GlobalApplication.getContext()) + "wind-phone/authOperatorAgreement.jsp");
                break;
            default:
                break;
        }
    }

    private void AuthRequest() {
        String username = mEtAuthUsername.getText().toString().trim();

        if (RegexUtils.isEmail(username)) {
            if (!mCbAgreeAuthbook.isChecked()) {
                SimplexToast.showMyToast("需勾选授权协议哦!", GlobalApplication.getContext());
            } else {
                String servicePwd = mEtAuthPassword.getText().toString().trim();
                if (TextUtils.isEmpty(servicePwd) || servicePwd.length() < 6 || servicePwd.length() > 18) {

                    SimplexToast.showMyToast("请输入有效的密码", GlobalApplication.getContext());
                    return;
                }

                mDialog.show();


            }
        } else {
            SimplexToast.showMyToast("邮箱有误.请重新填写!", GlobalApplication.getContext());
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

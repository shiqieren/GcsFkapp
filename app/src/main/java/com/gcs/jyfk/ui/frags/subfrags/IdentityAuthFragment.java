package com.gcs.jyfk.ui.frags.subfrags;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.ShowUIHelper;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.RichTextParser;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.atys.ResultActivity;
import com.gcs.jyfk.ui.atys.SimpleBackActivity;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.ui.frags.BaseFragment;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.UIUtils;
import com.gcs.jyfk.utils.VibratorUtil;
import com.google.gson.reflect.TypeToken;
import com.megvii.idcardlib.IDCardScanActivity;
import com.megvii.idcardlib.util.Util;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.M;

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
    private ImageView mIvidcardfront;
    private ImageView mIvidcardreverse;
    private Button mBtIdentitySubmit;

    private Button selectBtn;
    boolean isVertical;
    private LinearLayout contentRel;
    private LinearLayout barLinear;
    private TextView WarrantyText;
    private ProgressBar WarrantyBar;
    private Button againWarrantyBtn;

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

        contentRel = view.findViewById(R.id.traceroute_rootview);
        barLinear = view.findViewById(R.id.loading_layout_barLinear);
        WarrantyText = view.findViewById(R.id.loading_layout_WarrantyText);
        WarrantyBar = view.findViewById(R.id.loading_layout_WarrantyBar);
        againWarrantyBtn = view.findViewById(R.id.loading_layout_againWarrantyBtn);
        selectBtn = view.findViewById(R.id.loading_layout_isVerticalBtn);

        mLlIdentityName = view.findViewById(R.id.ll_identity_name);
        mLlIdentityNumber = view.findViewById(R.id.ll_identity_number);
        mIvIdentityNameDel = (ImageView) view.findViewById(R.id.iv_identity_name_del);
        mIvIdentityNumberDel = (ImageView) view.findViewById(R.id.iv_identity_number_del);
        mEtIdentityName = view.findViewById(R.id.et_identity_name);
        mEtIdentityNumber = view.findViewById(R.id.et_identity_number);
        mBtIdentitySubmit = view.findViewById(R.id.bt_identity_submit);
        mIvidcardfront = view.findViewById(R.id.iv_idcard_front);
        mIvidcardreverse = view.findViewById(R.id.iv_idcard_reverse);
        if (isVertical)
            selectBtn.setText("vertical");
        else
            selectBtn.setText("horizontal");
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


    }

    @Override
    protected void initData() {
        super.initData();
        if (AccountHelper.isLogin()){
            User user = AccountHelper.getUser();
            if(user.getName()!=null){ mEtIdentityName.setText(unAES(user.getName()));}
            if (user.getCertno()!=null){ mEtIdentityNumber.setText(unAES(user.getCertno()));}
        }
    }

    private void setListener() {
        contentRel.setOnClickListener(this);
        mLlIdentityName.setOnClickListener(this);
        mEtIdentityName.setOnClickListener(this);
        mIvIdentityNameDel.setOnClickListener(this);
        mLlIdentityNumber.setOnClickListener(this);
        mEtIdentityNumber.setOnClickListener(this);
        mIvIdentityNumberDel.setOnClickListener(this);
        mIvidcardfront.setOnClickListener(this);
        mIvidcardreverse.setOnClickListener(this);
        mBtIdentitySubmit.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
    }

    /**
     * 上传图片
     */
    private void network() {
        contentRel.setVisibility(View.GONE);
        barLinear.setVisibility(View.VISIBLE);
        againWarrantyBtn.setVisibility(View.GONE);
        WarrantyText.setText("正在联网授权中...");
        WarrantyBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
               // Manager manager = new Manager(LoadingActivity.this);
                Manager manager = new Manager(getActivity());
                //IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(LoadingActivity.this);
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(
                        getActivity());
                manager.registerLicenseManager(idCardLicenseManager);
                String uuid = "13213214321424";
                manager.takeLicenseFromNetwork(uuid);
                String contextStr = manager.getContext(uuid);
                Log.w("ceshi", "contextStr====" + contextStr);

                Log.w("ceshi",
                        "idCardLicenseManager.checkCachedLicense()===" + idCardLicenseManager.checkCachedLicense());
                if (idCardLicenseManager.checkCachedLicense() > 0)
                    UIAuthState(true);
                else
                    UIAuthState(false);
            }
        }).start();
    }

    private void UIAuthState(final boolean isSuccess) {
        UIUtils.runOnUIThread(new Runnable() {
            public void run() {
                authState(isSuccess);
            }
        });
    }

    private void authState(boolean isSuccess) {
        if (isSuccess) {
            barLinear.setVisibility(View.GONE);
            WarrantyBar.setVisibility(View.GONE);
            againWarrantyBtn.setVisibility(View.GONE);
            contentRel.setVisibility(View.VISIBLE);
        } else {
            barLinear.setVisibility(View.VISIBLE);
            WarrantyBar.setVisibility(View.GONE);
            againWarrantyBtn.setVisibility(View.VISIBLE);
            contentRel.setVisibility(View.GONE);
            WarrantyText.setText("联网授权失败！请检查网络或找服务商");
        }
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
            case R.id.loading_layout_againWarrantyBtn:
                network();
                break;
            case R.id.loading_layout_isVerticalBtn:
                isVertical = !isVertical;
                initData();
                break;
            case R.id.iv_idcard_front: {
                SimplexToast.showMyToast("正面",GlobalApplication.getContext());
                requestCameraPerm(0);
            }
            break;
            case R.id.iv_idcard_reverse: {
                SimplexToast.showMyToast("反面",GlobalApplication.getContext());
                requestCameraPerm(1);
                break;
            }
            default:
                break;
        }
    }

    int mSide = 0;
    private void requestCameraPerm(int side) {
        mSide = side;
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        EXTERNAL_STORAGE_REQ_CAMERA_CODE);
            } else {
                enterNextPage(side);
            }
        } else {
            enterNextPage(side);
        }
    }

    private void enterNextPage(int side){
        Intent intent = new Intent(getActivity(), IDCardScanActivity.class);
        intent.putExtra("side", side);
        intent.putExtra("isvertical", isVertical);
        startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
    }

    public static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;
    private void AuthIdentityRequest() {
        String tempUsername = mEtIdentityName.getText().toString().trim();
        String tempIdcard = mEtIdentityNumber.getText().toString().trim();
        if(!TextUtils.isEmpty(mEtIdentityName.getText().toString().trim())){
            if (mIsIDcardnumber){
                VibratorUtil.Vibrate(getActivity(), 100);
                mEtIdentityName.setFocusableInTouchMode(false);
                mEtIdentityName.clearFocus();
                mEtIdentityNumber.requestFocus();
                mEtIdentityNumber.setFocusableInTouchMode(true);
                mLlIdentityNumber.setBackgroundResource(R.drawable.bg_login_input_error);
                SimplexToast.showMyToast("身份证格式有误", GlobalApplication.getContext());
                return;
            }
        }else {
            SimplexToast.showMyToast("姓名不能为空", GlobalApplication.getContext());
            return;
        }

        if (!TextUtils.isEmpty(tempUsername)&&AccountHelper.isLogin()&&!TextUtils.isEmpty(tempIdcard)){

            //登录成功,请求数据进入用户个人中心页面
            User user = AccountHelper.getUser();
            String token =  user.getToken();
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
                    MyLog.i("GCS","身份认证返回Exception："+e);
                }

                @Override
                public void onResponse(String response, int id) {
                    hideWaitDialog();
                    MyLog.i("GCS","登录返回response："+response);
                    try {
                        Type type = new TypeToken<ResultBean>() {}.getType();
                        ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                        int code = resultBean.getCode();
                        switch (code) {

                            case 200://授权url获取成功
                                MyLog.i("GCS","跳转到回调url："+response);
                                String url =  resultBean.getResult().toString();
                                MyLog.i("GCS","授权按钮点击，打开webview："+url);
                                ShowUIHelper.openInternalBrowser(getActivity(), url);

                                break;
                            case 600://已经授权过,成功更新了用户芝麻数据
                                SimplexToast.showMyToast(resultBean.getMessage(),GlobalApplication.getContext());
                                getActivity().finish();
                                break;
                            case 300://账户问题
                                SimplexToast.showMyToast(resultBean.getMessage(),GlobalApplication.getContext());

                                break;
                            case 500://失败
                                SimplexToast.showMyToast(resultBean.getMessage(),GlobalApplication.getContext());
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
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_REQ_CAMERA_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {// Permission Granted

                Util.showToast(getActivity(), "获取相机权限失败");
            } else
                enterNextPage(mSide);
        }
    }


    private static final int INTO_IDCARDSCAN_PAGE = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTO_IDCARDSCAN_PAGE && resultCode == RESULT_OK) {
            Intent intent = new Intent(getActivity(), ResultActivity.class);
            intent.putExtra("side", data.getIntExtra("side", 0));
            intent.putExtra("idcardImg", data.getByteArrayExtra("idcardImg"));
            if (data.getIntExtra("side", 0) == 0) {
                intent.putExtra("portraitImg", data.getByteArrayExtra("portraitImg"));
            }
            startActivity(intent);
        }
    }

}

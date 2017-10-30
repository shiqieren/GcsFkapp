package com.gcs.jyfk.ui.frags;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bqs.crawler.cloud.sdk.BqsCrawlerCloudSDK;
import com.bqs.crawler.cloud.sdk.BqsParams;
import com.bqs.crawler.cloud.sdk.ServiceId;
import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.Setting;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.atys.LoginActivity;
import com.gcs.jyfk.ui.account.bean.UploadContacts;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.account.bean.VerifyStatus;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.baiqishiauthpager.ViewLoginActivity;
import com.gcs.jyfk.ui.bean.ContactBean;
import com.gcs.jyfk.ui.bean.SimpleBackPage;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.ui.faceid.LoadingActivity;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.ui.widget.statusbar.StatusBarCompat;
import com.gcs.jyfk.utils.ActivityManager;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.DialogUtil;
import com.gcs.jyfk.ui.ShowUIHelper;
import com.gcs.jyfk.utils.FastOneClick;
import com.gcs.jyfk.utils.GetContactsUtil;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.TDevice;
import com.gcs.jyfk.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by lyw on 15/9/22.
 */
public class StartPagerFragment extends BaseFragment implements View.OnClickListener,EasyPermissions.PermissionCallbacks{

    private boolean isHide = false;
    private CardView mCv_alipay ;
    private CardView mCv_bankcard;
    private CardView mCv_contact  ;
    private CardView mCv_identity;
    private CardView mCv_jd ;
    private CardView mCv_operator;
    private CardView mCv_taobao;
    private CardView mCv_zhima;
    private CardView mCv_drivercard;
    private CardView mCv_creditcard;
    private CardView mCv_email;
    private ImageView mIvLogoSetting;
    private TextView mTv_name;
    private LinearLayout mLlidentityiv;
    private LinearLayout mLlbankcardiv;
    private LinearLayout mLlzhimaiv;
    private LinearLayout mLldrivercardiv;
    private LinearLayout mLlcreditcardiv;
    private LinearLayout mLlalipayiv;
    private LinearLayout mLltaobaoiv;
    private LinearLayout mLljdiv;
    private LinearLayout mLloperatoriv;
    private LinearLayout mLlcontactiv;
    private LinearLayout mLlemailiv;
    private VerifyStatus status;
    @Override
    public void onResume() {
        super.onResume();
        if (AccountHelper.isLogin()) {
            User user = AccountHelper.getUser();
            sendRequestData(user);
            MyLog.i("GCS", "网络实时的User，目前先用本地获取的User");
            updateView(user);
        } else {
            hideView();
        }

    }

    /**
     * requestData
     */
    private void sendRequestData(final User user) {
        if (TDevice.hasInternet() && AccountHelper.isLogin()){
                MyLog.i("GCS", "每次到首页显示时就去获取个人认证信息并赋值给User,在响应结果中更新主页");
                MyApi.getverifyStatus(user.getToken(), new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    MyLog.i("GCS","获取返回status信息Exception："+e.toString());
                }

                @Override
                public void onResponse(String response, int id) {
                    MyLog.i("GCS","获取返回status成功信息："+response);
                    try {
                        Type type = new TypeToken<ResultBean<VerifyStatus>>() {}.getType();
                        ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                        int code = resultBean.getCode();
                        if (code == 200) {
                            status = (VerifyStatus) resultBean.getResult();
                            String loginid = user.getUserid();
                            String sendid = String.valueOf(status.getPhone_user_id());
                            if (loginid.equals(sendid)){
                                user.setPhone(status.getPhone());
                                user.setCertno(status.getCertno());
                                user.setName(status.getName());

                                changeLocalUser(status);
                                if(status.getName()!=null){
                                    mTv_name.setText("hi,"+unAES(status.getName()));
                                }else {
                                    mTv_name.setText("该用户尚未进行身份认证");
                                }
                                /*if(status.getIdentity().equals("1")){
                                    mLlidentityiv.setVisibility(View.VISIBLE);
                                }else {
                                    mLlidentityiv.setVisibility(View.GONE);
                                }*/
                                if(status.getIdentity().equals("1")){
                                    mLlzhimaiv.setVisibility(View.VISIBLE);
                                }else {
                                    mLlzhimaiv.setVisibility(View.GONE);
                                }
                                if(status.getBankcard().equals("1")){
                                    mLlbankcardiv.setVisibility(View.VISIBLE);
                                }else {
                                    mLlbankcardiv.setVisibility(View.GONE);
                                }

                                if(status.getAlipay().equals("1")){
                                    mLlalipayiv.setVisibility(View.VISIBLE);
                                }else {
                                    mLlalipayiv.setVisibility(View.GONE);
                                }

                                if(status.getTaobao().equals("1")){
                                    mLltaobaoiv.setVisibility(View.VISIBLE);
                                }else {
                                    mLltaobaoiv.setVisibility(View.GONE);
                                }

                                if(status.getJd().equals("1")){
                                    mLljdiv.setVisibility(View.VISIBLE);
                                }else {
                                    mLljdiv.setVisibility(View.GONE);
                                }

                                if(status.getOperator().equals("1")){
                                    mLloperatoriv.setVisibility(View.VISIBLE);
                                }else {
                                    mLloperatoriv.setVisibility(View.GONE);
                                }

                                if(status.getContact().equals("1")){
                                    mLlcontactiv.setVisibility(View.VISIBLE);
                                }else {
                                    mLlcontactiv.setVisibility(View.GONE);
                                }
                                if(status.getDriver().equals("1")){
                                    mLldrivercardiv.setVisibility(View.VISIBLE);
                                }else {
                                    mLldrivercardiv.setVisibility(View.GONE);
                                }
                                if(status.getCreditcard().equals("1")){
                                    mLlcreditcardiv.setVisibility(View.VISIBLE);
                                }else {
                                    mLlcreditcardiv.setVisibility(View.GONE);
                                }
                                if(status.getEmail().equals("1")){
                                    mLlemailiv.setVisibility(View.VISIBLE);
                                }else {
                                    mLlemailiv.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            String message = resultBean.getMessage();
                            if (code == 500) {

                            }else if (code == 300){
                                ShowUIHelper.clearAppCache(false);
                                AccountHelper.logoutauto(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                                SimplexToast.showMyToast(message,GlobalApplication.getContext());
                                LoginActivity.show(getContext());
                                ActivityManager.getActivityManager().finishAllActivity();
                            }

                            //更新失败应该是不进行任何的本地操作
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    private void changeLocalUser(VerifyStatus status) {
        if (AccountHelper.isLogin()){
            User user = AccountHelper.getUser();
            user.getAuthstate().setAuth_identity(status.getIdentity());
            user.getAuthstate().setAuth_bankcard(status.getBankcard());
            user.getAuthstate().setAuth_alipay(status.getAlipay());
            user.getAuthstate().setAuth_taobao(status.getTaobao());
            user.getAuthstate().setAuth_jd(status.getJd());
            user.getAuthstate().setAuth_operator(status.getOperator());
            user.getAuthstate().setAuth_contact(status.getContact());
            user.getAuthstate().setAuth_drivercard(status.getDriver());
            user.getAuthstate().setAuth_creditcard(status.getCreditcard());
            user.getAuthstate().setAuth_email(status.getEmail());
            AccountHelper.updateUserCache(user);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarCompat.translucentStatusBar(getActivity(), isHide);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.start_page;
    }

    @Override
    public void initView(View view) {
        mTv_name  = view.findViewById(R.id.tv_name);
        mCv_alipay  = view.findViewById(R.id.cv_alipay);
        mCv_bankcard  = view.findViewById(R.id.cv_bankcard);
        mCv_contact  = view.findViewById(R.id.cv_contact);
        mCv_identity  = view.findViewById(R.id.cv_identity);
        mCv_jd  = view.findViewById(R.id.cv_jd);
        mCv_operator  = view.findViewById(R.id.cv_operator);
        mCv_taobao  = view.findViewById(R.id.cv_taobao);
        mCv_zhima  = view.findViewById(R.id.cv_zhima);
        mCv_drivercard  = view.findViewById(R.id.cv_drivercard);
        mCv_creditcard = view.findViewById(R.id.cv_credit_card);
        mCv_email = view.findViewById(R.id.cv_email);
        mIvLogoSetting = view.findViewById(R.id.iv_logo_setting);

        //状态勾选
        mLlidentityiv = view.findViewById(R.id.ll_suauth_identity_iv);
        mLlbankcardiv = view.findViewById(R.id.ll_suauth_bankcard_iv);
        mLlzhimaiv = view.findViewById(R.id.ll_suauth_zhima_iv);
        mLlalipayiv = view.findViewById(R.id.ll_suauth_alipay_iv);
        mLltaobaoiv = view.findViewById(R.id.ll_suauth_taobao_iv);
        mLljdiv = view.findViewById(R.id.ll_suauth_jd_iv);
        mLloperatoriv = view.findViewById(R.id.ll_suauth_operator_iv);
        mLlcontactiv = view.findViewById(R.id.ll_suauth_contact_iv);
        mLldrivercardiv = view.findViewById(R.id.ll_suauth_driver_iv);
        mLlcreditcardiv = view.findViewById(R.id.ll_suauth_creditcard_iv);
        mLlcreditcardiv = view.findViewById(R.id.ll_suauth_email_iv);
        view.findViewById(R.id.iv_avatar).setOnClickListener(this);
    }
    @Override
    public void initData() {
        requestUserCache();
        setListener();
    }
    /**
     * if user isLogin,request user cache,
     * And then request user info and update user info
     */
    private void requestUserCache() {
        if (AccountHelper.isLogin()) {
            User user = AccountHelper.getUser();
            sendRequestData(user);
            updateView(user);
        } else {
            hideView();
        }
    }
    private void setListener() {
        mCv_alipay.setOnClickListener(this);
        mCv_bankcard.setOnClickListener(this);
        mCv_contact.setOnClickListener(this);
        mCv_identity.setOnClickListener(this);
        mCv_jd.setOnClickListener(this);
        mCv_operator.setOnClickListener(this);
        mCv_taobao.setOnClickListener(this);
        mCv_drivercard.setOnClickListener(this);
        mCv_creditcard.setOnClickListener(this);
        mCv_email.setOnClickListener(this);
        mCv_zhima.setOnClickListener(this);
        mIvLogoSetting.setOnClickListener(this);

    }

    /*需要作登录判断
      if (!AccountHelper.isLogin()) {
                  LoginActivity.show(getActivity());
                  return;
              }
      */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(getActivity(), ViewLoginActivity.class);
        switch (id) {
            case R.id.cv_identity:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                        return;
                    }
                    Intent i = new Intent(getActivity(), LoadingActivity.class);
                    startActivity(i);
                   // ShowUIHelper.showIdentityAuth(getActivity());
                    /*if (AccountHelper.isAuth()){
                        SimplexToast.showMyToast("身份信息已认证",GlobalApplication.getContext());
                    }else {
                        ShowUIHelper.showIdentityAuth(getActivity());
                    }*/
                }

                break;
            case R.id.cv_zhima:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                        return;
                    }else if (!AccountHelper.isAuth()){
                        DialogUtil.getConfirmDialog(getActivity(), "您尚未对帐号进行认证，是否立即进行认证？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShowUIHelper.showIdentityAuth(getActivity());
                            }
                        }).show();
                        return;
                    }
                    if (AccountHelper.isAuth()){
                        SimplexToast.showMyToast("身份信息已认证",GlobalApplication.getContext());
                    }else {
                        ShowUIHelper.showZhimaAuth(getActivity());
                    }

                 }
                break;
            case R.id.cv_bankcard:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                        return;
                    }else if (!AccountHelper.isAuth()){
                        AlertDialog dialog = DialogUtil.getConfirmDialog(getActivity(), "您尚未对帐号身份进行认证，是否立即进行认证？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShowUIHelper.showIdentityAuth(getActivity());
                            }
                        }).show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        return;
                    }
                    configBqsParams();
                    if (status!=null){
                        if (status.getBankcard().equals("1")){
                            SimplexToast.showMyToast("银行卡已认证",GlobalApplication.getContext());
                        }else {
                            ShowUIHelper.showBankAuth(getActivity());
                        }
                    }
                }


                break;
            case R.id.cv_drivercard:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                        return;
                    }else if (!AccountHelper.isAuth()){
                        AlertDialog dialog = DialogUtil.getConfirmDialog(getActivity(), "您尚未对帐号身份进行认证，是否立即进行认证？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShowUIHelper.showIdentityAuth(getActivity());
                            }
                        }).show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        return;
                    }
                    if (status!=null){
                        if (status.getDriver().equals("1")){
                            SimplexToast.showMyToast("驾驶证已认证",GlobalApplication.getContext());
                        }else {
                            ShowUIHelper.showDrivercardAuth(getActivity());
                        }
                    }
                }


                break;
            case R.id.cv_credit_card:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                        return;
                    }else if (!AccountHelper.isAuth()){
                        AlertDialog dialog = DialogUtil.getConfirmDialog(getActivity(), "您尚未对帐号身份进行认证，是否立即进行认证？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShowUIHelper.showIdentityAuth(getActivity());
                            }
                        }).show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        return;
                    }
                    if (status!=null){
                        if (status.getCreditcard().equals("1")){
                            SimplexToast.showMyToast("信用卡已认证",GlobalApplication.getContext());
                        }else {
                            ShowUIHelper.showCreditcardAuth(getActivity());
                        }
                    }
                }


                break;
            case R.id.cv_email:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                        return;
                    }else if (!AccountHelper.isAuth()){
                        AlertDialog dialog = DialogUtil.getConfirmDialog(getActivity(), "您尚未对帐号身份进行认证，是否立即进行认证？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShowUIHelper.showIdentityAuth(getActivity());
                            }
                        }).show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        return;
                    }
                    ShowUIHelper.showEmailAuth(getActivity());
                    /*if (status!=null){
                        if (status.getEmail().equals("1")){
                            SimplexToast.showMyToast("邮箱已认证",GlobalApplication.getContext());
                        }else {
                            ShowUIHelper.showEmailAuth(getActivity());
                        }
                    }*/
                }


                break;

            case R.id.cv_alipay:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                        return;
                    }else if (!AccountHelper.isAuth()){
                        AlertDialog dialog = DialogUtil.getConfirmDialog(getActivity(), "您尚未对帐号身份进行认证，是否立即进行认证？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShowUIHelper.showIdentityAuth(getActivity());
                            }
                        }).show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        return;
                    }
                    configBqsParams();
                    intent.putExtra(ViewLoginActivity.PARAMS_DATA_TYPE, ServiceId.ALIPAY_SERVICE_ID);
                    if (status!=null){
                        if (status.getAlipay().equals("1")){
                            SimplexToast.showMyToast("支付宝已认证",GlobalApplication.getContext());
                        }else {
                            startActivity(intent);
                        }
                    }

                }


                //ShowUIHelper.showAlipayAuth(getActivity());
                break;
            case R.id.cv_taobao:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                        return;
                    }else if (!AccountHelper.isAuth()){
                        AlertDialog dialog = DialogUtil.getConfirmDialog(getActivity(), "您尚未对帐号身份进行认证，是否立即进行认证？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShowUIHelper.showIdentityAuth(getActivity());
                            }
                        }).show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        return;
                    }
                    configBqsParams();
                    intent.putExtra(ViewLoginActivity.PARAMS_DATA_TYPE, ServiceId.TB_SERVICE_ID);
                    if (status!=null){
                        if (status.getTaobao().equals("1")){
                            SimplexToast.showMyToast("淘宝已认证",GlobalApplication.getContext());
                        }else {
                            startActivity(intent);
                        }
                    }

                }

               // ShowUIHelper.showTaobaoAuth(getActivity());
                break;
            case R.id.cv_jd:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                        return;
                    }else if (!AccountHelper.isAuth()){
                        AlertDialog dialog = DialogUtil.getConfirmDialog(getActivity(), "您尚未对帐号身份进行认证，是否立即进行认证？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShowUIHelper.showIdentityAuth(getActivity());
                            }
                        }).show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        return;
                    }
                    configBqsParams();
                    intent.putExtra(ViewLoginActivity.PARAMS_DATA_TYPE, ServiceId.JD_SERVICE_ID);
                    if (status!=null){
                        if (status.getJd().equals("1")){
                            SimplexToast.showMyToast("京东已认证",GlobalApplication.getContext());
                        }else {
                            startActivity(intent);
                        }
                    }

                }

               // ShowUIHelper.showJdAuth(getActivity());
                break;
            case R.id.cv_operator:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                        return;
                    }else if (!AccountHelper.isAuth()){
                        AlertDialog dialog = DialogUtil.getConfirmDialog(getActivity(), "您尚未对帐号身份进行认证，是否立即进行认证？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShowUIHelper.showIdentityAuth(getActivity());
                            }
                        }).show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(UIUtils.getColor(R.color.base_app_color));
                        return;
                    }
                    configBqsParams();
                    if (status!=null){
                        if (status.getOperator().equals("1")){
                            SimplexToast.showMyToast("运营商已认证",GlobalApplication.getContext());
                        }else {
                            ShowUIHelper.showOperatorAuth(getActivity());
                        }
                    }


                }

                break;
            case R.id.cv_contact:
                if (FastOneClick.isFastClick()){
                    if (!AccountHelper.isLogin()) {
                        ShowUIHelper.showLoginActivity(getActivity());
                    }else {
                        if (status!=null){
                            if (status.getContact().equals("1")){
                                SimplexToast.showMyToast("通讯录已授权",GlobalApplication.getContext());
                            }else {
                                showAuthbookconfirm();
                            }
                        }
                    }
                   /* if (mLlcontactiv.getVisibility() == View.VISIBLE){
                        SimplexToast.showMyToast("通讯录已认证",GlobalApplication.getContext());
                    }else {
                        showAuthbookconfirm();
                    }*/
                }

                break;
            case R.id.iv_logo_setting:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                ShowUIHelper.showSimpleBack(getActivity(), SimpleBackPage.SETTING);
                break;
            case R.id.iv_avatar:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
               // ShowUIHelper.showSimpleBack(getActivity(), SimpleBackPage.PERSONAL_DATA);

                break;

            default:
                break;
        }
    }
    /*设置该登录用户的身份认证信息*/
    private void configBqsParams() {
        User user = AccountHelper.getUser();
        BqsParams params = new BqsParams();
        params.setPartnerId("guanchesuo");
        if(user.getName()!=null){
            params.setName(unAES(user.getName()));
        }else {
            MyLog.i("GCS","白骑士姓名参数为空");
            SimplexToast.showMyToast("身份认证信息不完整或者有误，请重新认证",GlobalApplication.getContext());
            return;
        }
        if(user.getCertno()!=null){
            params.setCertNo(unAES(user.getCertno()));
        }
        else {
            MyLog.i("GCS","白骑士姓名参数为空");
            SimplexToast.showMyToast("身份认证信息不完整或者有误，请重新认证",GlobalApplication.getContext());
            return;
        }
        if(user.getPhone()!=null){
            params.setMobile(unAES(user.getPhone()));
        }

        MyLog.i("GCS","guanchesuo");
        MyLog.i("GCS","李全朴");
        MyLog.i("GCS","410927199307065033");
        MyLog.i("GCS","18018746184");

        BqsCrawlerCloudSDK.setParams(params);

        Setting.bqsParams = params;
    }



    private void showAuthbookconfirm() {
       /* TextView title = new TextView(getContext());
        title.setText("通讯录授权");
        title.setPadding(0, 0, 0, 0);
        title.setGravity(Gravity.CENTER);
        // title.setTextColor(getResources().getColor(R.color.greenBG));
        title.setTextSize(18);*/

        View dialogview = View.inflate(getActivity(),R.layout.custom_dialog,null);
        TextView tv_link = dialogview.findViewById(R.id.read_authbook_link);
        Button bt_cancle = dialogview.findViewById(R.id.btn_cancel);

        /*final AlertDialog dlgShowBack = DialogUtil.getDialog(getContext()).setView(dialogview).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), PhoneAdressActivity.class);
                startActivity(intent);
            }
        }).create();*/
        final AlertDialog dlgShowBack = DialogUtil.getDialog(getContext()).setView(dialogview).setCancelable(true).create();
        tv_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.i("GCS","模拟通讯录授权成功");
                ShowUIHelper.openInternalBrowser(getActivity(), Setting.getServerUrl(GlobalApplication.getContext())+"wind-phone/authAgreement.jsp");

            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgShowBack.dismiss();
                requestReadContacts();

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarCompat.translucentStatusBar(getActivity(), isHide);
            isHide = !isHide;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!AccountHelper.isLogin()) {
            hideView();
        }
    }
    /**
     * update the view
     *
     * @param userInfo userInfo
     */
    private void updateView(User userInfo) {
        MyLog.i("GCS","更新UI登录后的显示状态");
        MyLog.i("GCS","头像更新");

            if(userInfo.getName()!=null){
                mTv_name.setText("hi,"+unAES(userInfo.getName()));
            }else {
                mTv_name.setText("该用户尚未进行身份认证");
            }

    }
    /**
     *退出登录后清除信息，
     */
    private void hideView() {
        MyLog.i("GCS","恢复未登录的默认状态显示");
        mTv_name.setText(R.string.unlogin_string);
        mLlidentityiv.setVisibility(View.GONE);
        mLlbankcardiv.setVisibility(View.GONE);
        mLlzhimaiv.setVisibility(View.GONE);
        mLlalipayiv.setVisibility(View.GONE);
        mLltaobaoiv.setVisibility(View.GONE);
        mLljdiv.setVisibility(View.GONE);
        mLloperatoriv.setVisibility(View.GONE);
        mLlcontactiv.setVisibility(View.GONE);
        mLldrivercardiv.setVisibility(View.GONE);
        mLlcreditcardiv.setVisibility(View.GONE);
        mLlemailiv.setVisibility(View.GONE);
    }


    public void requestReadContacts() {
        if (EasyPermissions.hasPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS})) {
            MyLog.i("GCS","上传通讯录");
                List<ContactBean> contactlist = GetContactsUtil.getContactslist(getActivity());
                if (contactlist.size()==0){
                    SimplexToast.showMyToast("当前通讯录为空",GlobalApplication.getContext());
                    return;
                }
                MyLog.i("GCS",new Gson().toJson(contactlist));
                if (AccountHelper.isLogin()) {
                    final User user = AccountHelper.getUser();
                    sendRequestData(user);
                    updateView(user);
                    MyLog.i("GCS","token="+user.getToken());
                    MyApi.batchAdd(new UploadContacts(user.getToken(),contactlist), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            MyLog.i("GCS","上传通讯录返回Exception："+e.toString());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            MyLog.i("GCS","上传通讯录返回成功response："+response);
                            mLlcontactiv.setVisibility(View.VISIBLE);
                            user.getAuthstate().setAuth_contact("1");
                            Type type = new TypeToken<ResultBean>() {}.getType();
                            ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                            //注册结果返回该用户User
                            int code = resultBean.getCode();
                            String msg = resultBean.getMessage();
                            switch (code) {
                                case 200://
                                    MyLog.i("GCS","通讯录上传结果"+msg);
                                    if (AccountHelper.isLogin()) {
                                        User user = AccountHelper.getUser();
                                        sendRequestData(user);
                                        MyLog.i("GCS", "网络实时的User，目前先用本地获取的User");
                                        updateView(user);
                                    } else {
                                        hideView();
                                    }
                                    break;
                                case 300://
                                    MyLog.i("GCS","通讯录上传结果"+msg);
                                    break;
                                case 500://
                                    MyLog.i("GCS","通讯录上传结果"+msg);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                } else {
                    hideView();
                }



        } else {
            EasyPermissions.requestPermissions(this, "获取通讯录权限", 0, Manifest.permission.READ_CONTACTS);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

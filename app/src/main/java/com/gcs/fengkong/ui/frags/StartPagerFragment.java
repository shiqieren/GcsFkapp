package com.gcs.fengkong.ui.frags;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bqs.crawler.cloud.sdk.BqsCrawlerCloudSDK;
import com.bqs.crawler.cloud.sdk.BqsParams;
import com.bqs.crawler.cloud.sdk.OnLoginResultListener;
import com.gcs.fengkong.AppConfig;
import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.Setting;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.bean.User;
import com.gcs.fengkong.ui.api.MyApi;
import com.gcs.fengkong.ui.atys.MainActivity;
import com.gcs.fengkong.ui.atys.PhoneAdressActivity;
import com.gcs.fengkong.ui.bean.SimpleBackPage;
import com.gcs.fengkong.ui.widget.statusbar.StatusBarCompat;
import com.gcs.fengkong.utils.DialogUtil;
import com.gcs.fengkong.ui.ShowUIHelper;
import com.gcs.fengkong.utils.TDevice;


/**
 * Created by Ivan on 15/9/22.
 */
public class StartPagerFragment extends BaseFragment implements View.OnClickListener{

    private boolean isHide = false;
    private CardView mCv_alipay ;
    private CardView mCv_bankcard;
    private CardView mCv_contact  ;
    private CardView mCv_identity;
    private CardView mCv_jd ;
    private CardView mCv_operator;
    private CardView mCv_taobao;
    private CardView mCv_zhima;
    private ImageView mIvLogoSetting;

    /**
     * requestData
     */
    private void sendRequestData() {
        if (TDevice.hasInternet() && AccountHelper.isLogin())
            Log.i("GCS", "每次到首页显示时就去获取个人信息User");
        // MyApi.getUserInfo(requestUserInfoHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (AccountHelper.isLogin()) {
            sendRequestData();
            User user = AccountHelper.getUser();
            Log.i("GCS", "网络实时的User，目前先用本地获取的User");
            updateView(user);
        } else {
            hideView();
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
        mCv_alipay  = view.findViewById(R.id.cv_alipay);
        mCv_bankcard  = view.findViewById(R.id.cv_bankcard);
        mCv_contact  = view.findViewById(R.id.cv_contact);
        mCv_identity  = view.findViewById(R.id.cv_identity);
        mCv_jd  = view.findViewById(R.id.cv_jd);
        mCv_operator  = view.findViewById(R.id.cv_operator);
        mCv_taobao  = view.findViewById(R.id.cv_taobao);
        mCv_zhima  = view.findViewById(R.id.cv_zhima);
        mIvLogoSetting = view.findViewById(R.id.iv_logo_setting);
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
            updateView(user);
            sendRequestData();
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
        switch (id) {
            case R.id.cv_identity:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                configBqsParams();
                ShowUIHelper.showIdentityAuth(getActivity());
                break;
            case R.id.cv_bankcard:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                configBqsParams();
                ShowUIHelper.showBankAuth(getActivity());
                break;
            case R.id.cv_zhima:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                configBqsParams();
                ShowUIHelper.showZhimaAuth(getActivity());
                break;
            case R.id.cv_alipay:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                configBqsParams();
                ShowUIHelper.showAlipayAuth(getActivity());
                break;
            case R.id.cv_taobao:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                configBqsParams();
                ShowUIHelper.showTaobaoAuth(getActivity());
                break;
            case R.id.cv_jd:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                configBqsParams();
                ShowUIHelper.showJdAuth(getActivity());
                break;
            case R.id.cv_operator:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                configBqsParams();
                ShowUIHelper.showOperatorAuth(getActivity());
                break;
            case R.id.cv_contact:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                configBqsParams();
                showAuthbookconfirm();
                break;
            case R.id.iv_logo_setting:
                ShowUIHelper.showSimpleBack(getActivity(), SimpleBackPage.SETTING);
                break;
            case R.id.iv_avatar:
                ShowUIHelper.showSimpleBack(getActivity(), SimpleBackPage.PERSONAL_DATA);
                break;

            default:
                break;
        }
    }
    /*设置该登录用户的全局参数*/
    private void configBqsParams() {

        BqsParams params = new BqsParams();
        params.setPartnerId("guanchesuo");
        params.setName("李全朴");
        params.setCertNo("410927199307065033");
        params.setMobile("13414467522");


        Log.i("GCS","guanchesuo");
        Log.i("GCS","李全朴");
        Log.i("GCS","410927199307065033");
        Log.i("GCS","13414467522");

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
                Toast.makeText(getContext(),"xxxxx",Toast.LENGTH_SHORT).show();
            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgShowBack.dismiss();
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
        Log.i("GCS","更新UI登录后的显示状态");
    }
    /**
     *退出登录后清除信息，
     */
    private void hideView() {
        Log.i("GCS","恢复未登录的默认状态显示");
    }
}

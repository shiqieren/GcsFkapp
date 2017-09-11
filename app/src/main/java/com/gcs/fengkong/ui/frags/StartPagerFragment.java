package com.gcs.fengkong.ui.frags;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.AccountHelper;
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
        mIvLogoSetting =(ImageView) view.findViewById(R.id.iv_logo_setting);
       view.findViewById(R.id.iv_avatar).setOnClickListener(this);
    }
    @Override
    public void initData() {
        setListener();
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
                ShowUIHelper.showIdentityAuth(getActivity());
                break;
            case R.id.cv_bankcard:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                ShowUIHelper.showBankAuth(getActivity());
                break;
            case R.id.cv_zhima:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                ShowUIHelper.showZhimaAuth(getActivity());
                break;
            case R.id.cv_alipay:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                ShowUIHelper.showAlipayAuth(getActivity());
                break;
            case R.id.cv_taobao:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                ShowUIHelper.showTaobaoAuth(getActivity());
                break;
            case R.id.cv_jd:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                ShowUIHelper.showJdAuth(getActivity());
                break;
            case R.id.cv_operator:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
                ShowUIHelper.showOperatorAuth(getActivity());
                break;
            case R.id.cv_contact:
                if (!AccountHelper.isLogin()) {
                    ShowUIHelper.showLoginActivity(getActivity());
                    return;
                }
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

    private void showAuthbookconfirm() {
       /* TextView title = new TextView(getContext());
        title.setText("通讯录授权");
        title.setPadding(0, 0, 0, 0);
        title.setGravity(Gravity.CENTER);
        // title.setTextColor(getResources().getColor(R.color.greenBG));
        title.setTextSize(18);*/

        View dialogview = View.inflate(getActivity(),R.layout.custom_dialog,null);
        TextView tv_link = dialogview.findViewById(R.id.read_authbook_link);
        //Button bt_cancle = dialogview.findViewById(R.id.btn_cancel);

        final AlertDialog dlgShowBack = DialogUtil.getDialog(getContext()).setView(dialogview).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), PhoneAdressActivity.class);
                startActivity(intent);
            }
        }).create();
        tv_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"xxxxx",Toast.LENGTH_SHORT).show();
            }
        });
       /* bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgShowBack.dismiss();
            }
        });*/
        dlgShowBack.show();
        dlgShowBack.getWindow().setBackgroundDrawableResource(R.drawable.rounded_search_text);
        WindowManager.LayoutParams params = dlgShowBack.getWindow().getAttributes();
        params.width = (int) TDevice.dp2px(270);
        params.height = (int) TDevice.dp2px(155);
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
}

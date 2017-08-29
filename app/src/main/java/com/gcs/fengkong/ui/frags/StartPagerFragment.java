package com.gcs.fengkong.ui.frags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.widget.statusbar.StatusBarCompat;
import com.gcs.fengkong.utils.ShowUIHelper;
import com.gcs.fengkong.utils.UIUtils;

import de.hdodenhof.circleimageview.CircleImageView;


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
                ShowUIHelper.showIdentityAuth(getActivity());
                break;
            case R.id.cv_bankcard:
                ShowUIHelper.showBankAuth(getActivity());
                break;
            case R.id.cv_zhima:
                ShowUIHelper.showZhimaAuth(getActivity());
                break;
            case R.id.cv_alipay:
                ShowUIHelper.showAlipayAuth(getActivity());
                break;
            case R.id.cv_taobao:
                ShowUIHelper.showTaobaoAuth(getActivity());
                break;
            case R.id.cv_jd:
                ShowUIHelper.showJdAuth(getActivity());
                break;
            case R.id.cv_operator:
                ShowUIHelper.showOperatorAuth(getActivity());
                break;
            case R.id.cv_contact:
                ShowUIHelper.showContactsAuth(getActivity());
                break;

            default:
                break;
        }
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

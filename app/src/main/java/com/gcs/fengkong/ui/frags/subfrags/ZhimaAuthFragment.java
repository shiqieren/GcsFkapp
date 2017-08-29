package com.gcs.fengkong.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.view.View;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.frags.BaseFragment;

/**
 * Created by Administrator on 0029 8-29.
 */
@SuppressLint("NewApi")
public class ZhimaAuthFragment extends BaseFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zhima_auth;
    }
    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.zhima_string);
    }
}

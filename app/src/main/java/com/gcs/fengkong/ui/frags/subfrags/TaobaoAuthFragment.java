package com.gcs.fengkong.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.frags.BaseFragment;

/**
 * Created by Administrator on 0029 8-29.
 */
@SuppressLint("NewApi")
public class TaobaoAuthFragment extends BaseFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_taobao_auth;
    }
    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.taobao_string);
        view.findViewById(R.id.traceroute_rootview).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.traceroute_rootview:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }

    }
}

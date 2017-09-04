package com.gcs.fengkong.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.frags.BaseFragment;

/**
 * Created by Administrator on 0029 8-29.
 */
@SuppressLint("NewApi")
public class IdentityAuthFragment extends BaseFragment{

    private ImageView mIvIdentityNameDel;
    private ImageView mIvIdentityNumberDel;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_identity_auth;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.identity_string);

        mIvIdentityNameDel = (ImageView) view.findViewById(R.id.iv_identity_name_del);
        mIvIdentityNumberDel = (ImageView) view.findViewById(R.id.iv_identity_number_del);
    }

    @Override
    protected void initData() {
        super.initData();
    }
}

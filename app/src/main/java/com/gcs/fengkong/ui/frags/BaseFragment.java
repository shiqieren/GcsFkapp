package com.gcs.fengkong.ui.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.gcs.fengkong.GlobalApplication;

import java.io.Serializable;


public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    protected Context mContext;
    protected View mView;
    protected Bundle mBundle;
    protected LayoutInflater mInflater;

    public GlobalApplication getApplication() {
        return (GlobalApplication) getActivity().getApplication();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null)
                parent.removeView(mView);
        } else {
            mView = inflater.inflate(getLayoutId(), container, false);
            mInflater = inflater;
//            this.mInflater = inflater;
//            super.onCreateView(inflater, container, savedInstanceState);
            // Do something
            onBindViewBefore(mView);
            // Get savedInstanceState
            if (savedInstanceState != null)
                onRestartInstance(savedInstanceState);
            // Init
            initView(mView);
            initData();
        }
        return mView;

    }

    protected void onBindViewBefore(View root) {
        // ...
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBundle = null;
    }


    //获取布局id
    protected abstract int getLayoutId();

    protected void initBundle(Bundle bundle) {

    }
    protected void initView(View view) {

    }

    protected void initData() {

    }

    protected <T extends Serializable> T getBundleSerializable(String key) {
        if (mBundle == null) {
            return null;
        }
        return (T) mBundle.getSerializable(key);
    }

    /**
     * 获取一个图片加载管理器
     *
     * @return RequestManager
     */


    public boolean onBackPressed() {
        return false;
    }



    @Override
    public void onClick(View v) {

    }

    protected void onRestartInstance(Bundle bundle) {

    }
}

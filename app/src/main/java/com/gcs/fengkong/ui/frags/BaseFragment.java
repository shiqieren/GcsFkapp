package com.gcs.fengkong.ui.frags;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.api.secret.AES;
import com.gcs.fengkong.utils.DialogUtil;
import com.gcs.fengkong.utils.MyLog;

import java.io.Serializable;

import static android.content.Context.INPUT_METHOD_SERVICE;


public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    private ProgressDialog mDialog;
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
    @Override
    public void onStop() {
        super.onStop();
        hideWaitDialog();
    }
    /**
     * show FocusWaitDialog
     *
     * @return progressDialog
     */
    protected ProgressDialog showFocusWaitDialog() {

        String message = getResources().getString(R.string.progress_submit);
        if (mDialog == null) {
            mDialog = DialogUtil.getProgressDialog(getActivity(), message, false);//DialogHelp.getWaitDialog(this, message);
        }
        mDialog.show();

        return mDialog;
    }
    /**
     * hide waitDialog
     */
    protected void hideWaitDialog() {
        ProgressDialog dialog = mDialog;
        if (dialog != null) {
            mDialog = null;
            try {
                dialog.cancel();
                // dialog.dismiss();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    /**
     * 加密
     * AES
     *
     * @param tempPwd tempPwd
     * @return sha-1 pwd
     */
    @NonNull
    protected String getAES(String tempPwd) {
        AES mAes = new AES();
        byte[] mBytes = null;
        try {

            mBytes = tempPwd.getBytes("UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String enString = mAes.encrypt(mBytes);
        MyLog.i("GCS","加密后："+enString);
        return enString;
    }

    /**
     * 解密
     * AES
     *
     * @param tempPwd tempPwd
     * @return sha-1 pwd
     */
    @NonNull
    protected String unAES(String tempPwd) {
        AES mAes = new AES();
        String deString = mAes.decrypt(tempPwd);
        MyLog.i("GCS","解密后："+deString);
        return deString;
    }
}

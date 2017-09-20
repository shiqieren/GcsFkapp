package com.gcs.fengkong.ui.frags;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.atys.LoginActivity;
import com.gcs.fengkong.ui.bean.SimpleBackPage;
import com.gcs.fengkong.ui.interf.OnTabReselectListener;
import com.gcs.fengkong.ui.notice.NoticeBean;
import com.gcs.fengkong.ui.notice.NoticeManager;
import com.gcs.fengkong.ui.widget.SimplexToast;
import com.gcs.fengkong.utils.DialogUtil;
import com.gcs.fengkong.ui.ShowUIHelper;


/**
 * Created by lyw on 2016/8/15.
 * <p>
 * 用户个人界面
 */

public class UserInfoFragment extends BaseFragment implements View.OnClickListener, OnTabReselectListener,NoticeManager.NoticeNotify {

    private ImageView mIvLogoSetting;
    private LinearLayout mRl_message;
    private ProgressDialog mDialog;




/*
    private TextHttpResponseHandler requestUserInfoHandler = new TextHttpResponseHandler() {

        @Override
        public void onStart() {
            super.onStart();
            if (mSolarSystem != null) mSolarSystem.accelerate();
            if (mIsUploadIcon) {
                showWaitDialog(R.string.title_updating_user_avatar);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString
                , Throwable throwable) {
            if (mIsUploadIcon) {
                Toast.makeText(getActivity(), R.string.title_update_fail_status, Toast.LENGTH_SHORT).show();
                deleteCacheImage();
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                Type type = new TypeToken<ResultBean<User>>() {
                }.getType();

                ResultBean resultBean = AppOperator.createGson().fromJson(responseString, type);
                if (resultBean.isSuccess()) {
                    User userInfo = (User) resultBean.getResult();
                    updateView(userInfo);
                    //缓存用户信息
                    AccountHelper.updateUserCache(userInfo);
                }
                if (mIsUploadIcon) {
                    deleteCacheImage();
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            if (mSolarSystem != null) mSolarSystem.decelerate();
            if (mIsUploadIcon) mIsUploadIcon = false;
            if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        }
    };*/



   /* @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_user_home, container,
                false);
        initView(view);
        initData();
        return view;
    }*/


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_user_home;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mIvLogoSetting =(ImageView) view.findViewById(R.id.iv_logo_setting);
        mRl_message = (LinearLayout) view.findViewById(R.id.rl_message);
        setListener();
    }

    @Override
    public void initData() {
        super.initData();
        NoticeManager.bindNotify(this);
    }

    /**
     * format count
     *
     * @param count count
     * @return formatCount
     */
    private String formatCount(long count) {

        if (count > 1000) {
            int a = (int) (count / 100);
            int b = a % 10;
            int c = a / 10;
            String str;
            if (c <= 9 && b != 0)
                str = c + "." + b;
            else
                str = String.valueOf(c);

            return str + "k";
        } else {
            return String.valueOf(count);
        }

    }

    /**
     * requestData
     */
    private void sendRequestData() {
       //111 if (TDevice.hasInternet() && AccountHelper.isLogin()) OSChinaApi.getUserInfo(requestUserInfoHandler);
    }



    @SuppressWarnings("deprecation")
    private void setListener(){
        mIvLogoSetting.setOnClickListener(this);
        mRl_message.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.iv_logo_setting) {
            ShowUIHelper.showSimpleBack(getActivity(), SimpleBackPage.SETTING);
        } else {

            if (!AccountHelper.isLogin()) {
                LoginActivity.show(getActivity());
                return;
            }

            switch (id) {
                case R.id.rl_message:
                    SimplexToast.showMyToast("第一条目", GlobalApplication.getContext());
                    break;
                default:
                    break;
            }
        }
    }


    public ProgressDialog showWaitDialog(int messageId) {
        String message = getResources().getString(messageId);
        if (mDialog == null) {
            mDialog = DialogUtil.getProgressDialog(getActivity(), message);
        }

        mDialog.setMessage(message);
        mDialog.show();

        return mDialog;
    }

    @Override
    public void onTabReselect() {
        sendRequestData();
    }

    @Override
    public void onNoticeArrived(NoticeBean bean) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        NoticeManager.unBindNotify(this);
    }
}

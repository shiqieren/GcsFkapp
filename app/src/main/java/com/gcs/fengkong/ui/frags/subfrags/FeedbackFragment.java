package com.gcs.fengkong.ui.frags.subfrags;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.api.MyApi;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.bean.base.ResultBean;
import com.gcs.fengkong.ui.frags.BaseFragment;
import com.gcs.fengkong.ui.widget.SimplexToast;
import com.gcs.fengkong.utils.AppOperator;
import com.gcs.fengkong.utils.FastOneClick;
import com.gcs.fengkong.utils.MyLog;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;


public class FeedbackFragment extends BaseFragment implements View.OnClickListener{


    private Button mBtncommit;
    private EditText mEtnmessage;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feed_back;
    }


    @Override
    public void initView(View view) {
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.feedback);
        mBtncommit = view.findViewById(R.id.btn_commit);
        mEtnmessage = view.findViewById(R.id.et_feed_back);
    }

    @Override
    public void initData() {
        mBtncommit.setOnClickListener(this);
    }

    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.btn_commit:
                if (FastOneClick.isFastClick()){
                    feedBackRequest();
                }
                break;
            default:
                break;
        }
    }



    private void feedBackRequest() {
        String msg = mEtnmessage.getText().toString().trim();
        String token = AccountHelper.getUser().getToken();
        if (msg.equals("")){
            SimplexToast.showMyToast("您还未填写意见", GlobalApplication.getContext());
        }else {
            MyApi.feedBack(token, msg, new StringCallback() {
                @Override
                public void onBefore(Request request, int id) {
                    super.onBefore(request, id);
                    showFocusWaitDialog();
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    MyLog.i("GCS","意见反馈返回Exception"+e.toString());
                    hideWaitDialog();
                }

                @Override
                public void onResponse(String response, int id) {
                    MyLog.i("GCS","意见反馈返回response"+response);
                    try {
                        Type type = new TypeToken<ResultBean>() {}.getType();
                        ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                        int code = resultBean.getCode();
                        if (code == 200) {
                            SimplexToast.showMyToast(resultBean.getMessage(),GlobalApplication.getContext());
                            hideWaitDialog();
                        } else {
                            if (code == 500) {
                                SimplexToast.showMyToast(resultBean.getMessage(),GlobalApplication.getContext());
                                hideWaitDialog();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }
}

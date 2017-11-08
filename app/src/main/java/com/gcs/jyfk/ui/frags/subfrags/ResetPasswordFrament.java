package com.gcs.jyfk.ui.frags.subfrags;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.ShowUIHelper;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.atys.LoginActivity;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.atys.SimpleBackActivity;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.ui.frags.BaseFragment;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.utils.ActivityManager;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.DialogUtil;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.TDevice;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;


public class ResetPasswordFrament extends BaseFragment {


    private EditText mEtoldpassword;
    private EditText mEtnewpassword;
    private EditText mEtquerypassword;
    private Button mBtnchange;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_resetpassword;
    }


    @Override
    public void initView(View view) {
        ((SimpleBackActivity) getActivity()).setToolBarTitle(R.string.setting_reset_password);
        mEtoldpassword = view.findViewById(R.id.et_oldpassword);
        mEtnewpassword = view.findViewById(R.id.et_newpassword);
        mEtquerypassword = view.findViewById(R.id.et_querypassword);
        mBtnchange = view.findViewById(R.id.bt_query_change);

    }

    @Override
    public void initData() {
        mBtnchange.setOnClickListener(this);
    }

    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.bt_query_change:
                changeRequest();
            default:
                break;
        }
    }

    private void changeRequest() {
        String oldpass = mEtoldpassword.getText().toString().trim();
        String newpass = mEtnewpassword.getText().toString().trim();
        String querypass = mEtquerypassword.getText().toString().trim();

        if (AccountHelper.isLogin()) {
            User user = AccountHelper.getUser();
            String token = user.getToken();
            if (newpass.length() < 6 || newpass.length() > 12) {
                SimplexToast.showMyToast("请输入至少6-12位新密码！", GlobalApplication.getContext());
                return;
            }
            if (newpass.equals(querypass)) {
                MyApi.updatePasswd(token, getAES(oldpass), getAES(newpass), getAES(querypass), new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showFocusWaitDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        hideWaitDialog();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideWaitDialog();
                        MyLog.i("GCS", "更新密码错误" + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyLog.i("GCS", "登录返回response：" + response);
                        try {
                            Type type = new TypeToken<ResultBean>() {
                            }.getType();
                            ResultBean resultBean = AppOperator.createGson().fromJson(response, type);
                            int code = resultBean.getCode();
                            if (code == 200) {
                                hideWaitDialog();
                                showAuthbookconfirm("提示", "密码已修改需重新登录");
                                //  SimplexToast.showMyToast(resultBean.getMessage(),GlobalApplication.getContext());
                                ShowUIHelper.clearAppCache(false);
                                AccountHelper.logoutauto(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                                // 注销操作
                                // 清理所有缓存
                            } else {

                                String message = resultBean.getMessage();
                                if (code == 500) {

                                }
                                SimplexToast.showMyToast(message, GlobalApplication.getContext());
                                //更新失败应该是不进行任何的本地操作
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                SimplexToast.showMyToast("确认密码输入不一致", GlobalApplication.getContext());
            }
        }

    }

    private void showAuthbookconfirm(String title, String con) {
        View dialogview = View.inflate(getActivity(), R.layout.custom_dialog, null);
        TextView tv_title = dialogview.findViewById(R.id.dialog_tip);
        TextView tv_link = dialogview.findViewById(R.id.read_authbook_link);
        Button bt_cancle = dialogview.findViewById(R.id.btn_cancel);
        tv_title.setText(title);
        tv_link.setText(con);
        final AlertDialog dlgShowBack = DialogUtil.getDialog(getContext()).setView(dialogview).setCancelable(true).create();

        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgShowBack.dismiss();
                // 清理所有缓存
                ShowUIHelper.clearAppCache(false);
                // SimplexToast.showMyToast("清理缓存", GlobalApplication.getContext());
                // 注销操作
                AccountHelper.logoutauto(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                LoginActivity.show(getContext());
                ActivityManager.getActivityManager().finishAllActivity();
            }
        });
        dlgShowBack.show();
        dlgShowBack.getWindow().setBackgroundDrawableResource(R.drawable.rounded_search_text);
        WindowManager.LayoutParams params = dlgShowBack.getWindow().getAttributes();
        params.width = (int) TDevice.dp2px(270);
        params.height = (int) TDevice.dp2px(122);
        dlgShowBack.getWindow().setAttributes(params);
    }

}

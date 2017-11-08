package com.gcs.jyfk.update;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.bean.Version;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.TDevice;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;


/**
 * Created by lyw
 * on 2016/10/19.
 */
public class CheckUpdateManager {

    private boolean mIsShowDialog;
    private Context mContext;
    private RequestPermissions mCaller;

    public CheckUpdateManager(Context context, boolean showWaitingDialog) {
        this.mContext = context;
        mIsShowDialog = showWaitingDialog;
        if (mIsShowDialog) {
            Toast.makeText(GlobalApplication.getContext(), "正在检查中。。。。", Toast.LENGTH_LONG).show();

        }
    }


    public void checkUpdate() {
        if (mIsShowDialog) {
            Toast.makeText(GlobalApplication.getContext(), "正在检查中。。。。", Toast.LENGTH_LONG).show();

        }

        MyApi.checkUpdate(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(GlobalApplication.getContext(), "异常" + e, Toast.LENGTH_LONG).show();

            }


            @Override
            public void onResponse(String response, int id) {
                // Toast.makeText(GlobalApplication.getContext(),response,Toast.LENGTH_LONG).show();
                try {
                    ResultBean<List<Version>> bean = AppOperator.createGson()
                            .fromJson(response, new TypeToken<ResultBean<List<Version>>>() {
                            }.getType());
                    if (bean != null && bean.isSuccess()) {
                        List<Version> versions = bean.getResult();
                        if (versions.size() > 0) {
                            final Version version = versions.get(0);
                            int curVersionCode = TDevice.getVersionCode(GlobalApplication
                                    .getInstance().getPackageName());
                            if (curVersionCode < Integer.parseInt(version.getCode())) {


                                UpdateActivity.show((Activity) mContext, version);
                             /*   AlertDialog.Builder dialog = DialogUtil.getConfirmDialog(mContext, version.getMessage(), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //mCaller.call(version);
                                        if (!TDevice.isWifiOpen()) {
                                           DialogUtil.getConfirmDialog(mContext, "当前非wifi环境，是否升级？", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mCaller.call(version);
                                                }
                                            }).show();
                                        } else {
                                            mCaller.call(version);
                                        }
                                    }
                               });
                                dialog.setTitle("发现新版本");
                                dialog.show();*/
                            } else {
                                if (mIsShowDialog) {
                                    Toast.makeText(GlobalApplication.getContext(), "已经是新版本了", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });

    }

    public void setCaller(RequestPermissions caller) {
        this.mCaller = caller;
    }

    public interface RequestPermissions {
        void call(Version version);
    }
}

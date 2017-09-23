package com.gcs.fengkong.ui.frags.subfrags;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gcs.fengkong.AppConfig;
import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.atys.LoginActivity;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.bean.Version;
import com.gcs.fengkong.ui.frags.BaseFragment;
import com.gcs.fengkong.ui.widget.SimplexToast;
import com.gcs.fengkong.ui.widget.togglebutton.ToggleButton;
import com.gcs.fengkong.update.CheckUpdateManager;
import com.gcs.fengkong.update.DownloadService;
import com.gcs.fengkong.utils.DialogUtil;
import com.gcs.fengkong.utils.FileUtil;
import com.gcs.fengkong.utils.MethodsCompat;
import com.gcs.fengkong.ui.ShowUIHelper;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 系统设置界面
 *
 * @author lyw
 */
public class SettingsFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks, CheckUpdateManager.RequestPermissions {

    private static final int RC_EXTERNAL_STORAGE = 0x04;//存储权限

    private TextView mTvCacheSize;
    private FrameLayout mRlCheck_version;
    private ToggleButton mTbDoubleClickExit;
    private View mSettingLineTop;
    private View mSettingLineBottom;
    private FrameLayout mCancel;

    private Version mVersion;

    @Override
    public void initView(View view) {
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.actionbar_title_setting);
        mTvCacheSize = view.findViewById(R.id.tv_cache_size);
        mRlCheck_version= view.findViewById(R.id.rl_check_version);
        mTbDoubleClickExit= view.findViewById(R.id.tb_double_click_exit);
        mSettingLineTop= view.findViewById(R.id.setting_line_top);
        mSettingLineBottom= view.findViewById(R.id.setting_line_bottom);
        mCancel= view.findViewById(R.id.rl_cancel);


        mTbDoubleClickExit.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                GlobalApplication.set(AppConfig.KEY_DOUBLE_CLICK_EXIT, on);
            }
        });

        view.findViewById(R.id.rl_clean_cache).setOnClickListener(this);
        view.findViewById(R.id.rl_double_click_exit).setOnClickListener(this);
        view.findViewById(R.id.rl_about).setOnClickListener(this);
        view.findViewById(R.id.rl_feedback).setOnClickListener(this);
        view.findViewById(R.id.rl_reset_password).setOnClickListener(this);
        mRlCheck_version.setOnClickListener(this);
        // view.findViewById(R.id.rl_exit).setOnClickListener(this);
        mCancel.setOnClickListener(this);

        //111 SystemConfigView.show((ViewGroup) view.findViewById(R.id.lay_linear));
    }

    @Override
    public void initData() {
        if (GlobalApplication.get(AppConfig.KEY_DOUBLE_CLICK_EXIT, true)) {
            mTbDoubleClickExit.setToggleOn();
        } else {
            mTbDoubleClickExit.setToggleOff();
        }
        calculateCacheSize();
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean login = AccountHelper.isLogin();
        if (!login) {
            mCancel.setVisibility(View.INVISIBLE);
            mSettingLineTop.setVisibility(View.GONE);
            mSettingLineBottom.setVisibility(View.INVISIBLE);
        } else {
            mCancel.setVisibility(View.VISIBLE);
            mSettingLineTop.setVisibility(View.GONE);
            mSettingLineBottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }


    /**
     * 计算缓存的大小
     */
    private void calculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = getActivity().getFilesDir();
        File cacheDir = getActivity().getCacheDir();

        fileSize += FileUtil.getDirSize(filesDir);
        fileSize += FileUtil.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (GlobalApplication.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = MethodsCompat
                    .getExternalCacheDir(getActivity());
            fileSize += FileUtil.getDirSize(externalCacheDir);
        }
        if (fileSize > 0)
            cacheSize = FileUtil.formatFileSize(fileSize);
        mTvCacheSize.setText(cacheSize);
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.rl_clean_cache:
                onClickCleanCache();
                break;
            case R.id.rl_double_click_exit:
                mTbDoubleClickExit.toggle();
                break;
            case R.id.rl_check_version:
                onClickUpdate();
                break;
            case R.id.rl_about:
                ShowUIHelper.showAboutGCS(getActivity());
                break;
            case R.id.rl_feedback:
                if (!AccountHelper.isLogin()) {
                    LoginActivity.show(getContext());
                    return;
                }
                ShowUIHelper.showFeedBack(getActivity());
                break;
            case R.id.rl_reset_password:
                if (!AccountHelper.isLogin()) {
                    LoginActivity.show(getContext());
                    return;
                }
                 ShowUIHelper.showResetPassword(getActivity());
                getActivity().finish();
                break;
            case R.id.rl_cancel:
                // 清理所有缓存
                ShowUIHelper.clearAppCache(false);
                SimplexToast.showMyToast("清理缓存", GlobalApplication.getContext());
                // 注销操作
                AccountHelper.logout(mCancel, new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        //getActivity().finish();
                        mTvCacheSize.setText("0KB");
                        SimplexToast.showMyToast(R.string.logout_success_hint,GlobalApplication.getContext());
                        mCancel.setVisibility(View.INVISIBLE);
                        mSettingLineTop.setVisibility(View.GONE);
                        mSettingLineBottom.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            default:
                break;
        }

    }

    private void onClickUpdate() {
        CheckUpdateManager manager = new CheckUpdateManager(getActivity(), true);
        manager.setCaller(this);
        manager.checkUpdate();
    }

    private void onClickCleanCache() {
        DialogUtil.getConfirmDialog(getActivity(), "是否清空缓存?", new DialogInterface.OnClickListener
                () {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ShowUIHelper.clearAppCache(true);
                mTvCacheSize.setText("0KB");
            }
        }).show();
    }

    @Override
    public void call(Version version) {
        this.mVersion = version;
        requestExternalStorage();
    }

    @SuppressLint("InlinedApi")
    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DownloadService.startService(getActivity(), mVersion.getDownloadUrl());
        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        DialogUtil.getConfirmDialog(getActivity(), "温馨提示", "需要开启管云风控对您手机的存储权限才能下载安装，是否现在开启", "去开启", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
            }
        }).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

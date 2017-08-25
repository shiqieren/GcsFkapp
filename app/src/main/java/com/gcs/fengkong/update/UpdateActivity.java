package com.gcs.fengkong.update;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.atys.BaseActivity;
import com.gcs.fengkong.ui.bean.Version;
import com.gcs.fengkong.utils.DialogHelper;
import com.gcs.fengkong.utils.TDevice;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 在线更新对话框
 * Created by haibin on 2017/5/4.
 */

public class UpdateActivity extends BaseActivity implements View.OnClickListener,
       EasyPermissions.PermissionCallbacks{

    private TextView mTextUpdateInfo;
    private Button mBtn_update;
    private ImageButton mBtn_close;

    private Version mVersion;
    private static final int RC_EXTERNAL_STORAGE = 0x04;//存储权限

    public static void show(Activity activity, Version version) {
        Intent intent = new Intent(activity, UpdateActivity.class);
        intent.putExtra("version", version);
        activity.startActivityForResult(intent, 0x01);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_update;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initData() {
        super.initData();
        mTextUpdateInfo = (TextView) findViewById(R.id.tv_update_info);
        mBtn_update = (Button) findViewById(R.id.btn_update);
        mBtn_close = (ImageButton) findViewById(R.id.btn_close);
        setListener();
        setTitle("");
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mVersion = (Version) getIntent().getSerializableExtra("version");
        mTextUpdateInfo.setText(Html.fromHtml(mVersion.getMessage()));
    }

    private void setListener(){
        mBtn_update.setOnClickListener(this);
        mBtn_close.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                if (!TDevice.isWifiOpen()) {
                    DialogHelper.getConfirmDialog(this, "当前非wifi环境，是否升级？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           requestExternalStorage();
                            finish();
                        }
                    }).show();
                } else {
                     requestExternalStorage();
                    finish();
                }
                break;
            case R.id.btn_close:
                finish();
                break;
        }

    }
    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DownloadService.startService(this, mVersion.getDownloadUrl());
        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        DialogHelper.getConfirmDialog(this, "温馨提示", "需要开启管云风控对您手机的存储权限才能下载安装，是否现在开启", "去开启", "取消", true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
            }
        }, null).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}

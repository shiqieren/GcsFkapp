package com.gcs.jyfk.ui.atys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gcs.jyfk.R;


/**
 * 异常信息界面
 * Created by lyw on 2017/5/8.
 */

public class ErrorActivity extends BaseActivity implements View.OnClickListener {
    // @BindView(R.id.tv_crash_info)
    private TextView mTextCrashInfo;
    private Button mBtn_restart;

    @Override
    protected void initWindow() {
        super.initWindow();
        mTextCrashInfo = (TextView) findViewById(R.id.tv_crash_info);
        mBtn_restart = (Button) findViewById(R.id.btn_restart);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(false);
        }
    }

    public static void show(Context context, String message) {
        if (message == null)
            return;
        Intent intent = new Intent(context, ErrorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("message", message);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_error;
    }

    @Override
    protected void initData() {
        super.initData();
        setListener();
        mTextCrashInfo.setText(getIntent().getStringExtra("message"));
    }

    private void setListener() {
        mBtn_restart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_restart:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

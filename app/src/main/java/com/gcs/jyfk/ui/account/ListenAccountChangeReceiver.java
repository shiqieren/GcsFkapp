package com.gcs.jyfk.ui.account;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.gcs.jyfk.AppConfig;
import com.gcs.jyfk.utils.MyLog;


/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */

public class ListenAccountChangeReceiver extends BroadcastReceiver {
    public static final String TAG = ListenAccountChangeReceiver.class.getSimpleName();
    private Service service;

    private ListenAccountChangeReceiver(Service service) {
        this.service = service;
    }

    public static ListenAccountChangeReceiver start(Service service) {
        MyLog.d(TAG, "start: " + service);
        ListenAccountChangeReceiver receiveBroadCast = new ListenAccountChangeReceiver(service);
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.INTENT_ACTION_LOGOUT);
        service.registerReceiver(receiveBroadCast, filter);
        return receiveBroadCast;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.d(TAG, "onReceive: " + service);
        if (service != null)
            service.stopSelf();
    }

    public void destroy() {
        MyLog.d(TAG, "destroy: " + service);
        if (service != null) {
            service.unregisterReceiver(this);
            service = null;
        }
    }
}

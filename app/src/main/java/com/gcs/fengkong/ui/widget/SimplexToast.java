package com.gcs.fengkong.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gcs.fengkong.R;
import com.gcs.fengkong.utils.UIUtils;

/**
 * 自定义吐司
 * Created by lyw on 2017/7/23.
 */

public class SimplexToast {

    private static Toast mToast;
    private static long nextTimeMillis;
    private static int yOffset;

    private SimplexToast(Context context) {

    }

    public static Toast init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context should not be null!!!");
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            yOffset = mToast.getYOffset();
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM, 0, yOffset);
        mToast.setMargin(0, 0);
        return mToast;
    }


    public static void show(String content) {
        show(content, Toast.LENGTH_SHORT);
    }

    public static void show(String content, int duration) {
        show(null, content, Gravity.BOTTOM, duration);
    }

    public static void show(Context context, int rid) {
        show(context, context.getResources().getString(rid));
    }

    public static void show(Context context, String content) {
        show(context, content, Gravity.BOTTOM);
    }

    public static void show(Context context, String content, int gravity) {
        show(context, content, gravity, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String content, int gravity, int duration) {
        long current = System.currentTimeMillis();
        //if (current < nextTimeMillis) return;
        if (mToast == null) init(context.getApplicationContext());
        mToast.setText(content);
        mToast.setDuration(duration);
        mToast.setGravity(gravity, 0, yOffset);
        nextTimeMillis = current + (duration == Toast.LENGTH_LONG ? 3500 : 2000);
        mToast.show();
    }

    @SuppressLint("InflateParams")
    public static void showMyToast(String text,Context context) {
        if (mToast == null) init(context.getApplicationContext());
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_toast, null, false);
        TextView textView = (TextView) rootView.findViewById(R.id.title_tv);
        textView.setText(text);
        mToast.setView(rootView);
        mToast.setGravity(Gravity.CENTER, 0, yOffset);
        mToast.show();
    }

    /**
     * showToast
     *
     * @param id id
     */
    @SuppressLint("InflateParams")
    public static void showMyToast(@StringRes int id,Context context) {
        if (mToast == null) init(context.getApplicationContext());
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_toast, null, false);
        TextView textView = (TextView) rootView.findViewById(R.id.title_tv);
        textView.setText(id);
        mToast.setView(rootView);
        mToast.show();
    }

    @SuppressLint("InflateParams")
    public static void showMyToast(String text,Context context,Boolean openKeyBord) {
        if (mToast == null) init(context.getApplicationContext());
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_toast, null, false);
        TextView textView = (TextView) rootView.findViewById(R.id.title_tv);
        textView.setText(text);
        mToast.setView(rootView);
        initToastGravity(mToast,openKeyBord);
        mToast.show();
    }

    /**
     * showToast
     *
     * @param id id
     */
    @SuppressLint("InflateParams")
    public static void showMyToast(@StringRes int id,Context context,Boolean openKeyBord) {
        if (mToast == null) init(context.getApplicationContext());
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_toast, null, false);
        TextView textView = (TextView) rootView.findViewById(R.id.title_tv);
        textView.setText(id);
        mToast.setView(rootView);
        initToastGravity(mToast,openKeyBord);
        mToast.show();
    }


   /*键盘是否弹起的标志位
   private boolean mKeyBoardIsActive;

    protected void updateKeyBoardActiveStatus(boolean isActive) {
        this.mKeyBoardIsActive = isActive;
    }*/
    public static void initToastGravity(Toast toast,Boolean openKeyBord) {
        boolean isCenter = openKeyBord;
        if (isCenter) {
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            toast.setGravity(Gravity.CENTER, 0, UIUtils.getDimen(R.dimen.toast_y_offset));
        }
    }



    /**
     * cancelToast
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public static void showToastForKeyBord(@StringRes int id,Context context,Boolean openKeyBord) {
        showMyToast(id,context,openKeyBord);
    }

    public static void showToastForKeyBord(String message,Context context,Boolean openKeyBord) {
        showMyToast(message,context,openKeyBord);
    }

    /**
     * request network error
     *异常吐司
     * @param throwable throwable
     */
    public static void requestFailureHint(Throwable throwable,Context context) {
        if (throwable != null) {
            throwable.printStackTrace();
        }
        showMyToast(R.string.request_error_hint,context);
    }
}

package com.gcs.fengkong.utils;

/**
 * Created by Administrator on 0020 9-20.
 */

public class FastOneClick {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}

///////////////////////////////////////////////////////防止点击过快方法二/////////////////////////////////////////////////
                    /*
                    public abstract class OnMultiClickListener implements View.OnClickListener{
                        // 两次点击按钮之间的点击间隔不能少于1000毫秒
                        private static final int MIN_CLICK_DELAY_TIME = 1000;
                        private static long lastClickTime;

                        public abstract void onMultiClick(View v);

                        @Override
                        public void onClick(View v) {
                            long curClickTime = System.currentTimeMillis();
                            if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                                // 超过点击间隔后再将lastClickTime重置为当前点击时间
                                lastClickTime = curClickTime;
                                onMultiClick(v);
                            }
                        }
                    }

                    btn.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                            // 进行点击事件后的逻辑操作
                            }
                            });*/

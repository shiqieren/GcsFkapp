package com.gcs.fengkong.ui.bean;

/*导入要放入替换的同一个activity下可后退的fragment*/


import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.frags.subfrags.AboutGCSFragment;
import com.gcs.fengkong.ui.frags.subfrags.BrowserFragment;
import com.gcs.fengkong.ui.frags.subfrags.SettingsFragment;

public enum SimpleBackPage {
        /*枚举*/

    SETTING(15, R.string.actionbar_title_setting, SettingsFragment.class),

    BROWSER(26, R.string.app_name, BrowserFragment.class),

    ABOUT_GCS(17, R.string.actionbar_title_about_gcs, AboutGCSFragment.class);



    private int title;
    private Class<?> clz;
    private int value;

    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }


}

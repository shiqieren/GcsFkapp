package com.gcs.fengkong.ui.atys;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.gcs.fengkong.AppConfig;
import com.gcs.fengkong.GlobalApplication;
import com.gcs.fengkong.R;
import com.gcs.fengkong.Setting;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.bean.User;
import com.gcs.fengkong.ui.api.ApiClientHelper;
import com.gcs.fengkong.ui.bean.Tab;
import com.gcs.fengkong.ui.bean.Version;
import com.gcs.fengkong.ui.frags.StartPagerFragment;
import com.gcs.fengkong.ui.frags.UserInfoFragment;
import com.gcs.fengkong.ui.location.BDLocationAdapter;
import com.gcs.fengkong.ui.location.RadarSearchAdapter;
import com.gcs.fengkong.ui.widget.FragmentTabHost;
import com.gcs.fengkong.ui.widget.SimplexToast;
import com.gcs.fengkong.update.CheckUpdateManager;
import com.gcs.fengkong.update.DownloadService;
import com.gcs.fengkong.utils.DialogHelper;
import com.gcs.fengkong.utils.ShowUIHelper;
import com.gcs.fengkong.utils.TDevice;
import com.gcs.fengkong.utils.UIUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by lyw on 2017/7/25.
 */

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, CheckUpdateManager.RequestPermissions  {
    public static final int LOCATION_PERMISSION = 0x0100;//定位权限
    private static final int RC_EXTERNAL_STORAGE = 0x04;//存储权限
    public static final String CHARSET = "UTF-8";

    private Version mVersion;
    private long mBackPressedTime;

    private LayoutInflater mInflater;
    private FragmentTabHost mTabhost;
    //定位
    private LocationClient mLocationClient;
    private RadarSearchManager mRadarSearchManager;
    private RadarSearchAdapter mRadarSearchAdapter;
    private List<Tab> mTabs = new ArrayList<>(1);


  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();

    }*/

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWindow() {
        super.initWindow();

        initTab();

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Log.i("GCS","进入,Mainactivity首页如果未登录则跳转到登录页面");
        if (!AccountHelper.isLogin()) {
            DialogHelper.getConfirmDialog(this, "您尚未登录，是否先进行登录操作", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ShowUIHelper.showLoginActivity(MainActivity.this);
                }
            }).show();
            return;
        }
    }

    private void initTab() {


        Tab tab_mine1 = new Tab(StartPagerFragment.class,R.string.tab1,R.drawable.selector_icon_mine);
       // Tab tab_mine2 = new Tab( UserInfoFragment.class,R.string.tab2,R.drawable.selector_icon_mine);

        mTabs.add(tab_mine1);
      //  mTabs.add(tab_mine2);



        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);

        for (Tab tab : mTabs){

            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec,tab.getFragment(),null);

        }


        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);


    }





    private View buildIndicator(Tab tab){


        View view =mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return  view;
    }

    @Override
    protected void initData() {
        super.initData();
        checkUpdate();
        checkLocation();
        ApiClientHelper.getUserAgent(GlobalApplication.getInstance());
    }
    private void checkUpdate() {
        if (!GlobalApplication.get(AppConfig.KEY_CHECK_UPDATE, true)) {
            return;
        }
        CheckUpdateManager manager = new CheckUpdateManager(this, false);
        manager.setCaller(this);
        manager.checkUpdate();
    }
    private void checkLocation() {
        Log.i("GCS","开始定位");
        //首先判断appCode是否存在，如果存在是否大于当前版本的appCode，或者第一次全新安装(默认0)表示没有保存appCode
        int hasLocationAppCode = Setting.hasLocationAppCode(getApplicationContext());
        int versionCode = TDevice.getVersionCode();
        Log.i("GCS","hasLocationAppCode:"+hasLocationAppCode+"///"+"versionCode:"+versionCode);
        //当app第一次被安装时，不管是覆盖安装（不管是否有定位权限）还是全新安装都必须进行定位请求
        //直接进行位置信息定位并上传-暂时不做登录状态判断
        Setting.updateLocationAppCode(getApplicationContext(), versionCode);
        requestLocationPermission();
         /*if ((hasLocationAppCode <= 0) || (hasLocationAppCode > versionCode)) {
            //如果是登陆状态
            if (AccountHelper.isLogin()) {
            Log.i("GCS","hasLocationAppCode:"+hasLocationAppCode+"///"+"versionCode:"+versionCode);
                //当app第一次被安装时，不管是覆盖安装（不管是否有定位权限）还是全新安装都必须进行定位请求
                Setting.updateLocationAppCode(getApplicationContext(), versionCode);
                requestLocationPermission();
            }
            return;
         }*/

        //如果有账户登陆，并且有主动上传过位置信息。那么准备请求定位
       if (AccountHelper.isLogin() && Setting.hasLocation(getApplicationContext())) {
        Log.i("GCS","请求定位授权");
            //1.有主动授权过，直接进行定位，否则不进行操作任何操作
            if (Setting.hasLocationPermission(getApplicationContext())) {
                requestLocationPermission();
            }
        }
    }

    @Override
    public void call(Version version) {
        this.mVersion = version;
        requestExternalStorage();
    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DownloadService.startService(this, mVersion.getDownloadUrl());
        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    /**
     * proxy request permission
     */
    @AfterPermissionGranted(LOCATION_PERMISSION)
    private void requestLocationPermission() {
        Log.i("GCS","授权");
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE)) {
            startLbs();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.need_lbs_permission_hint), LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        for (String perm : perms) {
            if (perm.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                DialogHelper.getConfirmDialog(this, "温馨提示", "需要开启管云风控对您手机的存储权限才能下载安装，是否现在开启", "去开启", "取消", true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                    }
                }, null).show();

            }
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



    @Override
    public void onBackPressed() {
        boolean isDoubleClick = GlobalApplication.get(AppConfig.KEY_DOUBLE_CLICK_EXIT, true);
        if (isDoubleClick) {
            long curTime = SystemClock.uptimeMillis();
            if ((curTime - mBackPressedTime) < (3 * 1000)) {
                finish();
            } else {
                mBackPressedTime = curTime;
                Toast.makeText(this, R.string.tip_double_click_exit, Toast.LENGTH_LONG).show();
            }
        } else {
            finish();
        }
    }


    /**
     * start auto lbs service
     */
    private void startLbs() {
        Log.i("GCS","启动定位start（）");
        if (mRadarSearchManager == null || mLocationClient == null) {
            initLbs();
        }
        //进行定位
        mLocationClient.start();
    }

    /**
     * init lbs service
     */
    private void initLbs() {
        Log.i("GCS","初始化定位服务");
        if (mRadarSearchManager == null) {

            mRadarSearchManager = RadarSearchManager.getInstance();

            mRadarSearchManager.addNearbyInfoListener(this.mRadarSearchAdapter = new RadarSearchAdapter() {
                @Override
                public void onGetUploadState(RadarSearchError radarSearchError) {
                    super.onGetUploadState(radarSearchError);
                    //上传成功，更新用户本地定位信息标示

                    if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
                        Setting.updateLocationInfo(getApplicationContext(), true);
                    } else {
                        Setting.updateLocationInfo(getApplicationContext(), false);
                    }
                    //不管是否上传成功，都主动释放lbs资源
                    releaseLbs();
                }
            });
        }

        if (mLocationClient == null) {
            mLocationClient = new LocationClient(this);
            mLocationClient.registerLocationListener(new BDLocationAdapter() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    super.onReceiveLocation(bdLocation);
                    //处理返回的定位信息，进行用户位置信息上传
                    Log.i("GCS","位置信息回调处理");
                    ReceiveLocation(bdLocation);
                }
            });
        }

        LocationClientOption option = new LocationClientOption();

        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");

        //根据网络情况和gps进行准确定位，只定位一次 当获取到真实有效的经纬度时，主动关闭定位功能
        option.setScanSpan(0);

        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);

        //设置是否需要位置语义化结果
        option.setIsNeedLocationDescribe(false);

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        //option.setIgnoreKillProcess(false);

        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);

        mLocationClient.setLocOption(option);
    }

    /**
     * 定位回调处理
     *
     * @param location location
     */
    private void ReceiveLocation(BDLocation location) {

        final int code = location.getLocType();
        Log.i("GCS","code:"+code);
        switch (code) {
            case BDLocation.TypeCriteriaException://62
                releaseLbs();
                return;
            case BDLocation.TypeNetWorkException://63
                releaseLbs();
                return;
            case BDLocation.TypeServerError://167
                releaseLbs();
                return;
            case BDLocation.TypeNetWorkLocation://161 网络定位模式
                break;
            case BDLocation.TypeOffLineLocation://66  离线模式

                if (!TDevice.hasInternet()) {
                    SimplexToast.show(this, getString(R.string.tip_network_error));
                    releaseLbs();
                    return;
                }

                break;
        }

        if (code >= 501) {//非法key
            releaseLbs();
            return;
        }

        //定位成功，网络ok，主动上传用户位置信息
        if (TDevice.hasInternet() && location.getLatitude() != 4.9E-324 && location.getLongitude() != 4.9E-324) {
            Log.i("GCS","位置信息经纬度");
            LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("GCS","经度："+location.getLatitude()+"纬度："+location.getLongitude()+"位置："+location.getAddrStr());
            Setting.updateLocationPermission(getApplicationContext(), true);

            //周边雷达设置用户身份标识，id为空默认是设备标识
            String userId = null;

            //上传位置
            RadarUploadInfo info = new RadarUploadInfo();

            if (AccountHelper.isLogin()) {
                userId = String.valueOf(AccountHelper.getUserId());

                User user = AccountHelper.getUser();
                try {
                    String comments = String.format(
                            "{" +
                                    "\"id\":\"%s\"," +
                                    "\"name\":\"%s\"," +
                                    "}"
                            , user.getId(), user.getName());
                    comments = comments.replaceAll("[\\s\n]+", "");
                    comments = URLEncoder.encode(comments, CHARSET);
                    info.comments = comments;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    SimplexToast.show(this, getString(R.string.upload_lbs_info_hint));
                }

            }

            mRadarSearchManager.setUserID(userId);
            info.pt = userLatLng;
            mRadarSearchManager.uploadInfoRequest(info);
        } else {
            //返回的位置信息异常或网络有问题，即定位失败，停止定位功能，并释放lbs资源
            releaseLbs();
        }
    }

    /**
     * release lbs source
     */
    private void releaseLbs() {
        if (mLocationClient != null && mLocationClient.isStarted())
            mLocationClient.stop();
        mLocationClient = null;
        //移除监听
        if (mRadarSearchManager != null) {
            mRadarSearchManager.removeNearbyInfoListener(mRadarSearchAdapter);
            //释放资源
            mRadarSearchManager.destroy();
            mRadarSearchManager = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseLbs();
    }
}

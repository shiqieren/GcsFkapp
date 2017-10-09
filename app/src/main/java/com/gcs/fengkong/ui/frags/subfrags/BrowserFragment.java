package com.gcs.fengkong.ui.frags.subfrags;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.atys.MainActivity;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.frags.BaseFragment;
import com.gcs.fengkong.utils.ActivityManager;
import com.gcs.fengkong.utils.MyLog;
import com.gcs.fengkong.utils.TDevice;
import com.gcs.fengkong.utils.UIUtils;


/**
 * 浏览器界面
 *
 * @author lyw
 */
@SuppressLint("NewApi")
public class BrowserFragment extends BaseFragment {
   private WebView mWebView;
    private ProgressBar mProgress;

    public static final String BROWSER_KEY = "browser_url";
    public static final String DEFAULT = "http://www.guanchesuo.com/";

    private int TAG = 1; // 双击事件需要
    private Activity aty;
    private String mCurrentUrl = DEFAULT;

    private Animation animBottomIn, animBottomOut;
    private GestureDetector mGestureDetector;
    private CookieManager cookie;
    private AlertDialog alertDialog;

    @Override
    public void onClick(View v) {

    }


    @Override
    public void initView(View view) {
        mWebView = view.findViewById(R.id.webview);
        mProgress= view.findViewById(R.id.progress);

        initWebView();
        initBarAnim();
        initData();
        mGestureDetector = new GestureDetector(aty, new MyGestureListener());
        mWebView.addJavascriptInterface(new JsToJava(), "jscalljava");
        mWebView.loadUrl(mCurrentUrl);
        mWebView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

    }

    private class JsToJava{
        //这里需要加@JavascriptInterface，4.2之后提供给javascript调用的函数必须带有@JavascriptInterface
        @JavascriptInterface
        public void jsMethod(String paramFromJS){
            System.out.println("js返回结果:" + paramFromJS);//处理返回的结果
        }

        @JavascriptInterface
        public void auth_success() {
          //  Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();
            UIUtils.runOnUIThread(new Runnable() {

                @Override
                public void run() {
                    Intent i =new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    ActivityManager.getActivityManager().finishActivity(SimpleBackActivity.class);

                }
            });
        }

        @JavascriptInterface
        public void auth_failure() {
           // Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
            UIUtils.runOnUIThread(new Runnable() {

                @Override
                public void run() {
                   // msgView.setText(msgView.getText() + "\njs调用了java函数");
                    getActivity().finish();
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null)
            mWebView.destroy();
    }




    @Override
    public void initData() {
        Bundle bundle = getActivity().getIntent().getBundleExtra(
                SimpleBackActivity.BUNDLE_KEY_ARGS);
        if (bundle != null) {
            mCurrentUrl = bundle.getString(BROWSER_KEY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            if (mView != null) {
                ViewGroup parent = (ViewGroup) mView.getParent();
                if (parent != null)
                    parent.removeView(mView);
            } else {
                if (TDevice.hasWebView(getContext())) {
                            mView = inflater.inflate(getLayoutId(), container, false);
                            mInflater = inflater;
                            aty = getActivity();
                            // Do something
                            onBindViewBefore(mView);
                            // Get savedInstanceState
                            if (savedInstanceState != null)
                                onRestartInstance(savedInstanceState);
                            // Init
                            initView(mView);
                            initData();

                } else {
                    getActivity().finish();
                    mView = super.onCreateView(inflater, container, savedInstanceState);
                }
            }
        return mView;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browser;
    }



    /**
     * 初始化上下栏的动画并设置结束监听事件
     */
    private void initBarAnim() {
        animBottomIn = AnimationUtils.loadAnimation(aty, R.anim.anim_bottom_in);
        animBottomOut = AnimationUtils.loadAnimation(aty,
                R.anim.anim_bottom_out);
        animBottomIn.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //mLayoutBottom.setVisibility(View.VISIBLE);
            }
        });
        animBottomOut.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //mLayoutBottom.setVisibility(View.GONE);
            }
        });
    }




    /**
     * 载入链接之前会被调用
     *
     * @param view WebView
     * @param url  链接地址
     */
    protected void onUrlLoading(WebView view, String url) {

        if (url != null && url.contains("auth_success")) {
            UIUtils.runOnUIThread(new Runnable() {
                //auth_success
                @Override
                public void run() {
                    Intent i =new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    ActivityManager.getActivityManager().finishActivity(SimpleBackActivity.class);

                }
            });
        }else if (url != null && url.contains("auth_fail")){
            UIUtils.runOnUIThread(new Runnable() {
                //auth_failure
                @Override
                public void run() {
                    // msgView.setText(msgView.getText() + "\njs调用了java函数");
                    getActivity().finish();
                }
            });
        }
        mProgress.setVisibility(View.VISIBLE);
        cookie.setCookie(url, AccountHelper.getCookie());
    }

    /**
     * 链接载入成功后会被调用
     *
     * @param view WebView
     * @param url  链接地址
     */
    protected void onUrlFinished(WebView view, String url) {
        mCurrentUrl = url;
        mProgress.setVisibility(View.GONE);
    }

    /**
     * 当前WebView显示页面的标题
     *
     * @param view  WebView
     * @param title web页面标题
     */
    protected void onWebTitle(WebView view, String title) {
        if (aty != null && mWebView != null) { // 必须做判断，由于webview加载属于耗时操作，可能会本Activity已经关闭了才被调用
            ((SimpleBackActivity) aty).setToolBarTitle(mWebView.getTitle());
        }
    }

    /**
     * 当前WebView显示页面的图标
     *
     * @param view WebView
     * @param icon web页面图标
     */
    protected void onWebIcon(WebView view, Bitmap icon) {
    }

    /**
     * 初始化浏览器设置信息
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        cookie = CookieManager.getInstance();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用支持javascript
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存
        webSettings.setAllowFileAccess(true);// 可以访问文件
        webSettings.setBuiltInZoomControls(true);// 支持缩放
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            webSettings.setPluginState(PluginState.ON);
            webSettings.setDisplayZoomControls(false);// 支持缩放
        }
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            onWebTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            onWebIcon(view, icon);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
            if (newProgress > 90) {
                mProgress.setVisibility(View.GONE);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            onUrlLoading(view, url);
            boolean flag = super.shouldOverrideUrlLoading(view, url);
            mCurrentUrl = url;
            return flag;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //网页加载完之后，java调用js方法
            mWebView.loadUrl("javascript:shareToFriends()");
            super.onPageFinished(view, url);
            onUrlFinished(view, url);
        }
    }

    private class MyGestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {// webview的双击事件
            if (TAG % 2 == 0) {
                TAG++;
                //mLayoutBottom.startAnimation(animBottomIn);
            } else {
                TAG++;
               // mLayoutBottom.startAnimation(animBottomOut);
            }
            return super.onDoubleTap(e);
        }
    }



}

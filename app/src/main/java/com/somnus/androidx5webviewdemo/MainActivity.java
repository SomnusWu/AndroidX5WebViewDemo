package com.somnus.androidx5webviewdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.somnus.androidx5webviewdemo.x5webview.X5WebView;
import com.somnus.androidx5webviewdemo.x5webview.X5WebViewJavaScriptFunction;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mRootFrameLayout;
    private X5WebView mWebView;
    //    private String mHomeUrl = "http://192.168.0.123:8083/buyapp/web/sd/index";
    private String mHomeUrl = "http://sj.qq.com/myapp/detail.htm?apkName=com.bjg.lcc.privateproject";

//    private String mHomeUrl = "http://192.168.0.123:8083/buyapp/web/lc/index";

    //    private String mHomeUrl = "http://www.baidu.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_main);
        mRootFrameLayout = (FrameLayout) findViewById(R.id.fragment_webroot_layout);
        webViewTransportTest();

        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
    }

    private void webViewTransportTest() {
        X5WebView.setSmallWebViewEnabled(true);
    }

    public static final int MSG_INIT_UI = 1;
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT_UI:
                    initView();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private void initView() {
        mWebView = new X5WebView(this);

        mRootFrameLayout.addView(mWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                // TODO Auto-generated method stub
                Log.e("should", "request.getUrl().toString() is " + request.getUrl().toString());
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(final String arg0, String arg1, String arg2, String arg3, long arg4) {
                Log.d("arg0", arg0);
                Log.d("arg1", arg1);
                Log.d("arg2", arg2);
                Log.d("arg3", arg3);
                new AlertDialog.Builder(MainActivity.this).setTitle("下载")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "fake message: i'll download...", Toast.LENGTH_SHORT).show();
                                Uri uri = Uri.parse(arg0);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).show();
            }
        });


        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        mWebView.loadUrl(mHomeUrl);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();


        mWebView.addJavascriptInterface(new X5WebViewJavaScriptFunction() {
            @Override
            public void onJsFunctionCalled(String tag) {

            }


            @JavascriptInterface
            public void backFun() {
                finish();
            }

        }, "fgqqg");
        //设置UserAgent
        String ua = mWebView.getSettings().getUserAgentString();
        webSetting.setUserAgentString(ua + " fgqqg " + VersionUtils.getVersionName(this));

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }


}

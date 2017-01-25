package com.gdou.www.gdouaqualib.view.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gdou.www.gdouaqualib.R;
import com.gdou.www.gdouaqualib.utils.ActivityCollector;
import com.gdou.www.gdouaqualib.utils.ToastUtil;

public class DetailsActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    WebView mWebView;
    private String url;
    // 用于记录出错页面的url 方便重新加载
    private String mFailingUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        int flag = getIntent().getExtras().getInt("flag");
        url = getIntent().getExtras().getString("url");
        String title = getIntent().getExtras().getString("title");
        // 设置不同的动画效果
        switch (flag) {
            case 0:
                getWindow().setEnterTransition(new Explode());
                break;
            case 1:
                getWindow().setEnterTransition(new Slide());
                break;
            case 2:
                getWindow().setEnterTransition(new Fade());
                getWindow().setExitTransition(new Fade());
                break;
            case 3:
                break;
        }
        setContentView(R.layout.activity_details);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWebView = (WebView)findViewById(R.id.webview);
        initWebViewSettings();
        initWebview();
        //progressBar.setVisibility(View.GONE);
        ActivityCollector.addActivity(this);
    }

    private void initWebViewSettings() {
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                            v.requestFocusFromTouch();
                        }
                        break;
                }
                return false;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
       // mWebView.getSettings().setDomStorageEnabled(true);
       // mWebView.getSettings().setBlockNetworkImage(false);//防止阻塞加载图片
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
       // mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        if (Build.VERSION.SDK_INT < 19) {
            if (Build.VERSION.SDK_INT >8) {
                mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            }
        }
    }
    private void initWebview() {
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);
    }



    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 在webview加载下一页，而不会打开浏览器
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.e("TAG","onReceivedError"+description);
            mFailingUrl = failingUrl;//记录失败的url

            //view.setLayoutParams();
            view.addJavascriptInterface(new AndroidAndJSInterface(), "Android");
            view.loadUrl("file:///android_asset/error.html");//添加显示本地文件
        }


    }
    class AndroidAndJSInterface{
        @JavascriptInterface
        public void reLoad(){
           // mWebView.loadUrl(url);
            Log.e("TAG", "reload.....");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //重新加载
                    if (mFailingUrl != null){
                        Log.e("TAG","在reload...");
                        mWebView.loadUrl(mFailingUrl);
                    }
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
            mWebView.resumeTimers();
        }
    }
    @Override
    public void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
            mWebView.pauseTimers();
        }
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.clearCache(true);
        mWebView.destroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

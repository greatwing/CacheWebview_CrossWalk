package com.github.greatwing;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkActivity;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;

import ren.yale.android.cachewebviewlib.WebViewCacheInterceptorInst;

public class MainActivity extends XWalkActivity {
    private final String TAG = "greatwing";
    private XWalkView xWalkWebView;
    private XWalkSettings xWVSettings;

    public class JsInterface {
        @JavascriptInterface
        public void greet(String name) {
            System.out.println("Hello, " + name);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xWalkWebView = findViewById(R.id.xWalkView);
    }

    @Override
    protected void onXWalkReady() {
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
        XWalkPreferences.setValue(XWalkPreferences.SPATIAL_NAVIGATION, true);
        XWalkPreferences.setValue(XWalkPreferences.ENABLE_JAVASCRIPT, true);
        XWalkPreferences.setValue(XWalkPreferences.ENABLE_EXTENSIONS, true);

        xWalkWebView.addJavascriptInterface(new JsInterface(),"NativeInterface");

        //获取setting
        xWVSettings = xWalkWebView.getSettings();
        xWVSettings.setSupportZoom(true);
        xWVSettings.setBuiltInZoomControls(true);
        xWVSettings.setLoadWithOverviewMode(true);
        xWVSettings.setUseWideViewPort(true);
        xWVSettings.setLoadsImagesAutomatically(true);
        xWVSettings.setJavaScriptEnabled(true);
        xWVSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        xWVSettings.setSupportMultipleWindows(false);
        xWVSettings.setAllowFileAccess(true);
        xWVSettings.setDomStorageEnabled(true);
        xWVSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        xWVSettings.setAllowUniversalAccessFromFileURLs(true);

        xWalkWebView.setResourceClient(new XWalkResourceClient(xWalkWebView) {
            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptLoadRequest(XWalkView view, String url) {
                return WebViewCacheInterceptorInst.getInstance().interceptRequest(url);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public XWalkWebResourceResponse shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
                return WebResourceResponseAdapter.adapter(this, WebViewCacheInterceptorInst.getInstance().
                        interceptRequest(WebResourceRequestAdapter.adapter(request)));
            }

            @Override
            public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {
                Log.e(TAG, "onReceivedSslError");
                Toast.makeText(MainActivity.this, "证书不合法", Toast.LENGTH_SHORT).show();
            }
        });

        xWalkWebView.loadUrl("http://html5test.com/");
    }
}

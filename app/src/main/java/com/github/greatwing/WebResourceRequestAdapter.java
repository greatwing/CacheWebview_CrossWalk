package com.github.greatwing;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.xwalk.core.XWalkWebResourceRequest;

import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class WebResourceRequestAdapter implements android.webkit.WebResourceRequest {
    private XWalkWebResourceRequest mWebResourceRequest;

    private WebResourceRequestAdapter(XWalkWebResourceRequest xWalkRequest){
        mWebResourceRequest = xWalkRequest;
    }

    public static WebResourceRequestAdapter adapter(XWalkWebResourceRequest xWalkRequest){
        return new WebResourceRequestAdapter(xWalkRequest);
    }

    @Override
    public Uri getUrl() {
        return mWebResourceRequest.getUrl();
    }

    @Override
    public boolean isForMainFrame() {
        return mWebResourceRequest.isForMainFrame();
    }


    @Override
    public boolean isRedirect() {
        return false;
        //return mWebResourceRequest.isRedirect();
    }

    @Override
    public boolean hasGesture() {
        return mWebResourceRequest.hasGesture();
    }

    @Override
    public String getMethod() {
        return mWebResourceRequest.getMethod();
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return mWebResourceRequest.getRequestHeaders();
    }
}

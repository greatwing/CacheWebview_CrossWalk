package com.github.greatwing;


import android.os.Build;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkWebResourceResponse;

import java.io.InputStream;
import java.util.Map;

public class WebResourceResponseAdapter {

    public static XWalkWebResourceResponse adapter(XWalkResourceClient client, android.webkit.WebResourceResponse webResourceResponse){
        if (webResourceResponse == null){
            return null;
        }

        String mimeType = webResourceResponse.getMimeType();
        String encoding = webResourceResponse.getEncoding();
        InputStream data = webResourceResponse.getData();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusCode = webResourceResponse.getStatusCode();
            String reasonPhrase = webResourceResponse.getReasonPhrase();
            Map<String, String> headers = webResourceResponse.getResponseHeaders();

            return client.createXWalkWebResourceResponse(mimeType, encoding, data, statusCode, reasonPhrase, headers);
        }
        else {
            return client.createXWalkWebResourceResponse(mimeType, encoding, data);
        }
    }
}

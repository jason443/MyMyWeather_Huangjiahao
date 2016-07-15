package com.huangjiahao.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ASUS on 2016/7/14.
 */
public class HttpClientUtil {

    public static void sendHttpRequest(final String adress, final HttpCallbackListener listener) {
        HttpClient httpClient = null;
        String response = null;
        try {
            httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,8000);
            HttpGet httpGet = new HttpGet(adress);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                response = entity.toString();
            }
            if((listener != null) && (response !=null)) {
                listener.onFinish(response);
            }
        }catch(Exception e) {
            if(listener != null) {
                listener.onError(e);
            }
        } finally {
            if(httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }

}

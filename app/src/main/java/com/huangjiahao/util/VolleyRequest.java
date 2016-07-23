package com.huangjiahao.util;

import android.os.Handler;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by ASUS on 2016/7/21.
 */
public class VolleyRequest {

    public static void sendVolleyRequest(String address, RequestQueue requestQueue, final Handler handler) {
        StringRequest stringRequest = new StringRequest(address, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Message message = new Message();
                message.what = 0;
                message.obj = s;
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        });
        requestQueue.add(stringRequest);
    }

}

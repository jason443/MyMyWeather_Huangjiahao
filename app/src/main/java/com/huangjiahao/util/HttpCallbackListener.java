package com.huangjiahao.util;

/**
 * Created by ASUS on 2016/7/14.
 */
public interface HttpCallbackListener {

    void onFinish(String response);
    void onError(Exception e);
}

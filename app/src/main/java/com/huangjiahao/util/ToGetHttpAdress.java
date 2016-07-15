package com.huangjiahao.util;

import android.util.Log;

import java.net.URLEncoder;

/**
 * Created by ASUS on 2016/7/15.
 */
public class ToGetHttpAdress {

    public  static  String toURLEncoded(String cityName) {
        if (cityName == null || cityName.equals("")) {
            return "";
        }
        try {
            String str = URLEncoder.encode(cityName, "UTF-8"); //进行Encoder操作
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String excute(String cityName){
        String city = toURLEncoded(cityName);
        String url= "http://v.juhe.cn/weather/index?cityname="+city+"&dtype=json&format=2&key=e3f3e3e2887d9512713dea4dfcfa5786";//拼装成所需格式
        Log.d("Decode","111111111111111");
        return url;
    }

}

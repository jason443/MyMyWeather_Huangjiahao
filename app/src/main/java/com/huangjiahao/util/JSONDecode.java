package com.huangjiahao.util;

import android.util.Log;

import com.huangjiahao.bean.City;
import com.huangjiahao.bean.Province;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2016/7/14.
 */
public class JSONDecode {

    public static ArrayList<Province> provinceDecode(String response) {
        ArrayList<Province> returnList = new ArrayList<>();
        try{
            JSONObject obj = new JSONObject(response);
            String result = obj.getString("result");
            JSONArray array = new JSONArray(result);
            for(int i=0; i<array.length(); i++) {
                String provinceName = array.getJSONObject(i).getString("province");
                returnList.add(new Province(provinceName));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static ArrayList<City> cityDecode(String response, String provinceName) {
        ArrayList<City> returnList = new ArrayList<>();
        try{
            JSONObject obj = new JSONObject(response);
            String result = obj.getString("result");
            JSONArray array = new JSONArray(result);
            for(int i=0; i<array.length(); i++) {
                if(array.getJSONObject(i).getString("province").matches(provinceName)) {
                    String cityName = array.getJSONObject(i).getString("city");
                    returnList.add(new City(cityName));
                    Log.d("JSONDecode",cityName);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static Map<String,String> nowInfoDecode(String response) {
        Map<String,String> map = new HashMap<>();
        try{
            JSONObject obj = new JSONObject(response);
            JSONObject result = obj.getJSONObject("result");
            JSONObject today = result.getJSONObject("today");
            String todayTemp = today.getString("temperature");
            String weather = today.getString("weather");
            JSONObject weatherId = today.getJSONObject("weather_id");
            String id = weatherId.getString("fa");
            map.put("todayTemp","今日温度："+todayTemp);
            map.put("weather","今日天气："+weather);
            map.put("id",id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    public static Map<String,String> serviceInfoDecode(String response) {
        Map<String, String> map = new HashMap<>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONObject result = obj.getJSONObject("result");
            JSONObject today = result.getJSONObject("today");
            String cityName = today.getString("city");
            String weather = today.getString("weather");
            String temp = today.getString("temperature");
            map.put("cityName","你在" + cityName);
            map.put("cityNameSolo",cityName);
            map.put("weather","现在天气是" + weather + "，气温为" + temp);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    public static ArrayList<Map<String, String>> futureDecode(String response) {
        ArrayList<Map<String,String>> returnList = new ArrayList<>();
        try{
            JSONObject obj = new JSONObject(response);
            Log.d("JSONObject",response);
            JSONObject result = obj.getJSONObject("result");
            String futureString = result.getString("future");
            JSONArray future = new JSONArray(futureString);
            for(int i=0; i<future.length(); i++) {
                Map<String,String> map = new HashMap<>();
                map.put("week",future.getJSONObject(i).getString("week"));
                map.put("temperature",future.getJSONObject(i).getString("temperature"));
                map.put("weather",future.getJSONObject(i).getString("weather"));
                returnList.add(map);
            }
            Log.d("JSONDecode",returnList.get(1).get("week"));
        }catch(Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }
}

package com.huangjiahao.util;

import com.google.gson.Gson;
import com.huangjiahao.bean.City;
import com.huangjiahao.bean.Future;
import com.huangjiahao.bean.JsonBean;
import com.huangjiahao.bean.JsonCityBean;
import com.huangjiahao.bean.Place;
import com.huangjiahao.bean.Today;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/7/21.
 */
public class GsonDecode {

    public static Today todayDecode(String s) {
        Gson gson = new Gson();
        JsonBean jsonBean = gson.fromJson(s,JsonBean.class);
        return jsonBean.getResult().getToday();
    }

    public static List<Future> futureDecode(String s) {
        Gson gson = new Gson();
        JsonBean jsonBean = gson.fromJson(s,JsonBean.class);
        List<Future> futures = jsonBean.getResult().getFuture();
        return futures;
    }

    public static List<Place> provinceDecode(String s) {
        Gson gson = new Gson();
        List<Place> returnList = new ArrayList<>();
        JsonCityBean jsonCityBean = gson.fromJson(s, JsonCityBean.class);
        List<Place> places = jsonCityBean.getResult();
        for(int i=0; i<places.size(); i++) {
            if(i == 0) {
                returnList.add(places.get(i));
            } else {
                if(!places.get(i).getProvince().equals(places.get(i-1).getProvince())) {
                    returnList.add(places.get(i));
                }
            }
        }
        return returnList;
    }

    public static List<Place> cityDecode(String s, String province) {
        List<Place> tempList = new ArrayList<>();
        List<Place> returnList = new ArrayList<>();
        Gson gson = new Gson();
        JsonCityBean jsonCityBean = gson.fromJson(s, JsonCityBean.class);
        List<Place> places = jsonCityBean.getResult();
        for(int i=0; i<places.size(); i++) {
            if(places.get(i).getProvince().equals(province)) {
                tempList.add(places.get(i));
            }
        }
        for(int i=0; i<tempList.size(); i++) {
            if(i == 0) {
                returnList.add(tempList.get(i));
            } else {
                if(!tempList.get(i).getCity().equals(tempList.get(i-1).getCity())) {
                    returnList.add(tempList.get(i));
                }
            }
        }
        return returnList;
    }

}

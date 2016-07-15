package com.huangjiahao.util;

import com.huangjiahao.bean.City;
import com.huangjiahao.bean.Province;

import java.util.ArrayList;

/**
 * Created by ASUS on 2016/7/14.
 */
public class ListChangeUtil {
    public static ArrayList<Province> toGetProvince(ArrayList<Province> arr) { //改变数据成为展示省份的方法
        ArrayList<Province> returnList = new ArrayList<Province>();
        for (int i = 0; i < arr.size(); i++) {
            if (i == 0) {
                returnList.add(arr.get(i));
            } else {
                if (!arr.get(i).getProvinceName().equals(arr.get(i - 1).getProvinceName())) {
                    returnList.add(arr.get(i));
                }
            }
        }
        return returnList;
    }

    public static ArrayList<City> toGetCity(ArrayList<City> arr) {
        ArrayList<City> returnList = new ArrayList<City>();
        for(int i=0; i<arr.size(); i++) {
            if(i == 0) {
                returnList.add(arr.get(i));
            }else {
                if(!arr.get(i).getCityName().equals(arr.get(i-1).getCityName())) {
                    returnList.add(arr.get(i));
                }
            }
        }
        return returnList;
    }
}


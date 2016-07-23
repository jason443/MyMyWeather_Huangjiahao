package com.huangjiahao.bean;

/**
 * Created by ASUS on 2016/7/21.
 */
public class Place {

    private String province;
    private String city;

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public Place(String province, String city) {
        this.province = province;
        this.city = city;
    }
}

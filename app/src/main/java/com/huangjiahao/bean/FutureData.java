package com.huangjiahao.bean;

/**
 * Created by ASUS on 2016/7/17.
 */
public class FutureData  {

    private String week;
    private String weather;
    private String temperature;

    public FutureData(String week, String weather, String temperature) {
        this.week = week;
        this.weather = weather;
        this.temperature = temperature;
    }

    public String getWeek() {
        return week;
    }

    public String getWeather() {
        return weather;
    }

    public String getTemperature() {
        return temperature;
    }
}

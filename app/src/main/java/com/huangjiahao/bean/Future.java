package com.huangjiahao.bean;

/**
 * Created by ASUS on 2016/7/21.
 */
public class Future {

    private String temperature;
    private String weather;
    private String week;

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

}

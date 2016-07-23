package com.huangjiahao.bean;

/**
 * Created by ASUS on 2016/7/21.
 */
public class Today {

    private String city;
    private String temperature;
    private String weather;
    private WeatherId weather_id;

    public String getCity() {
        return city;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setWeather_id(WeatherId weather_id) {
        this.weather_id = weather_id;
    }

    public WeatherId getWeather_id() {
        return weather_id;
    }
}

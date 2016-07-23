package com.huangjiahao.bean;

import java.util.List;

/**
 * Created by ASUS on 2016/7/23.
 */
public class Result {

    private Today today;
    private List<Future> future;

    public List<Future> getFuture() {
        return future;
    }

    public void setFuture(List<Future> future) {
        this.future = future;
    }

    public Today getToday() {
        return today;
    }

    public void setToday(Today today) {
        this.today = today;
    }
}

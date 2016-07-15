package com.huangjiahao.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huangjiahao.Service.StatusService;

/**
 * Created by ASUS on 2016/7/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, StatusService.class);
        context.startService(i);
    }

}

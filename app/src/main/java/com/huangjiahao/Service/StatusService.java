package com.huangjiahao.Service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.huangjiahao.R;
import com.huangjiahao.activity.WeatherActivity;
import com.huangjiahao.util.AlarmReceiver;
import com.huangjiahao.util.HttpCallbackListener;
import com.huangjiahao.util.HttpURLUtil;
import com.huangjiahao.util.JSONDecode;
import com.huangjiahao.util.ToGetHttpAdress;

import java.util.Map;

/**
 * Created by ASUS on 2016/7/14.
 */
public class StatusService extends Service {

    public static final int SHOW_RESPONSE = 0;
    public static final int ERROR_RESPONSE = 1;

    private ServiceBroadcastReceiver serviceBroadcastReceiver = null;
    private OnCreateReceiver onCreateReceiver = null;
    private String cityName = "广州";

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String)msg.obj;
                    stopForeground(true);
                    Map<String, String> map = JSONDecode.serviceInfoDecode(response);
                    Intent notificationIntent = new Intent(StatusService.this, WeatherActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(StatusService.this, 0 , notificationIntent, 0);
                    Notification.Builder builder = new Notification.Builder(StatusService.this);
                    cityName = map.get("cityNameSolo");
                    builder.setSmallIcon(R.drawable.small).setContentText(map.get("weather")).setContentTitle(map.get("cityName")).setContentIntent(pendingIntent);
                    Notification notification = builder.getNotification();
                    startForeground(3,notification);
                    break;
                case ERROR_RESPONSE:
                    Log.d("StatusService","ERROR_RESPONSE");
                    break;
                default:
                    break;
            }
        }
    };

    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.weather.REFLUSH");
        serviceBroadcastReceiver = new ServiceBroadcastReceiver();
        registerReceiver(serviceBroadcastReceiver, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("com.weather.ONCREATE");
        onCreateReceiver = new OnCreateReceiver();
        registerReceiver(onCreateReceiver, intentFilter1);

    }

    public IBinder onBind(Intent intent) {
        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        final String adress = ToGetHttpAdress.excute(cityName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLUtil.sendHttpRequest(adress, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = response;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Exception e) {
                        Message message = new Message();
                        message.what = ERROR_RESPONSE;
                        handler.sendMessage(message);
                    }
                });
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int sixHour = 60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + sixHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent,flags,startId);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceBroadcastReceiver);
        unregisterReceiver(onCreateReceiver);
    }


    class ServiceBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            stopForeground(true);
            String response = intent.getStringExtra("refushInfo");
            Map<String, String> map = JSONDecode.serviceInfoDecode(response);
            Intent notificationIntent = new Intent(StatusService.this, WeatherActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(StatusService.this, 0 , notificationIntent, 0);
            Notification.Builder builder = new Notification.Builder(StatusService.this);
            cityName = map.get("cityNameSolo");
            builder.setSmallIcon(R.drawable.small).setContentText(map.get("weather")).setContentTitle(map.get("cityName")).setContentIntent(pendingIntent);
            Notification notification = builder.getNotification();
            startForeground(2,notification);
        }
    }

    class OnCreateReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("createInfo");
            Map<String, String> map = JSONDecode.serviceInfoDecode(response);
            Intent notificationIntent = new Intent(StatusService.this, WeatherActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(StatusService.this, 0 , notificationIntent, 0);
            Notification.Builder builder = new Notification.Builder(StatusService.this);
            cityName = map.get("cityNameSolo");
            builder.setSmallIcon(R.drawable.small).setContentText(map.get("weather")).setContentTitle(map.get("cityName")).setContentIntent(pendingIntent);
            Notification notification = builder.getNotification();
            startForeground(1,notification);
        }
    }

}

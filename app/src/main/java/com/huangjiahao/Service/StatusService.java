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
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.huangjiahao.R;
import com.huangjiahao.Receiver.AlarmReceiver;
import com.huangjiahao.activity.WeatherActivity;
import com.huangjiahao.bean.Today;
import com.huangjiahao.util.GsonDecode;
import com.huangjiahao.util.HttpCallbackListener;
import com.huangjiahao.util.HttpURLUtil;
import com.huangjiahao.util.JSONDecode;
import com.huangjiahao.util.ToGetHttpAdress;
import com.huangjiahao.util.VolleyRequest;

import java.util.Map;

/**
 * Created by ASUS on 2016/7/14.
 */
public class StatusService extends Service {

    private ServiceBroadcastReceiver serviceBroadcastReceiver = null;

    private RequestQueue queue;

    private String cityName = "广州";

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    stopForeground(true);
                    String response = (String)msg.obj;
                    Today today = GsonDecode.todayDecode(response);
                    Intent notificationIntent = new Intent(StatusService.this, WeatherActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(StatusService.this, 0 , notificationIntent, 0);
                    Notification.Builder builder = new Notification.Builder(StatusService.this);
                    builder.setSmallIcon(R.drawable.small).setContentText("现在天气是" + today.getWeather() + "，气温为" + today.getTemperature()).setContentIntent(pendingIntent).setContentTitle("你在" + today.getCity());
                    Notification notification = builder.getNotification();
                    startForeground(2,notification);
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    };

    public void onCreate() {
        super.onCreate();

        queue = Volley.newRequestQueue(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.broadcast.PLACE_CHANGE");
        serviceBroadcastReceiver = new ServiceBroadcastReceiver();
        registerReceiver(serviceBroadcastReceiver,intentFilter);

    }

    public IBinder onBind(Intent intent) {
        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        String address = ToGetHttpAdress.excute(cityName);
        VolleyRequest.sendVolleyRequest(address, queue, handler);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int sixHour = 6*60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + sixHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent,flags,startId);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceBroadcastReceiver);
    }


    class ServiceBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            stopForeground(true);
            Intent notificationIntent = new Intent(StatusService.this, WeatherActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(StatusService.this, 0 , notificationIntent, 0);
            Notification.Builder builder = new Notification.Builder(StatusService.this);
            cityName = intent.getStringExtra("cityName");
            builder.setSmallIcon(R.drawable.small).setContentText("现在天气是" + intent.getStringExtra("weather") + "，气温为" + intent.getStringExtra("temp")).setContentTitle("你在" + intent.getStringExtra("cityName")).setContentIntent(pendingIntent);
            Notification notification = builder.getNotification();
            startForeground(1,notification);
            Log.d("StatusService","1111111111");
        }
    }


}

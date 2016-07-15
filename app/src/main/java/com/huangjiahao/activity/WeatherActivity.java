package com.huangjiahao.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangjiahao.R;
import com.huangjiahao.Service.StatusService;
import com.huangjiahao.util.HttpCallbackListener;
import com.huangjiahao.util.HttpURLUtil;
import com.huangjiahao.util.JSONDecode;
import com.huangjiahao.util.ToGetHttpAdress;

import java.util.HashMap;
import java.util.Map;

public class WeatherActivity extends Activity{

    public static final int SHOW_RESPONSE = 0;
    public static final int ERROR_RESPONSE = 1;

    private String setCityName;

    private TextView mShowCityName;
    private ImageView mShowPicture;
    private TextView mShowWeather;
    private TextView mShowTemp;
    private ImageButton mMenuButton;
    private ImageButton mCancleButton;

    private IntentFilter intentFilter;
    private CallBackBroadcast callBackBroadcast;
    private LocalBroadcastManager localBroadcastManager;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    Map map = (HashMap)JSONDecode.nowInfoDecode(response);
                    if(map != null) {
                        mShowWeather.setText(map.get("weather").toString());
                        mShowTemp.setText(map.get("todayTemp").toString());
                        mShowCityName.setText(setCityName);
                    } else {
                        Toast.makeText(WeatherActivity.this, "数据解析错误！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ERROR_RESPONSE:
                    Toast.makeText(WeatherActivity.this, "网络连接错误！",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String cityName;
        final String adress;
        super.onCreate(savedInstanceState);
        setContentView(com.huangjiahao.R.layout.activity_weather);
        Intent bindIntent = new Intent(this, StatusService.class);
        startService(bindIntent);
        mMenuButton = (ImageButton)findViewById(R.id.weatherActivity_bt_menu);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
        });
        mCancleButton = (ImageButton) findViewById(R.id.weatherActivity_bt_finish);
        mCancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mShowCityName = (TextView)findViewById(R.id.weatherActivity_tv_cityName);
        mShowPicture = (ImageView)findViewById(R.id.weatherActivity_iv_weatherPicture);
        mShowWeather = (TextView)findViewById(R.id.weatherActivity_tv_showWeather);
        mShowTemp = (TextView)findViewById(R.id.weatherActivity_tv_showTemp);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.huangjiahao.broadcast.CITYNAME");
        callBackBroadcast = new CallBackBroadcast();
        localBroadcastManager.registerReceiver(callBackBroadcast,intentFilter);
        cityName = "广州";
        adress = ToGetHttpAdress.excute("广州");
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
                        setCityName = cityName;
                        Intent broadcastIntent = new Intent("com.weather.ONCREATE");
                        broadcastIntent.putExtra("createInfo",response);
                        sendBroadcast(broadcastIntent);
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
    }

    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(callBackBroadcast);
    }

    class CallBackBroadcast extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            final String cityName = intent.getStringExtra("cityName");
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
                            setCityName = cityName;
                            Intent broadcastIntent = new Intent("com.weather.REFLUSH");
                            broadcastIntent.putExtra("refushInfo",response);
                            sendBroadcast(broadcastIntent);
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
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeCity_item:
                final Intent intent = new Intent(WeatherActivity.this, PickProvinceActivity.class);
                startActivity(intent);
                break;
            case R.id.reflush_item:
                Toast.makeText(WeatherActivity.this, "正在刷新",Toast.LENGTH_SHORT).show();
                final String adress = ToGetHttpAdress.excute(setCityName);
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
                                Intent broadcastIntent = new Intent("com.weather.REFLUSH");
                                broadcastIntent.putExtra("refushInfo",response);
                                sendBroadcast(broadcastIntent);
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
                break;
            default:
                break;
        }
        return true;
    }

}

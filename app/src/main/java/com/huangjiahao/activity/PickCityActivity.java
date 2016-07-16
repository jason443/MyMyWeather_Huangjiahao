package com.huangjiahao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.huangjiahao.R;
import com.huangjiahao.adapter.PickCityAdapter;
import com.huangjiahao.bean.City;
import com.huangjiahao.bean.Province;
import com.huangjiahao.util.ActivityCollector;
import com.huangjiahao.util.HttpCallbackListener;
import com.huangjiahao.util.HttpURLUtil;
import com.huangjiahao.util.JSONDecode;
import com.huangjiahao.util.ListChangeUtil;

import java.util.ArrayList;

/**
 * Created by ASUS on 2016/7/14.
 */
public class PickCityActivity extends Activity {

    public static final int SHOW_RESPONSE = 0;
    public static final int ERROR_RESPONSE = 1;

    private String provinceName;
    private ListView mPickCityLv;

    private LocalBroadcastManager localBroadcastManager;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    ArrayList<City> cities = JSONDecode.cityDecode(response,provinceName);
                    final ArrayList<City> showList = ListChangeUtil.toGetCity(cities);
                    PickCityAdapter adapter = new PickCityAdapter(PickCityActivity.this,showList);
                    mPickCityLv.setAdapter(adapter);
                    mPickCityLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            City city = showList.get(position);
                            String cityName = city.getCityName();
                            Intent intent = new Intent("com.huangjiahao.broadcast.CITYNAME");
                            localBroadcastManager = LocalBroadcastManager.getInstance(PickCityActivity.this);
                            intent.putExtra("cityName",cityName);
                            localBroadcastManager.sendBroadcast(intent);
                            ActivityCollector.finishAll();
                            finish();
                        }
                    });
                    break;
                case ERROR_RESPONSE:
                    Toast.makeText(PickCityActivity.this,"网络请求错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_city);
        mPickCityLv = (ListView) findViewById(R.id.pickCity_lv_cityName) ;
        ActivityCollector.addActivity(this);
        LocalBroadcastManager.getInstance(this);
        provinceName = getIntent().getStringExtra("provinceName");
        Log.d("PickCity",provinceName+"111111");
        final String adress = "http://v.juhe.cn/weather/citys?key=e3f3e3e2887d9512713dea4dfcfa5786";
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
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}

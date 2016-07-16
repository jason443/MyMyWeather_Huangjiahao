package com.huangjiahao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huangjiahao.R;
import com.huangjiahao.util.HttpCallbackListener;
import com.huangjiahao.util.HttpURLUtil;
import com.huangjiahao.util.JSONDecode;
import com.huangjiahao.util.ToGetHttpAdress;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ASUS on 2016/7/16.
 */
public class FutureActivity extends Activity {

    public static final int SHOW_RESPONSE = 0;
    public static final int ERROR_RESPONSE = 1;

    private String cityName;
    private TextView mFuture_tv_cityName;

    private TextView mFirst_tv_day;
    private TextView mFirst_tv_temp;
    private TextView mFirst_tv_weather;

    private TextView mSecond_tv_day;
    private TextView mSecond_tv_temp;
    private TextView mSecond_tv_weather;

    private TextView mThird_tv_day;
    private TextView mThird_tv_temp;
    private TextView mThird_tv_weather;

    private TextView mFourth_tv_day;
    private TextView mFourth_tv_temp;
    private TextView mFourth_tv_weather;

    private TextView mFifth_tv_day;
    private TextView mFifth_tv_temp;
    private TextView mFifth_tv_weather;

    private TextView mSixth_tv_day;
    private TextView mSixth_tv_temp;
    private TextView mSixth_tv_weather;

    private TextView mSeventh_tv_day;
    private TextView mSeventh_tv_temp;
    private TextView mSeventh_tv_weather;

    private ImageButton mFuture_bt_back;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_RESPONSE:
                    String response = (String)msg.obj;
                    ArrayList<Map<String,String>> arrayList = JSONDecode.futureDecode(response);
                    for(int i=0; i<arrayList.size(); i++) {
                        switch(i) {
                            case 0:
                                mFirst_tv_day.setText(arrayList.get(i).get("week"));
                                mFirst_tv_temp.setText(arrayList.get(i).get("temperature"));
                                mFirst_tv_weather.setText(arrayList.get(i).get("weather"));
                                break;
                            case 1:
                                mSecond_tv_day.setText(arrayList.get(i).get("week"));
                                mSecond_tv_temp.setText(arrayList.get(i).get("temperature"));
                                mSecond_tv_weather.setText(arrayList.get(i).get("weather"));
                                break;
                            case 2:
                                mThird_tv_day.setText(arrayList.get(i).get("week"));
                                mThird_tv_temp.setText(arrayList.get(i).get("temperature"));
                                mThird_tv_weather.setText(arrayList.get(i).get("weather"));
                                break;
                            case 3:
                                mFourth_tv_day.setText(arrayList.get(i).get("week"));
                                mFourth_tv_temp.setText(arrayList.get(i).get("temperature"));
                                mFourth_tv_weather.setText(arrayList.get(i).get("weather"));
                                break;
                            case 4:
                                mFifth_tv_day.setText(arrayList.get(i).get("week"));
                                mFifth_tv_temp.setText(arrayList.get(i).get("temperature"));
                                mFifth_tv_weather.setText(arrayList.get(i).get("weather"));
                                break;
                            case 5:
                                mSixth_tv_day.setText(arrayList.get(i).get("week"));
                                mSixth_tv_temp.setText(arrayList.get(i).get("temperature"));
                                mSixth_tv_weather.setText(arrayList.get(i).get("weather"));
                                break;
                            case 6:
                                mSeventh_tv_day.setText(arrayList.get(i).get("week"));
                                mSeventh_tv_temp.setText(arrayList.get(i).get("temperature"));
                                mSeventh_tv_weather.setText(arrayList.get(i).get("weather"));
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case ERROR_RESPONSE:
                    Toast.makeText(FutureActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);

        mFuture_tv_cityName = (TextView) findViewById(R.id.future_tv_cityName);

        mFuture_bt_back = (ImageButton)findViewById(R.id.future_bt_back);
        mFuture_bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFirst_tv_day = (TextView) findViewById(R.id.future_first_day);
        mFirst_tv_temp = (TextView)findViewById(R.id.future_first_temp);
        mFirst_tv_weather = (TextView)findViewById(R.id.future_first_weather);
        mSecond_tv_day = (TextView) findViewById(R.id.future_second_day);
        mSecond_tv_temp = (TextView) findViewById(R.id.future_second_temp);
        mSecond_tv_weather = (TextView) findViewById(R.id.future_second_weather);
        mThird_tv_day = (TextView) findViewById(R.id.future_third_day);
        mThird_tv_temp = (TextView) findViewById(R.id.future_third_temp);
        mThird_tv_weather = (TextView) findViewById(R.id.future_third_weather);
        mFourth_tv_day = (TextView) findViewById(R.id.future_fourth_day);
        mFourth_tv_temp = (TextView) findViewById(R.id.future_fourth_temp);
        mFourth_tv_weather = (TextView) findViewById(R.id.future_fourth_weather);
        mFifth_tv_day = (TextView)findViewById(R.id.future_fifth_day);
        mFifth_tv_temp = (TextView) findViewById(R.id.future_fifth_temp);
        mFifth_tv_weather = (TextView) findViewById(R.id.future_fifth_weather);
        mSixth_tv_day = (TextView) findViewById(R.id.future_sixth_day);
        mSixth_tv_temp = (TextView) findViewById(R.id.future_sixth_temp);
        mSixth_tv_weather = (TextView) findViewById(R.id.future_sixth_weather);
        mSeventh_tv_day = (TextView) findViewById(R.id.future_seventh_day);
        mSeventh_tv_temp = (TextView) findViewById(R.id.future_seventh_temp);
        mSeventh_tv_weather = (TextView) findViewById(R.id.future_seventh_weather);
        Intent intent = getIntent();
        cityName = intent.getStringExtra("cityName");
        mFuture_tv_cityName.setText(cityName);
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
    }

}

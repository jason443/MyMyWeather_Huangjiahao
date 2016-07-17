package com.huangjiahao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangjiahao.R;
import com.huangjiahao.adapter.FutureInfoAdapter;
import com.huangjiahao.bean.FutureData;
import com.huangjiahao.util.HttpCallbackListener;
import com.huangjiahao.util.HttpURLUtil;
import com.huangjiahao.util.JSONDecode;
import com.huangjiahao.util.ToGetHttpAdress;

import java.util.List;

/**
 * Created by ASUS on 2016/7/17.
 */
public class FutureActivityFuben extends Activity {

    public static final int SHOW_RESPONSE = 0;
    public static final int ERROR_RESPONSE = 1;

    private String cityName;
    private TextView mFuture_tv_cityName;
    private ImageButton mFuture_bt_back;
    private ListView mShowInfoLv;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String)msg.obj;
                    List<FutureData> futureDatas = JSONDecode.futureDecode1(response);
                    FutureInfoAdapter adapter = new FutureInfoAdapter(FutureActivityFuben.this, futureDatas);
                    mShowInfoLv.setAdapter(adapter);
                    break;
                case ERROR_RESPONSE:
                    Toast.makeText(FutureActivityFuben.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_fuben);
        mFuture_tv_cityName = (TextView) findViewById(R.id.future_tv_cityName);

        mFuture_bt_back = (ImageButton)findViewById(R.id.future_bt_back);
        mFuture_bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mShowInfoLv = (ListView) findViewById(R.id.future_lv_show);

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

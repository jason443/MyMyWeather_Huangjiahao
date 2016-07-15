package com.huangjiahao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.huangjiahao.R;
import com.huangjiahao.adapter.PickProvinceAdapter;
import com.huangjiahao.bean.Province;
import com.huangjiahao.util.ActivityCollector;
import com.huangjiahao.util.HttpCallbackListener;
import com.huangjiahao.util.HttpURLUtil;
import com.huangjiahao.util.JSONDecode;
import com.huangjiahao.util.ListChangeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/7/14.
 */
public class PickProvinceActivity extends Activity {

    public static final int SHOW_RESPONSE = 0;
    public static final int ERROR_RESPONSE = 1;

    private ListView mPickProvinceLv;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_RESPONSE:
                    String response = (String)msg.obj;
                    ArrayList<Province> provinces = JSONDecode.provinceDecode(response);
                    final List<Province> showList = ListChangeUtil.toGetProvince(provinces);
                    mPickProvinceLv = (ListView) findViewById(R.id.pickProvince_lv_ProvinceName);
                    PickProvinceAdapter adapter = new PickProvinceAdapter(PickProvinceActivity.this,showList);
                    mPickProvinceLv.setAdapter(adapter);
                    mPickProvinceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Province province = showList.get(position);
                            String provinceName = province.getProvinceName();
                            Intent intent = new Intent(PickProvinceActivity.this, PickCityActivity.class);
                            intent.putExtra("provinceName",provinceName);
                            Log.d("PickProvince",provinceName + "111111");
                            startActivity(intent);
                        }
                    });
                    break;
                case ERROR_RESPONSE:
                    Toast.makeText(PickProvinceActivity.this, "网络请求错误",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_province);
        ActivityCollector.addActivity(this);
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

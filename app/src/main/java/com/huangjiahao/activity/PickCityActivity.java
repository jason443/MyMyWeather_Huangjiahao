package com.huangjiahao.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.huangjiahao.db.MyDatabaseHelper;
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
public class PickCityActivity extends Activity {

    public static final int SHOW_RESPONSE = 0;
    public static final int ERROR_RESPONSE = 1;

    private String provinceName;
    private ListView mPickCityLv;

    private LocalBroadcastManager localBroadcastManager;

    private MyDatabaseHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_city);
        mPickCityLv = (ListView) findViewById(R.id.pickCity_lv_cityName) ;
        ActivityCollector.addActivity(this);
        LocalBroadcastManager.getInstance(this);
        provinceName = getIntent().getStringExtra("provinceName");

        dbHelper = MyDatabaseHelper.getInstance(PickCityActivity.this, "Place.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final List<City> cities = new ArrayList<>();
        Cursor cursor = db.query("City", null, "province = ?", new String[]{provinceName}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String city = cursor.getString(cursor.getColumnIndex("city"));
                cities.add(new City(city));
            } while(cursor.moveToNext());
        }


        PickCityAdapter adapter = new PickCityAdapter(PickCityActivity.this, cities);
        mPickCityLv.setAdapter(adapter);
        mPickCityLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = cities.get(position);
                String cityName = city.getCityName();

                SharedPreferences preferences = getSharedPreferences("cityData",MODE_PRIVATE);
                boolean flags = false;
                int index = 0;
                int count = preferences.getInt("count",0);
                List<String> list = new ArrayList<>();
                for(int i=0; i<count+1; i++) {
                    if(cityName.equals(preferences.getString("data"+i, null))) {
                        flags = true;
                        index = i;
                    }
                    list.add(preferences.getString("data"+i,null));
                }
                count++;
                list.add(cityName);
                SharedPreferences.Editor editor = preferences.edit();
                for(int i=0; i< list.size(); i++) {
                    editor.putString("data"+i,list.get(i));
                }
                editor.putInt("count",count);
                editor.commit();

                Intent intent = new Intent("com.broadcast.CITY_PICK");
                localBroadcastManager = LocalBroadcastManager.getInstance(PickCityActivity.this);
                localBroadcastManager.sendBroadcast(intent);
                intent.putExtra("flags", flags);
                intent.putExtra("index",index);
                ActivityCollector.finishAll();
                finish();
            }
        });
    }


    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}

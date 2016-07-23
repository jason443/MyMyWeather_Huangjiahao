package com.huangjiahao.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.huangjiahao.R;
import com.huangjiahao.adapter.PickProvinceAdapter;
import com.huangjiahao.bean.Place;
import com.huangjiahao.bean.Province;
import com.huangjiahao.db.MyDatabaseHelper;
import com.huangjiahao.util.ActivityCollector;
import com.huangjiahao.util.GsonDecode;
import com.huangjiahao.util.HttpCallbackListener;
import com.huangjiahao.util.HttpURLUtil;
import com.huangjiahao.util.JSONDecode;
import com.huangjiahao.util.ListChangeUtil;
import com.huangjiahao.util.VolleyRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/7/14.
 */
public class PickProvinceActivity extends Activity {

    private ListView mPickProvinceLv;
    private RequestQueue queue;
    private MyDatabaseHelper dbHelper;
    private PickProvinceAdapter adapter;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    String response = (String)msg.obj;
                    dbHelper = MyDatabaseHelper.getInstance(PickProvinceActivity.this, "Place.db", null, 1);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.beginTransaction();
                    try {
                        ContentValues values = new ContentValues();
                        List<Place> places = GsonDecode.provinceDecode(response);
                        for (int i = 0; i < places.size(); i++) {
                            values.put("province", places.get(i).getProvince());
                            db.insert("Province", null, values);
                            values.clear();
                        }

                        for (int i = 0; i < places.size(); i++) {
                            List<Place> cityList = GsonDecode.cityDecode(response, places.get(i).getProvince());
                            ContentValues contentValues = new ContentValues();
                            for (int k = 0; k < cityList.size(); k++) {
                                contentValues.put("city", cityList.get(k).getCity());
                                contentValues.put("province", cityList.get(k).getProvince());
                                db.insert("City", null, contentValues);
                                contentValues.clear();
                            }
                        }
                        db.setTransactionSuccessful();
                    } catch(Exception e) {
                        e.printStackTrace();
                    } finally {
                        db.endTransaction();
                    }

                    Cursor cursor = db.query("Province", null, null, null, null, null, null);
                    final List<Province> provinces = new ArrayList<>();
                    if(cursor.moveToFirst()) {
                        do {
                            String province = cursor.getString(cursor.getColumnIndex("province"));
                            provinces.add(new Province(province));
                        } while(cursor.moveToNext());
                    }

                    adapter = new PickProvinceAdapter(PickProvinceActivity.this, provinces);
                    mPickProvinceLv.setAdapter(adapter);
                    mPickProvinceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Province province = provinces.get(position);
                            Intent intent = new Intent(PickProvinceActivity.this, PickCityActivity.class);
                            intent.putExtra("provinceName", province.getProvinceName());
                            Log.d("PickProvinceActivity", "在网络中查找");
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
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
        mPickProvinceLv = (ListView) findViewById(R.id.pickProvince_lv_ProvinceName);
        queue = Volley.newRequestQueue(this);
        ActivityCollector.addActivity(this);

        dbHelper = MyDatabaseHelper.getInstance(PickProvinceActivity.this,"Place.db", null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final List<Province> provinces = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                String province = cursor.getString(cursor.getColumnIndex("province"));
                provinces.add(new Province(province));
            } while(cursor.moveToNext());
        }

        if(provinces.size() > 0) {
            adapter = new PickProvinceAdapter(PickProvinceActivity.this, provinces);
            mPickProvinceLv.setAdapter(adapter);
            mPickProvinceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Province province = provinces.get(position);
                    String  s = province.getProvinceName();
                    Intent intent = new Intent(PickProvinceActivity.this, PickCityActivity.class);
                    intent.putExtra("provinceName", s);
                    Log.d("PickProvinceActivity", "数据库中查找");
                    startActivity(intent);
                }
            });
        } else {
            String address = "http://v.juhe.cn/weather/citys?key=932899bf88adaab75cebf1406bee49f6";
            VolleyRequest.sendVolleyRequest(address,queue,handler);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}

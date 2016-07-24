package com.huangjiahao.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
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
import com.huangjiahao.adapter.FragAdapter;
import com.huangjiahao.fragment.MyFragment;
import com.huangjiahao.util.HttpCallbackListener;
import com.huangjiahao.util.HttpURLUtil;
import com.huangjiahao.util.JSONDecode;
import com.huangjiahao.util.ToGetHttpAdress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherActivity extends FragmentActivity{

    private ViewPager mViePager;
    private FragmentManager fm;
    private FragAdapter fragAdapter;

    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private CallBackBroadcast callBackBroadcast;
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mViePager = (ViewPager) findViewById(R.id.weather_vp_show);
        fm = getSupportFragmentManager();

        Intent bindIntent = new Intent(this, StatusService.class);//开启服务
        startService(bindIntent);

        sharedPreferences = getSharedPreferences("cityData", MODE_PRIVATE);
        boolean flags = sharedPreferences.getBoolean("flags", true);
        if(flags) { //初始化
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("flags", false);
            editor.putString("data0","广州");
            editor.putInt("count",0);
            editor.commit();
        }

        int count = sharedPreferences.getInt("count", 0); // 取出
        List<String> list = new ArrayList<>();
        for(int i=0; i<count+1; i++) {
            list.add(sharedPreferences.getString("data"+i, null));
        }

        List<MyFragment> myFragments = new ArrayList<>(); // 初始化需要加载的fragment
        for(int i=0; i<list.size(); i++) {
            myFragments.add(new MyFragment());
        }

        fragAdapter = new FragAdapter(fm, myFragments);
        mViePager.setAdapter(fragAdapter);

        for(int i=0; i<myFragments.size(); i++) { // 向fragment传递对应城市信息数据
            Bundle bundle = new Bundle();
            bundle.putString("cityName", list.get(i));
            myFragments.get(i).setArguments(bundle);
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.broadcast.CITY_PICK");
        callBackBroadcast = new CallBackBroadcast();
        localBroadcastManager.registerReceiver(callBackBroadcast,intentFilter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.changeCity_item:
                Intent intent = new Intent(WeatherActivity.this,PickProvinceActivity.class);
                startActivity(intent);
                break;
            case R.id.reflush_item:
                MyFragment myFragment =(MyFragment) fragAdapter.fragment;
                myFragment.onRefresh();
                break;
            case R.id.future_item:
                MyFragment myFragment1 = (MyFragment) fragAdapter.fragment;
                String cityName = myFragment1.getmCityName();
                Intent intent1 = new Intent(WeatherActivity.this, FutureActivity.class);
                intent1.putExtra("cityName", cityName);
                startActivity(intent1);
                break;
            default:
                break;
        }
        return true;
    }

    class CallBackBroadcast extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            boolean flags = intent.getBooleanExtra("flags",false);

            if(flags) {
                int index = intent.getIntExtra("index",0);
                mViePager.setCurrentItem(index);
            } else {
                sharedPreferences = getSharedPreferences("cityData", MODE_PRIVATE);
                int count = sharedPreferences.getInt("count", 0); // 取出
                List<String> list = new ArrayList<>();
                for(int i=0; i<count+1; i++) {
                    list.add(sharedPreferences.getString("data"+i, null));
                }

                List<MyFragment> myFragments = new ArrayList<>();
                for(int i=0; i<list.size(); i++) {
                    myFragments.add(new MyFragment());
                }

                for(int i=0; i<myFragments.size(); i++) { // 向fragment传递对应城市信息数据
                    Bundle bundle = new Bundle();
                    bundle.putString("cityName", list.get(i));
                    myFragments.get(i).setArguments(bundle);
                }

                fragAdapter.upDataList(myFragments);

                mViePager.setCurrentItem(myFragments.size()-1);
            }
        }
    }


}

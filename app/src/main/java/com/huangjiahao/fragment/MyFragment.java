package com.huangjiahao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.huangjiahao.R;
import com.huangjiahao.bean.Today;
import com.huangjiahao.util.GsonDecode;
import com.huangjiahao.util.ToGetHttpAdress;
import com.huangjiahao.util.VolleyRequest;

/**
 * Created by ASUS on 2016/7/24.
 */
public class MyFragment extends Fragment {

    private View mView;
    private ImageButton mMenuButton;
    private ImageButton mCancleButton;
    private ImageView mShowPicture;
    private TextView mShowCityName;
    private TextView mShowWeather;
    private TextView mShowTemp;

    private String mCityName;

    private RequestQueue queue;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    Today today = GsonDecode.todayDecode((String)message.obj);
                    mShowCityName.setText(today.getCity());
                    mShowWeather.setText(today.getWeather());
                    mShowTemp.setText(today.getTemperature());
                    int i = Integer.parseInt(today.getWeather_id().getFa());
                    if(i == 00) {
                        mShowPicture.setImageResource(R.drawable.sunsmile);
                    } else if(((i>=03)&&(i<=12))||(i == 19)||((i>=21)&&(i<=25))) {
                        mShowPicture.setImageResource(R.drawable.rain);
                    } else if(((i>=13)&&(i<=17))||((i>=26)&&(i<=28))) {
                        mShowPicture.setImageResource(R.drawable.snow);
                    } else if(((i>=01)&&(i<=02))||(i == 18)) {
                        mShowPicture.setImageResource(R.drawable.cloud);
                    }else {
                        mShowPicture.setImageResource(R.drawable.sand);
                    }
                    break;
                case 1:
                    Toast.makeText(getActivity(), "网络异常请重试", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container == null) {
            return null;
        } else {
            mView = inflater.inflate(R.layout.fragment_weather, null);
            mMenuButton = (ImageButton) mView.findViewById(R.id.fragment_bt_menu);
            mMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().openOptionsMenu();
                }
            });

            mCancleButton = (ImageButton) mView.findViewById(R.id.fragment_bt_finish);
            mCancleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            mShowPicture = (ImageView) mView.findViewById(R.id.fragment_iv_weatherPicture);
            mShowCityName = (TextView) mView.findViewById(R.id.fragment_tv_cityName);
            mShowWeather = (TextView) mView.findViewById(R.id.fragment_tv_showWeather);
            mShowTemp = (TextView) mView.findViewById(R.id.fragment_tv_showTemp);

            mCityName = getArguments().getString("cityName");
            String address = ToGetHttpAdress.excute(mCityName);
            queue = Volley.newRequestQueue(getActivity());
            VolleyRequest.sendVolleyRequest(address, queue, handler);

            return mView;
        }
    }

    public void onRefresh() {
        String address = ToGetHttpAdress.excute(mCityName);
        Toast.makeText(getActivity(),"正在刷新", Toast.LENGTH_SHORT).show();
        VolleyRequest.sendVolleyRequest(address, queue, handler);
    }

    public String getmCityName() {
        return mCityName;
    }

    public void onResume() {
        super.onResume();
        Intent intent = new Intent("com.broadcast.PLACE_CHANGE");
        intent.putExtra("cityName",mShowCityName.getText());
        intent.putExtra("weather", mShowWeather.getText());
        intent.putExtra("temp", mShowTemp.getText());
        getActivity().sendBroadcast(intent);
        Log.d("MyFragment","111111111");
    }

}

package com.huangjiahao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huangjiahao.R;
import com.huangjiahao.bean.FutureData;

import java.util.List;

/**
 * Created by ASUS on 2016/7/17.
 */
public class FutureInfoAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private List<FutureData> futureDatas = null;

    public FutureInfoAdapter(Context context, List<FutureData> futureDatas){
        this.inflater = LayoutInflater.from(context);
        this.futureDatas = futureDatas;
    }

    public int getCount() {
        return (futureDatas == null)?0:futureDatas.size();
    }

    public Object getItem(int position) {
        return futureDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        FutureData futureData = (FutureData) getItem(position);
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.future_data_item,null);
            viewHolder = new ViewHolder();
            viewHolder.day = (TextView)convertView.findViewById(R.id.future_item_tv_day);
            viewHolder.temp = (TextView) convertView.findViewById(R.id.future_item_tv_temp);
            viewHolder.weather = (TextView) convertView.findViewById(R.id.future_item_tv_weather);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.day.setText(futureData.getWeek());
        viewHolder.temp.setText(futureData.getTemperature());
        viewHolder.weather.setText(futureData.getWeather());
        return convertView;
    }

    class ViewHolder {
        TextView day;
        TextView temp;
        TextView weather;
    }
}

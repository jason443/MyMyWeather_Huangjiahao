package com.huangjiahao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huangjiahao.R;
import com.huangjiahao.bean.City;

import java.util.List;

/**
 * Created by ASUS on 2016/7/14.
 */
public class PickCityAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private List<City> cities = null;

    public PickCityAdapter(Context context, List<City> cities) {
        inflater = LayoutInflater.from(context);
        this.cities = cities;
    }

    public int getCount() {
        return (cities == null)?0:cities.size();
    }

    public Object getItem(int position) {
        return cities.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View converView, ViewGroup parent) {
        City city = (City) getItem(position);
        ViewHolder viewHolder = null;
        if(converView == null) {
            converView = inflater.inflate(R.layout.pick_city_item, null);
            viewHolder = new ViewHolder();
            viewHolder.cityName = (TextView) converView.findViewById(R.id.pickCity_tv_cityName);
            converView.setTag(viewHolder);
        } else {
            viewHolder =(ViewHolder)converView.getTag();
        }
        viewHolder.cityName.setText(city.getCityName());
        return converView;
    }

    class ViewHolder {
        TextView cityName;
    }

}

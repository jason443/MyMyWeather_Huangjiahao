package com.huangjiahao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huangjiahao.R;
import com.huangjiahao.bean.Province;

import java.util.List;

/**
 * Created by ASUS on 2016/7/14.
 */
public class PickProvinceAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private List<Province> provinces = null;

    public PickProvinceAdapter(Context context, List<Province> provinces) {
        inflater = LayoutInflater.from(context);
        this.provinces = provinces;
    }

    public int getCount() {
        return (provinces == null)?0:provinces.size();
    }

    public Object getItem(int position) {
        return provinces.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Province province = (Province) getItem(position);
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.pick_province_item, null);
            viewHolder = new ViewHolder();
            viewHolder.provinceName = (TextView) convertView.findViewById(R.id.pickProvince_tv_provinceName);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.provinceName.setText(province.getProvinceName());
        return convertView;
    }

    class ViewHolder {
        TextView provinceName;
    }

}

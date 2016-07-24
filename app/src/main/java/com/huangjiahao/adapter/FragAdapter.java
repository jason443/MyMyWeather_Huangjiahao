package com.huangjiahao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.huangjiahao.fragment.MyFragment;

import java.util.List;

/**
 * Created by ASUS on 2016/7/24.
 */
public class FragAdapter extends FragmentStatePagerAdapter {

    public Fragment fragment;

    private List<MyFragment> myFragments;

    public FragAdapter(FragmentManager fm, List<MyFragment> myFragments) {
        super(fm);
        this.myFragments = myFragments;
    }

    public int getCount() {
        return myFragments.size();
    }

    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        fragment = (MyFragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public void upDataList(List<MyFragment> list) {
        this.myFragments.clear();
        this.myFragments.addAll(list);
        notifyDataSetChanged();
    }

    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

}

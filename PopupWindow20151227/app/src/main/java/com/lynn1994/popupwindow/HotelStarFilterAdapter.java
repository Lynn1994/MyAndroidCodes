package com.lynn1994.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Lynn on 2015/12/16.
 */
public class HotelStarFilterAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private int[] starInfo = {0, 1, 2, 3, 4, 5};
    private Context Activity;

    public HotelStarFilterAdapter(Context context) {
        Activity = context;
        myInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return starInfo.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return starInfo[position];
    }

    /**
     * 获取成员变量
     */
    public final class ViewHolder {
        public TextView showStarInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            // 初始化布局
            convertView = myInflater
                    .inflate(R.layout.hotel_filter_item, null);
            // 绑定控件
            holder.showStarInfo = (TextView) convertView
                    .findViewById(R.id.hotelFilterItem);
            // 将控件放入布局F
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (starInfo[position] != 0)
            holder.showStarInfo.setText(starInfo[position] + "星级");
        else
            holder.showStarInfo.setText("全部...");
        return convertView;
    }
}

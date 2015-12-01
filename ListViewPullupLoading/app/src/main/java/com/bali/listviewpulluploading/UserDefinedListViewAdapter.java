package com.bali.listviewpulluploading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lynn on 2015/12/1.
 */
public class UserDefinedListViewAdapter extends BaseAdapter{
    // This variable for saving data
    private List<Map<String,String>> Data = new ArrayList<Map<String, String>>();
    // This variable for get local context
    private Context context;
    // This variable for setting activity layout
    private LayoutInflater myLayoutInflater;
    // Initialize user-defined ListView Adapter
    public  UserDefinedListViewAdapter(List<Map<String,String>> dataTemp,Context context){
        // Get data
        this.Data = dataTemp;
        // Get Local context
        this.context = context;
        // Initialize Layout
        myLayoutInflater = LayoutInflater.from(context);
    }
    /***
     *
     * This method for getting the number of item
     */
    @Override
    public int getCount() {
        return Data.size();
    }
    /**
     * This method for getting item information
     * */
    @Override
    public Object getItem(int position) {
        return Data.get(position);
    }
    /**
     * This method for getting item id
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * This method for initializing the view of ListView
     * if the result of getCount() is not zero, it will be called
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Defined a new ViewHolder for initializing controls
        ViewHolder myHolder = null;
        /* judge the convertView
        * if the convertView is null, create a new convertView
        * if the convertView is not null, set myHolder equals to convert view's tag
        * */
        if(convertView == null){
            // Initialize myHolder for getting my controls
            myHolder = new ViewHolder();
            // Initialize layout
            convertView = myLayoutInflater.inflate(R.layout.list_view_item,null);
            // Initialize controls
            myHolder.mainItem = (TextView) convertView.findViewById(R.id.mainItem);
            myHolder.subItem = (TextView) convertView.findViewById(R.id.subItem);
            // Set convert view tag
            convertView.setTag(myHolder);
        }else{
            // Set myHolder , get convertView's tag
            myHolder = (ViewHolder) convertView.getTag();
        }
        // Set Information
        myHolder.mainItem.setText(Data.get(position).get(Constant.MAIN_ITEM));
        myHolder.subItem.setText(Data.get(position).get(Constant.SUB_ITEM));
        return convertView;
    }
    /**
     * Defined this class for getting my controls
     * */
    class ViewHolder {
        /* Follow code for defining my controls names,*/
        // Defined a TextView for getting main-item
        private TextView mainItem;
        // Defined a TextView for getting sub-item
        private TextView subItem;
    }
    /**
     * This method for adding item information
     * */
    public void addItem(HashMap<String,String> dataTemp){
        Data.add(dataTemp);
    }
}

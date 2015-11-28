package com.bali.hotelview;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a simple list view adapter for hotel information
 * Created by Lynn on 2015/11/28.
 */
public class HotelViewAdapter extends BaseAdapter {
    /* Follow Codes for defined variables */
    // Save hotel information html address
    public List<Map<String,String>> hotelInformationAddress = new ArrayList<>();
    // Save hotel image address
    public List<Map<String,String>> hotelImageAddress = new ArrayList<>();
    // Save hotel Chinese name
    public List<Map<String,String>> hotelChineseName = new ArrayList<>();
    // Save hotel English name
    public List<Map<String,String>> hotelEnglishName = new ArrayList<>();
    // Save hotel star information
    public List<Map<String,String>> hotelStarInfo = new ArrayList<>();
    // Save hotel category infomation
    public List<Map<String,String>> hotelCategoryInfo = new ArrayList<>();
    // Save hotel Latitude
    public List<Map<String,String>> hotelLatitude = new ArrayList<>();
    // Save hotel Longitude
    public List<Map<String,String>> hotelLongitude = new ArrayList<>();
    // Save the result of distance
    public List<Map<String,String>> hotelDistance = new ArrayList<>();
    // Save the result of hotel bookings information
    public Map<Integer,Boolean> hotelIsAvailable = new HashMap<Integer,Boolean>();
    // Save the hotel reservation price
    public List<Map<String,String>> hotelReservationPrice = new ArrayList<>();
    // Defined a context for getting local context
    public Context hotelViewActivity;
    // Defined a LayoutInflater for getting layout
    public LayoutInflater myInflater;
    /**
     *　Initialize HotelViewAdapter adapter
     * */
    public HotelViewAdapter(Context context){
        hotelViewActivity = context;
        myInflater = LayoutInflater.from(context);
        parseWebServiceData();
    }
    /**
     *  Defined member variables
     * */
    public final class ViewHolder{
        public ImageView imgHotel;
        public TextView hotelChineseName;
        public TextView hotelEnglishName;
        public TextView hotelStarInfo;
        public TextView hotelCategoryInfo;
        public TextView hotelDistance;
        public Button hotelIsAvailable;
    }
    /**
     *  Initialize adapter methods
     * */
    @Override
    public int getCount() {
        return hotelImageAddress.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    /**
     *  Parsing webService data
     * */
    public void parseWebServiceData(){
        for(int i = 0; i<10;i++){
            HashMap<String,String> info = new HashMap<String,String>();
            String strHotelImageAddress = "HotelImageAddress"+" "+String.valueOf(i);
            info.put("HotelImageAddress", strHotelImageAddress);
            hotelImageAddress.add(info);
            HashMap<String,String> info2 = new HashMap<String,String>();
            String strHotelChineseName = "HotelChineseName"+" "+String.valueOf(i);
            info2.put("HotelChineseName",strHotelChineseName);
            hotelChineseName.add(info2);
            HashMap<String,String> info3 = new HashMap<String,String>();
            String strHotelEnglishName = "HotelEnglishName"+" "+String.valueOf(i);
            info3.put("HotelEnglishName",strHotelEnglishName);
            hotelEnglishName.add(info3);
            HashMap<String,String> info4 = new HashMap<String,String>();
            String strHotelStarInfo = "HotelStarInfo"+" "+String.valueOf(i);
            info4.put("HotelStarInfo",strHotelStarInfo);
            hotelStarInfo.add(info4);
            HashMap<String,String> info5 = new HashMap<String,String>();
            String strHotelCategoryInfo = "HotelCategoryInfo"+" "+String.valueOf(i);
            info5.put("HotelCategoryInfo",strHotelCategoryInfo);
            hotelCategoryInfo.add(info5);
            HashMap<String,String> info6 = new HashMap<String,String>();
            String strHotelDistance = "HotelDistance"+" "+String.valueOf(i);
            info6.put("HotelDistance",strHotelDistance);
            hotelDistance.add(info6);
            HashMap<String,String> info7 = new HashMap<String,String>();
            String strHotelReservationPrice = "5000";
            info7.put("HotelReservationPrice",strHotelReservationPrice);
            hotelReservationPrice.add(info7);
        }
        for(int i = 0; i<hotelImageAddress.size();i++){
            if(i%2 == 0)
                hotelIsAvailable.put(i,false);
            else
                hotelIsAvailable.put(i,true);
        }
    }
    /**
     *  Set member variables information
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //parseWebServiceData();
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            // initialize layout
            convertView = myInflater.inflate(R.layout.hotel_listview_item,null);
            // get my controls
            holder.imgHotel = (ImageView) convertView.findViewById(R.id.imgHotel);
            holder.hotelChineseName = (TextView) convertView.findViewById(R.id.txtHotelCName);
            holder.hotelEnglishName = (TextView) convertView.findViewById(R.id.txtHotelEName);
            holder.hotelStarInfo = (TextView) convertView.findViewById(R.id.txtHotelStarInfo);
            holder.hotelCategoryInfo = (TextView) convertView.findViewById(R.id.txtHotelCategoryInfo);
            holder.hotelDistance = (TextView) convertView.findViewById(R.id.txtHotelDistance);
            holder.hotelIsAvailable = (Button) convertView.findViewById(R.id.btnHotelReservation);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        // set information
        holder.imgHotel.setBackgroundResource(R.drawable.ic_launcher);
        holder.hotelChineseName.setText(hotelChineseName.get(position).get("HotelChineseName").toString());
        holder.hotelEnglishName.setText(hotelEnglishName.get(position).get("HotelEnglishName").toString());
        holder.hotelStarInfo.setText(hotelStarInfo.get(position).get("HotelStarInfo").toString());
        holder.hotelCategoryInfo.setText(hotelCategoryInfo.get(position).get("HotelCategoryInfo").toString());
        holder.hotelDistance.setText(hotelDistance.get(position).get("HotelDistance").toString());
        holder.hotelIsAvailable.setText(hotelReservationPrice.get(position).get("HotelReservationPrice").toString());
        if(hotelIsAvailable.get(position)) {
            holder.hotelIsAvailable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(hotelViewActivity,"Hotel reservation detail!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(hotelViewActivity, HotelInfoActivity.class);
                    hotelViewActivity.startActivity(intent);
                }
            });
        }else{
            holder.hotelIsAvailable.setBackgroundColor(0x949494);
        }
        return convertView;
    }
}

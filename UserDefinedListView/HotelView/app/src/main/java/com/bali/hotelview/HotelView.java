package com.bali.hotelview;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class HotelView extends AppCompatActivity {
    public Context context;
    public ListView list;
    public Button btn;
    public HotelViewAdapter hotelViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_view);
        hotelViewAdapter = new HotelViewAdapter(this);
        list = (ListView) findViewById(R.id.hotelListView);
//        list.setItemsCanFocus(false);
        list.setAdapter(hotelViewAdapter);
//        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HotelView.this,"Hotel detail!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HotelView.this, HotelInfoActivity.class);
                HotelView.this.startActivity(intent);
            }
        });

    }
}

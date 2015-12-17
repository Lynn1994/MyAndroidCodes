package com.lynn1994.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button b;
    PopupWindow popupWindow;
    private ListView list;
    private HotelStarFilterAdapter hotelStarFilterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hotelStarFilterAdapter = new HotelStarFilterAdapter(MainActivity.this);
        b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow(v, MainActivity.this);
            }
        });
    }

    private void initPopupWindow(View parent, Context context) {
        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.hotel_star_filter, null);
            // 设置popupWindow显示格式
            popupWindow = new PopupWindow(v, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            try {
                list = (ListView) v.findViewById(R.id.list);
                list.setAdapter(hotelStarFilterAdapter);
                list.setItemsCanFocus(true);
                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HotelStarFilterAdapter.ViewHolder holder = (HotelStarFilterAdapter.ViewHolder) view.getTag();
                    b.setText(holder.showStarInfo.getText());
                    Toast.makeText(MainActivity.this, String.valueOf(hotelStarFilterAdapter.getItemId(position)), Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            });

        }
        // 这是背景，如果不设置将无法显示Popupwindow.
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        // 设置是否选中
        popupWindow.setFocusable(true);
        // 设置更新
        popupWindow.update();
        // 设置显示位置
        popupWindow.showAsDropDown(b);
    }

}

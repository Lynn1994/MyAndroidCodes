package com.bali.hotelview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Follow codes for deal with hotel button click event */
        // Find my hotel skip button by id
        Button btnHotel  = (Button) findViewById(R.id.HotelButton);
        // Set my hotel skip button on click listener for click event
        btnHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            // Override onClick method for skip activity
            public void onClick(View v) {
                // Set new Intent for set skip action
                Intent intent  =  new Intent(MainActivity.this,HotelView.class);
                // Start intent
                startActivity(intent);
            }
        });
    }
}

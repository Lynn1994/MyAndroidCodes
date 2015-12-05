package com.bali.simpledownload;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DownLoadFile("http://www.tooopen.com/favicon.ico", Environment.getExternalStorageDirectory()+"//myImageDemo","/a.jpg");
    }
}

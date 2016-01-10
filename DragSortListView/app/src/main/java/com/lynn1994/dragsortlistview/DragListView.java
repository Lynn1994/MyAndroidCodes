package com.lynn1994.dragsortlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class DragListView extends AppCompatActivity {
    private DragListAdapter adapter = null;
    ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_list_view);
        initData();
        DragListViewControl dragListViewControl = (DragListViewControl) findViewById(R.id.user_defined_draglist);
        adapter = new DragListAdapter(DragListView.this, data);
        dragListViewControl.setAdapter(adapter);
    }

    public void initData() {
        //数据结果
        data = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            data.add("item" + i);
        }
    }
}

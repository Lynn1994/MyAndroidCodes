package com.lynn1994.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button b;
    PopupWindow popupWindow;
    private ListView list;
    private HotelStarFilterAdapter hotelStarFilterAdapter;
    /* new codes */

    private List<String> groupArray;
    private List<List<String>> childArray;

    /*********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* new codes*/
        groupArray = new ArrayList<String>();
        childArray = new ArrayList<List<String>>();
        groupArray.add("第一行");
        groupArray.add("第二行");
        List<String> tempArray = new ArrayList<String>();
        tempArray.add("第一条");
        tempArray.add("第二条");
        tempArray.add("第三条");
        for (int index = 0; index < groupArray.size(); ++index) {
            childArray.add(tempArray);
        }
        /**************************************************/
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
            View v = layoutInflater.inflate(R.layout.expandable, null);
            // 设置popupWindow显示格式
            popupWindow = new PopupWindow(v, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            try {
//                list = (ListView) v.findViewById(R.id.list);
//                list.setAdapter(hotelStarFilterAdapter);
//                list.setItemsCanFocus(true);
//                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//            } catch (Exception e) {
//                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//            }
//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    HotelStarFilterAdapter.ViewHolder holder = (HotelStarFilterAdapter.ViewHolder) view.getTag();
//                    b.setText(holder.showStarInfo.getText());
//                    Toast.makeText(MainActivity.this, String.valueOf(hotelStarFilterAdapter.getItemId(position)), Toast.LENGTH_SHORT).show();
//                    popupWindow.dismiss();
//                }
//            });
            /* new codes */
            try {
                final ExpandableListView expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
                final ExpandableAdapter e = new ExpandableAdapter(MainActivity.this);
                expandableListView.setAdapter(e);
                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        String s = e.getGroup(groupPosition) + "  " + e.getChild(groupPosition, childPosition);
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                        return false;
                    }
                });
            } catch (Exception e) {

            }
        }
        /*****************************************************************************/
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

    /* new codes */
    class ExpandableAdapter extends BaseExpandableListAdapter {
        Activity activity;

        public ExpandableAdapter(Activity a) {
            activity = a;
        }

        @Override
        public int getGroupCount() {
            return groupArray.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childArray.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupArray.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childArray.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String string = groupArray.get(groupPosition);
            return getGenericView(string);
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String string = childArray.get(groupPosition).get(childPosition);
            return getChildGenericView(string);
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public TextView getGenericView(String string) {
            // layout parameters for expandable list view
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);
            TextView text = new TextView(activity);
            text.setLayoutParams(layoutParams);
            // center the text vertically
            text.setGravity(Gravity.CENTER);
            // set the text starting position
            text.setPadding(100, 0, 0, 0);
            text.setText(string);
            return text;
        }

        public TextView getChildGenericView(String string) {
            // layout parameters for expandable list view
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);
            TextView text = new TextView(activity);
            text.setLayoutParams(layoutParams);
            // center the text vertically
            text.setGravity(Gravity.CENTER);
            // set the text starting position
            text.setPadding(200, 0, 0, 0);
            text.setText(string);
            return text;
        }
    }
    /****************************************************************/
}

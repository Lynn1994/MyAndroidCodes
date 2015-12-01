package com.bali.listviewpulluploading;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // This variable for saving data
    private List<Map<String, String>> Data = new ArrayList<Map<String, String>>();
    // Defined a new adapter
    private UserDefinedListViewAdapter userDefinedListViewAdapter;
    // Defined a new listView
    private ListView listView;
    // Defined a textView for showing loading notification
    private Button loadMore;
    // Defined a new Handler
    private Handler handler;
    // Defined a String for saving data temp
    private String strTemp;
    // Defined a variable for saving user's action
    private boolean isLastRow = false;
    // Defined a view for showing in list bottom
    private View bottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize a new Handler
        handler = new Handler();
        // Initialize my location data for testing
        parseLocalData();
        // Initialize my user-adapter data for ListView
        initData();
        // Initialize my ListView
        listView = (ListView) findViewById(R.id.testListView);
        // Set user-adapter for listView
        listView.setAdapter(userDefinedListViewAdapter);
        // Initialize bottomView and get bottomView layout
        bottomView = getLayoutInflater().inflate(R.layout.bottom_view, null);
        // Initialize button of loadMore
        loadMore = (Button) bottomView.findViewById(R.id.newButton);
        // Add footerView to listView
        listView.addFooterView(bottomView);
        // Set on scroll listener for ListView
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                 /**
                  * This method for control the action of user get the bottom of listView and stop his scroll action
                  * if the listView is in the bottom, adding new data
                  * if not in the bottom of listView, Do not add new data
                  * */
                 @Override
                 public void onScrollStateChanged(AbsListView view, int scrollState) {
                     /* Judge the listView is or not in the bottom add user stop his scroll action
                     *  If it is the bottom and user stop his action, adding new data
                     * */
                     if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                         try {
                             /*
                             * Judge the number of item is or not less-than data's number
                             * if true, load more item
                             * if false, Set a notification of loading completed
                             * */
                             if (userDefinedListViewAdapter.getCount() + 1 < Data.size()) {
                                 // Set loadMore button text
                                 loadMore.setText("loading......");
                                 // Start a new thread for loading data and set 2000ms delay
                                 handler.postDelayed(new Runnable() {
                                     @Override
                                     public void run() {
                                         // Loading item
                                         loadData();
                                         // Get the change of item, and show in ListView
                                         userDefinedListViewAdapter.notifyDataSetChanged();
                                         /*
                                         * Judge the number of item is or not less-than data's number
                                         * if true, set loadMore button text as "Load more information"
                                         * if false, set loadMore button text as "No more information"
                                         * */
                                         if (userDefinedListViewAdapter.getCount() + 1 < Data.size()) {
                                             loadMore.setText("Load more information");
                                         } else
                                             loadMore.setText("No more information");
                                     }
                                 }, 2000);

                             } else {
                                 // Toast a notification of loading completed
                                 Toast.makeText(MainActivity.this, "loading completed!", Toast.LENGTH_LONG).show();
                             }

                         }catch (Exception e){
                             Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                         }
                         // after adding items, set isLastRow is false
                         isLastRow = false;
                     }
                 }

                /**
                 * This method for getting the massage of listView is or not in the bottom
                 * if true, set isLastRow is true
                 * if false, set isLastRow is false
                 * */
                 @Override
                 public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                      int totalItemCount) {
                     /**
                      * Judge the number of visibleItem is or not less-than data number
                      * if false, set isLastRow is true and set loadMore button as "No more information"
                      * if true,Judge the ListView is or not in the bottom
                      * if the number of  firstVisibleItem ID add the number of visibleItemCount(in the phone screen)
                      * equals the number of totalItemCount, set isLastRow is true
                      * */
                     if(visibleItemCount == Data.size() || visibleItemCount > Data.size()){
                         isLastRow = true;
                         loadMore.setText("No more information");
                     }else if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0){
                         isLastRow = true;
                     }
                 }
             }
        );
    }
    /**
     * This method for parsing the location data
     * */
    private void parseLocalData() {
        for (int position = 0; position < 55; position++) {
            // Defined a new HashMap for saving temporary data
            HashMap<String, String> dataTemp = new HashMap<String, String>();
            /* follow code for setting data */
            strTemp = Constant.MAIN_ITEM + ":" + String.valueOf(position);
            dataTemp.put(Constant.MAIN_ITEM, strTemp);
            strTemp = Constant.SUB_ITEM + ":" + String.valueOf(position);
            dataTemp.put(Constant.SUB_ITEM, strTemp);
            // Add data
            Data.add(dataTemp);
        }
    }
    /**
     * This method for initialize user-defined adapter data
     * */
    private void initData() {
        // Defined a List for saving temporary data
        List<Map<String, String>> dataTemp = new ArrayList<Map<String, String>>();
        if(Constant.LOAD_NUMBER < Data.size()) {
            for (int position = 0; position < Constant.LOAD_NUMBER; position++) {
                // Add data
                dataTemp = initListData(Data);
            }
        }else{
            dataTemp = Data;
        }
        // initialize user-defined adapter
        userDefinedListViewAdapter = new UserDefinedListViewAdapter(dataTemp, this);
    }
    /**
     * This method for parsing a List Data
     * */
    private List<Map<String, String>> initListData(List<Map<String, String>> dataTemp) {
        // Defined a List for saving temporary list data
        List<Map<String, String>> listTemp = new ArrayList<Map<String, String>>();
        for (int position = 0; position < Constant.LOAD_NUMBER; position++) {
            // Add data
            listTemp.add(parseListData(dataTemp, position));
        }
        return listTemp;
    }
    /**
     * This method for parsing HashMap data
     * */
    private HashMap<String, String> parseListData(List<Map<String, String>> listTemp, int position) {
        // Defined a HashMap for saving temporary HashMap data
        HashMap<String, String> hmTemp = new HashMap<String, String>();
        /* follow code for parsing data */
        strTemp = listTemp.get(position).get(Constant.MAIN_ITEM).toString();
        hmTemp.put(Constant.MAIN_ITEM, strTemp);
        strTemp = listTemp.get(position).get(Constant.SUB_ITEM).toString();
        hmTemp.put(Constant.SUB_ITEM, strTemp);
        return hmTemp;
    }
    /**
     * This method for loading data
     * */
    private void loadData() {
        // Defined a variable for getting the number of item
        int count = userDefinedListViewAdapter.getCount();
        /*
        * Judge the item number add LOAD_NUMBER is or not less-than data number
        * if true, position less-than the number of count add LOAD_NUMBER
        * if false, position less-than the number of data number
        * */
        if (count + Constant.LOAD_NUMBER < Data.size()) {
            for (int position = count; position < count + Constant.LOAD_NUMBER; position++) {
                // Add item
                userDefinedListViewAdapter.addItem(parseListData(Data, position));
            }
        } else {
            for (int position = count; position < Data.size(); position++) {
                // Add item
                userDefinedListViewAdapter.addItem(parseListData(Data, position));
            }
        }
    }
}

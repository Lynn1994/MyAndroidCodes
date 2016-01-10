package com.lynn1994.dragsortlistview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Lynn on 2016/1/10.
 * User-defined adapter
 */
public class DragListAdapter extends BaseAdapter {
    private static final String TAG = "DragListAdapter";
    private ArrayList<String> arrayTitles;
    private Context context;
    public boolean isHidden;
    private int invisiblePosition = -1;
    private boolean isChanged = true;
    private boolean showItem = false;
    private boolean isSameDragDirection = true;
    private int lastFlag = -1;
    private int height;
    private int dragPosition = -1;
    private ArrayList<String> mCopyList = new ArrayList<String>();

    public DragListAdapter(Context context, ArrayList<String> arrayTitles) {
        this.context = context;
        this.arrayTitles = arrayTitles;
    }

    public void showDropItem(boolean showItem) {
        this.showItem = showItem;
    }

    public void setInvisiblePosition(int invisiblePosition) {
        this.invisiblePosition = invisiblePosition;
    }

    @Override
    public int getCount() {
        return arrayTitles.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayTitles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /***
         * 在这里尽可能每次都进行实例化新的，这样在拖拽ListView的时候不会出现错乱.
         * 具体原因不明，不过这样经过测试，目前没有发现错乱。虽说效率不高，但是做拖拽LisView足够了。
         */
        convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.detail);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.drag);
        textView.setText(arrayTitles.get(position));
        if (isChanged) {
            Log.i("Message", "position == " + position);
            Log.i("Message", "holdPosition == " + invisiblePosition);
            if (position == invisiblePosition) {
                if (!showItem) {
                    convertView.findViewById(R.id.detail).setVisibility(View.INVISIBLE);
                    convertView.findViewById(R.id.drag).setVisibility(View.INVISIBLE);
                }
            }
            if (lastFlag != -1) {
                if (lastFlag == 1) {
                    if (position > invisiblePosition) {
                        Animation animation;
                        animation = getFromSelfAnimation(0, -height);
                        convertView.startAnimation(animation);
                    }
                } else if (lastFlag == 0) {
                    if (position < invisiblePosition) {
                        Animation animation;
                        animation = getFromSelfAnimation(0, height);
                        convertView.startAnimation(animation);
                    }
                }
            }
        }
        return convertView;
    }

    /***
     * 动态修改ListVIiw的方位.
     * start 点击移动的position
     * down  松开时候的position
     */
    public void exchange(int startPosition, int endPosition) {
        System.out.println(startPosition + "--" + endPosition);
        Object startObject = getItem(startPosition);
        System.out.println(startPosition + "========" + endPosition);
        Log.d("ON", "startPostion ==== " + startPosition);
        Log.d("ON", "endPosition ==== " + endPosition);
        if (startPosition < endPosition) {
            arrayTitles.add(endPosition + 1, (String) startObject);
            arrayTitles.remove(startPosition);
        } else {
            arrayTitles.add(endPosition, (String) startObject);
            arrayTitles.remove(startPosition + 1);
        }
        isChanged = true;
    }

    public void exchangeCopy(int startPosition, int endPosition) {
        System.out.println(startPosition + "--" + endPosition);
        Object startObject = getCopyItem(startPosition);
        Log.d("ON", "startPostion ==== " + startPosition);
        Log.d("ON", "endPosition ==== " + endPosition);
        if (startPosition < endPosition) {
            mCopyList.add(endPosition + 1, (String) startObject);
            mCopyList.remove(startPosition);
        } else {
            mCopyList.add(endPosition, (String) startObject);
            mCopyList.remove(startPosition + 1);
        }
        isChanged = true;
    }

    public Object getCopyItem(int position) {
        return mCopyList.get(position);
    }

    public void addDragItem(int start, Object object) {
        Log.i(TAG, "start" + start);
        String title = arrayTitles.get(start);
        arrayTitles.remove(start);// 删除该项
        arrayTitles.add(start, (String) object);// 添加删除项
    }

    public void copyList() {
        mCopyList.clear();
        for (String str : arrayTitles) {
            mCopyList.add(str);
        }
    }

    public void pastList() {
        arrayTitles.clear();
        for (String str : mCopyList) {
            arrayTitles.add(str);
        }
    }

    public void setIsSameDragDirection(boolean value) {
        isSameDragDirection = value;
    }

    public void setLastFlag(int flag) {
        lastFlag = flag;
    }

    public void setHeight(int value) {
        height = value;
    }

    public void setCurrentDragPosition(int position) {
        dragPosition = position;
    }

    public Animation getFromSelfAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(100);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    public Animation getToSelfAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.ABSOLUTE, x, Animation.RELATIVE_TO_SELF, 0,
                Animation.ABSOLUTE, y, Animation.RELATIVE_TO_SELF, 0);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(100);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }
}

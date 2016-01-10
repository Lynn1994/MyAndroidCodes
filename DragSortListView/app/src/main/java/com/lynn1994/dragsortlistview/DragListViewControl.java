package com.lynn1994.dragsortlistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Lynn on 2016/1/10.
 */
public class DragListViewControl extends ListView {
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;
    private int scaledTouchSlop;
    private ImageView dragImageView;
    private int startPosition;
    private int dragPosition;
    private int lastPosition;
    private ViewGroup dragItemView = null;
    private int dragPoint;
    private int dragoffset;
    private int upScrollBounce;
    private int downScrollBounce;
    private final static int step = 1;
    private int current_Step;
    private boolean isLock;
    private ItemInfo myItemInfo;
    private boolean isMoving = false;
    private boolean isDragItemMoving = false;
    private int mItemVerticalSpacing = 0;
    private boolean hasGetSpacing = false;
    public static final int MSG_DRAG_STOP = 0X1001;
    public static final int MSG_DRAG_MOVE = 0X1002;
    private static final int ANIMATION_DURATION = 200;

    private boolean isSameDragDirection = true;
    private int lastFlag = -1; //-1,0 == down,1== up
    private int mFirstVisiblePosition, mLastVisiblePosition;
    private int mCurFirstVisiblePosition, mCurLastVisiblePosition;
    private boolean isNormal = true;
    private int turnUpPosition, turnDownPosition;

    private boolean isScroll = false;

    private int holdPosition;

    public DragListViewControl(Context context) {
        super(context);
    }

    public DragListViewControl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        myItemInfo = new ItemInfo();
        init();
    }

    private void init() {
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    private void getSpacing() {
        hasGetSpacing = true;
        upScrollBounce = getHeight() / 3;
        downScrollBounce = getHeight() * 2 / 3;
        int[] tempLocation0 = new int[2];
        int[] tempLocation1 = new int[2];
        ViewGroup itemView0 = (ViewGroup) getChildAt(0);
        ViewGroup itemView1 = (ViewGroup) getChildAt(1);
        if (itemView0 != null) {
            itemView0.getLocationOnScreen(tempLocation0);
        } else {
            return;
        }
        if (itemView1 != null) {
            itemView1.getLocationOnScreen(tempLocation1);
            mItemVerticalSpacing = Math.abs(tempLocation1[1] - tempLocation0[1]);
        } else {
            return;
        }
    }

    /***
     * touch事件拦截 在这里我进行相应拦截，
     */
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        // 按下
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !isLock && !isMoving && !isDragItemMoving) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            lastPosition = startPosition = dragPosition = pointToPosition(x, y);
            // 无效不进行处理
            if (dragPosition == AdapterView.INVALID_POSITION) {
                return super.onInterceptTouchEvent(motionEvent);
            }
            if (false == hasGetSpacing) {
                getSpacing();
            }
            // 获取当前位置的视图(可见状态)
            ViewGroup dragger = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());
            DragListAdapter adapter = (DragListAdapter) getAdapter();
            myItemInfo.obj = adapter.getItem(dragPosition - getFirstVisiblePosition());
            // 获取到的dragPoint其实就是在你点击指定item项中的高度.
            dragPoint = y - dragger.getTop();
            // 这个值是固定的:其实就是ListView这个控件与屏幕最顶部的距离（一般为标题栏+状态栏）.
            dragoffset = (int) (motionEvent.getRawY() - y);
            // 获取可拖拽的图标
            View draggerIcon = dragger.findViewById(R.id.drag);
            if (draggerIcon != null && x > draggerIcon.getLeft() - 20) {
                dragItemView = dragger;
                dragger.destroyDrawingCache();
                dragger.setDrawingCacheEnabled(true);
                dragger.setBackgroundColor(0x555555);
                // 根据cache创建一个新的bitmap对象.
                Bitmap bitmap = Bitmap.createBitmap(dragger.getDrawingCache(true));
                hideDropItem();
                adapter.setInvisiblePosition(startPosition);
                adapter.notifyDataSetChanged();
                // 初始化影像
                startDrag(bitmap, y);
                isMoving = false;
                adapter.copyList();
            }
            return false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public Animation getScaleAnimation() {
        Animation scaleAnimation = new ScaleAnimation(0.0f, 0.0f, 0.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }

    private void hideDropItem() {
        final DragListAdapter adapter = (DragListAdapter) this.getAdapter();
        adapter.showDropItem(false);
    }

    /**
     * 触摸事件处理
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (dragImageView != null && dragPosition != INVALID_POSITION && !isLock) {
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    int upY = (int) motionEvent.getY();
                    stopDrag();
                    onDrop(upY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveY = (int) motionEvent.getY();
                    onDrag(moveY);
                    testAnimation(moveY);
                    break;
                case MotionEvent.ACTION_DOWN:
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    private void testAnimation(int y) {
        final DragListAdapter adapter = (DragListAdapter) getAdapter();
        int tempPosition = pointToPosition(0, y);
        if (tempPosition == INVALID_POSITION || tempPosition == lastPosition) {
            return;
        }
        mFirstVisiblePosition = getFirstVisiblePosition();
        dragPosition = tempPosition;
        onChangeCopy(lastPosition, dragPosition);
        int moveNum = tempPosition - lastPosition;
        int count = Math.abs(moveNum);
        for (int i = 1; i <= count; i++) {
            int xAbsOffset, yAbsOffset;
            //down
            if (moveNum > 0) {
                if (lastFlag == -1) {
                    lastFlag = 0;
                    isSameDragDirection = true;
                }
                if (lastFlag == 1) {
                    turnUpPosition = tempPosition;
                    lastFlag = 0;
                    isSameDragDirection = !isSameDragDirection;
                }
                if (isSameDragDirection) {
                    holdPosition = lastPosition + 1;
                } else {
                    if (startPosition < tempPosition) {
                        holdPosition = lastPosition + 1;
                        isSameDragDirection = !isSameDragDirection;
                    } else {
                        holdPosition = lastPosition;
                    }
                }
                xAbsOffset = 0;
                yAbsOffset = -mItemVerticalSpacing;
                lastPosition++;
            }
            //向上drag
            else {
                if (lastFlag == -1) {
                    lastFlag = 1;
                    isSameDragDirection = true;
                }
                if (lastFlag == 0) {
                    turnDownPosition = tempPosition;
                    lastFlag = 1;
                    isSameDragDirection = !isSameDragDirection;
                }
                if (isSameDragDirection) {
                    holdPosition = lastPosition - 1;
                } else {
                    if (startPosition > tempPosition) {
                        holdPosition = lastPosition - 1;
                        isSameDragDirection = !isSameDragDirection;
                    } else {
                        holdPosition = lastPosition;
                    }
                }
                xAbsOffset = 0;
                yAbsOffset = mItemVerticalSpacing;
                lastPosition--;
            }
            adapter.setHeight(mItemVerticalSpacing);
            adapter.setIsSameDragDirection(isSameDragDirection);
            adapter.setLastFlag(lastFlag);
            ViewGroup moveView = (ViewGroup) getChildAt(holdPosition - getFirstVisiblePosition());

            //Animation animation = getMoveAnimation(Xoffset,Yoffset);
            Animation animation;
            if (isSameDragDirection) {
                animation = getFromSelfAnimation(xAbsOffset, yAbsOffset);
            } else {
                animation = getToSelfAnimation(xAbsOffset, -yAbsOffset);
            }
            moveView.startAnimation(animation);
        }
    }

    public Animation getFromSelfAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(ANIMATION_DURATION);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    public Animation getToSelfAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(
                Animation.ABSOLUTE, x, Animation.RELATIVE_TO_SELF, 0,
                Animation.ABSOLUTE, y, Animation.RELATIVE_TO_SELF, 0);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(ANIMATION_DURATION);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    private void onChangeCopy(int last, int current) {
        DragListAdapter adapter = (DragListAdapter) getAdapter();
        if (last != current) {
            adapter.exchangeCopy(last, current);
        }
    }

    private void startDrag(Bitmap bitmap, int y) {
        /***
         * 初始化window.
         */
        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = y - dragPoint + dragoffset;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE// 不需获取焦点
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE// 不需接受触摸事件
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON// 保持设备常开，并保持亮度不变。
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;// 窗口占满整个屏幕，忽略周围的装饰边框（例如状态栏）。此窗口需考虑到装饰边框的内容。
        // windowParams.format = PixelFormat.TRANSLUCENT;// 默认为不透明，这里设成透明效果.
        windowParams.windowAnimations = 0;// 窗口所使用的动画设置

        windowParams.alpha = 0.8f;
        windowParams.format = PixelFormat.TRANSLUCENT;

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bitmap);

        windowManager.addView(imageView, windowParams);
        dragImageView = imageView;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DRAG_STOP:
                    stopDrag();
                    onDrop(msg.arg1);
                    break;
                case MSG_DRAG_MOVE:
                    onDrag(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    };

    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }

    /**
     * 拖动执行，在Move方法中执行
     *
     * @param y
     */
    public void onDrag(int y) {
        int drag_top = y - dragPoint;//拖拽view的top值不能＜0，否则则出界.
        if (dragImageView != null && drag_top >= 0) {
            windowParams.alpha = 1.0f;
            windowParams.y = y - dragPoint + dragoffset;
            windowManager.updateViewLayout(dragImageView, windowParams);// 实时拖动
        }
        doScroller(y);// listView 移动
    }

    public void doScroller(int y) {
        if (y < upScrollBounce) {
            current_Step = step + (upScrollBounce - y) / 10;
        } else if (y > downScrollBounce) {
            current_Step = -(step + (y - downScrollBounce)) / 10;
        } else {
            isScroll = false;
            current_Step = 0;
        }

        // 获取你拖拽滑动到位置及显示item相应的view上（注：可显示部分）（position）
        View view = getChildAt(dragPosition - getFirstVisiblePosition());
        // 真正滚动的方法setSelectionFromTop()
        setSelectionFromTop(dragPosition, view.getTop() + current_Step);
    }

    /**
     * 停止拖动，删除影像
     */
    public void stopDrag() {
        isMoving = false;
        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
        isSameDragDirection = true;
        lastFlag = -1;
        DragListAdapter adapter = (DragListAdapter) getAdapter();
        adapter.setLastFlag(lastFlag);
        adapter.pastList();

    }

    /**
     * 拖动放下的时候
     *
     * @param y
     */
    public void onDrop(int y) {
        onDrop(0, y);
    }

    private void onDrop(int x, int y) {
        final DragListAdapter adapter = (DragListAdapter) getAdapter();
        adapter.setInvisiblePosition(-1);
        adapter.showDropItem(true);
        adapter.notifyDataSetChanged();
    }

    public Animation getAbsMoveAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(ANIMATION_DURATION);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    public Animation getAnimation(int fromY, int toY) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, fromY, Animation.ABSOLUTE, toY);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(ANIMATION_DURATION);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    public Animation getAbsMoveAnimation2(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.ABSOLUTE, x, Animation.RELATIVE_TO_SELF, 0,
                Animation.ABSOLUTE, y, Animation.RELATIVE_TO_SELF, 0);
        go.setInterpolator(new AccelerateDecelerateInterpolator());

        go.setFillAfter(true);
        go.setDuration(ANIMATION_DURATION);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }
}

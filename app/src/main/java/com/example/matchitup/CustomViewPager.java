package com.example.matchitup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;


public class CustomViewPager extends ViewPager {
    private boolean enabled;
    private int time;

    /**
     * CustomViewPager constructor
     * @param context Application context
     * @param attrs
     */
    public CustomViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
        this.enabled = true;
        setMyScroller();
    }

    /**
     * Method which is called when the user has performed a touch on the ViewPager
     * @param event
     * @return Boolean representing the state
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(this.enabled){
            return super.onTouchEvent(event);
        }
        return false;
    }

    /**
     * This method intercept all touch screen motion events.
     * @param event
     * @return Boolean representing the state
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        if(this.enabled){
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    /**
     * Called when it's necessary to block the viewpager for not to receive any interaction from
     * the user
     * @param enabled Boolean representing the state
     */
    public void setPagingEnabled(boolean enabled){
        this.enabled = enabled;
    }

    /**
     * Get an integer which represents the milliseconds it takes for the viewpager to move
     * @return Integer
     */
    public int getTime() {
        return time;
    }

    /**
     * Set an integer which represents the milliseconds it takes for the viewpager to move
     * @return Integer
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Private method responsible for setting a scroller on this ViewPager
     */
    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, getTime() /*1 secs*/);
        }
    }

}
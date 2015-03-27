package com.wrl.viewtest.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by wangrulin on 15/3/16.
 */
public class OutScrollView extends ScrollView{
    private View innerView;

    public OutScrollView(Context context) {
        super(context);
        init();
    }

    public OutScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OutScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    public void setInnerView(View innerView){
        this.innerView = innerView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getYInScreen(this)>=getYInScreen(innerView)){
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getYInScreen(this)>=getYInScreen(innerView)){
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public int getYInScreen(View v){
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return location[1];
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (t-oldt>0){
            //downScroll

        }else{

        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        innerView.getLayoutParams().height = getHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

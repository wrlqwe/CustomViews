package com.wrl.viewtest.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.wrl.viewtest.R;

/**
 * Created by wangrulin on 15/3/18.
 */
public class QQSlideContainer extends HorizontalScrollView {
	public enum Status {
		OPEN, CLOSE
	}

	public QQSlideContainer(Context context) {
		this(context, null, 0);
	}

	public QQSlideContainer(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public QQSlideContainer(Context context, AttributeSet attrs,
                            int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mScreenWidth = getResources().getDisplayMetrics().widthPixels;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.NormalSlideContainer, defStyleAttr, 0);
		mMenuRightPadding = (int) a.getDimension(
				R.styleable.NormalSlideContainer_menuRightPadding,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						50, getResources().getDisplayMetrics()));

	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public QQSlideContainer(Context context, AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		mScreenWidth = getResources().getDisplayMetrics().widthPixels;
		mMenuRightPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 150, getResources()
						.getDisplayMetrics());

	}

	private LinearLayout mWrapper;
	private ViewGroup mMenu;
	private ViewGroup mMenuWrapper;
	private ViewGroup mContent;

	private int mScreenWidth;
	private int mMenuRightPadding;

	private Status status;

	private boolean firstLoad = true;

	/**
	 * 设置子view的宽和高，设定自己的宽高
	 * 
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (firstLoad) {
			firstLoad = false;
			mWrapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWrapper.getChildAt(0);
            mMenuWrapper = (ViewGroup) mMenu.getChildAt(0);
			mContent = (ViewGroup) mWrapper.getChildAt(1);

            mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;
		}
	}

	/**
	 * 通过设置偏移量将menu隐藏
	 * 
	 * @param changed
	 * @param l
	 * @param t
	 * @param r
	 * @param b
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		if (changed) {
			scrollTo(mMenu.getMeasuredHeight(), 0);
		}
	}

	/**
	 *
	 * @param ev
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
        case MotionEvent.ACTION_MOVE:
            break;
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();
            if (status == Status.CLOSE) {
                if (scrollX <= mMenu.getWidth() * 2 / 3) {
                    openMenu();
                } else {
                    closeMenu();
                }
            }else if (scrollX >= mMenu.getWidth() * 1 / 3) {
                closeMenu();
            } else {
                openMenu();
            }
			return true;
		}
		return super.onTouchEvent(ev);
	}

	public void openMenu() {
		status = Status.OPEN;
        smoothScrollTo(0, 0);
	}

	public void closeMenu() {
		status = Status.CLOSE;
        smoothScrollTo(mMenu.getWidth(), 0);
	}

	public boolean isOpen() {
		return status == Status.OPEN;
	}


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l/(float)mMenu.getWidth();//1~0
        ViewHelper.setTranslationX(mMenu,l*0.7f);
        ViewHelper.setPivotX(mMenuWrapper,0);
        ViewHelper.setPivotY(mMenuWrapper,mMenuWrapper.getHeight()/2);
        ViewHelper.setScaleX(mMenuWrapper,1-scale*0.3f);
        ViewHelper.setScaleY(mMenuWrapper,1-scale*0.3f);
        ViewHelper.setAlpha(mMenuWrapper,1-scale*0.3f);

        ViewHelper.setPivotX(mContent,0);
        ViewHelper.setPivotY(mContent,mContent.getHeight()/2);
        ViewHelper.setScaleX(mContent,scale*0.4f +0.6f);
        ViewHelper.setScaleY(mContent,scale*0.4f +0.6f);
//        ViewHelper.set
    }
}

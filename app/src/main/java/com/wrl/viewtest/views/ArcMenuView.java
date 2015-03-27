package com.wrl.viewtest.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.wrl.viewtest.R;

public class ArcMenuView extends ViewGroup implements View.OnClickListener {

	public enum Position {
		LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
	}

	private static final int POS_LEFT_TOP = 0;
	private static final int POS_LEFT_BOTTOM = 1;
	private static final int POS_RIGHT_TOP = 2;
	private static final int POS_RIGHT_BOTTOM = 3;

	public enum Status {
		OPEN, CLOSE
	}

	public interface OnMenuClickListener {
		/**
		 * 菜单子项从1记
		 * 
		 * @param v
		 * @param position
		 */
		public void onMenuItemClickListener(View v, int position);
	}

	private Position mPosition = Position.LEFT_TOP;
	private Status mStatus = Status.CLOSE;
	private int mRadius;
	private View mButton;

	public OnMenuClickListener getmOnMenuClickListener() {
		return mOnMenuClickListener;
	}

	public void setmOnMenuClickListener(OnMenuClickListener mOnMenuClickListener) {
		this.mOnMenuClickListener = mOnMenuClickListener;
	}

	private OnMenuClickListener mOnMenuClickListener;

	private TextPaint mTextPaint;
	private float mTextWidth;
	private float mTextHeight;

	public ArcMenuView(Context context) {
		super(context);
		init(null, 0);
	}

	public ArcMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public ArcMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.ArcMenuView, defStyle, 0);
		int pos = a.getInt(R.styleable.ArcMenuView_position, POS_LEFT_TOP);
		switch (pos) {
		case POS_LEFT_TOP:
			mPosition = Position.LEFT_TOP;
			break;
		case POS_LEFT_BOTTOM:
			mPosition = Position.LEFT_BOTTOM;
			break;
		case POS_RIGHT_TOP:
			mPosition = Position.RIGHT_TOP;
			break;
		case POS_RIGHT_BOTTOM:
			mPosition = Position.RIGHT_BOTTOM;
			break;
		}
		mRadius = (int) a.getDimension(R.styleable.ArcMenuView_radius,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
						getResources().getDisplayMetrics()));
		a.recycle();


		// Set up a default TextPaint object
		mTextPaint = new TextPaint();
		mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextAlign(Paint.Align.LEFT);

		// Update TextPaint and text measurements from attributes
		invalidateTextPaintAndMeasurements();
	}

	private void invalidateTextPaintAndMeasurements() {

		Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		mTextHeight = fontMetrics.bottom;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			layoutMainBtn();
			int count = getChildCount();
			for (int i = 1; i < count; i++) {
				View view = getChildAt(i);
				view.setVisibility(View.GONE);
				int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2)
						* (i - 1)));
				int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2)
						* (i - 1)));
				int cw = view.getMeasuredWidth();
				int ch = view.getMeasuredHeight();

				if (mPosition == Position.LEFT_BOTTOM
						|| mPosition == Position.RIGHT_BOTTOM) {
					ct = getMeasuredHeight() - ch - ct;
				}
				if (mPosition == Position.RIGHT_BOTTOM
						|| mPosition == Position.RIGHT_TOP) {
					cl = getMeasuredWidth() - cw - cl;
				}
				view.layout(cl, ct, cl + cw, ct + ch);
				final int finalI = i;
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onMenuItemClick(finalI);
					}
				});
			}
		}
	}

	private void onMenuItemClick(final int position) {
        setOnClickListener(null);
		int count = getChildCount();
		for (int i = 1; i < count; i++) {
			final View view = getChildAt(i);
			AnimationSet animSet = new AnimationSet(true);
			if (i == position) {
				animSet.setDuration(300);
				ScaleAnimation sa = new ScaleAnimation(1f, 4f, 1f, 4f,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				animSet.addAnimation(sa);
			} else {
				animSet.setDuration(200);
				ScaleAnimation sa = new ScaleAnimation(1f, 0f, 1f, 0f,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				animSet.addAnimation(sa);
			}

			AlphaAnimation aa = new AlphaAnimation(1f, 0f);
			animSet.addAnimation(aa);
			animSet.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (mStatus == Status.CLOSE) {
						view.setVisibility(View.GONE);
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}
			});
			view.startAnimation(animSet);
			mStatus = Status.CLOSE;
			if (mOnMenuClickListener != null && position != 0) {
				mOnMenuClickListener.onMenuItemClickListener(view, position);
			}

		}
	}

	private void layoutMainBtn() {
		mButton = getChildAt(0);
		mButton.setOnClickListener(this);
		int l = 0, t = 0;
		int measuredWidth = mButton.getMeasuredWidth();
		int measuredHeight = mButton.getMeasuredHeight();
		switch (mPosition) {
		case LEFT_TOP:
			break;
		case LEFT_BOTTOM:
			l = 0;
			t = getMeasuredHeight() - measuredHeight;
			break;
		case RIGHT_TOP:
			l = getMeasuredWidth() - measuredWidth;
			t = 0;
			break;
		case RIGHT_BOTTOM:
			l = getMeasuredWidth() - measuredWidth;
			t = getMeasuredHeight() - measuredHeight;
			break;
		}
		mButton.layout(l, t, l + measuredWidth, t + measuredHeight);
	}

	@Override
	public void onClick(final View v) {
		if (mStatus == Status.OPEN) {
			rotateMainBtn(v, 0f, 360f, 300);
			toggleMenu(200);
			setOnClickListener(null);
		} else {
			rotateMainBtn(v, 360f, 0f, 300);
			toggleMenu(200);
			setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					rotateMainBtn(v, 0f, 360f, 300);
					toggleMenu(200);
					setOnClickListener(null);
				}
			});
		}
	}

	private void toggleMenu(int duration) {
		int count = getChildCount();
		for (int i = 1; i < count; i++) {
			int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2)
					* (i - 1)));
			int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2)
					* (i - 1)));
			int xflag = 1;
			int yflag = 1;
			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.LEFT_BOTTOM) {
				xflag = -1;
			}
			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.RIGHT_TOP) {
				yflag = -1;
			}

			AnimationSet animSet = new AnimationSet(true);
			final View child = getChildAt(i);

			if (mStatus == Status.OPEN) {
				RotateAnimation ra = new RotateAnimation(0, 720,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				ra.setDuration(duration);
				animSet.addAnimation(ra);
				TranslateAnimation ta = new TranslateAnimation(0, cl * xflag,
						0, ct * yflag);
				ta.setDuration(duration);
				animSet.addAnimation(ta);
				child.setClickable(false);
				child.setFocusable(false);
			} else {
				RotateAnimation ra = new RotateAnimation(720, 0,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				ra.setDuration(duration);
				animSet.addAnimation(ra);
				TranslateAnimation ta = new TranslateAnimation(cl * xflag, 0,
						ct * yflag, 0);
				ta.setDuration(duration);
				animSet.addAnimation(ta);
				child.setClickable(true);
				child.setFocusable(true);
			}
			animSet.setStartOffset(i * 20);
			animSet.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					child.setVisibility(VISIBLE);
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (mStatus == Status.CLOSE) {
						child.setVisibility(GONE);
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}
			});
			child.startAnimation(animSet);
		}

		if (mStatus == Status.OPEN) {
			mStatus = Status.CLOSE;
		} else {
			mStatus = Status.OPEN;
		}
	}

	private void rotateMainBtn(View v, float start, float end, int duration) {
		RotateAnimation rotateAnimation = new RotateAnimation(start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(duration);
		v.startAnimation(rotateAnimation);
	}
}

package com.wrl.viewtest.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.wrl.viewtest.R;

/**
 * Created by wangrulin on 15/3/24.
 */
public class AnimateProgressView extends View {

	public AnimateProgressView(Context context) {
		this(context, null);
	}

	public AnimateProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AnimateProgressView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr, 0);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public AnimateProgressView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(attrs, defStyleAttr, defStyleRes);

	}

	private int internalColor, outsideColor;
	private int internalWidth, outsideWidth;
	private int minWidth, minHeight;

	private int radius;
	private int measuredWidth, measuredHeight;

	private Paint mInternalPaint, mOutsidePaint;
	private Paint mCirclePaint, mSweepPaint;
	private Drawable backgroundDrawable, sweepBackgroundDrawable;

	private RectF mInternalRect, mOutsideRect;

	// if use animation, we should calculate the progress,Scroller can do this
	private Scroller mProgressScroller;
	private int duration;
	private int currentDegree = 0;
	private int targetDegree;

	private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.AnimateProgressView, defStyleAttr, defStyleRes);
		internalColor = a.getColor(
				R.styleable.AnimateProgressView_apv_internal_arc_color,
				0xff8474cf);
		outsideColor = a.getColor(
				R.styleable.AnimateProgressView_apv_internal_arc_color,
				0xff41f2b7);
		internalWidth = a.getDimensionPixelSize(
				R.styleable.AnimateProgressView_apv_internal_arc_width,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
						getResources().getDisplayMetrics()));
		outsideWidth = a.getDimensionPixelSize(
				R.styleable.AnimateProgressView_apv_outside_arc_width,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
						getResources().getDisplayMetrics()));
		duration = a.getInt(R.styleable.AnimateProgressView_apv_anim_duration,
				1000);
		backgroundDrawable = a
				.getDrawable(R.styleable.AnimateProgressView_apv_background);
		sweepBackgroundDrawable = a
				.getDrawable(R.styleable.AnimateProgressView_apv_sweep_background);
		a.recycle();

		mInternalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mInternalPaint.setColor(internalColor);
		mInternalPaint.setStrokeWidth(internalWidth);
		mInternalPaint.setStyle(Paint.Style.STROKE);

		mOutsidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mOutsidePaint.setColor(outsideColor);
		mOutsidePaint.setStrokeWidth(outsideWidth);
		mOutsidePaint.setStyle(Paint.Style.STROKE);
		mOutsidePaint.setStrokeCap(Paint.Cap.ROUND);

		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint.setStyle(Paint.Style.FILL);
		mSweepPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mSweepPaint.setStyle(Paint.Style.FILL);

		mProgressScroller = new Scroller(getContext(), new LinearInterpolator());
	}

	/**
	 * Determines the width of this view
	 *
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			result = getSuggestedMinimumWidth();
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * Determines the height of this view
	 *
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text (beware: ascent is a negative number)
			result = getSuggestedMinimumHeight();
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measuredWidth = measureWidth(widthMeasureSpec);
		measuredHeight = measureHeight(heightMeasureSpec);

		setMeasuredDimension(measuredWidth, measuredHeight);
		int radius = Math.min(measuredWidth, measuredHeight) / 2;

		// setup the rects
		if (measuredWidth >= measuredHeight) {
			mOutsideRect = new RectF(measuredWidth / 2 - radius, 0,
					measuredWidth / 2 + radius, measuredHeight);
		} else {
			mOutsideRect = new RectF(0, measuredHeight / 2 - radius,
					measuredWidth, measuredHeight / 2 + radius);
		}
		mOutsideRect.inset(outsideWidth / 2, outsideWidth / 2);
		mInternalRect = new RectF(mOutsideRect);
		mInternalRect.inset(internalWidth / 2, internalWidth / 2);

		mCirclePaint.setShader(getShaderWithDrawable(backgroundDrawable));
		Shader sweepShader = getShaderWithDrawable(sweepBackgroundDrawable);
		mSweepPaint.setShader(sweepShader);

	}

	private Shader getShaderWithDrawable(Drawable backgroundDrawable) {
		if (backgroundDrawable == null) {
			return null;
		}
		int intrinsicWidth = (int) mInternalRect.width();
		int intrinsicHeight = (int) mInternalRect.height();

		Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight,
				Bitmap.Config.ARGB_8888);
		// setup background size
		backgroundDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
		Canvas canvas = new Canvas(bitmap);
		backgroundDrawable.draw(canvas);
		BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		Matrix localM = new Matrix();
		localM.setTranslate(mInternalRect.left, mInternalRect.top);
		shader.setLocalMatrix(localM);

		return shader;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// if currentDegree!=targetDegree,start animation
		if (currentDegree != targetDegree
				&& !mProgressScroller.computeScrollOffset()) {
			mProgressScroller.startScroll(currentDegree, 0, targetDegree
					- currentDegree, 0, duration);
		}
		// calculate currentDegree
		if (mProgressScroller.computeScrollOffset()) {
			currentDegree = mProgressScroller.getCurrX();
		}

		// int delta = targetDegree - currentDegree;
		// if (delta != 0) {
		// if (delta > 0) {
		// currentDegree += delta < 3 ? delta : 3;
		// } else {
		// currentDegree -= delta > -3 ? delta : 3;
		// }
		// }

        canvas.drawArc(mInternalRect, 0, 360, true, mCirclePaint);
        if (currentDegree!=0) {
            canvas.drawArc(mInternalRect, -90, currentDegree, true, mSweepPaint);
        }

        canvas.drawArc(mInternalRect, 0, 360, false, mInternalPaint);
        if (currentDegree!=0) {
            canvas.drawArc(mOutsideRect, -90, currentDegree, false, mOutsidePaint);
        }


		if (targetDegree != currentDegree) {
			invalidate();
		}

	}

	public void setProgress(float percent) {
		setProgress(percent, true);
	}

	public void setProgress(float percent, boolean anim) {
		setProgressWithDegree((int) (360 * percent / 100), anim);
	}

	private void setProgressWithDegree(int degree, boolean anim) {
		targetDegree = degree;
		if (!anim) {
			currentDegree = degree;
		}
		invalidate();
	}
}

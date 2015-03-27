package com.wrl.viewtest.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wrl.viewtest.R;

/**
 * Created by wangrulin on 15/3/26.
 */
public class RatioImageView extends ImageView {
	public RatioImageView(Context context) {
		super(context);
	}

	public RatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0, 0);
	}

	public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr, 0);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public RatioImageView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(attrs, defStyleAttr, defStyleRes);
	}

	private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.RatioImageView, defStyleAttr, defStyleRes);
		int wWeight = a.getInt(R.styleable.RatioImageView_wWeight, 0);
		int hWeight = a.getInt(R.styleable.RatioImageView_hWeight, 0);
		float ratio = a.getFloat(R.styleable.RatioImageView_ratio, 0);
		if (wWeight > 0 || hWeight > 0) {
			this.ratio = wWeight * 1f / hWeight;
		} else if (ratio > 0) {
			this.ratio = ratio;
		} else {
			ratio = 1;
		}
		a.recycle();
	}

	private float ratio;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int wMode = MeasureSpec.getMode(widthMeasureSpec);
		int wSize = MeasureSpec.getSize(widthMeasureSpec);
		int hMode = MeasureSpec.getMode(heightMeasureSpec);
		int hSize = MeasureSpec.getSize(heightMeasureSpec);
		if (wMode == MeasureSpec.EXACTLY && hMode != MeasureSpec.EXACTLY) {

			hSize = (int) (wSize / ratio);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(hSize,
					MeasureSpec.EXACTLY);
			setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
			return;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

    public void setRatio(float ratio){
        if (ratio<=0){
            throw new RuntimeException("cannot pass negative value to method setRatio() for object "+this);
        }
        requestLayout();
        invalidate();
    }
}

package com.qindachang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * Created by Qin DaChang on 2016/11/20.
 *
 * @see <a href="https://github.com/qindachang">https://github.com/qindachang</a>
 */

public class RulerView extends View {
    private int mMinVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mWidth;
    private int mHeight;

    private float mSelectorValue = 0.0f;
    private float mMaxValue = 100.0f;
    private float mMinValue = 0.0f;
    private float mPerValue = 1;

    private float mLineSpaceWidth = 5;
    private float mLineWidth = 1;
    private float mLineMaxHeight = 42;
    private float mLineMidHeight = 30;
    private float mLineMinHeight = 17;
    private int mLineColor = 1;

    private float mTextMarginTop = 8;
    private float mTextSize = 14;
    private int mTextColor = 1;

    private boolean mAlphaEnable = true;

    private float mTextHeight;

    private Paint mTextPaint;
    private Paint mLinePaint;

    private int mTotalLine;
    private int mMaxOffset;
    private float mOffset;
    private int mLastX, mMove;
    private OnValueChangeListener mListener;


    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RulerView);

        mAlphaEnable = typedArray.getBoolean(R.styleable.RulerView_alphaEnable, mAlphaEnable);

        mLineSpaceWidth = typedArray.getDimension(R.styleable.RulerView_lineSpaceWidth, dp2px(context, mLineSpaceWidth));
        mLineWidth = typedArray.getDimension(R.styleable.RulerView_lineWidth, dp2px(context, mLineWidth));
        mLineMaxHeight = typedArray.getDimension(R.styleable.RulerView_lineMaxHeight, dp2px(context, mLineMaxHeight));
        mLineMidHeight = typedArray.getDimension(R.styleable.RulerView_lineMidHeight, dp2px(context, mLineMidHeight));
        mLineMinHeight = typedArray.getDimension(R.styleable.RulerView_lineMinHeight, dp2px(context, mLineMinHeight));
        mLineColor = typedArray.getColor(R.styleable.RulerView_lineColor, mLineColor);

        mTextSize = typedArray.getDimension(R.styleable.RulerView_textSize, dp2px(context, mTextSize));
        mTextColor = typedArray.getColor(R.styleable.RulerView_textColor, mTextColor);
        mTextMarginTop = typedArray.getDimension(R.styleable.RulerView_textMarginTop, dp2px(context, mTextMarginTop));

        mSelectorValue = typedArray.getFloat(R.styleable.RulerView_selectorValue, 0.0f);
        mMinValue = typedArray.getFloat(R.styleable.RulerView_minValue, 0.0f);
        mMaxValue = typedArray.getFloat(R.styleable.RulerView_maxValue, 100.0f);
        mPerValue = typedArray.getFloat(R.styleable.RulerView_perValue, 0.1f);

        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextHeight = getFontHeight(mTextPaint);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setColor(mLineColor);

        setValue(mSelectorValue, mMinValue, mMaxValue, mPerValue);

        typedArray.recycle();
    }

    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    public void setTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
        invalidate();
    }

    public void setTextMarginTop(float marginTop) {
        mTextMarginTop = marginTop;
        invalidate();
    }

    public void setLineColor(int color) {
        mLinePaint.setColor(color);
        invalidate();
    }

    public void setLineWidth(float width) {
        mLineWidth = width;
        invalidate();
    }

    public void setLineSpaceWidth(float width) {
        mLineSpaceWidth = width;
        invalidate();
    }

    public void setLineMinHeight(float height) {
        mLineMinHeight = height;
        invalidate();
    }

    public void setLineMidHeight(float height) {
        mLineMidHeight = height;
        invalidate();
    }

    public void setLineMaxHeight(float height) {
        mLineMaxHeight = height;
        invalidate();
    }

    public void setAlphaEnable(boolean enable) {
        mAlphaEnable = enable;
        invalidate();
    }

    public void setValue(float selectorValue, float minValue, float maxValue, float per) {
        this.mSelectorValue = selectorValue;
        this.mMaxValue = maxValue;
        this.mMinValue = minValue;
        this.mPerValue = (int) (per * 10.0f);
        this.mTotalLine = ((int) ((mMaxValue * 10 - mMinValue * 10) / mPerValue)) + 1;
        mMaxOffset = (int) (-(mTotalLine - 1) * mLineSpaceWidth);

        mOffset = (mMinValue - mSelectorValue) / mPerValue * mLineSpaceWidth * 10;
        invalidate();
        setVisibility(VISIBLE);
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mWidth = w;
            mHeight = h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left, height;
        String value;
        int alpha = 0;
        float scale;
        int srcPointX = mWidth / 2;
        for (int i = 0; i < mTotalLine; i++) {
            left = srcPointX + mOffset + i * mLineSpaceWidth;

            if (left < 0 || left > mWidth) {
                continue;
            }

            if (i % 10 == 0) {
                height = mLineMaxHeight;
            } else if (i % 5 == 0) {
                height = mLineMidHeight;
            } else {
                height = mLineMinHeight;
            }
            if (mAlphaEnable) {
                scale = 1 - Math.abs(left - srcPointX) / srcPointX;
                alpha = (int) (255 * scale * scale);
                mLinePaint.setAlpha(alpha);
            }

            canvas.drawLine(left, 0, left, height, mLinePaint);

            if (i % 10 == 0) {
                value = String.valueOf((int) (mMinValue + i * mPerValue / 10));
                if (mAlphaEnable) {
                    mTextPaint.setAlpha(alpha);
                }
                canvas.drawText(value, left - mTextPaint.measureText(value) / 2,
                        height + mTextMarginTop + mTextHeight, mTextPaint);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int xPosition = (int) event.getX();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastX = xPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove = (mLastX - xPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker();
                return false;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    private void countVelocityTracker() {
        mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = mVelocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }

    private void countMoveEnd() {
        mOffset -= mMove;
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset;
        } else if (mOffset >= 0) {
            mOffset = 0;
        }

        mLastX = 0;
        mMove = 0;

        mSelectorValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mLineSpaceWidth) * mPerValue / 10.0f;
        mOffset = (mMinValue - mSelectorValue) * 10.0f / mPerValue * mLineSpaceWidth;
        notifyValueChange();
        postInvalidate();
    }

    private void changeMoveAndValue() {
        mOffset -= mMove;
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset;
            mMove = 0;
            mScroller.forceFinished(true);
        } else if (mOffset >= 0) {
            mOffset = 0;
            mMove = 0;
            mScroller.forceFinished(true);
        }
        mSelectorValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mLineSpaceWidth) * mPerValue / 10.0f;
        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            mListener.onValueChange(mSelectorValue);
        }
    }

    public interface OnValueChangeListener {
        void onValueChange(float value);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() == mScroller.getFinalX()) {
                countMoveEnd();
            } else {
                int xPosition = mScroller.getCurrX();
                mMove = (mLastX - xPosition);
                changeMoveAndValue();
                mLastX = xPosition;
            }
        }
    }
}

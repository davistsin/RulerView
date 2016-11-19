package com.qindachang.library;

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

    private float mValue = 50;
    private float mMaxValue = 100;
    private float mMinValue = 0;

    private float mItemSpacing = 5;
    private float mLineWidth = 1;
    private float mMaxLineHeight = 42;
    private float mMiddleLineHeight = 30;
    private float mMinLineHeight = 17;
    private int mLineColor=1;

    private float mTextMarginTop = 8;
    private float mTextSize = 14;
    private int mTextColor=1;

    private boolean mAlphaEnable = true;

    private int mPerSpanValue = 1;

    private float mTextHeight;

    private Paint mTextPaint;
    private Paint mLinePaint;

    private int mTotalLine;
    private int mMaxOffset;
    private float mOffset; // 默认尺起始点在屏幕中心, offset是指尺起始点的偏移值
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

        mItemSpacing = typedArray.getDimension(R.styleable.RulerView_lineSpaceWidth, dp2px(context, mItemSpacing));
        mLineWidth = typedArray.getDimension(R.styleable.RulerView_lineWidth, dp2px(context, mLineWidth));
        mMaxLineHeight = typedArray.getDimension(R.styleable.RulerView_lineMaxHeight, dp2px(context, mMaxLineHeight));
        mMiddleLineHeight = typedArray.getDimension(R.styleable.RulerView_lineMidHeight, dp2px(context, mMiddleLineHeight));
        mMinLineHeight = typedArray.getDimension(R.styleable.RulerView_lineMinHeight, dp2px(context, mMinLineHeight));
        mLineColor = typedArray.getColor(R.styleable.RulerView_lineColor, mLineColor);

        mTextSize = typedArray.getDimension(R.styleable.RulerView_textSize, dp2px(context, mTextSize));
        mTextColor = typedArray.getColor(R.styleable.RulerView_textColor, mTextColor);
        mTextMarginTop = typedArray.getDimension(R.styleable.RulerView_textMarginTop, dp2px(context, mTextMarginTop));

        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextHeight = getFontHeight(mTextPaint);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setColor(mLineColor);

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

    public void setTextPaintColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    public void setLinePaintColor(int color) {
        mLinePaint.setColor(color);
        invalidate();
    }

    public void setAlphaEnable(boolean enable) {
        mAlphaEnable = enable;
        invalidate();
    }

    public void initViewParam(float defaultValue, float minValue, float maxValue, int spanValue) {
        this.mValue = defaultValue;
        this.mMaxValue = maxValue;
        this.mMinValue = minValue;
        this.mPerSpanValue = spanValue;
        this.mTotalLine = (int) (maxValue * 10 - minValue * 10) / spanValue + 1;
        mMaxOffset = (int) (-(mTotalLine - 1) * mItemSpacing);

        mOffset = (minValue - defaultValue) / spanValue * mItemSpacing * 10;
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
        int srcPointX = mWidth / 2; // 默认表尺起始点在屏幕中心
        for (int i = 0; i < mTotalLine; i++) {
            left = srcPointX + mOffset + i * mItemSpacing;

            if (left < 0 || left > mWidth) {
                continue;
            }

            if (i % 10 == 0) {
                height = mMaxLineHeight;
            } else if (i % 5 == 0) {
                height = mMiddleLineHeight;
            } else {
                height = mMinLineHeight;
            }
            if (mAlphaEnable) {
                scale = 1 - Math.abs(left - srcPointX) / srcPointX;
                alpha = (int) (255 * scale * scale);
                mLinePaint.setAlpha(alpha);
            }

            canvas.drawLine(left, 0, left, height, mLinePaint);

            if (i % 10 == 0) { // 大指标,要标注文字
                value = String.valueOf((int) (mMinValue + i * mPerSpanValue / 10));
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

        mValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mItemSpacing) * mPerSpanValue / 10.0f;
        mOffset = (mMinValue - mValue) * 10.0f / mPerSpanValue * mItemSpacing; // 矫正位置,保证不会停留在两个相邻刻度之间
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
        mValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mItemSpacing) * mPerSpanValue / 10.0f;
        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            mListener.onValueChange(mValue);
        }
    }

    public interface OnValueChangeListener {
        void onValueChange(float value);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() == mScroller.getFinalX()) { // over
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

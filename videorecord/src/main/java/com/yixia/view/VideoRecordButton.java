package com.yixia.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.yixia.camera.util.DensityUtil;

/**
 * Created by wangwei on 2017/8/26.
 */

public class VideoRecordButton extends View implements View.OnLongClickListener {

    private static String TAG = "VideoRecordButton";
    private Paint mCenterPaint;
    private Paint mRingPaint;
    private int mCenterX;
    private int mCenterY;
    private int mRingRadius;
    private ValueAnimator mAnimator;
    private OnLongPressListener mOnLongPressListener;
    private boolean mHasLongPressed;
    private int mCenterRadius;
    private int mRingStartRadius;
    private int mRingEndRadius;

    public VideoRecordButton(Context context) {
        this(context, null);
    }

    public VideoRecordButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoRecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mCenterPaint = new Paint();
        mCenterPaint.setColor(Color.YELLOW);
        mCenterPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCenterPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mRingPaint = new Paint();
        mRingPaint.setColor(Color.WHITE);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(DensityUtil.dp2px(getContext(), 2));
        mRingPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        setOnLongClickListener(this);

        int totalSize = DensityUtil.dp2px(getContext(), 130);

        mCenterRadius = DensityUtil.dp2px(getContext(), 90) / 2;

        mRingRadius = mRingStartRadius = DensityUtil.dp2px(getContext(), 100) / 2;

        mRingEndRadius = DensityUtil.dp2px(getContext(), 120) / 2;

        mCenterX = totalSize / 2;
        mCenterY = totalSize / 2;

        initAnim();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw ring
        canvas.drawCircle(mCenterX, mCenterY, mRingRadius, mRingPaint);
        //draw center
        canvas.drawCircle(mCenterX, mCenterY, mCenterRadius, mCenterPaint);
    }

    private void initAnim() {

        mAnimator = ValueAnimator.ofInt(mRingStartRadius, mRingEndRadius);
        mAnimator.setDuration(800);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRingRadius = (Integer) animation.getAnimatedValue();
                Log.e(TAG, "mRingRadius: " + mRingRadius);
                //中间画笔的明暗 125-255
                float fraction = animation.getAnimatedFraction();
                mCenterPaint.setAlpha((int) (125 + (255 - 125) * fraction));

                invalidate();
            }
        });

        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });

    }


    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (!pressed && mHasLongPressed && mOnLongPressListener != null) {
            mHasLongPressed = false;
            mOnLongPressListener.onLongPressEnd();
            mAnimator.end();
            mRingRadius = mRingStartRadius;
            mCenterPaint.setAlpha(255);
            return;
        }


    }





    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        mOnLongPressListener = onLongPressListener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnLongPressListener != null) {
            mOnLongPressListener.onLongPressBegin();
            mHasLongPressed = true;
            mAnimator.start();

        }
        return true;
    }


    public interface OnLongPressListener {
        void onLongPressBegin();

        void onLongPressEnd();
    }
}

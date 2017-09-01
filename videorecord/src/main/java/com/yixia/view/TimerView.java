package com.yixia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yixia.camera.util.DensityUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangwei on 2017/9/1.
 */

public class TimerView extends View {

    private static String TAG = "Timer";
    int oneTenSecond;
    private Paint mRectPaint;
    private Paint mTextPaint;
    private int mWidth;
    private int mHeight;
    private int mRectRadius;
    private float mFontHeight;
    private float mFontWidth;
    private boolean DEBUG = true;
    private String mTime;
    private Timer mTimer;
    private long mSecond;
    private long mStartTime;
    private long mTemp;

    public TimerView(Context context) {
        this(context, null);
    }

    public TimerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mWidth = DensityUtil.dp2px(context, 50);
        mHeight = DensityUtil.dp2px(context, 18);

        mRectRadius = DensityUtil.dp2px(context, 6);

        mRectPaint = new Paint();
        mRectPaint.setColor(Color.WHITE);
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(DensityUtil.dp2px(context, 1));

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mHeight / 2);
        mTime = "0.0秒";

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mFontHeight = fm.bottom - fm.top;
        mFontWidth = mTextPaint.measureText(mTime);

        log("mWidth", mWidth);
        log("mFontWidth", mFontWidth);

        // mWidth = (int) Math.max(mFontWidth + 2, mWidth);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(0, 0, mWidth, mHeight, mRectRadius, mRectRadius, mRectPaint);

        canvas.drawText(getTimeString(), mWidth / 2 - mFontWidth / 2, mHeight / 2 + mFontHeight / 4, mTextPaint);
    }

    private String getTimeString() {
        return mSecond + "." + oneTenSecond + "秒";
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    public void start() {
        oneTenSecond = 0;
        mTimer = new Timer();
        mSecond = 0;
        mStartTime = System.currentTimeMillis();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                long span = System.currentTimeMillis() - mStartTime + mTemp;
                format(span);
            }
        }, 0, 100);
    }

    private void format(long span) {
        long days = span / (1000 * 60 * 60 * 24);
        long hours = (span - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long mMinutes = (span - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);

        mSecond = (span - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - (1000 * 60) * mMinutes) / 1000;
        oneTenSecond = (int) ((span - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - (1000 * 60) * mMinutes - 1000 * mSecond) / 100);
        //oneTenSecond = oneTenSecond++ % 9;
        postInvalidate();
    }

    public void stop() {
        mTimer.cancel();
        mTemp += System.currentTimeMillis() - mStartTime;
    }

    public void reset() {
        mSecond = 0;
        oneTenSecond = 0;
        mTemp = 0;
        invalidate();
    }

    public void log(String prefix, Object log) {
        if (DEBUG) {
            Log.e(TAG, prefix + " : " + log);
        }
    }


    public void truncate(int duration) {
        mTemp -= duration;
        format(mTemp);
    }
}

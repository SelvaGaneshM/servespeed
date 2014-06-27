package com.example.ServeSpeed;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;


public class MyVideoView extends VideoView {

    String TAG = "servespeed";
    public MyVideoView(Context context) {
        super(context);
        setWillNotDraw(false);
        Log.d(TAG, "fps :");
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        Log.d(TAG, "fps :");
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        Log.d(TAG, "fps :");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG,"fpsondraw :");
        double fps = fps();
        Log.d(TAG,"fps :" + fps);
        super.onDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG,"fpsdraw :");
        double fps = fps();
        Log.d(TAG,"fps :" + fps);
        super.draw(canvas);
    }

    private final double NANOS = 1000000000.0;
    private long lastTime = 0;

    /**
     * Calculates and returns frames per second
     */
    private double fps() {
        long nowTime = System.nanoTime();
        double difference = (nowTime - lastTime) / NANOS;
        lastTime = nowTime;
        return 1/difference;
    }
}

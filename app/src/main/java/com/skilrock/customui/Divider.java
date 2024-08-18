package com.skilrock.customui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stpl on 7/2/2015.
 */
public class Divider extends View{

    private float VIEW_HEIGHT;
    private float QUATER_HEIGHT;
    private float HALF_HEIGHT;
    private Paint paint;
    private int color = 0xFFFFFFFF;

    public Divider(Context context) {
        super(context);
    }

    public Divider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Divider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Divider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        VIEW_HEIGHT = getMeasuredHeight();
        HALF_HEIGHT = VIEW_HEIGHT / 2;
        QUATER_HEIGHT = HALF_HEIGHT / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        canvas.drawRect(0, HALF_HEIGHT, getMeasuredWidth(), HALF_HEIGHT + QUATER_HEIGHT, paint);
    }
}

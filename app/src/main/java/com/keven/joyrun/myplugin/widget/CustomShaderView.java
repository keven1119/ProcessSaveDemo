package com.keven.joyrun.myplugin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by keven on 16/11/18.
 */

public class CustomShaderView extends View {
    public Paint paint;
    private Rect rect;

    public CustomShaderView(Context context) {
        this(context, null);
    }

    public CustomShaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect = new Rect(0, 0, getWidth(), getHeight());
        canvas.drawCircle(100, 100, 100, paint);
    }
}

package com.keven.joyrun.myplugin.widget.water;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by keven on 16/10/9.
 */

public class Renderable {

    public Bitmap bitmap;
    public float x;
    public float y;

    public Renderable(Bitmap bitmap, float x, float y){
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

    public void update(float deltaTime){

    }

    public void draw(Canvas canvas){
        canvas.save();
        canvas.drawBitmap(bitmap, x, y, null);
        canvas.restore();
    }
}

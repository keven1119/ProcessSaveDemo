package com.keven.joyrun.myplugin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.keven.joyrun.myplugin.widget.CustomShaderView;

/***
 * Shader实现图形渲染
 * @author Administrator
 *
 */
public class ShaderActivity extends Activity implements OnClickListener {
    /** 声明位图渲染对象 */
    private Shader[] shaders = new Shader[5];
    /** 声明颜色数组 */
    private int[] colors;
    /** 自定义组件 */
    private CustomShaderView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shader);
        myView = (CustomShaderView) findViewById(R.id.my_view);
        // 获取bitmap实例
        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.tree);
        // 设置渐变颜色组,也就是按红,绿,蓝的方式渐变
        colors = new int[] { Color.RED, Color.GREEN, Color.BLUE };
        // 实例化BitmapShader,x坐标方向重复图形,y坐标方向镜像图形
        shaders[0] = new BitmapShader(bm, TileMode.REPEAT, TileMode.MIRROR);
        // 实例化LinearGradient
        shaders[1] = new LinearGradient(0, 0, 100, 100, colors, null,
                TileMode.REPEAT);
        // 实例化RadialGradient
        shaders[2] = new RadialGradient(100, 100, 80, colors, null,
                TileMode.REPEAT);
        // 实例化SweepGradient
        shaders[3] = new SweepGradient(160, 160, colors, null);
        // 实例化ComposeShader
        shaders[4] = new ComposeShader(shaders[0], shaders[2],
                PorterDuff.Mode.DARKEN);

        initEnent();
    }

    private void initEnent() {
        findViewById(R.id.bitmap_shader).setOnClickListener(this);
        findViewById(R.id.linear_gradient).setOnClickListener(this);
        findViewById(R.id.radial_gradient).setOnClickListener(this);
        findViewById(R.id.sweep_gradient).setOnClickListener(this);
        findViewById(R.id.compose_shader).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bitmap_shader:
                myView.paint.setShader(shaders[0]);
                break;
            case R.id.linear_gradient:
                myView.paint.setShader(shaders[1]);
                break;
            case R.id.radial_gradient:
                myView.paint.setShader(shaders[2]);
                break;
            case R.id.sweep_gradient:
                myView.paint.setShader(shaders[3]);
                break;
            case R.id.compose_shader:
                myView.paint.setShader(shaders[4]);
                break;
        }
        myView.invalidate();
    }

}

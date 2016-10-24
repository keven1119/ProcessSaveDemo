package com.keven.joyrun.myplugin;

import android.content.Context;
import android.content.res.Resources;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by keven on 16/9/30.
 */

public class ScreenUtil {
    private static int   screenHeight;    // 高度
    private static int   screenWidth;    // 宽度
    private static float density;    // 宽度
//	private static Context mContext;

    private ScreenUtil() {

    }

    public static int getScreenHeight(Context context) {
//		if (mContext == null) {
//			throw new RuntimeException("You need to be init.In Application.onCreate()");
//		}
        if (context == null){
            return 100;
        }
        if (screenHeight == 0) {
            screenHeight = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getHeight();
        }
        return screenHeight;
    }

    public static int getScreenWidth(Context context) {
//		if (mContext == null) {
//			throw new RuntimeException("You need to be init.In Application.onCreate()");
//		}
        if (context == null){
            return 100;
        }
        if (screenWidth == 0) {
            screenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getWidth();
        }
        return screenWidth;
    }

    /**
     * please use  {@linkplain #dpToPx(float)}
     */
    @Deprecated
    public static int dpToPx(Context context, float dp) {
        return dpToPx(dp);
    }

    /**
     * dp转换为px
     */
    public static int dpToPx(float dp) {
        if (density == 0) {
            density = Resources.getSystem().getDisplayMetrics().density;
        }
        return (int) (dp * density + 0.5f);
    }

    /**
     * please use  {@linkplain #pxToDp(float)}
     */
    @Deprecated
    public static int px2dip(Context context, float pxValue) {
        return pxToDp(pxValue);
    }

    /**
     * px(像素) 的单位 转成为 dp
     */
    public static int pxToDp(float pxValue) {
        if (density == 0) {
            density = Resources.getSystem().getDisplayMetrics().density;
        }
        return (int) (pxValue / density + 0.5f);
    }

//	public static void init(Context context) {
//		ScreenUtils.mContext = context;
//	}

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c     = Class.forName("com.android.internal.R$dimen");
            Object   obj   = c.newInstance();
            Field field = c.getField("status_bar_height");
            int      x     = Integer.parseInt(field.get(obj).toString());

            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
        }

        return 0;
    }
}

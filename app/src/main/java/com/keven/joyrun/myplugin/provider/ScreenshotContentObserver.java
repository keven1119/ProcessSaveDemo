package com.keven.joyrun.myplugin.provider;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.keven.joyrun.myplugin.MyApplication;
import com.keven.joyrun.myplugin.ScreenUtil;

import java.io.File;

/**
 * Created by keven on 16/11/11.
 */

public class ScreenshotContentObserver extends ContentObserver {

    private Context mContext;
    private int imageNum;

    private static ScreenshotContentObserver instance;

    private ScreenshotContentObserver(Context context) {
        super(null);
        mContext = context;
    }

    public static void startObserve() {
        if (instance == null) {
            instance = new ScreenshotContentObserver(MyApplication.getInstance());
        }
        instance.register();
    }

    public static void stopObserve() {
        if(instance != null) {
            instance.unregister();
        }
    }

    private void register() {
        mContext.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, this);
    }

    private void unregister() {
        mContext.getContentResolver().unregisterContentObserver(this);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Log.d("keven1119" , "ScreenshotContentObserver  onChange ==>" +selfChange);
        String[] columns = {
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.DATA,
        };
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    MediaStore.MediaColumns.DATE_MODIFIED + " desc");
            if (cursor == null) {
                return;
            }
            int count = cursor.getCount();
            if (imageNum == 0) {
                imageNum = count;
            } else if (imageNum >= count) {
                return;
            }
            imageNum = count;
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                long addTime = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED));
                if (matchAddTime(addTime) && matchPath(filePath) && matchSize(filePath)) {
//                    doReport(filePath);

                    Log.d("keven1119" , "ScreenshotContentObserver addTime ==>" + addTime +
                                            "filepath===>"+ filePath );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加时间与当前时间不超过1.5s,大部分时候不超过1s。
     *
     * @param addTime 图片添加时间，单位:秒
     */
    private boolean matchAddTime(long addTime) {
        return System.currentTimeMillis() - addTime * 1000 < 1500;
    }

    /**
     * 尺寸不大于屏幕尺寸（发现360奇酷手机可以对截屏进行裁剪）
     */
    private boolean matchSize(String filePath) {
        Point size = getScreenWidthAndHeight(mContext);//获取屏幕尺寸

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        return size.x >= options.outWidth && size.y >= options.outHeight;
    }

    /**
     * 已调查的手机截屏图片的路径中带有screenshot
     */
    private boolean matchPath(String filePath) {
        String lower = filePath.toLowerCase();
        return lower.contains("screenshot");
    }

    private void doReport(String filePath) {
        //删除截屏
        File file = new File(filePath);
        file.delete();
        //TODO:
    }

    private Point getScreenWidthAndHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(mContext.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }
}

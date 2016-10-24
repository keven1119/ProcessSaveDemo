package com.keven.joyrun.myplugin;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by keven on 16/10/20.
 */

public class AppUtils {

    private static CharSequence MY_PKG_NAME = "com.keven.joyrun.myplugin";

    public static void wakeupProgress(Context context){
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.keven.joyrun.myplugin", "com.keven.joyrun.myplugin.BubbleActvity");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 判断主进程是否运行
     * @param context
     * @return
     */
    public static boolean isMainActivityWorking(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().contains(MY_PKG_NAME) && info.baseActivity.getPackageName().contains(MY_PKG_NAME)) {
                return true;
            }
        }

        Log.d("keven1119","MainActivity  is Killed");

        return false;
    }

    /**
     * 判断进程是否在运行
     * @param context
     * @param className
     * @return
     */
    public static boolean isWorking(Context context, String className) {
        ActivityManager myManager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(100);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断Activity 是否存在
     * @param context
     * @param intent
     * @return
     */
    public static boolean isActivityExist(Context context, Intent intent){
        return context.getPackageManager().resolveActivity(intent, 0) == null;
    }
}

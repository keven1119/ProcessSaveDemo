package com.keven.joyrun.myplugin;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.keven.joyrun.myplugin.services.DaemonService;
import com.keven.joyrun.myplugin.services.WorkingService;

/**
 * Created by keven on 16/10/13.
 */

public class MyApplication extends Application {

    public static MyApplication  mGlodenApp;

    public static MyApplication getInstance(){
        return mGlodenApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGlodenApp = this;
        Log.d("keven1119", "onCreate process name ==>" + getProcessName(this));
        startService(new Intent(this, WorkingService.class));
        startService(new Intent(this, DaemonService.class));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d("keven1119", "onTerminate process name ==>" + getProcessName(this));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d("keven1119", "attachBaseContext process name ==>" + getProcessName(this));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("keven1119", "onConfigurationChanged process name ==>" + getProcessName(this));
    }

    private String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }    return null;
    }
}

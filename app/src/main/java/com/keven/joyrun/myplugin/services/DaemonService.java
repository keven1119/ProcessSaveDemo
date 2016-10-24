package com.keven.joyrun.myplugin.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.keven.joyrun.myplugin.AppUtils;

/**
 * Created by keven on 16/10/20.
 */

public class DaemonService extends Service implements Handler.Callback{

    private static Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        Log.d("keven1119", "DaemonService   onCreate  ==>"+ toString());
        super.onCreate();

        if(null == mHandler){
            mHandler = new Handler(this);

            mHandler.sendEmptyMessageDelayed(1,5000);
        }

    }

    @Override
    public boolean handleMessage(Message msg) {

        if (!AppUtils.isWorking(DaemonService.this,"com.keven.joyrun.myplugin.services.WorkingService")){
            AppUtils.wakeupProgress(DaemonService.this);
            Log.d("keven1119","wakeup BubbleActivity");
        }
//                    sendBroadcast(new Intent("android.hardware.usb.action.USB_STATE"));
        mHandler.sendEmptyMessageDelayed(1,5000);
        return false;
    }
}

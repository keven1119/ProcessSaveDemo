package com.keven.joyrun.myplugin.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.keven.joyrun.myplugin.AppUtils;
import com.keven.joyrun.myplugin.services.DaemonService;
import com.keven.joyrun.myplugin.services.WorkingService;

/**
 * Created by keven on 16/10/20.
 */

public class GetuiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("keven1119","GetuiReceiver   onReceive");

        if(!AppUtils.isWorking(context,"com.keven.joyrun.myplugin.services.DaemonService")){
//            AppUtils.wakeupProgress(context);
            context.startService(new Intent(context, DaemonService.class));
        }
    }
}

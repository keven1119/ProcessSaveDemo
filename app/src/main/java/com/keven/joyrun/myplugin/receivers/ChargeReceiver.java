package com.keven.joyrun.myplugin.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by keven on 16/10/20.
 */

public class ChargeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("keven1119","接上 usb");
    }
}

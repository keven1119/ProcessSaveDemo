package com.keven.joyrun.myplugin.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by keven on 16/10/13.
 */

public class AService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("keven1119","start  AService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

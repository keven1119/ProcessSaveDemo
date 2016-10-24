package com.keven.joyrun.myplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.keven.joyrun.myplugin.services.WorkingService;

/**
 * Created by keven on 16/10/17.
 */

public class RecyclerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("keven1119","RecyclerActivity  onCreate");
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.activity_recycler);
        startService(new Intent(this, WorkingService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("keven1119","RecyclerActivity  onResume");
    }
}

package com.keven.joyrun.myplugin.services;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.keven.joyrun.myplugin.AppUtils;
import com.keven.joyrun.myplugin.BubbleActvity;
import com.keven.joyrun.myplugin.CountTimer;
import com.keven.joyrun.myplugin.MainActivity;
import com.keven.joyrun.myplugin.RecyclerActivity;
import com.keven.joyrun.myplugin.Wakeupctivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.BatchUpdateException;
import java.util.Random;


/**
 * Created by keven on 16/10/20.
 */

public class WorkingService extends Service {

    private static final int OPEN_WAKELOCK = 1;
    private static final int CHECK_START = 2;
    private static final int SCREEN_ON = 3;
    private static final int OPEN_WAKEUP = 4;
    private static final int KILL_PROCESS = 5;

    CountTimer mCountTimer;
    private long mOneTimer = 0;
    private int mOn5sTimer = 0;
    private Socket mClientSocket;
    private PrintWriter mPrintWriter;

    private Random mRandom;

    Intent intent ;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case OPEN_WAKELOCK:
                    Intent intent2 = new Intent(WorkingService.this, RecyclerActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                    break;

                case CHECK_START:
//                    if(!AppUtils.isWorking(WorkingService.this,"com.keven.joyrun.myplugin.services.DaemonService")){
//                        AppUtils.wakeupProgress(WorkingService.this);
//                        Log.d("keven1119","WorkingService  to  startDaemonService");
//                    }

                    if(mPrintWriter != null){
                        mPrintWriter.println(" DaemonService  你好");
                    }
                    break;

                case SCREEN_ON:
//                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//                    //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
//                    PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "simpler");
//                    //点亮屏幕，只有OPPO手机需要唤醒
//
//                    if(pm.isScreenOn()) {
//                        wakeLock.acquire();
//                        wakeLock.release();
//                    }
                    unlockScreen();
                    lockScreen();
                    break;

                case OPEN_WAKEUP:
                    Intent intent3 = new Intent(WorkingService.this, Wakeupctivity.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(intent3);
                    break;

                case KILL_PROCESS:
                    android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    } ;
    private PowerManager.WakeLock mWakelock;
    private KeyguardManager km;
    private KeyguardManager.KeyguardLock mKeyguardLock;
    private boolean isFinishing;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent();
        intent.setClassName("com.keven.joyrun.myplugin", "com.keven.joyrun.myplugin.BubbleActvity");
        isFinishing = false;
        //开启锁屏监测
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenReceiver, filter);

        new Thread() {
            @Override
            public void run() {
                connectSocketServer();
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFinishing = true;
        if(null != mScreenReceiver){
            unregisterReceiver(mScreenReceiver);
            mScreenReceiver = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mCountTimer == null) {
            mCountTimer = new CountTimer(new Runnable() {
                @Override
                public void run() {
                    mOneTimer++;
                    action1s(mOneTimer);
                }
            },500);
            mCountTimer.startTimer();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void action1s(long aLong) {

        if(screenLockTime != 0){
            mHandler.sendEmptyMessageDelayed(OPEN_WAKEUP,200);
            mHandler.sendEmptyMessageDelayed(CHECK_START,200);
        }

        if (aLong % 5 == 0) {
            mOn5sTimer++;
            do5sTimerAction();
        }
    }

    private void do5sTimerAction() {

         //每10分钟唤醒屏幕一次
//        if (mOn5sTimer % 4 == 2) {
//            Log.e("keven1119","唤醒屏幕");
//            //获取电源管理器对象
//            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
//            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
//            //点亮屏幕，只有OPPO手机需要唤醒
//            wakeLock.acquire();
//            wakeLock.release();
//        }

        if(screenLockTime != 0 && mOn5sTimer % 4 == 2){
            mHandler.sendEmptyMessageDelayed(SCREEN_ON,1000);
            mHandler.sendEmptyMessage(OPEN_WAKEUP);
        }

        if (mOn5sTimer % 4 == 2) {
            Log.d("keven1119","WorkingService  do on 5 second");
            mHandler.sendEmptyMessage(CHECK_START);
//            if (!AppUtils.isWorking(this,"com.keven.joyrun.myplugin.services.DaemonService")){
//                startService(new Intent(this,DaemonService.class));
//                Log.d("keven1119","wakeup DaemonService");
//            }
        }

//        if(screenLockTime != 0 && (System.currentTimeMillis() - screenLockTime)/1000/60 > 2){
//            Log.d("keven1119","kill main process");
//            mHandler.sendEmptyMessage(KILL_PROCESS);
//        }
//        // 每10分钟唤醒屏幕一次
//        if (mOn5sTimer % 120 == 4) {
//            Log.e("keven1119","唤醒屏幕");
//            //获取电源管理器对象
//            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
//            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
//            //点亮屏幕，只有OPPO手机需要唤醒
//            wakeLock.acquire();
//            wakeLock.release();
//        }
    }


    private long screenLockTime;
    /**
     * 开锁屏监听
     */
    private BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Intent.ACTION_SCREEN_ON:
                    screenLockTime = 0;
                    Log.d("keven1119", "屏幕解锁广播...");
                    mHandler.sendEmptyMessageDelayed(CHECK_START,1000);
                    mHandler.sendEmptyMessageDelayed(OPEN_WAKELOCK,2000);
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    screenLockTime = System.currentTimeMillis();
                    Log.d("keven1119", "屏幕加锁广播...");
                    mHandler.sendEmptyMessageDelayed(CHECK_START,1000);
                    mHandler.sendEmptyMessageDelayed(OPEN_WAKELOCK,2000);
                    break;
            }
        }
    };


    public void unlockScreen() {
        // 获取PowerManager的实例 
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        // 得到一个WakeLock唤醒锁

        try {
            mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.PARTIAL_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE
                    , "SimpleTimer");
            if (!mWakelock.isHeld()) {
                // 唤醒屏幕
                mWakelock.acquire();
            }

            // 获得一个KeyguardManager的实例
            km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
            // 得到一个键盘锁KeyguardLock
            mKeyguardLock = km.newKeyguardLock("SimpleTimer");
            if (km.inKeyguardRestrictedInputMode()) {
                // 解锁键盘
                mKeyguardLock.disableKeyguard();
            }
        }catch (Exception e){
            Log.d("keven1119","e ====>" + e.toString());
        }

    }

    public void lockScreen() {
        // release screen
        if (!km.inKeyguardRestrictedInputMode()) {
            // 锁键盘
            mKeyguardLock.reenableKeyguard();
        }
        // 使屏幕休眠
        if (mWakelock.isHeld()) {
            mWakelock.release();
        }
    }


    private void connectSocketServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                //选择和服务器相同的端口8688
                socket = new Socket("localhost", 8688);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (IOException e) {
                SystemClock.sleep(1000);
            }
        }
        try {
            // 接收服务器端的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!isFinishing) {
                final String msg = br.readLine();
                if (msg != null) {
                    Log.d("keven1119","working service msg==>" + msg);
                }
            }
            mPrintWriter.close();
            br.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

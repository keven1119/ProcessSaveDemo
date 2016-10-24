package com.keven.joyrun.myplugin;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by keven on 16/10/20.
 */

public class CountTimer {

    //	private Handler		mHandler;
    private Runnable mRunnable;
    private Timer mTimer;
    private TimerTask mTimerTask;            // 定时器任务
    private int mTimerInterval = 1000; // 定时器触发间隔时间(ms)
//	private boolean isStartTimer;            // 定时器是否已开启

    /**
     * @param runnable CountTimer会定时回调
     */
    public CountTimer(Runnable runnable, int timerInterval) {
        this.mTimerInterval = timerInterval;
        mRunnable = runnable;

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

    }

    public void startTimer() {
        if (mRunnable == null || mTimerTask != null) {
            return;
        }
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mRunnable != null) {
                    mRunnable.run();
                }
            }
        };
        mTimer = new Timer(true);
        mTimer.schedule(mTimerTask, mTimerInterval, mTimerInterval);
    }

    public void stopTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mRunnable = null;
    }
}

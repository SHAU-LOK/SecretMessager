package com.shootloking.secretmessager.view;

import android.app.Application;
import android.os.Handler;
import android.os.Message;

/**
 * Created by shau-lok on 1/18/16.
 */
public class SMApplication extends Application {

    private static SMApplication mApp;
    Handler mHandler;

    public static SMApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });
        mApp = this;
    }

    public void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void postDelay(Runnable runnable, long time) {
        mHandler.postDelayed(runnable, time);
    }

    public void removeCallBacks(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }

    public Handler getHandler() {
        return mHandler;
    }
}

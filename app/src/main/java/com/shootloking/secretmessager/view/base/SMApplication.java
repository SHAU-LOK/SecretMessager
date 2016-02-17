package com.shootloking.secretmessager.view.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.utility.log.Logger;

/**
 * Created by shau-lok on 1/18/16.
 */
public class SMApplication extends Application {

    public static final String TAG = "SMApplication";
    public static final int REQUEST_CODE_READ_SMS_PERMISSIONS = 0;
    public static final int REQUEST_CODE_READ_CONTACT_PERMISSIONS = 1;


    private static SMApplication mApp;
    Handler mHandler;

    public static SMApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init();
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


    public static boolean hasPermission(final Context context, final String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean requestPermission(final Activity activity, final String permission,
                                     final int requestCode) {
        Debug.log(TAG, "请求 permission: " + permission);
        if (!hasPermission(activity, permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            return false;
        } else {
            return true;
        }
    }

}

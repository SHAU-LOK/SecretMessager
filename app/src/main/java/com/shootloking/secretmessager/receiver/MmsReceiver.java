package com.shootloking.secretmessager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shootloking.secretmessager.utility.Utils;

/**
 * Created by shau-lok on 2/19/16.
 */
public class MmsReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Utils.isDefaultApp(context)) {
            SmsReceiver.handleOnReceive(this, context, intent);
        }
    }
}

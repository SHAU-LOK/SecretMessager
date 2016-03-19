package com.shootloking.secretmessager.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.widget.Toast;

import com.shootloking.secretmessager.data.SmsHelper;
import com.shootloking.secretmessager.event.NotifyReceiveEvent;
import com.shootloking.secretmessager.event.NotifySentSuccessEvent;
import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.view.SendSmsActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by shau-lok on 2/19/16.
 */
public class SmsReceiver extends BroadcastReceiver {

    public static final String TAG = "SmsReceiver";

    static final String ACTION_SMS_DELIVER = "android.provider.Telephony.SMS_DELIVER";


    private static Uri mUri;
    private static String mBody;
    private static String mAddress;
    private static long mDate;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Utils.isDefaultApp(context)) {
            handleOnReceive(this, context, intent);
        }
    }

    static void handleOnReceive(BroadcastReceiver broadcastReceiver, Context context, Intent intent) {

        String action = intent.getAction();
        if (TextUtils.equals(SendSmsActivity.MESSAGE_SENT_ACTION, action)) {
            handleSent(context, intent, broadcastReceiver.getResultCode());
        } else if (TextUtils.equals(ACTION_SMS_DELIVER, action)) {
            Bundle bundle = intent.getExtras();

            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] smsMessages = new SmsMessage[pdus.length];
            for (int i = 0; i < smsMessages.length; i++) {
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
//
            SmsMessage sms = smsMessages[0];
            if (smsMessages.length == 1 || sms.isReplace()) {
                mBody = sms.getDisplayMessageBody();
            } else {
                StringBuilder builder = new StringBuilder();
                for (SmsMessage message : smsMessages) {
                    builder.append(message.getMessageBody());
                }
                mBody = builder.toString();
            }
            mAddress = sms.getOriginatingAddress();
            mDate = sms.getTimestampMillis();

            insertMessage(context);
            updateNotification(context, mBody);
        }
    }


    private static void handleSent(Context context, Intent intent, int resultCode) {
        Uri uri = intent.getData();
        Debug.log(TAG, "uri: " + uri.toString() + " , result code: " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            long currentTime = System.currentTimeMillis();
            ContentValues content = new ContentValues(1);
            content.put("type", CallLog.Calls.OUTGOING_TYPE);
            context.getContentResolver().update(uri, content, null, null);
            Debug.log(TAG, "发送成功");
            Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
            NotifySentSuccessEvent event = new NotifySentSuccessEvent();
            event.time = currentTime;
            EventBus.getDefault().post(event);
        } else {
            Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
            updateFailure(context, uri);
        }
    }

    private static void updateFailure(Context context, Uri uri) {

        Debug.error(TAG, "Failure: " + uri.toString());
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // TODO: 2/22/16 提示消息失败
        }

    }

    private static void insertMessage(Context context) {

        mUri = SmsHelper.addMessageToInbox(context, mAddress, mBody, mDate);

        if (mUri != null) {
            EventBus.getDefault().post(new NotifyReceiveEvent());
        }

        // TODO: 2/19/16 提示消息来
    }


    private static void updateNotification(Context context, String mBody) {


    }

}

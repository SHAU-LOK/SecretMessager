package com.shootloking.secretmessager.view;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.shootloking.secretmessager.data.Constants;
import com.shootloking.secretmessager.receiver.SmsReceiver;
import com.shootloking.secretmessager.view.base.SMActivity;

import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by shau-lok on 2/19/16.
 */
public class SendSmsActivity extends SMActivity {
    @Override
    protected String getPageName() {
        return "SendSmsActivity";
    }


    public static final String MESSAGE_SENT_ACTION = "com.android.mms.transaction.MESSAGE_SENT";

    private String to;
    private String body;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (parseIntent(intent)) {
            setTheme(android.R.style.Theme_Translucent_NoTitleBar);
            send(to, body);

            finish();
        }
    }


    private boolean parseIntent(Intent intent) {

        if (intent == null) return false;

        String data = intent.getDataString();

        if (!TextUtils.isEmpty(data) && data.contains(":")) {
            String t = data.split(":")[1];
            if (t.startsWith("+")) {
                to = "+" + URLDecoder.decode(t.substring(1));
            } else {
                to = URLDecoder.decode(t);
            }
        }
        CharSequence cstext = intent.getCharSequenceExtra(Intent.EXTRA_TEXT);
        if (cstext != null) {
            body = cstext.toString();
        }
        if (TextUtils.isEmpty(body) || TextUtils.isEmpty(to)) {
            return false;
        }
        return true;

    }

    private void send(String to, String body) {
        if (TextUtils.isEmpty(to) || TextUtils.isEmpty(body)) {
            return;
        }


        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", 3);
        contentValues.put("body", body);
        contentValues.put("read", 1);
        contentValues.put("address", to);
        Uri draft = null;
//        Cursor cursor = contentResolver.query(Constants.URI_SMS_SENT, PROJECTIONS,
//                "type" + " = " + 3 + " AND " + "address" + " = '" + to
//                        + "' AND " + "body" + " like '" + body.replace("'", "_") + "'", null, "date"
//                        + " DESC");

        draft = contentResolver.insert(Constants.URI_SMS_SENT, contentValues);
        SmsManager sm = SmsManager.getDefault();
        ArrayList<String> messages = sm.divideMessage(body);
        final int size = messages.size();

        ArrayList<PendingIntent> sentIntents = new ArrayList<>(size);

        try {
//            Debug.log(getPageName(), " send message to : " + to);
            for (String msg : messages) {
                Intent intent = new Intent(MESSAGE_SENT_ACTION, draft, this, SmsReceiver.class);
                sentIntents.add(PendingIntent.getBroadcast(this, 0, intent, 0));
            }
            sm.sendMultipartTextMessage(to, null, messages, sentIntents, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

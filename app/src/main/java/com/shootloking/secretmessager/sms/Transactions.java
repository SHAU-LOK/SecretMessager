package com.shootloking.secretmessager.sms;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
import android.text.TextUtils;

import com.shootloking.secretmessager.utility.log.Debug;

/**
 * Created by shau-lok on 2/25/16.
 */
public class Transactions {


    public static final String TAG = "transactions";
    private Context context;

    public Transactions(Context context) {
        this.context = context;
    }

    public void sendMessage(String body, String address) {
        if (context == null) {
            Debug.error(TAG, "context 不能为空");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);

        if (address == null) {
            intent.setData(Uri.parse("sms:"));
        } else {
            intent.setData(Uri.parse("smsto:" + address));
        }
        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
        if (!TextUtils.isEmpty(defaultSmsPackageName)) {
            intent.setPackage(defaultSmsPackageName);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra("sms_body", body);
//        intent.putExtra("AUTOSEND", "1");

        context.startActivity(intent);
    }

}

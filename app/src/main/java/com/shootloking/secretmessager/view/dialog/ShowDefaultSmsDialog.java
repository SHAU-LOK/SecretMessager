package com.shootloking.secretmessager.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Telephony;
import android.widget.Toast;

/**
 * Created by shau-lok on 2/19/16.
 */
public class ShowDefaultSmsDialog implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

    private Context context;

    public ShowDefaultSmsDialog(Context context) {
        this.context = context;
    }

    public void show() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("更短信应用");
//        builder.setMessage("使用\"SecretMessage\"作为默认短信应用");
//        builder.setNegativeButton(android.R.string.cancel, this);
//        builder.setPositiveButton(android.R.string.ok, this);
//        builder.show();
        handleIntent();

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Toast.makeText(context, "已取消", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        handleIntent();
    }

    private void handleIntent() {
        Intent intent =
                new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                context.getPackageName());
        context.startActivity(intent);
    }
}

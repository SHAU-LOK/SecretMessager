package com.shootloking.secretmessager.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.shootloking.secretmessager.encryption.RSAEncryptManager;
import com.shootloking.secretmessager.event.EncryptEvent;
import com.shootloking.secretmessager.view.base.SMActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by shau-lok on 3/21/16.
 */
public class RSAEncryptSendAsyncTask extends AsyncTask<String, Integer, Boolean> {

    private ProgressDialog progressDialog;
    private SMActivity activity;

    public RSAEncryptSendAsyncTask(SMActivity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("加密中...");
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String body;
        if (TextUtils.isEmpty(params[0])) return false;
        try {
            long start = System.nanoTime();
            body = RSAEncryptManager.getInstance().encrypt(activity, params[0]);
            long end = System.nanoTime();
            long consumeTime = end - start;
            if (!TextUtils.isEmpty(body)) {
                EncryptEvent event = new EncryptEvent(body);
                event.EncryptConsumeTime = consumeTime;
                event.beforeLength = String.valueOf(params[0].getBytes().length);
                event.afterLength = String.valueOf(body.getBytes().length);
                event.description = "RSA加密";
                event.plainText = params[0];
                event.cipherText = body;
                Thread.sleep(2000);
                EventBus.getDefault().post(event);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (aBoolean) {
            //success
            Toast.makeText(activity, "加密成功", Toast.LENGTH_SHORT).show();
        } else {
            //fail
            Toast.makeText(activity, "加密失败", Toast.LENGTH_SHORT).show();
        }
    }
}

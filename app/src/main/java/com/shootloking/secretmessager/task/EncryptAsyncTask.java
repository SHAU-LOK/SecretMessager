package com.shootloking.secretmessager.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.shootloking.secretmessager.encryption.EncryptManger;
import com.shootloking.secretmessager.view.base.SMActivity;

/**
 * Created by shau-lok on 3/20/16.
 */
public class EncryptAsyncTask extends AsyncTask<String, Integer, String> {

    private ProgressDialog progressDialog;
    private SMActivity activity;


    public EncryptAsyncTask(SMActivity activity) {
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
    protected String doInBackground(String... params) {
        String body = params[0];
        try {
            String cipher = EncryptManger.getInstance().Encrypt(body);
            Thread.sleep(2000);
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String plain) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (!TextUtils.isEmpty(plain)) {
            //success
            Toast.makeText(activity, plain, Toast.LENGTH_SHORT).show();
        } else {
            //fail
            Toast.makeText(activity, "加密失败", Toast.LENGTH_SHORT).show();
        }

    }


}

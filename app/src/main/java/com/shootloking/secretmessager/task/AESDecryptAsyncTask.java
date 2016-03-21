package com.shootloking.secretmessager.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.shootloking.secretmessager.encryption.AESEncryptManger;
import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.view.base.SMActivity;

/**
 * Created by shau-lok on 3/20/16.
 */
public class AESDecryptAsyncTask extends AsyncTask<String, Integer, String> {

    private ProgressDialog progressDialog;
    private SMActivity activity;

    public AESDecryptAsyncTask(SMActivity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("解密中...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String body = params[0];
        try {
            long start = System.nanoTime();
            String plain = AESEncryptManger.getInstance().Decrypt(body);
            long end = System.nanoTime();
            long consume = end - start;
            Debug.log("统计", "AES解密完成: \n" +
                    "解密前字符串(Base64加密后): " + body + "\n" +
                    "解密后字符串(Base64解密后): " + plain + "\n" +
                    "消耗时间: " + consume + "ns , 约 " + consume / 1000000.0f + "ms"
            );
            Thread.sleep(2000);
            return plain;
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
            Toast.makeText(activity, "解密失败", Toast.LENGTH_SHORT).show();
        }

    }
}

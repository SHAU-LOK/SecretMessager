package com.shootloking.secretmessager.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.event.EncryptEvent;
import com.shootloking.secretmessager.sms.Transactions;
import com.shootloking.secretmessager.task.EncryptSendAsyncTask;
import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.view.base.SMActivity;
import com.shootloking.secretmessager.view.dialog.ShowDefaultSmsDialog;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by shau-lok on 1/20/16.
 */
public class ComposeActivity extends SMActivity {

    public static final int ENCRYPT_ITEM_NULL = 0;
    public static final int ENCRYPT_ITEM_AES = 1;
    public static final int ENCRYPT_ITEM_RSA = 2;

    private int EncryptType = ENCRYPT_ITEM_NULL;  //默认不加密

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edit_sendto)
    EditText edit_sendto;
    //    @Bind(R.id.checkbox_encrypt)
//    CheckBox checkbox_encrypt;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.composeEditText)
    EditText composeEditText;
    @Bind(R.id.send)
    FloatingActionButton send;


    String sendto;


    @Override
    protected String getPageName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_compose);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("发送消息");
        EventBus.getDefault().register(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinish();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EncryptType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                EncryptType = ENCRYPT_ITEM_NULL;
            }
        });

    }


    @OnClick(R.id.send)
    void onSendClick() {
        Debug.log(getPageName(), "send:");
        sendto = edit_sendto.getText().toString().trim();
        if (TextUtils.isEmpty(sendto)) {
            Toast.makeText(getSelfContext(), "请输入联系人号码", Toast.LENGTH_SHORT).show();
        } else {
            send();
        }
    }


    public static void launch(Context context) {
        Intent intent = new Intent(context, ComposeActivity.class);
        context.startActivity(intent);
    }


    private void send() {

        if (!TextUtils.isEmpty(composeEditText.getText())) {
            if (Build.VERSION.SDK_INT < 19) {
                sendSms();
            } else {
                if (!Utils.isDefaultApp(getSelfContext())) {
                    new ShowDefaultSmsDialog(getSelfContext()).show();
                } else {
                    sendSms();
                }
            }
        } else {
            Toast.makeText(getSelfContext(), "内容为空", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendSms() {
        String body = composeEditText.getText().toString().trim();
//        if (checkbox_encrypt.isChecked()) {
////            Toast.makeText(getSelfContext(), "加密中", Toast.LENGTH_SHORT).show();
//            //加密处理
////            try {
////                body = EncryptManger.getInstance().Encrypt(body);
////            } catch (Exception e) {
////                e.printStackTrace();
////                Toast.makeText(getSelfContext(), "加密失败", Toast.LENGTH_SHORT).show();
////            }
//
//            EncryptSendAsyncTask task = new EncryptSendAsyncTask(this);
//            task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, body);
//        } else {
//
////        Transactions transactions = new Transactions(this);
////        transactions.sendMessage(body, sendto);
////        composeEditText.setText("");
////        mFinish();
//
//            Transactions transactions = new Transactions(this);
//            transactions.sendMessage(body, sendto);
//            composeEditText.setText("");
//            mFinish();
//        }

        switch (EncryptType) {
            case ENCRYPT_ITEM_NULL:

                Transactions transactions = new Transactions(this);
                transactions.sendMessage(body, sendto);
                composeEditText.setText("");
                mFinish();


                break;
            case ENCRYPT_ITEM_AES:
                EncryptSendAsyncTask task = new EncryptSendAsyncTask(this);
                task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, body);
                break;
            case ENCRYPT_ITEM_RSA:


                break;
        }

    }

    public void onEventMainThread(EncryptEvent event) {
        if (event != null && !TextUtils.isEmpty(event.body)) {
            Transactions transactions = new Transactions(this);
            transactions.sendMessage(event.body, sendto);
            composeEditText.setText("");
            mFinish();
        }
    }

}

package com.shootloking.secretmessager.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.data.Constants;
import com.shootloking.secretmessager.event.NotifySentSuccessEvent;
import com.shootloking.secretmessager.model.Contact;
import com.shootloking.secretmessager.model.Conversation;
import com.shootloking.secretmessager.utility.RecycleViewSpacingDecoration;
import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.view.adapter.MessageListAdapter;
import com.shootloking.secretmessager.view.base.SMActivity;
import com.shootloking.secretmessager.view.dialog.ShowDefaultSmsDialog;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by shau-lok on 2/17/16.
 */
public class MessageListActivity extends SMActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.conversation)
    RecyclerView conversation;
    @Bind(R.id.checkbox_encrypt)
    CheckBox checkbox_encrypt;
    @Bind(R.id.send)
    FloatingActionButton send;
    @Bind(R.id.composeEditText)
    EditText composeEditText;

    Conversation conv;
    Contact contact;
    LinearLayoutManager linearLayoutManager;

    MessageListAdapter adapter;


    @Override
    protected String getPageName() {
        return "MessageListActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_messagelist);

        setSupportActionBar(toolbar);
        Toast.makeText(this, "成功", Toast.LENGTH_LONG).show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("消息");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getSelfContext(), "back", Toast.LENGTH_SHORT).show();
                mFinish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                send();
            }
        });
        EventBus.getDefault().register(this);
        linearLayoutManager = new LinearLayoutManager(getSelfContext());
        linearLayoutManager.setStackFromEnd(true);
        conversation.setHasFixedSize(true);
        conversation.setLayoutManager(linearLayoutManager);

        parseIntent(getIntent());

    }


    private void parseIntent(Intent intent) {
        if (intent == null) return;

        Uri uri = intent.getData();

        if (uri != null) {
            if (!uri.toString().startsWith(Constants.MMS_SMS_CONVERSATION)) {
                uri = Uri.parse(Constants.MMS_SMS_CONVERSATION + uri.getLastPathSegment());
            }
        }

        int threadId = -1;
        try {
            threadId = Integer.parseInt(uri.getLastPathSegment());
            Debug.log(getPageName(), "threadId before: " + String.valueOf(threadId));
        } catch (Exception e) {
            Debug.error(getPageName(), "解析threadId失败\n" + e.toString());
            Toast.makeText(getSelfContext(), "读取数据库失败", Toast.LENGTH_LONG).show();
            mFinish();
        }

        Conversation conversations = Conversation.getConversation(getSelfContext(), threadId);
        conv = conversations;
        threadId = conversations.getThreadId();
        Debug.log(getPageName(), "threadId after: " + String.valueOf(threadId));

        Contact contact = conversations.getContact();
        this.contact = contact;
        Debug.log(getPageName(), contact.toString());
        getSupportActionBar().setTitle(contact.getDisplayName());


        initAdapter(uri);
    }

    private void initAdapter(Uri uri) {
        adapter = new MessageListAdapter(getSelfContext(), uri);
        conversation.addItemDecoration(new RecycleViewSpacingDecoration(20));
        conversation.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
//                super.onChanged();
//                linearLayoutManager
                int position = adapter.getItemCount() - 1;
                linearLayoutManager.smoothScrollToPosition(conversation, null, position);
            }
        });
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
        if (checkbox_encrypt.isChecked()) {
            Toast.makeText(getSelfContext(), "加密中", Toast.LENGTH_SHORT).show();
            //// TODO: 2/19/16 加密处理
        }
        String body = composeEditText.getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_SEND);

        if (contact.getmNumber() == null) {
            intent.setData(Uri.parse("sms:"));
        } else {
            intent.setData(Uri.parse("smsto:" + contact.getmNumber()));
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra("sms_body", body);
        intent.putExtra("AUTOSEND", "1");

        startActivity(intent);
        composeEditText.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void onEventMainThread(NotifySentSuccessEvent event) {
        if (event != null) {
            Debug.log(getPageName(), "更新数据");
//            adapter.messages = Message.getMessageArrayList()
//            adapter.notifyDataSetChanged();
            adapter.updateResource();

        }

    }


}
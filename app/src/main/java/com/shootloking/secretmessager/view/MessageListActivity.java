package com.shootloking.secretmessager.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.data.Constants;
import com.shootloking.secretmessager.model.Contact;
import com.shootloking.secretmessager.model.Conversation;
import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.view.base.SMActivity;

import butterknife.Bind;

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

        parseIntent(getIntent());

    }


    private void parseIntent(Intent intent) {
        if (intent == null) return;

        Uri uri = intent.getData();

        if (uri != null) {
            if (!uri.toString().startsWith(Constants.SMS_CONVERSATION_URI)) {
                uri = Uri.parse(Constants.SMS_CONVERSATION_URI + uri.getLastPathSegment());
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

        Conversation conversation = Conversation.getConversation(getSelfContext(), threadId);
        conv = conversation;
        threadId = conversation.getThreadId();
        Debug.log(getPageName(), "threadId after: " + String.valueOf(threadId));

        Contact contact = conversation.getContact();
        this.contact = contact;
        Debug.log(getPageName(), contact.toString());
        getSupportActionBar().setTitle(contact.getDisplayName());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}

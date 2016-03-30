package com.shootloking.secretmessager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shootloking.secretmessager.view.ComposeActivity;
import com.shootloking.secretmessager.view.ConversationListFragment;
import com.shootloking.secretmessager.view.base.SMActivity;

import butterknife.Bind;

public class MainActivity extends SMActivity {

    public static final String TAG = "MainActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    Fragment fragment;

    @Override
    protected String getPageName() {
        return "MainActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComposeActivity.launch(getSelfContext());
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        fragment = new ConversationListFragment();
        manager.beginTransaction().replace(R.id.content_main, fragment).commit();
    }

}

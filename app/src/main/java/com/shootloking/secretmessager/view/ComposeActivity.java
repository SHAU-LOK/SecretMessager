package com.shootloking.secretmessager.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.view.base.SMActivity;

/**
 * Created by shau-lok on 1/20/16.
 */
public class ComposeActivity extends SMActivity {
    @Override
    protected String getPageName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("发送消息");


    }
}

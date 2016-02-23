package com.shootloking.secretmessager.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.view.base.SMActivity;

import butterknife.Bind;

/**
 * Created by shau-lok on 1/20/16.
 */
public class ComposeActivity extends SMActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;


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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinish();
            }
        });

    }


    public static void launch(Context context) {
        Intent intent = new Intent(context, ComposeActivity.class);
        context.startActivity(intent);
    }


}

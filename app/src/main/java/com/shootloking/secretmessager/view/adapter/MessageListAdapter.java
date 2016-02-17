package com.shootloking.secretmessager.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shau-lok on 2/17/16.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder> {


    public static final String TAG = "MessageListAdapter";

    public static final int INCOMING_ITEM = 0;
    public static final int OUTGOING_ITEM = 1;


    String displayName;

    Context context;
    Cursor cursor;

    public MessageListAdapter(Context context) {
        this.context = context;
    }


    @Override
    public MessageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MessageListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
        // TODO: 2/17/16 itemType
    }

    public class MessageListViewHolder extends RecyclerView.ViewHolder {

        public MessageListViewHolder(View itemView) {
            super(itemView);
        }
    }


}
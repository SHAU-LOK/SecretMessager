package com.shootloking.secretmessager.view.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.data.Constants;
import com.shootloking.secretmessager.model.Conversation;
import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.view.MessageListActivity;

/**
 * Created by shau-lok on 1/31/16.
 */
public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ConversionListViewHolder> {

    public static final String TAG = "ConversationListAdapter";
    Context context;
    Cursor cursor;
    ContentResolver contentResolver;

    public ConversationListAdapter(Context ctx) {
        this.context = ctx;
        cursor = null;
        refreshMsg();
    }

    public void refreshMsg() {
        if (contentResolver == null) {
            contentResolver = context.getContentResolver();
        }
        try {
            cursor = contentResolver.query(Constants.URI_SIMPLE, Conversation.PROJECTION, Conversation.COLUMN_COUNT + ">0", null, Conversation.SORT_ORDER);
            Debug.log(TAG, DatabaseUtils.dumpCursorToString(cursor));
        } catch (Exception e) {
            Debug.error(TAG, e.getMessage());
        }
    }


    @Override
    public ConversionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_conversion, parent, false);

        ConversionListViewHolder holder = new ConversionListViewHolder(view);
//        holder.logo.setImageResource(R.mipmap.user_avatar);
        holder.logo.setImageResource(R.mipmap.ic_person);
//        holder.logo.setBackgroundResource(R.drawable.person_default_circle_bg);
        return holder;
    }

    @Override
    public void onBindViewHolder(ConversionListViewHolder holder, int position) {
        Conversation conversation = getItem(position);
//
        holder.message.setText(conversation.getBody());
        holder.name.setText(conversation.getContact().getDisplayName());
        holder.date.setText(Utils.DateFormat(context, conversation.getDate()));
        holder.mData = conversation;
        holder.root.setOnClickListener(holder);

    }

    @Override
    public int getItemCount() {
        return cursor != null && !cursor.isClosed() ? cursor.getCount() : 0;
    }

    public Conversation getItem(int position) {
        cursor.moveToPosition(position);
        return Conversation.getConversation(context, cursor);
    }


    public class ConversionListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView logo;
        protected TextView message;
        protected TextView name;
        protected TextView date;
        protected View root;

        public Conversation mData;

        public ConversionListViewHolder(View itemView) {
            super(itemView);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            message = (TextView) itemView.findViewById(R.id.message);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            root = itemView;
        }


        public View getBackground() {
            return root;
        }

        public ImageView getLogo() {
            return logo;
        }

        public TextView getMessage() {
            return message;
        }

        public TextView getName() {
            return name;
        }


        @Override
        public void onClick(View v) {
            if (mData == null) return;

            Uri target = mData.getUri();
            Intent intent = new Intent(context, MessageListActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("Conversation", mData);
            intent.setData(target);
//            intent.putExtra("Bundle", bundle);
            context.startActivity(intent);
        }
    }

}

package com.shootloking.secretmessager.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.model.Conversation;
import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.view.MessageListActivity;

/**
 * Created by shau-lok on 1/31/16.
 */
public class ConversationListAdapter extends RecyclerCursorAdapter<ConversationListAdapter.ConversionListViewHolder, Conversation> {

    public static final String TAG = "ConversationListAdapter";
    //    Context context;
//    Cursor cursor;
//    ContentResolver contentResolver;

    public ConversationListAdapter(Context ctx) {
        super(ctx);
//        this.context = ctx;
//        cursor = null;
//        refreshMsg();
    }

//    public void refreshMsg() {
//        if (contentResolver == null) {
//            contentResolver = context.getContentResolver();
//        }
//        try {
//            cursor = contentResolver.query(Constants.URI_SIMPLE, Conversation.PROJECTION, Conversation.COLUMN_COUNT + ">0", null, Conversation.SORT_ORDER);
//            Debug.log(TAG, DatabaseUtils.dumpCursorToString(cursor));
//        } catch (Exception e) {
//            Debug.error(TAG, e.getMessage());
//        }
//    }


    @Override
    public ConversionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_conversion, parent, false);

        ConversionListViewHolder holder = new ConversionListViewHolder(view);
//        holder.logo.setImageResource(R.mipmap.user_avatar);
//        holder.logo.setImageResource(R.mipmap.ic_person);
//        holder.logo.setBackgroundResource(R.drawable.person_default_circle_bg);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ConversionListViewHolder holder, int position) {
        final Conversation conversation = getItem(position);


//        Observable<Contact> observable = Observable.create(new Observable.OnSubscribe<Contact>() {
//            @Override
//            public void call(Subscriber<? super Contact> subscriber) {
//                Contact newContact = ContactHelper.getContact(context, conversation.getPersonId());
//                subscriber.onNext(newContact);
//            }
//        });
//        observable.subscribe(new Action1<Contact>() {
//            @Override
//            public void call(Contact contact) {
//                conversation.setContact(contact);
//                holder.name.setText(conversation.getContact().getDisplayName());
//            }
//        });

        holder.message.setText(conversation.getBody());
        holder.name.setText(conversation.getContact().getDisplayName());
        holder.date.setText(Utils.DateFormat(context, conversation.getDate()));
        holder.mData = conversation;
//        holder.logo.
        int textColor = context.getResources().getColor(R.color.colorBrownLight);
        TextDrawable drawable = TextDrawable.builder().
                beginConfig().
                textColor(Color.WHITE).fontSize(70).bold().toUpperCase().
                endConfig().
//                buildRoundRect(Character.toString(conversation.getContact().getDisplayName().charAt(0)), textColor, 5);
                buildRound(Character.toString(conversation.getContact().getDisplayName().charAt(0)), textColor);
        holder.logo.setImageDrawable(drawable);
        if (conversation.getRead() == 1) {
            holder.name.setTextColor(context.getResources().getColor(R.color.conversation_list_item_read));
            holder.date.setTextColor(context.getResources().getColor(R.color.conversation_list_item_read));
            holder.message.setTextColor(context.getResources().getColor(R.color.conversation_list_item_read));
            holder.message.setMaxLines(1);
            holder.message.setSingleLine();
            holder.message.setEllipsize(TextUtils.TruncateAt.END);
        } else {
            holder.name.setTextColor(context.getResources().getColor(R.color.conversation_list_item_unread));
            holder.date.setTextColor(context.getResources().getColor(R.color.conversation_list_item_unread));
            holder.message.setTextColor(context.getResources().getColor(R.color.conversation_list_item_unread));
            holder.message.setMaxLines(3);
        }
        holder.root.setOnClickListener(holder);

    }

//    @Override
//    public int getItemCount() {
//        return cursor != null && !cursor.isClosed() ? cursor.getCount() : 0;
//    }

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

            MessageListActivity.launch(context, mData);

        }
    }

}

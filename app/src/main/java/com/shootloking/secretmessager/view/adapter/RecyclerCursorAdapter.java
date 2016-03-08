package com.shootloking.secretmessager.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shootloking.secretmessager.utility.Utils;

/**
 * Created by shau-lok on 2/23/16.
 */
public abstract class RecyclerCursorAdapter<VH extends RecyclerView.ViewHolder, DataType>
        extends RecyclerView.Adapter<VH> {


    //    protected SMActivity context;
    protected Context context;
    protected Cursor cursor;

    protected ItemClickListener<DataType> mItemClickListener;

    public ItemClickListener<DataType> getmItemClickListener() {
        return mItemClickListener;
    }

    public void setmItemClickListener(ItemClickListener<DataType> mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface ItemClickListener<DataType> {
        void onItemClick(DataType object, View view);

        void onItemLongClick(DataType object, View view);
    }

    public RecyclerCursorAdapter(Context context) {
        this.context = context;
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (this.cursor == cursor) {
            return null;
        }

        Cursor oldCursor = this.cursor;
        this.cursor = cursor;
        if (cursor != null) {
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    protected abstract DataType getItem(int position);

    @Override
    public int getItemCount() {
        return Utils.isCursorValid(cursor) ? cursor.getCount() : 0;
    }
}

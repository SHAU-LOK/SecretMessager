package com.shootloking.secretmessager.utility;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shau-lok on 2/17/16.
 */
public class RecycleViewSpacingDecoration extends RecyclerView.ItemDecoration {

    private int mVerticalSpaceHeight;

    public RecycleViewSpacingDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mVerticalSpaceHeight;
    }
}

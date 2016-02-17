package com.shootloking.secretmessager.view.base;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by shau-lok on 1/30/16.
 */
public abstract class SwipeToRefreshFragment extends SMFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;


    protected final boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }


    protected final void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }


    protected final void initRefreshLayout() {
//        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setColorSchemeResources(R.color.green);
    }


    protected final void disableRefreshing() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(false);
        }
    }


    protected final void enableSwipeRefresh(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }

}

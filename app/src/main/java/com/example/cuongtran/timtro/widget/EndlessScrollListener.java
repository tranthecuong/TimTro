package com.example.cuongtran.timtro.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    public static final int VISIBLE_THRESHOLD = 1;

    private boolean loading = true;

    private LinearLayoutManager mLayoutManager;

    public EndlessScrollListener(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy <= 0) {
            return;
        }

        int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        int totalItemCount = mLayoutManager.getItemCount();
        if (!loading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD && totalItemCount > VISIBLE_THRESHOLD) {
            onLoadMore();
            loading = true;
        }
    }

    public abstract void onLoadMore();

    public void setLoaded() {
        loading = false;
    }
}

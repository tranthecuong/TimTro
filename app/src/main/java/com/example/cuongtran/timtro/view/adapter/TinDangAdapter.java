package com.example.cuongtran.timtro.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.TinDang;

import java.util.ArrayList;

public class TinDangAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_LOADING = 0;
    public static final int ITEM_TIN_DANG = 1;

    private ArrayList<TinDang> listTinDang = new ArrayList<>();
    private LayoutInflater inflater;
    private TinDangViewHolder.OnClickViewHolderListener onClickViewHolderListener;
    private boolean hasLoadingItem = false;

    public TinDangAdapter(Context context, ArrayList<TinDang> listTinDang, TinDangViewHolder.OnClickViewHolderListener onClickViewHolderListener) {
        this.listTinDang = listTinDang;
        this.inflater = LayoutInflater.from(context);
        this.onClickViewHolderListener = onClickViewHolderListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_LOADING) {
            View view = inflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

        View view = inflater.inflate(R.layout.customitem, parent, false);
        return new TinDangViewHolder(view, onClickViewHolderListener);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TinDangViewHolder) {
            ((TinDangViewHolder) holder).bindData(listTinDang.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (hasLoadingItem) {
            return listTinDang.size() + 1;
        } else {
            return listTinDang.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == listTinDang.size()) {
            return ITEM_LOADING;
        } else {
            return ITEM_TIN_DANG;
        }
    }

    public void showLoadingItem(boolean isShow) {
        if (isShow) {
            hasLoadingItem = true;
            notifyItemInserted(listTinDang.size());
        } else {
            hasLoadingItem = false;
            notifyItemRemoved(listTinDang.size());
        }
    }
}

package com.miso.menu.multiplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miso.menu.R;

import java.util.List;

/**
 * Created by michal.hornak on 2/16/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<Integer> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<Integer> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_picker_item, parent, false);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        int color = itemList.get(position);
        holder.setColor(color);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}

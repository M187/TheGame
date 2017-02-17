package com.miso.menu.multiplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miso.menu.R;

import java.util.List;

/**
 * Created by michal.hornak on 2/16/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ColorViewHolder> {

    private List<Integer> itemList;
    private Context context;

    private static MyClickListener mClickListener;

    public interface MyClickListener {
        void onItemClick(int position, View view);
    }

    public RecyclerViewAdapter(Context context, List<Integer> itemList, MyClickListener mClickListener) {
        this.itemList = itemList;
        this.mClickListener = mClickListener;
        this.context = context;
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_picker_item, parent, false);
        return new ColorViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, int position) {
        int color = itemList.get(position);
        holder.setColor(color);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    public class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView colorImage;

        public ColorViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            colorImage = (ImageView)itemView.findViewById(R.id.color_item);
        }

        public void setColor(int color){
            this.colorImage.setBackgroundColor(color);
        }

        @Override
        public void onClick(View view) {
            //todo invoke "changeColorLogic"
            mClickListener.onItemClick(getPosition(), view);
        }
    }
}

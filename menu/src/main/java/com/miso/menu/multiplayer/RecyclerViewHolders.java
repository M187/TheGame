package com.miso.menu.multiplayer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.miso.menu.R;

/**
 * Created by michal.hornak on 2/16/2017.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView colorImage;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        colorImage = (ImageView)itemView.findViewById(R.id.color_item);
    }

    public void setColor(int color){
        this.colorImage.setBackgroundColor(color);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        //todo invoke "changeColorLogic"
    }
}
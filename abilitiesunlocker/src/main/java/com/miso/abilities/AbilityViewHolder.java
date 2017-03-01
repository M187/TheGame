package com.miso.abilities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miso.abilities.abilitiesunlocker.R;

/**
 * Created by michal.hornak on 3/1/2017.
 */

public class AbilityViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImageView;
    public TextView mDescription;
    public TextView mPrice;


    public AbilityViewHolder(View itemView) {
        super(itemView);
        this.mImageView = (ImageView) itemView.findViewById(R.id.ability_image);
        this.mDescription = (TextView) itemView.findViewById(R.id.ability_description);
        this.mPrice = (TextView) itemView.findViewById(R.id.ability_kill_price);
    }
}

package com.miso.abilities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miso.abilities.abilitiesunlocker.R;

/**
 * Created by michal.hornak on 3/1/2017.
 */

public class AbilityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView mImageView;
    public TextView mName;
    public TextView mDescription;
    public TextView mPrice;

    private Activity parentActivity;

    public AbilityViewHolder(View itemView, Activity parentActivity) {
        super(itemView);
        this.parentActivity = parentActivity;
        this.mImageView = (ImageView) itemView.findViewById(R.id.ability_image);
        this.mName = (TextView) itemView.findViewById(R.id.ability_name);
        this.mDescription = (TextView) itemView.findViewById(R.id.ability_description);
        this.mPrice = (TextView) itemView.findViewById(R.id.ability_kill_price);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Do you want to stop ?");
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                // todo: buy ability here
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

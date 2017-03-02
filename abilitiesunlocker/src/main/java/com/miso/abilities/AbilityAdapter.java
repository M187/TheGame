package com.miso.abilities;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.miso.abilities.abilitiesunlocker.R;

/**
 * Created by michal.hornak on 3/1/2017.
 */

public class AbilityAdapter extends RecyclerView.Adapter<AbilityViewHolder> {

    private Activity parentActivity;
    private Cursor mCursor;

    AbilityAdapter(Activity parentActivity, Cursor mCursor){
        this.parentActivity = parentActivity;
        this.mCursor = mCursor;
    }

    @Override
    public AbilityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.parentActivity.getLayoutInflater().inflate(R.layout.ability_list_item, parent, false);
        final AbilityViewHolder temp = new AbilityViewHolder(view, parentActivity);
        //todo add onClick listener
        return temp;
    }

    @Override
    public void onBindViewHolder(AbilityViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.mName.setText(mCursor.getString(0).toUpperCase().replace("_"," "));
        holder.mDescription.setText(mCursor.getString(1));
        holder.mPrice.setText(String.valueOf(mCursor.getInt(2)));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}

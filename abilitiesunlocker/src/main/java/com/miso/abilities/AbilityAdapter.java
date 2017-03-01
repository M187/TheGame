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

public class AbilityAdapter extends RecyclerView.Adapter {

    private Activity parentActivity;
    private Cursor mCursor;

    AbilityAdapter(Activity parentActivity, Cursor mCursor){
        this.parentActivity = parentActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.parentActivity.getLayoutInflater().inflate(R.layout.ability_list_item, parent, false);
        final AbilityViewHolder temp = new AbilityViewHolder(view);
        //todo add onClick listener
        return temp;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}

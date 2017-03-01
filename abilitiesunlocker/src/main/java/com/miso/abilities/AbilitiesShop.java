package com.miso.abilities;


import android.app.ProgressDialog;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.miso.abilities.abilitiesunlocker.R;
import com.miso.persistence.player.AbilityActivityLoaderCallbackImpl;

/**
 * Created by michal.hornak on 3/1/2017.
 */

public class AbilitiesShop extends AbilityActivityLoaderCallbackImpl {

    private ProgressDialog dialog;
    // kills are used as an in-game currency.
    private int killCount;
    private final int PLAYER_STATS_LIST_ID = 1120;
    private TextView mKillCountTextView;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_abilities);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.abilities_recycler_view);
        this.mKillCountTextView = (TextView) findViewById(R.id.player_kill_points);
        this.killCount = getIntent().getExtras().getInt("kills", 0);
        mKillCountTextView.setText("Kill points: " + String.valueOf(killCount));

        getLoaderManager().initLoader(PLAYER_STATS_LIST_ID, null, this);

        this.dialog=new ProgressDialog(AbilitiesShop.this);
        dialog.setMessage("Waiting to fetch data.");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        AbilityAdapter adapter = new AbilityAdapter(this, data);
        adapter.setHasStableIds(true);
        mRecyclerView.setAdapter(adapter);
        LinearLayoutManager lM = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lM);

        this.dialog.hide();
    }


}

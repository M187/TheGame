package com.miso.abilities;


import android.app.ProgressDialog;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.miso.abilities.abilitiesunlocker.R;
import com.miso.persistence.player.OptionsActivityLoaderCallbackImpl;

/**
 * Created by michal.hornak on 3/1/2017.
 */

public class AbilitiesShop extends OptionsActivityLoaderCallbackImpl {

    private ProgressDialog dialog;
    // kills are used as an in-game currency.
    private int killCount;
    private final int PLAYER_STATS_LIST_ID = 1120;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_abilities);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.abilities_recycler_view);

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
        this.killCount = data.getInt(0);
        this.dialog.hide();
    }


}

package com.miso.abilities;


import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.miso.abilities.abilitiesunlocker.R;
import com.miso.inapbilling.AppCompatActivityWithInAppBilling;
import com.miso.inapbilling.util.IabResult;
import com.miso.inapbilling.util.Purchase;
import com.miso.persistence.player.PlayerStatsContract;

/**
 * Created by michal.hornak on 3/1/2017.
 */

public class AbilitiesShop extends AppCompatActivityWithInAppBilling implements LoaderManager.LoaderCallbacks<Cursor>{

    private ProgressDialog dialog;
    // kills are used as an in-game currency.
    private int killCount;
    private final int PLAYER_STATS_LIST_ID = 1120;
    private RecyclerView mRecyclerView;

    @Override
    public void onConsumeFinished(Purchase purchase, IabResult result) {
        buyAbility(0, abilityToBeBought);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_abilities);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.abilities_recycler_view);
        this.killCount = getIntent().getExtras().getInt("kills", 0);

        setTitle("Buy ability - your kill points: " + killCount);

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

        this.dialog.dismiss();
    }

    private static final String[] settingsProjection = {
            PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_NAME,
            PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_DESCRIPTION,
            PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_PRICE,
            PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_UNLOCKED
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                this,
                PlayerStatsContract.PlayerAbilitiesEntry.CONTENT_URI.buildUpon().appendPath("abilities").build(),
                settingsProjection,
                null,
                null,
                null);
        return loader;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void buyAbility(int price, String abilityName){
        ContentValues values = new ContentValues();
        killCount = killCount - price;
        values.put(PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS, killCount);
        values.put(PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_NAME, abilityName);

        getContentResolver().update(Uri.withAppendedPath(PlayerStatsContract.BASE_CONTENT_URI, "PlayerStatistics/buy_ability"), values, null, null);
    }

    public void attemptToBuy(final AbilityViewHolder ability){

        if (ability.killPointsPrice < 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Already bought!");
            AlertDialog alert = builder.create();
            alert.show();
        } else if (killCount >= ability.killPointsPrice) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("BUY ABILITY?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    buyAbility(ability.killPointsPrice, ability.abilityName);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Purchase", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    abilityBuyClick(ability.abilityName);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Not enough kill points to buy " + ability.abilityName.toUpperCase().replace("_"," ") + " !");
            builder.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    abilityBuyClick(ability.abilityName);
                }
            });
            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public Bitmap resolveImage(String abilityName){

        switch (abilityName){
            case "shockwave":
                return BitmapFactory.decodeResource(getResources(), R.drawable.buttonshockwave2);
            case "timestop":
                return BitmapFactory.decodeResource(getResources(), R.drawable.timestop);
            case "freezing_projectiles":
                return BitmapFactory.decodeResource(getResources(), R.drawable.freeze);
            case "mock_ability":
                return BitmapFactory.decodeResource(getResources(), R.drawable.mock);
            case "mock_ability2":
                return BitmapFactory.decodeResource(getResources(), R.drawable.mock);
            default:
                return null;
        }
    }
}

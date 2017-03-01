package com.miso.persistence.player;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miso.data.Abilities;

/**
 * Created by michal.hornak on 2/15/2017.
 */

public class PlayerStatsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "game.db";

    private final static int DATABASE_VERSION = 4;

    public PlayerStatsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_PLAYER_STATS_TABLE =
                "CREATE TABLE " + PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME + " (" +
                        PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS + " INTEGER DEFAULT 0, " +
                        PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_COMPLETED + " INTEGER DEFAULT 0," +
                        PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_POINTS + " INTEGER DEFAULT 0 )";
        final String SQL_CREATE_PLAYER_ABILITIES_TABLE =
                "CREATE TABLE " + PlayerStatsContract.PlayerAbilitiesEntry.TABLE_NAME + " (" +
                        PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_NAME + " STRING DEFAULT '', " +
                        PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_DESCRIPTION + " STRING DEFAULT ''," +
                        PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_PRICE + " INTEGER DEFAULT 0," +
                        PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_UNLOCKED + " BOOLEAN DEFAULT 0 )";

        final String ADD_PLAYER_SETTING_ENTRY =
                "INSERT INTO " + PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME + " ("
                        + PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS + ","
                        + PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_COMPLETED + ","
                        + PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_COMPLETED + ") "
                        + "VALUES (0,0,0)";

        db.execSQL(SQL_CREATE_PLAYER_STATS_TABLE);
        db.execSQL(SQL_CREATE_PLAYER_ABILITIES_TABLE);

        db.execSQL(ADD_PLAYER_SETTING_ENTRY);

        // inserts abilities as defined in list.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Abilities.Ability ability : Abilities.getAbilityList()) {
                values.put(PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_NAME, ability.name);
                values.put(PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_DESCRIPTION, ability.description);
                values.put(PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_PRICE, ability.price);
                values.put(PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_UNLOCKED, false);
                db.insert(PlayerStatsContract.PlayerAbilitiesEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME);
        onCreate(db);
    }
}

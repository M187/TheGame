package com.miso.persistence.player;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by michal.hornak on 2/15/2017.
 */

public class PlayerStatsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "game.db";

    private final static int DATABASE_VERSION = 1;

    public PlayerStatsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME + " (" +
                        PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS + " INTEGER DEFAULT 0, " +
                        PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_COMPLETED + " INTEGER DEFAULT 0," +
                        PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_POINTS + " INTEGER DEFAULT 0 )";

        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME);
        onCreate(db);
    }
}

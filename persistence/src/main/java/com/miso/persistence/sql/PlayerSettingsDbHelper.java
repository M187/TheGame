package com.miso.persistence.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miso.persistence.sql.PlayerSettingsContract.PlayerSettingsEntry;

/**
 * Created by michal.hornak on 2/9/2017.
 */

public class PlayerSettingsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "game.db";

    private final static int DATABASE_VERSION = 1;

    public PlayerSettingsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + PlayerSettingsEntry.TABLE_NAME + " (" +
                        PlayerSettingsEntry.COLUMN_FISRT_BUTTON + " STRING NOT NULL, " +
                        PlayerSettingsEntry.COLUMN_SECOND_BUTTON + " STRING NOT NULL," +
                        PlayerSettingsEntry.COLUMN_PLAYER_BONUS_HEALTH + " INTEGER NOT NULL, " +
                        PlayerSettingsEntry.COLUMN_PLAYER_BONUS_SPEED + " INTEGER NOT NULL, " +
                        PlayerSettingsEntry.COLUMN_PLAYER_BONUS_AMMO + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlayerSettingsEntry.TABLE_NAME);
        onCreate(db);
    }
}

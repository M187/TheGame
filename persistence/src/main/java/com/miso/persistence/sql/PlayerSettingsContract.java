package com.miso.persistence.sql;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by michal.hornak on 2/9/2017.
 */

public class PlayerSettingsContract {

    public static final String CONTENT_AUTHORITY = "com.miso.persistence";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PLAYER_SETTINGS = "PlayerSettings";

    public static final class PlayerSettingsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYER_SETTINGS).build();
        public static final String TABLE_NAME = "player_settings";

        public static final String COLUMN_FISRT_BUTTON = "first_button";
        public static final String COLUMN_SECOND_BUTTON = "second_button";
        public static final String COLUMN_PLAYER_BONUS_HEALTH = "player_bonus_health";
        public static final String COLUMN_PLAYER_BONUS_SPEED = "player_bonus_speed";
        public static final String COLUMN_PLAYER_BONUS_AMMO = "player_bonus_ammo";
    }

}

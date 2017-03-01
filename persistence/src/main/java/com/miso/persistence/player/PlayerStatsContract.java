package com.miso.persistence.player;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by michal.hornak on 2/15/2017.
 */

public class PlayerStatsContract {

    public static final String CONTENT_AUTHORITY = "com.miso.persistence";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PLAYER_SETTINGS = "PlayerStatistics";

    public static final class PlayerStatisticssEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYER_SETTINGS).build();
        public static final String TABLE_NAME = "player_statistics";

        public static final String COLUMN_PLAYER_KILLS = "kills";
        public static final String COLUMN_PLAYER_LEVELS_COMPLETED = "levels_completed";
        public static final String COLUMN_PLAYER_LEVELS_POINTS = "level_points";
    }

    public static final class PlayerAbilitiesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYER_SETTINGS).build();
        public static final String TABLE_NAME = "player_abilities";

        public static final String COLUMN_ABILITY_NAME = "name";
        public static final String COLUMN_ABILITY_DESCRIPTION = "description";
        public static final String COLUMN_ABILITY_PRICE = "price";
        public static final String COLUMN_ABILITY_UNLOCKED = "unlocked";
    }
}

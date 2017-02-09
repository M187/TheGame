package com.miso.persistence;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.miso.persistence.sql.PlayerSettingsContract;
import com.miso.persistence.sql.PlayerSettingsDbHelper;


/**
 * Created by michal.hornak on 2/9/2017.
 */

public class PlayerSettingsProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int CODE_SETTINGS = 100;

    private PlayerSettingsDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PlayerSettingsContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PlayerSettingsContract.CONTENT_AUTHORITY, CODE_SETTINGS);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PlayerSettingsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            /**
             * Only one row of data is present in database for now.
             */
            case CODE_SETTINGS: {

                cursor = mOpenHelper.getReadableDatabase().query(
                        PlayerSettingsContract.PlayerSettingsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

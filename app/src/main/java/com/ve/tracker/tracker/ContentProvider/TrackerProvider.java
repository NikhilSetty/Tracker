package com.ve.tracker.tracker.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.ve.tracker.tracker.DBClasses.DbHelper;
import com.ve.tracker.tracker.DBClasses.DbTableStrings;

/**
 * Created by adithyar on 5/20/2016.
 */

public class TrackerProvider extends ContentProvider {

    private static final String AUTH = "com.ve.tracker.tracker.TrackerProvide";
    public final Uri USER_URI = Uri.parse("content://"+AUTH+"/"+ DbTableStrings.TABLE_NAME_USER_MODEL);
    public final Uri LOCATION_URI = Uri.parse("content://"+AUTH+"/"+ DbTableStrings.TABLE_NAME_LOCATION_MODEL);

    final static int USERS_MATCH = 1;
    final static int LOCATION_MATCH = 2;

    SQLiteDatabase db;
    DbHelper dbHelper;

    private final static UriMatcher urimatcher;
    static {
        urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        urimatcher.addURI(AUTH,DbTableStrings.TABLE_NAME_USER_MODEL,USERS_MATCH);
        urimatcher.addURI(AUTH,DbTableStrings.TABLE_NAME_LOCATION_MODEL,LOCATION_MATCH);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext().getApplicationContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor;
        int uriType = urimatcher.match(uri);
        switch (uriType){
            case USERS_MATCH:
                cursor = db.query(DbTableStrings.TABLE_NAME_USER_MODEL,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case LOCATION_MATCH:
                cursor = db.query(DbTableStrings.TABLE_NAME_LOCATION_MODEL,projection,selection,selectionArgs,null,null,sortOrder);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

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
        db=dbHelper.getWritableDatabase();
        int uriType = urimatcher.match(uri);
        switch (uriType){
            case USERS_MATCH:
                db.insert(DbTableStrings.TABLE_NAME_USER_MODEL,null,values);
                break;
            case LOCATION_MATCH:
                db.insert(DbTableStrings.TABLE_NAME_LOCATION_MODEL,null,values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        db.close();

        getContext().getContentResolver().notifyChange(uri,null);

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

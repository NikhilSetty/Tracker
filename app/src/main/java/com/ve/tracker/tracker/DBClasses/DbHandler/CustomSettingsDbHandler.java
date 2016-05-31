package com.ve.tracker.tracker.DBClasses.DbHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.ve.tracker.tracker.DBClasses.DbHelper;
import com.ve.tracker.tracker.DBClasses.DbTableStrings;
import com.ve.tracker.tracker.Helper.Constants;
import com.ve.tracker.tracker.Models.CustomSettingsModel;

/**
 * Created by nravishankar on 5/26/2016.
 */
public class CustomSettingsDbHandler {

    static DbHelper DBHELPER;
    static SQLiteDatabase db;

    public static void setIsServiceRunningFlag(Context context, boolean value){
        try{
            CustomSettingsModel model = new CustomSettingsModel();
            model.Key = Constants.IsServiceRunning;
            model.Value = value ? "true" : "false";

            insertOrUpdateIntoCustomSettingsTable(context, model);

        }catch (Exception e){
            Toast.makeText(context, "setIsServiceRunningFlag" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static boolean getIsServiceRunning(Context context){
        try{
            CustomSettingsModel model = getCustomSettingForKey(context, Constants.IsServiceRunning);
            if(model != null){
                return model.Value.equals("true");
            }else {
                return false;
            }
        }catch (Exception e){
            Toast.makeText(context, "getIsServiceRunning" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static void insertOrUpdateIntoCustomSettingsTable(Context context, CustomSettingsModel model){
        try{
            DBHELPER = new DbHelper(context);
            db = DBHELPER.getWritableDatabase();

            CustomSettingsModel customSettingsModel = getCustomSettingForKey(context, model.Key);

            if(customSettingsModel != null && !customSettingsModel.Key.equals("")){

                db.execSQL("update " + DbTableStrings.TABLE_NAME_CUSTOM_SETTINGS + " set " + DbTableStrings.VALUE + "='" + model.Value +"' where _id='" + customSettingsModel._id + "'");
            }else{
                ContentValues values = new ContentValues();
                values.put(DbTableStrings.KEY, model.Key);
                values.put(DbTableStrings.VALUE, model.Value);

                db.insert(DbTableStrings.TABLE_NAME_CUSTOM_SETTINGS, null, values);
            }

        }catch (Exception e){
            Toast.makeText(context, "InsertOrUpdateCustomSettingsTable" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static CustomSettingsModel getCustomSettingForKey(Context context, String key){
        try{
            DBHELPER = new DbHelper(context);
            db = DBHELPER.getWritableDatabase();

            CustomSettingsModel model = new CustomSettingsModel();

            Cursor c = db.rawQuery("SELECT * FROM " +
                    DbTableStrings.TABLE_NAME_CUSTOM_SETTINGS + " WHERE " + DbTableStrings.KEY + " = '" + key + "'", null);

            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        model._id = c.getString(c.getColumnIndex("_id"));
                        model.Key = c.getString(c.getColumnIndex(DbTableStrings.KEY));
                        model.Value = c.getString(c.getColumnIndex(DbTableStrings.VALUE));

                    }while (c.moveToNext());
                }
                c.close();
            }

            return model;
        }catch (Exception e){
            Toast.makeText(context, "getCustomSettingForKey" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}

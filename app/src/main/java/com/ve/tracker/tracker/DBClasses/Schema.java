package com.ve.tracker.tracker.DBClasses;

public class Schema {
    public static final String CREATE_TABLE_USER = "create table if not exists " + DbTableStrings.TABLE_NAME_USER_MODEL +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.USERID + " string, "
            + DbTableStrings.USERNAME + " string, "
            + DbTableStrings.USEREMAILID + " string, "
            + DbTableStrings.USERPHONENUMBER + " string, ";

    public static final String CREATE_TABLE_LOCATION = "create table if not exists " + DbTableStrings.TABLE_NAME_LOCATION_MODEL +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.USERID + " string, "
            + DbTableStrings.LOCATION + " string, ";

    public static final String CREATE_TABLE_CUSTOM_SETTINGS = "create table if not exists " + DbTableStrings.TABLE_NAME_CUSTOM_SETTINGS +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.KEY + " string, "
            + DbTableStrings.VALUE + " string, ";
}



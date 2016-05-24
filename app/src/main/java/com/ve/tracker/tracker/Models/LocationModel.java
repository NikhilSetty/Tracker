package com.ve.tracker.tracker.Models;

/**
 * Created by adithyar on 5/23/2016.
 */

public class LocationModel {
    public static String UserId;
    public static String Location;

    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String userId) {
        UserId = userId;
    }

    public static String getLocation() {
        return Location;
    }

    public static void setLocation(String location) {
        Location = location;
    }
}

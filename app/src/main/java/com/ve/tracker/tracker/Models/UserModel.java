package com.ve.tracker.tracker.Models;

/**
 * Created by adithyar on 5/22/2016.
 */

public class UserModel {
    public static  String UserId;
    public static  String UserName;
    public static  String UserEmailId;
    public static  String UserPhoneNumber;

    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String userId) {
        UserId = userId;
    }

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static String getUserEmailId() {
        return UserEmailId;
    }

    public static void setUserEmailId(String userEmailId) {
        UserEmailId = userEmailId;
    }

    public static String getUserPhoneNumber() {
        return UserPhoneNumber;
    }

    public static void setUserPhoneNumber(String userPhoneNumber) {
        UserPhoneNumber = userPhoneNumber;
    }
}

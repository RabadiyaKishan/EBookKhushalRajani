package com.ebook.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.ebook.Constant.Constant;

public class Authorization {

    private final Context context;

    public Authorization(Context context) {
        this.context = context;
    }

    public static void SaveAuthorizationHeader(Context context, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.AuthorizationKey, value);
        editor.apply();
    }

    public static String GetAuthorizationHeader(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PreferenceName, 0);
        return prefs.getString(Constant.AuthorizationKey, null);
    }

    public static void DeleteAuthorizationHeader(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PreferenceName, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
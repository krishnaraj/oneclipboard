package com.cb.oneclipboard;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by krishnaraj on 16/12/15.
 */
public class Preferences {
    private static final String PREFERENCE_NAME = "OneClipboard";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    private SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void setUsername(String username) {
        write(USERNAME, username);
    }

    public String getUsername() {
        return sharedPreferences.getString(USERNAME, null);
    }

    public void setPassword(String password) {
        write(PASSWORD, password);
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, null);
    }

    private void write(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}

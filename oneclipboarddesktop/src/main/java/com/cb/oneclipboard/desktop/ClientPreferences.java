package com.cb.oneclipboard.desktop;

import java.util.prefs.Preferences;

/**
 * Created by krishnaraj on 16/12/15.
 */
public class ClientPreferences {
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    Preferences preferences;

    public ClientPreferences() {
        preferences = Preferences.userRoot().node(this.getClass().getName());
    }

    public void setUsername(String username) {
        preferences.put(USERNAME, username);
    }

    public String getUsername() {
        return preferences.get(USERNAME, null);
    }

    public void setPassword(String password) {
        preferences.put(PASSWORD, password);
    }

    public String getPassword() {
        return preferences.get(PASSWORD, null);
    }

    public void clear() {
        preferences.remove(USERNAME);
        preferences.remove(PASSWORD);
    }
}

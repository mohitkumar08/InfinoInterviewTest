package com.bit.myapplication.repo.local;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreferences {

    private static final int MODE_PRIVATE = 0;
    private static AppSharedPreferences pref;
    private static SharedPreferences appSharedPrefs;
    private static SharedPreferences.Editor prefsEditor;
    private static String PREF_FILE = "Infino.XML";
    private static boolean isEncryptionException = false;

    private AppSharedPreferences(Context context) {
        appSharedPrefs = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        prefsEditor = appSharedPrefs.edit();
    }

    public static AppSharedPreferences getPreferenceReference(Context context) {
        synchronized (AppSharedPreferences.class) {
            if (pref == null) {
                pref = new AppSharedPreferences(context);
            }
        }
        return pref;
    }

    public static String getStringValue(String keyName) {
        return appSharedPrefs.getString(keyName, null);

    }

    public static void setStringValue(String keyName, String value) {
        prefsEditor.putString(keyName, value);

        prefsEditor.commit();
    }
}
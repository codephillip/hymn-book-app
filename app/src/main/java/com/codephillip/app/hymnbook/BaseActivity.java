package com.codephillip.app.hymnbook;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by codephillip on 08/04/17.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activateTheme();
        super.onCreate(savedInstanceState);
    }

    private void activateTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = prefs.getBoolean("app_theme", false);
        Log.d("Changing theme", "activateTheme: " + useDarkTheme);
        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }
    }
}



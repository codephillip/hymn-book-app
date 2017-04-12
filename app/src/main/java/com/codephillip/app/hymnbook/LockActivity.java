package com.codephillip.app.hymnbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codephillip.app.hymnbook.utilities.Utils;

public class LockActivity extends AppCompatActivity {

    private TextView passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        Button unlockButton = (Button) findViewById(R.id.unlock_button);
        passwordView = (TextView) findViewById(R.id.password);

        if (!isLocked())
            startActivity(new Intent(this, MainActivity.class));

        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPassword();
            }
        });
    }

    private void verifyPassword() {
        if (passwordView.getText().toString().equals("mukiriza")) {
            Toast.makeText(this, "Unlocked successfully :)", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLocked() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean(Utils.HAS_LOCKED, false);
    }
}

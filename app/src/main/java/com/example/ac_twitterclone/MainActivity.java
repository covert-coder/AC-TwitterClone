package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.parse.ParseInstallation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // the following increments a counter on the parse server every time the app is downloaded
        // com.parse.ParseInstallation library was imported to facilitate this language
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}

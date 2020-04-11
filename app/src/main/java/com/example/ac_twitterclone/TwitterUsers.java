package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class TwitterUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        Toast.makeText(TwitterUsers.this, "You have logged in successfully!", Toast.LENGTH_SHORT).show();
    }
}

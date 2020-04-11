package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mLogin;
    private Button mSignUpReturn;
    private EditText mTxtPasswordOnFile;
    private EditText mTxtLoginOnFile;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                // user name, as entered, and password, as entered are sent to parse server for match
                        // parse server will produce error if either username or password do not match records (e),then e != null
                        // using Back4.com for parse server, lack of either username or password generates built-in error, e
                ParseUser.logInInBackground(mTxtPasswordOnFile.getText().toString(), mTxtLoginOnFile.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null && e==null){
                            Toast.makeText(LoginActivity.this, "your login was successful "+user.get("username")
                                            , Toast.LENGTH_SHORT).show();
                            TransitionToActivityTwitterUsers();

                        }else{
                            Toast.makeText(LoginActivity.this, "your login failed: "+ e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                        }
                    }
                });
            case R.id.btnSignUp:
                Intent intent = new Intent(this, SignUp.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login to Twitter");
        mLogin = findViewById(R.id.btnLogin);
        mSignUpReturn = findViewById(R.id.btnSignUp);
        mTxtPasswordOnFile= findViewById(R.id.edtTxtPsswd);
        mTxtLoginOnFile=findViewById(R.id.edtTxtPsswd);

        mLogin.setOnClickListener(LoginActivity.this);
        mSignUpReturn.setOnClickListener(LoginActivity.this);

        ParseUser.getCurrentUser();
        ParseUser.logOut();

        // logout any user that is logged in
        if (ParseUser.getCurrentUser() != null) {

            TransitionToActivityTwitterUsers();
        }
    }
        protected void TransitionToActivityTwitterUsers(){

            Intent intent = new Intent(this, TwitterUsers.class);
            startActivity(intent);

        }
}



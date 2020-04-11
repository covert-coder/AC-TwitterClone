package com.example.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button mSignUp;
    private Button mLoginGo;
    private EditText mUserNameTwitter;
    private EditText mPasswordTwitter;
    private EditText mEmailTwitter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setTitle("SignUp");
        mLoginGo=findViewById(R.id.btnReturnLogin);
        mSignUp=findViewById(R.id.btnSignUp);
        mLoginGo.setOnClickListener(this);
        mSignUp.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReturnLogin:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.btnSignUp:

                final ParseUser appUser = new ParseUser();
                appUser.setUsername(mUserNameTwitter.getText().toString());
                appUser.setPassword(mPasswordTwitter.getText().toString());
                appUser.setEmail(mEmailTwitter.getText().toString());

                //create a progress dialog to notify the user that their signup is progressing
                final ProgressDialog signUpDialog = new ProgressDialog(SignUp.this);
                signUpDialog.setTitle("Working");
                signUpDialog.setMessage(mUserNameTwitter.getText().toString() + " Your signUp is in progress");
                signUpDialog.show();

                // check for an empty field in password, username, email address
                if (mEmailTwitter.getText().toString().equals("") || mPasswordTwitter.getText().toString().equals("")
                        || mPasswordTwitter.getText().toString().equals("")) {
                    Toast.makeText(SignUp.this, "email address, password, " +
                            "and username must be provided", Toast.LENGTH_LONG).show();
                    signUpDialog.dismiss(); // signup is not occurring so..
                }
                // but.., if all fields have been filled, then
                else {
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(SignUp.this, "your password and login were " +
                                        "set successfully;", Toast.LENGTH_SHORT).show();
                                signUpDialog.dismiss();
                                // possibility of server error since e is not null
                                TransitionToActivityTwitterUsers();
                            } else {
                                Toast.makeText(SignUp.this, "your password and login were " +
                                        "not successful;" + e.getMessage(), Toast.LENGTH_LONG).show();
                                signUpDialog.dismiss();
                            }
                        }
                    });
                    break; // break from case R.id.btnSignupInstag
                }
        }
    }
    public void TransitionToActivityTwitterUsers(){
        Intent intent = new Intent(this, TwitterUsers.class);
        startActivity(intent);
    }
}

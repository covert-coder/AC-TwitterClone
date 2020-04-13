package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList arrayList;
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);
        Toast.makeText(TwitterUsers.this, ParseUser.getCurrentUser().getUsername()
                +": You have logged in successfully!", Toast.LENGTH_SHORT).show();

        mListView = findViewById(R.id.listView);

        // assign the array for storing the usernames in the for loop below
        arrayList = new ArrayList<>();

        // set the ListView as the onItemClickListener (context is the parameter)
        // now an instance of the users tab, List View mListView is going to be the itemOnClickListener
        // specifically, for items inside the ListView such that, tapping on an item will trigger the listener
        mListView.setOnItemClickListener(TwitterUsers.this);

        final ParseUser parseUser = ParseUser.getCurrentUser();
        // set the type of parse query to <ParseUser>
        final ParseQuery<ParseUser> userQuery = ParseUser.getQuery();

        // filter out the currentUser using "where not equal to" followed by what we want to filter out
        // that exception is the user name and we get it using ParseUser.get.....
        //userQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        // now we create the "findInBackground method with a callback to find all objects that match the query of type ParseUser
        // less the excluded current user
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> ourUserList, ParseException e) {
                if(e==null){
                    if(ourUserList.size()>0){

                        for(ParseUser user: ourUserList){
                            // this is where we will create our array list, but we first needed to initialize and define
                            // that array in our class userTab and in the onCreateView, above
                            // the parameters of ArrayAdapter are; the context, the line item
                            // designated to populate the array, and the name of our array
                            mAdapter = new ArrayAdapter(TwitterUsers.this, android.R.layout.simple_list_item_checked, arrayList);
                            mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                            // get the next username in the database and add it to the list
                            arrayList.add(user.getUsername());
                            // add a straight line separator on the array list to separate the users visually on the page
                            //arrayList.add("_______________________________________");

                            // let the adapter know where we are in the list as iteration progresses
                            mAdapter.notifyDataSetChanged();
                        }
                        // set the adapter to our ListView, mListView so we can see the list on the screen(ListView)
                        mListView.setAdapter(mAdapter);
                    }else{
                        Toast.makeText(TwitterUsers.this, "No records to show, or, data retrieval error. " +
                                "Try again later", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout_user_item){
            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    Intent intent = new Intent(TwitterUsers.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if(checkedTextView.isChecked() == true){

            Toast.makeText(TwitterUsers.this, "This user has been followed: "+arrayList.get(position).toString(), Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "This user has been unfollowed: "+arrayList.get(position).toString(), Toast.LENGTH_SHORT).show();
        }

    }
}

package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList<String> arrayList; // <String> indicates explicitly what will be stored
    private ArrayAdapter<String> mAdapter; // <String> indicates explicitly what will be stored

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);
        Toast.makeText(TwitterUsers.this, ParseUser.getCurrentUser().getUsername()
                +": You have logged in successfully!", Toast.LENGTH_SHORT).show();

        mListView = findViewById(R.id.listView);

        // assign the array for storing the usernames in the for loop below
        arrayList = new ArrayList<>(); // the angle brackets "generify" the variable
                                        // String can be applied, if wanted to make it clear the array
                                        // list contains a string. This is done when creating the variable (above)

        // set the ListView as the onItemClickListener (context is the parameter)
        // now an instance of the users tab, List View mListView is going to be the itemOnClickListener
        // specifically, for items inside the ListView such that, tapping on an item will trigger the listener
        mListView.setOnItemClickListener(TwitterUsers.this);

        // set the type of parse query to <ParseUser>
        final ParseQuery<ParseUser> userQuery = ParseUser.getQuery();

        // exclude the current user from display/accessing
        userQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        // now we create the "findInBackground method with a callback to find all objects that match the query of type ParseUser
        // and we store them in ourUserList
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
                            mAdapter = new ArrayAdapter<>(TwitterUsers.this, android.R.layout.simple_list_item_checked, arrayList);
                            mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                            // get the next username in the database and add it to the list
                            arrayList.add(user.getUsername());

                            // let the adapter know where we are in the list as iteration progresses
                            mAdapter.notifyDataSetChanged();
                        }
                        // set the adapter to our ListView, mListView so we can see the list on the screen(ListView)
                        mListView.setAdapter(mAdapter);

                            for (String user : arrayList) {
                                if (ParseUser.getCurrentUser().getList("followerOf") != null) {
                                    // if the current users, followerOf column (array) contains any user names from that arrayList
                                    if (Objects.requireNonNull(ParseUser.getCurrentUser().getList("followerOf")).contains(user)) {

                                        // check the checkbox for the user having that name within our arrayList using that user name as the index to do so
                                        mListView.setItemChecked(arrayList.indexOf(user), true);
                                    }
                                }
                            }

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
            ParseUser.getCurrentUser();
            ParseUser.logOutInBackground(new LogOutCallback() {
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;

        if (checkedTextView.isChecked()) {

            ParseUser.getCurrentUser().add("followerOf", arrayList.get(position));
            Toast.makeText(TwitterUsers.this, "the user " + arrayList.get(position) + "  was added to your following list ", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "This user was removed from your following list: "+arrayList.get(position), Toast.LENGTH_SHORT).show();

            // get access to the parse column with the array followerOf and store its contents
            // in our own local listarray called UserFollowerOf
            List UserFollowerOf = ParseUser.getCurrentUser().getList("followerOf");
            // delete the user that is now unchecked by the device user from our array
            UserFollowerOf.remove(arrayList.get(position));
            // remove the entire listarray from the server; delete it
            ParseUser.getCurrentUser().remove("followerOf");
            // replace the deleted array on the server with our modified array that has the unchecked user removed
            ParseUser.getCurrentUser().put("followerOf", UserFollowerOf);
            }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            }
        });
    }
}


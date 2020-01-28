/**
 * Created by: Tyler, Matt, Dip, and Connor
 * Created in part using Pradyuman's tutorials
 */
package com.example.androidchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pd.chatapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class appStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_start_activity);
    }


    public void announcementsButton(View v)
    {
        UserDetails.chatWith = "";
        startActivity(new Intent(appStartActivity.this, announcements_main.class));
    }

    public void chatsButton(View v)
    {
        startActivity(new Intent(appStartActivity.this, Users.class));
    }

    public void listsButton(View v)
    {
        startActivity(new Intent(appStartActivity.this, completedLists.class));
    }

    public void pollsButton(View v)
    {
        startActivity(new Intent(appStartActivity.this, completedPolls.class));
    }
}
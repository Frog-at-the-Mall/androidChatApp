package com.example.androidchatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pd.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class completedLists extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference listRef;
    private EditText editText;
    List<CheckBox> checklist;
    List<String> data;
    CheckBox newbox;
    LinearLayout listlayout;
    long countonFire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);

        editText = (EditText) findViewById(R.id.editText);
        listlayout = (LinearLayout) findViewById(R.id.listLayout);
        checklist = new ArrayList<CheckBox>();
        data = new ArrayList<String>();
        database = FirebaseDatabase.getInstance();
        listRef = database.getReference("List");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ValueEventListener optioneventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                checklist.clear();
                listlayout.removeAllViews();
                countonFire = dataSnapshot.getChildrenCount();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String opVal = ds.getValue(String.class);
                    createCheckbox(opVal);
                    //Toast.makeText(AllList.this, opVal, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        listRef.addListenerForSingleValueEvent(optioneventListener);

    }

    public void addtolist(View v) {
        newbox = new CheckBox(this);
        newbox.setText(editText.getText().toString());
        listlayout.addView(newbox);
        checklist.add(newbox);
        data.add(newbox.getText().toString());
        editText.setText("");
        closeKeyboard();
    }

    public void update(View v) {
        ValueEventListener optioneventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                checklist.clear();
                listlayout.removeAllViews();
                countonFire = dataSnapshot.getChildrenCount();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String opVal = ds.getValue(String.class);
                    createCheckbox(opVal);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        listRef.addListenerForSingleValueEvent(optioneventListener);

    }

    public void post(View view) {
        listRef.removeValue();
        listRef.setValue(data);
    }

    public void remove(View v) {
        int size = checklist.size();
        for (int i = 0; i < checklist.size(); i++) {
            CheckBox cb = (CheckBox) listlayout.getChildAt(i);

            if (cb.isChecked()) {
                listlayout.removeView(cb);
                data.remove(i);
                checklist.remove(i);
            }
        }
        listRef.setValue(data);

    }

    public void createCheckbox(String datafromFire) {
        newbox = new CheckBox(this);
        newbox.setText(datafromFire);
        data.add(datafromFire);
        listlayout.addView(newbox);
        checklist.add(newbox);
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
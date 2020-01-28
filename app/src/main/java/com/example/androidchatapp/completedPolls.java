package com.example.androidchatapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.androidchatapp.OptionList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pd.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class completedPolls extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef, OpRef, btref;
    TableLayout table;
    EditText item, content, title;
    TableRow row;
    Button button, post, button1;
    String titleQuest;
    TableRow.LayoutParams textViewParam;
    Boolean empty;
    OptionList optionList;
    long maxID = 0;
    String btvalue;
    int buttoncount;
    List<Button> buttonlist;
    List<String> optiontolist;
    List<String> optionsData;
    List<String> buttonData;
    List<String> datafromFire;
    long countfromFire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_polls);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        table = (TableLayout) findViewById(R.id.group);
        item = (EditText) findViewById(R.id.item);
        title = (EditText) findViewById(R.id.title);
        post = (Button) findViewById(R.id.post);
        optionsData = new ArrayList<String>();
        buttonlist = new ArrayList<Button>();
        optionsData = new ArrayList<String>();
        buttonData = new ArrayList<String>();
        datafromFire = new ArrayList<String>();
        optionList = new OptionList();
        optiontolist = new ArrayList<String>();
        textViewParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2.0f);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        //firstGroup
        myRef = database.getReference("title");
        OpRef = database.getReference("OptionList");
        btref = database.getReference("ButtonList");


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countfromFire = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        OpRef.addListenerForSingleValueEvent(valueEventListener);


        ValueEventListener optioneventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String opVal = ds.getValue(String.class);
                    optionsData.add(opVal);
                    //  Toast.makeText(Polls.this, "vlaue data is " + ds.getValue(String.class), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        OpRef.addListenerForSingleValueEvent(optioneventListener);

        ValueEventListener buttoneventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String btVal = ds.getValue(String.class);
                    buttonData.add(btVal);
                    //  Toast.makeText(Polls.this, "vlaue data is " + ds.getValue(String.class), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        btref.addListenerForSingleValueEvent(buttoneventListener);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                title.setText(value);
                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        OpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxID = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        title.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                titleQuest = title.getText().toString();
            }

        });


        //text input for optionsData

        item.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() < 1) {
                    empty = true;
                } else if (s.length() >= 25) {
                    Toast.makeText(completedPolls.this, "Maximum limit reached", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void add(View v) {

        if ((item.getText().toString()).isEmpty()) {
            Toast.makeText(completedPolls.this, "There is nothing to add. Please type to add.", Toast.LENGTH_LONG).show();
        } else {

            dataload(item.getText().toString(), "0");

        }
        item.setText("");

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    View.OnClickListener btnclick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            for (int i = 0; i < buttonlist.size(); i++) {
                int u = view.getId();
                int y = buttonlist.get(i).getId();
                if (u == y) {
                    int current = Integer.parseInt(buttonlist.get(i).getText().toString());
                    buttonlist.get(i).setText(String.valueOf(current + 1));
                    buttonlist.get(i).setEnabled(false);
                    Toast.makeText(completedPolls.this, "Your poll has been added", Toast.LENGTH_LONG).show();
                }
            }
        }

    };

    View.OnClickListener btn2click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            for (int i = 0; i < buttonlist.size(); i++) {
                int u = view.getId();
                int y = buttonlist.get(i).getId();
                if (u == y) {
                    int current = Integer.parseInt(buttonlist.get(i).getText().toString());
                    buttonlist.get(i).setText(String.valueOf(current + 1));
                    buttonlist.get(i).setEnabled(false);
                    Toast.makeText(completedPolls.this, "Your poll has been added", Toast.LENGTH_LONG).show();
                }
            }
        }

    };

    public void refresh(View v) {

        optionsData.clear();
        buttonData.clear();
        buttoncount = 0;

        ValueEventListener optioneventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countfromFire = dataSnapshot.getChildrenCount();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String opVal = ds.getValue(String.class);
                    optionsData.add(opVal);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        OpRef.addListenerForSingleValueEvent(optioneventListener);

        ValueEventListener buttoneventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String btVal = ds.getValue(String.class);
                    buttonData.add(btVal);
                    //Toast.makeText(Polls.this, "bt data is " + ds.getValue(String.class), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        btref.addListenerForSingleValueEvent(buttoneventListener);

    }

    public void delete(View v){
        OpRef.removeValue();
        btref.removeValue();
        myRef.removeValue();
        table.removeAllViewsInLayout();
        buttonData.clear();
        optionsData.clear();
        buttonlist.clear();
        optiontolist.clear();
        title.setText("");
    }



    public void update(View v) {

        if (optionsData.size() != 0) {
            table.removeAllViewsInLayout();
            buttoncount = 0;
            buttonlist.clear();
            optiontolist.clear();
            for (int i = 0; i < optionsData.size(); i++) {

                buttoncount++;
                newDataload(optionsData.get(i), buttonData.get(i));

            }
        } else
            Toast.makeText(completedPolls.this, "size is" + String.valueOf(optionsData.size()), Toast.LENGTH_LONG).show();
    }

    public void dataload(String opdata, String btdata) {

        row = new TableRow(this);
        row.setLayoutParams(textViewParam);

        content = new EditText(this);
        content.setMaxWidth(500);
        int maxLength = 20;
        content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        content.setMaxLines(1);
        content.setText(opdata);
        content.setTextSize(18);
        content.setBackground(null);
        optionsData.add(opdata);
        optiontolist.add(opdata);
        content.setTextColor((Color.BLACK));
        content.setGravity(Gravity.LEFT);
        content.setPadding(10, 0, 10, 0);
        row.addView(content);


        button = new Button(this);
        button.setText(btdata);
        button.setId(buttoncount);
        button.setHeight(67);
        button.setWidth(30);
        buttonlist.add(button);
        buttonData.add(btvalue);
        button.setOnClickListener(btnclick);
        button.setTextSize(18);
        button.setTextColor((Color.BLACK));
        button.setGravity(Gravity.CENTER);
        button.setPadding(10, 0, 10, 0);
        row.addView(button);

        table.addView(row);

        buttoncount++;
    }

    public void newDataload(String opdata, String btdata) {

        row = new TableRow(this);
        row.setLayoutParams(textViewParam);


        content = new EditText(this);
        content.setMaxWidth(500);
        int maxLength = 20;
        content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        content.setMaxLines(1);
        content.setText(opdata);
        content.setTextSize(18);
        optiontolist.add(opdata);
        content.setBackground(null);
        content.setTextColor((Color.BLACK));
        content.setGravity(Gravity.LEFT);
        content.setPadding(10, 0, 10, 0);
        row.addView(content);


        button1 = new Button(this);
        button1.setText(btdata);
        button1.setHeight(67);
        button1.setWidth(30);
        button1.setId(buttoncount);
        button1.setOnClickListener(btn2click);
        button1.setTextSize(18);
        buttonlist.add(button1);
        button1.setTextColor((Color.BLACK));
        button1.setGravity(Gravity.CENTER);
        button1.setPadding(10, 0, 10, 0);
        row.addView(button1);

        table.addView(row);

        buttoncount++;


    }

    public void onPostClick(View v) {

        myRef.setValue(titleQuest);

        OpRef.removeValue();
        btref.removeValue();

        buttonData.clear();
        optionsData.clear();

        for (int i = 0; i < buttonlist.size(); i++) {
            buttonData.add(buttonlist.get(i).getText().toString());
            optionsData.add(optiontolist.get(i));
        }

        OpRef.setValue(optionsData);
        btref.setValue(buttonData);

    }

}









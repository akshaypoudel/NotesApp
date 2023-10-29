package com.example.notes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DisplayNotes extends AppCompatActivity {

    private EditText titleText,contentText;
    private TextView date_time_text,character_text;
    private FloatingActionButton notesDoneFab;
    private static int positionToReplaceNote;

    private LocalDateTime currentTime;

    private static int charCount=0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);

        ActionBar actionBar = getSupportActionBar();

// Set the background color of the ActionBar to blue
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0E0E16")));

        titleText=findViewById(R.id.__titleEditText);
        contentText = findViewById(R.id.__contentEditText1);
        date_time_text = findViewById(R.id.__dateTextinDisplayNote);
        character_text=findViewById(R.id.__charTextinDisplayNote);
        notesDoneFab = findViewById(R.id.__notesDoneFAB);
        notesDoneFab.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.TITLE_TEXT);
        String date_time = intent.getStringExtra(MainActivity.DATE_TEXT);
        String character = intent.getStringExtra(MainActivity.CHARACTER_TEXT);
        String content = intent.getStringExtra(MainActivity.CONTENT_TEXT);
        positionToReplaceNote = Integer.parseInt(intent.getStringExtra((MainActivity.POSITION_OF_ARRAYLIST)));


        titleText.setText(title);
        contentText.setText(content);
        date_time_text.setText(date_time);
        character_text.setText(character);

        //////////////////////////////////////////////

        contentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1=contentText.getText().toString();
                String s2=s1.replaceAll("\n","");
                String s3=s2.replaceAll(" ","");

                String sTitle=titleText.getText().toString();
                String sTitle2=sTitle.replaceAll("\n","");
                String sTitle3=sTitle2.replaceAll(" ","");

                charCount=(s3.length()+sTitle3.length());
                String finalString = "|   "+charCount+" characters";
                character_text.setText(finalString);
                notesDoneFab.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        titleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesDoneFab.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        titleText.setOnTouchListener((v, event) -> {

            return false;
        });

        titleText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    titleText.clearFocus();
                }
            }
        });
        contentText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    contentText.clearFocus();
                }
            }
        });
    }

    public void Save1(View view)
    {
        ////////////////////changing date and time when user click on save button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy   HH:mm:ss");
            String formattedDateTime = currentTime.format(formatter);
            date_time_text.setText(formattedDateTime);
        }
        notesDoneFab.setVisibility(View.INVISIBLE);
        contentText.clearFocus();
        titleText.clearFocus();
    }
    public void AddToDataBase1()
    {

        //Saving the data when user presses back button

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy   HH:mm:ss");
            String formattedDateTime = currentTime.format(formatter);
            date_time_text.setText(formattedDateTime);
        }

        String Title = titleText.getText().toString();
        String date_time = date_time_text.getText().toString();
        String characters = character_text.getText().toString();
        String contents = contentText.getText().toString();
        if(Title.equals("") && contents.equals("")) return;


        CreateNotes note = new CreateNotes(Title,contents,date_time,characters);

        DBHandler handler = new DBHandler(this,SaveNotes.MY_DATABASE_NAME,null,1);
        handler.replaceNote(note,positionToReplaceNote);
    }

    @Override
    public void onBackPressed() {
        titleText.clearFocus();
        contentText.clearFocus();
        AddToDataBase1();
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
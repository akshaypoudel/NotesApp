package com.example.notes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Add_Note extends AppCompatActivity {

    private EditText titleEditText;
    private TextView _dateTextView,characterTextView;
    private EditText contentEditText;
    private LocalDateTime currentTime;

    private FloatingActionButton notesDoneFab;
    private static boolean isOnPage;
    private boolean isDataSaved;


    private static int charCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ConstraintLayout myConstraintLayout = findViewById(R.id.myConstraintLayout1);
        myConstraintLayout.setBackgroundResource(BackgroundThemes.themesArr[BackgroundThemes.theme]);

        isOnPage=true;

        titleEditText     =   findViewById(R.id.titleEditText);
        _dateTextView     =   findViewById(R.id.dateTextinAddNote);
        contentEditText   =   findViewById(R.id.contentEditText1);
        characterTextView =   findViewById(R.id.charTextinAddNote);
        notesDoneFab      =   findViewById(R.id.notesDoneFAB);

        notesDoneFab.setVisibility(View.INVISIBLE);
// Get the ActionBar object
        ActionBar actionBar = getSupportActionBar();

// Set the background color of the ActionBar to blue
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0E0E16")));


        ////////////////////// SETTING DATE AND TIME IN THE NOTE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy   HH:mm:ss");
            String formattedDateTime = currentTime.format(formatter);
            _dateTextView.setText(formattedDateTime);
        }

        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1=contentEditText.getText().toString();

                String sTitle=titleEditText.getText().toString();
                String sTitle2=sTitle.replaceAll("\n","");
                String sTitle3=sTitle2.replaceAll(" ","");

                String s2=s1.replaceAll("\n","");
                String s3=s2.replaceAll(" ","");

                charCount=(s3.length()+sTitle3.length());

                String finalString = "|   "+charCount+" characters";
                characterTextView.setText(finalString);
                charCount=0;
                notesDoneFab.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        titleEditText.addTextChangedListener(new TextWatcher() {
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
        titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    titleEditText.clearFocus();
                }
            }
        });
        contentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    contentEditText.clearFocus();
                }
            }
        });
    }
    public void Save(View view)
    {
        notesDoneFab.setVisibility(View.INVISIBLE);
        contentEditText.clearFocus();
        titleEditText.clearFocus();
        if(!isDataSaved)
        {
            isDataSaved=true;
            AddToDataBase();
        }

    }
    private void AddToDataBase()
    {
        String Title = titleEditText.getText().toString();
        String date_time = _dateTextView.getText().toString();
        String characters = characterTextView.getText().toString();
        String contents = contentEditText.getText().toString();

        if(Title.equals("") && contents.equals("")) return;

        SharedPreferences s = getSharedPreferences(SaveNotes.SHARED_PREFERENCE,MODE_PRIVATE);
        int position =s.getInt(SaveNotes.SHARED_PREFERENCE_POSITION,-1);

        CreateNotes note = new CreateNotes(Title,contents,date_time,characters);

        DBHandler handler = new DBHandler(this,SaveNotes.MY_DATABASE_NAME,null,1);
        int pos = handler.addNote(note,position+1);

        SharedPreferences s1=getSharedPreferences(SaveNotes.SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor eD=s1.edit();
        eD.putInt(SaveNotes.SHARED_PREFERENCE_POSITION,pos);
        eD.apply();

        notesDoneFab.setVisibility(View.INVISIBLE);
        contentEditText.clearFocus();
        titleEditText.clearFocus();
    }

    @Override
    public void onBackPressed() {
        contentEditText.clearFocus();
        titleEditText.clearFocus();
        if(!isDataSaved)
        {
            isDataSaved=true;
            AddToDataBase();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
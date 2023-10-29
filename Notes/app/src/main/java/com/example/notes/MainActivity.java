package com.example.notes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TITLE_TEXT="TitleTextView";
    public static final String DATE_TEXT="Date_Time_TextView";
    public static final String CHARACTER_TEXT = "Character_TextView";
    public static final String CONTENT_TEXT="Content_TextView";

    public static final String POSITION_OF_ARRAYLIST="Position_of_Arraylist";

    private ListView _listView;
    private TextView emptyScreenTxtView;
    private AkkuAdapter _adapter;
    private boolean onCreateCalled = false;

    private static ArrayList<CreateNotes> notes = new ArrayList<CreateNotes>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateCalled=true;
        _listView = findViewById(R.id.listView);
        emptyScreenTxtView=findViewById(R.id.emptyScreenTextView);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Notes");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF28282A")));
        actionBar.setDisplayShowHomeEnabled(true);
///////////////////////////////////////////////////
        FetchFromDataBase();
        SetEmptyNotesTextBox();
        _adapter = new AkkuAdapter(this, R.layout.akku_layout, notes);
        _listView.setAdapter(_adapter);
    }

    @Override
    protected void onResume() {
        if(!onCreateCalled)
        {
            FetchFromDataBase();
            SetEmptyNotesTextBox();
            _adapter = new AkkuAdapter(this, R.layout.akku_layout, notes);
            _listView.setAdapter(_adapter);
        }
        super.onResume();
    }

    private void SetEmptyNotesTextBox()
    {
        if(!notes.isEmpty())
            emptyScreenTxtView.setVisibility(View.INVISIBLE);
        else
            emptyScreenTxtView.setVisibility(View.VISIBLE);
    }
    public void Fab(View view)
    {
        onCreateCalled=false;
        Intent intent=new Intent(this,Add_Note.class);
        startActivity(intent);
    }


    public void RemoveElement(int position)
    {
        notes.remove(position);
        if(_adapter!=null)
            _adapter.notifyDataSetChanged();

        SetEmptyNotesTextBox();
        int a=notes.size();
        DeleteDataBase(a);
    }
    public void FetchFromDataBase()
    {
        DBHandler handler = new DBHandler(this,SaveNotes.MY_DATABASE_NAME,null,1);
        notes.clear();
        long k = handler.getNotesCount();
        for(long i=0;i<k;i++)
        {
            notes.add(handler.getNote((int)i));
        }
    }
    public void DeleteDataBase(int a)
    {
        DBHandler handler = new DBHandler(this,SaveNotes.MY_DATABASE_NAME,null,1);
        handler.DeleteDataBase();
        AddToDataBase(a);
    }
    public void ShowDeleteDialogBox(int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note?");
        builder.setMessage("Do You Want to Delete this Note?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RemoveElement(position);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();
        dialog.setCancelable(false);
    }
    public void OnNotesClicked(int position)
    {
        onCreateCalled=false;

        Intent intent = new Intent(this,DisplayNotes.class);
        String titleText = notes.get(position).getTitle();
        String dateTime = notes.get(position).getDate();
        String characters = notes.get(position).getCharacter();
        String content = notes.get(position).getContent();

        intent.putExtra(TITLE_TEXT,titleText);
        intent.putExtra(DATE_TEXT,dateTime);
        intent.putExtra(CHARACTER_TEXT,characters);
        intent.putExtra(CONTENT_TEXT,content);
        intent.putExtra(POSITION_OF_ARRAYLIST,String.valueOf(position));

        startActivity(intent);
    }

    public void AddToDataBase(int size)
    {
        int a=0;
        if(size!=0)
        {
            DBHandler handler = new DBHandler(this,SaveNotes.MY_DATABASE_NAME,null,1);
            for(int i=0;i<notes.size();i++)
                a=handler.addNote(notes.get(i),i);

            SharedPreferences s1=getSharedPreferences(SaveNotes.SHARED_PREFERENCE,MODE_PRIVATE);
            SharedPreferences.Editor eD=s1.edit();
            eD.putInt(SaveNotes.SHARED_PREFERENCE_POSITION,a);
            eD.apply();
        }
        else if(size==0)
        {
            SharedPreferences s1=getSharedPreferences(SaveNotes.SHARED_PREFERENCE,MODE_PRIVATE);
            SharedPreferences.Editor eD=s1.edit();
            eD.putInt(SaveNotes.SHARED_PREFERENCE_POSITION,-1);
            eD.apply();
        }
    }

}
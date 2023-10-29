package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

        fetchSharedPreference();
        setBackgroundThemes(BackgroundThemes.theme);

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
    private void saveSharedPreference(int data)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putInt("theme", data);
        myEdit.apply();
    }
    private void fetchSharedPreference()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("MySharedPref",MODE_PRIVATE);
        BackgroundThemes.theme=sharedPreferences.getInt("theme",0);
    }
    public void setBackgroundThemes(int theme)
    {
        ConstraintLayout myConstraintLayout = findViewById(R.id.myConstraintLayout);
        myConstraintLayout.setBackgroundResource(BackgroundThemes.themesArr[theme]);
        BackgroundThemes.theme=theme;
        saveSharedPreference(theme);
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
        {
            _adapter.notifyDataSetChanged();
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.action_mode_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_themes1:
                setBackgroundThemes(0);
                break;
            case R.id.action_themes2:
                setBackgroundThemes(1);
                break;
            case R.id.action_themes3:
                setBackgroundThemes(2);
                break;
            case R.id.action_themes4:
                setBackgroundThemes(3);
                break;
            case R.id.action_themes5:
                setBackgroundThemes(4);
                break;
            case R.id.action_themes6:
                setBackgroundThemes(5);
                break;
            default:
                //setBackgroundThemes(0);
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper
{

    public DBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create = "CREATE TABLE "+SaveNotes.MY_TABLE_NAME+" (id INTEGER PRIMARY KEY, title TEXT,content TEXT,date TEXT,character TEXT)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(drop,new String[]{SaveNotes.MY_TABLE_NAME});
    }

    public int addNote(CreateNotes notes,int pos)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SaveNotes.ID,pos);
        values.put(SaveNotes.TITLE,notes.getTitle());
        values.put(SaveNotes.CONTENT,notes.getContent());
        values.put(SaveNotes.DATE,notes.getDate());
        values.put(SaveNotes.CHARACTER,notes.getCharacter());
        long k=database.insert(SaveNotes.MY_TABLE_NAME,null,values );
        database.close();
        return (int)k;
    }

    public CreateNotes getNote(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query(SaveNotes.MY_TABLE_NAME,new String[]{SaveNotes.ID,SaveNotes.TITLE,SaveNotes.CONTENT,SaveNotes.DATE,SaveNotes.CHARACTER},
                SaveNotes.ID+"=?",new String[]{String.valueOf(id)},null,null,null);

        if(cursor!=null && cursor.moveToFirst())
        {
            CreateNotes note = new CreateNotes(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            db.close();
            return note;
        }
        db.close();
        return null;
    }
    public void replaceNote(CreateNotes notes,int pos)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SaveNotes.ID,pos);
        values.put(SaveNotes.TITLE,notes.getTitle());
        values.put(SaveNotes.CONTENT,notes.getContent());
        values.put(SaveNotes.DATE,notes.getDate());
        values.put(SaveNotes.CHARACTER,notes.getCharacter());
        long k = db.replace(SaveNotes.MY_TABLE_NAME,null,values);
        db.close();
    }


    public long getNotesCount()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + SaveNotes.MY_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

// Get the result of the query (the number of rows in the table)

        int rowCount = 0;
        if (cursor.moveToFirst()) {
            rowCount = cursor.getInt(0);
        }
        db.close();
        return rowCount;
    }

    public void DeleteDataBase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SaveNotes.MY_TABLE_NAME, null, null);

        db.close();
    }
}

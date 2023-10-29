package com.example.notes;

import java.util.ArrayList;

public class CreateNotes {
    private String title;
    private String content;
    private String date;
    private String character;// format = 12-04-2023   08:31   |  31 characters

    public CreateNotes(String title, String content, String date,String character) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.character=character;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String character) {
        this.date = character;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}

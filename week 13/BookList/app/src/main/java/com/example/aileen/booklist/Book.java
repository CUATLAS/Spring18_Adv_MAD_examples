package com.example.aileen.booklist;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Book extends RealmObject{
    @Required
    @PrimaryKey
    private String id;
    private String book_name;
    private String author_name;
    private boolean read;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book) {
        this.book_name = book;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author) {
        this.author_name = author;
    }

    public boolean hasRead() {
        return read;
    }

    public void setRead(boolean done) {
        this.read = done;
    }
}

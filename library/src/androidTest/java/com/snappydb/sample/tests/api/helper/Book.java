package com.snappydb.sample.tests.api.helper;

/**
 * Created by Nabil on 11/09/14.
 */
public class Book {

    //no-arg constructor for Kryo
    public Book(){}

    public Book(String title, String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    public String title;
    public String isbn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return isbn.equals(book.isbn) && title.equals(book.title);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + isbn.hashCode();
        return result;
    }
}

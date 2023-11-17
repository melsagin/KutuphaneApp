package com.melsasagin.kutuphaneapp;

public class BooksItem {

    String kitapID;
    String kitapName;
    String kitapAuthor;
    String kitapYayinevi;

    public BooksItem() {}

    public BooksItem(String kitapID, String kitapName, String kitapAuthor, String kitapYayinevi) {
        this.kitapID = kitapID;
        this.kitapName = kitapName;
        this.kitapAuthor = kitapAuthor;
        this.kitapYayinevi = kitapYayinevi;
    }

    public String getKitapID() {
        return kitapID;
    }

    public void setKitapID(String kitapID) {
        this.kitapID = kitapID;
    }

    public String getKitapName() {
        return kitapName;
    }

    public void setKitapName(String kitapName) {
        this.kitapName = kitapName;
    }

    public String getKitapAuthor() {
        return kitapAuthor;
    }

    public void setKitapAuthor(String kitapAuthor) {
        this.kitapAuthor = kitapAuthor;
    }

    public String getKitapYayinevi() {
        return kitapYayinevi;
    }

    public void setKitapYayinevi(String kitapYayinevi) {
        this.kitapYayinevi = kitapYayinevi;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

/**
 *
 * @author Prateek
 */
public class Book {
    private  int bookID;
    private String title;
    private String author;
    private String ISBN10;
    private String ISBN13;
    private String genre;
    private String edition;
    private String publisher;
    private String description;
    private String imageLink;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
    
    public Book() {
        title = "";
        author = "";
        ISBN10 = "";
        ISBN13 = "";
        genre = "";
        edition = "";
        publisher = "";
        description = "";
        imageLink = "";
    }

    public Book( String title, String author, String ISBN10, String ISBN13, String genre, String edition, String publisher, String description) {

        this.title = title;
        this.author = author;
        this.ISBN10 = ISBN10;
        this.ISBN13 = ISBN13;
        this.genre = genre;
        this.edition = edition;
        this.publisher = publisher;
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public int getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getEdition() {
        return edition;
    }

    public String getGenre() {
        return genre;
    }

    public String getISBN10() {
        return ISBN10;
    }

    
    public String getISBN13() {
        return ISBN13;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public void setISBN10(String ISBN10) {
        this.ISBN10 = ISBN10;
    }

    public void setISBN13(String ISBN13) {
        this.ISBN13 = ISBN13;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
}

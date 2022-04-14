package com.example.booksmanagement.Model;

public class Library {

    public static Book[] books = new Book[100];
    public static BorrowBook [] borrowBooks = new BorrowBook[100];
    public static Book[] favoriteBooks = new Book[100];
    public static int totalBooks = 0;
    public static int totalBorrowBooks = 0;
    public static int totalNumberFavoriteBooks = 0;

    public static void addBookToLibrary(String name, String author, String category){
        books[totalBooks] = new Book(name, author, category);
        totalBooks++;
    }

    public static void addBookToBorrow(String name, String author, String category, String borrowToName,String date){
        borrowBooks[totalBorrowBooks] = new BorrowBook(name, author, category, borrowToName, date);
        totalBorrowBooks++;
    }

    public static void addToFavorite(String name, String author, String category){
        favoriteBooks[totalNumberFavoriteBooks] = new Book(name, author, category);
        totalNumberFavoriteBooks++;
    }




}

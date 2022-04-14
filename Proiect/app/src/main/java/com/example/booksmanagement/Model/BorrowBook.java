package com.example.booksmanagement.Model;

public class BorrowBook extends Book{

    private String borrowToName;
    private String date;

    public BorrowBook(String name, String author, String category,String borrowToName, String date) {
        super(name, author, category);
        this.borrowToName = borrowToName;
        this.date = date;
    }

    public String getBorrowToName() {
        return borrowToName;
    }

    public void setBorrowToName(String borrowToName) {
        this.borrowToName = borrowToName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

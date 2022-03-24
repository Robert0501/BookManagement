package com.example.booksmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void goToLibrary(View v){
        setContentView(R.layout.library);
    }

    public void goToAddBooks(View v){
        setContentView(R.layout.add_books);
    }

    public void goToBookInfo(View v){
        setContentView(R.layout.book_info);
    }

    public void goToBorrowTo(View v){
        setContentView(R.layout.borrow_to);
    }

    public void goToHome(View v){
        setContentView(R.layout.home);
    }
}
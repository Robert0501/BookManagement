package com.example.booksmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksmanagement.Model.Book;
import com.example.booksmanagement.Model.Library;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    ProgressBar pb;
    Button startButton;

    Book borrowedBook;
    String borrowToName;
    String date;

    int progress = 0;

    LinearLayout libraryLayout;
    LinearLayout favoriteLayout;
    LinearLayout borrowLayout;
    private Button addToBorrow;
    private Button borrowedDate;
    private TextView borrowToIn;

    private NotificationManager mNotifyManager;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;

    Book favoritedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        startButton = (Button) findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Uploader().execute();
            }
        });

        createNotificationChannel();
    }

    class Uploader extends AsyncTask<Void, Integer, Integer>{

        @Override
        protected void onPreExecute(){
            //TODO Auto-generated method stub
            super.onPreExecute();

            pb.setMax(100);
        }

        @Override
        protected void onProgressUpdate(Integer ... values){
            //TODO Auto-generated method stub
            super.onProgressUpdate(values);
            pb.setProgress(values[0]);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            for( progress = 0 ; progress <= 100 ; progress += 5 ){
                publishProgress(progress);
                try{
                    Thread.sleep(100);
                } catch(InterruptedException ie){
                    ie.printStackTrace();
                }

            }
            return progress;

        }

        @Override
        protected void onPostExecute(Integer result){
            //TODO Auto-generated method stub
            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(), "THE APP IS STARTING", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setContentView(R.layout.home);
        }
    }



    public void goToLibrary(View v){
        setContentView(R.layout.library);
        libraryLayout = findViewById(R.id.libraryLayout);

        addToBorrow = findViewById(R.id.borrowBook);
        borrowToIn = findViewById(R.id.borrowToIn);
        borrowedDate = findViewById(R.id.datePickerButton);

        TextView textViews [] = new TextView[100];
        Button borrowTo [] = new Button[100];
        Button addToFavorite [] = new Button[100];

        for(int i = 0 ; i < textViews.length ; i++){
            textViews[i] = new TextView(MainActivity.this);
            borrowTo[i] = new Button(MainActivity.this);
            addToFavorite[i] = new Button(MainActivity.this);
        }


        for(int i = 0 ; i < Library.totalBooks ; i++){
            textViews[i].setText("\nTitle: " + Library.books[i].getName() +"\n" +
                    "Author: " + Library.books[i].getAuthor() + "\n" +
                    "Category: " + Library.books[i].getCategory());
            textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            borrowTo[i].setText("Borrow");
            addToFavorite[i].setText("Favorite");
            libraryLayout.addView(textViews[i]);
            libraryLayout.addView(borrowTo[i]);
            libraryLayout.addView(addToFavorite[i]);

            int finalI = i;
            borrowTo[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContentView(R.layout.borrow_to);
                    initDatePicker();
                    dateButton = findViewById(R.id.datePickerButton);
                    dateButton.setText(getTodaysDate());
                    borrowedBook = new Book(Library.books[finalI].getName(),Library.books[finalI].getAuthor(),Library.books[finalI].getCategory());

                }
            });

            addToFavorite[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Library.addToFavorite(Library.books[finalI].getName(),Library.books[finalI].getAuthor(),Library.books[finalI].getCategory());
                    Toast.makeText(MainActivity.this, Library.books[finalI].getName() + " has been added to favorite list", Toast.LENGTH_SHORT).show();
                }
            });
        }



        TextView totalBooks = new TextView(MainActivity.this);
        String totalBooksString = "Total books in library: " + Library.totalBooks;
        totalBooks.setText(totalBooksString);
        totalBooks.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        libraryLayout.addView(totalBooks);

    }

    public void borrowBook(View v){
        date = dateButton.getText().toString();
        TextView borrowName;
        borrowName = findViewById(R.id.borrowToIn);
        borrowToName = borrowName.getText().toString();
        if(borrowToName.length() == 0){
            borrowName.setError("This field is required");
        }
            else {
                Library.addBookToBorrow(borrowedBook.getName(), borrowedBook.getAuthor(), borrowedBook.getCategory(),borrowToName, date);
                Toast.makeText(MainActivity.this, borrowedBook.getName() + " was added to borrowed books", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.home);
                sendNotification();
            }
    }

    public void goToAddBooks(View v){
        setContentView(R.layout.add_books);

       Button addBookButton = findViewById(R.id.addBookButton);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleIn = findViewById(R.id.titleIn);
                TextView authorIn = findViewById(R.id.authorIn);
                TextView categoryIn = findViewById(R.id.categoryIn);

                if(titleIn.length() == 0){
                    titleIn.setError("This field is required");
                } else  if(authorIn.length() == 0){
                    authorIn.setError("This field is required");
                } else  if(categoryIn.length() == 0){
                    categoryIn.setError("This field is required");
                } else {
                    Library.addBookToLibrary(titleIn.getText().toString(), authorIn.getText().toString(), categoryIn.getText().toString());
                    Toast.makeText(MainActivity.this, titleIn.getText().toString() + " was added successfully ", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.home);
                }
            }
        });

    }

    public void goToBookInfo(View v){
        setContentView(R.layout.book_info);
    }


    public void createNotificationChannel() {
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Mascot Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Borrowed book")
                .setContentText( borrowedBook.getName() + " has been borrowed to " + borrowToName)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }

    public void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    public void goToFavorite(View v){
        setContentView(R.layout.favorite);
        favoriteLayout = findViewById(R.id.favoriteLayout);

        TextView textViews [] = new TextView[100];

        for(int i = 0 ; i < textViews.length ; i++){
            textViews[i] = new TextView(MainActivity.this);
        }

        for(int i = 0 ; i < Library.totalNumberFavoriteBooks; i++) {
            textViews[i].setText("\nTitle: " + Library.favoriteBooks[i].getName() + "\n" +
                    "Author: " + Library.favoriteBooks[i].getAuthor() + "\n" +
                    "Category: " + Library.favoriteBooks[i].getCategory());
            textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            favoriteLayout.addView(textViews[i]);
        }

        TextView totalFavoriteBooks = new TextView(MainActivity.this);
        String totalNumberFavoriteBooks = "Total favorite books: " + Library.totalNumberFavoriteBooks;
        totalFavoriteBooks.setText(totalNumberFavoriteBooks);
        totalFavoriteBooks.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        favoriteLayout.addView(totalFavoriteBooks);
    }

    public void goToBorrow(View v){
        setContentView(R.layout.borrow);
        borrowLayout = findViewById(R.id.borrowLayout);

        TextView textViews [] = new TextView[100];



        for(int i = 0 ; i < textViews.length ; i++){
            textViews[i] = new TextView(MainActivity.this);
        }

        for(int i = 0 ; i < Library.totalBorrowBooks; i++) {
            textViews[i].setText("\nTitle: " + Library.borrowBooks[i].getName() + "\n" +
                    "Author: " + Library.borrowBooks[i].getAuthor() + "\n" +
                    "Category: " + Library.borrowBooks[i].getCategory() + "\n" +
                    "Borrowed to: " + Library.borrowBooks[i].getBorrowToName() + "\n" +
                    "Borrowed date: " + Library.borrowBooks[i].getDate());
            textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            borrowLayout.addView(textViews[i]);
        }

        TextView totalBorrowedBooks = new TextView(MainActivity.this);
        String totalNumberBorrowedBooks = "Total borrowed books: " + Library.totalBorrowBooks;
        totalBorrowedBooks.setText(totalNumberBorrowedBooks);
        totalBorrowedBooks.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        borrowLayout.addView(totalBorrowedBooks);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month , year);
                dateButton.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month  = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1){
            return "JAN";
        }

        if(month == 2){
            return "FEB";
        }

        if(month == 3){
            return "MAR";
        }

        if(month == 4){
            return "APR";
        }
        if(month == 5){
            return "MAY";
        }

        if(month == 6){
            return "JUN";
        }

        if(month == 7){
            return "JUL";
        }

        if(month == 8){
            return "AUG";
        }

        if(month == 9){
            return "SEP";
        }

        if(month == 10){
            return "OCT";
        }

        if(month == 11){
            return "NOV";
        }

        if(month == 12){
            return "DEC";
        }

        return "JAN";
    }


    public void goToHome(View v){
        setContentView(R.layout.home);
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}
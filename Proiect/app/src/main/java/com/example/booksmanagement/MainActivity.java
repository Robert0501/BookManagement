package com.example.booksmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    ProgressBar pb;
    Button startButton;

    int progress = 0;

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
    }

    public void goToAddBooks(View v){
        setContentView(R.layout.add_books);
    }

    public void goToBookInfo(View v){
        setContentView(R.layout.book_info);
    }

    public void goToBorrowTo(View v){
        setContentView(R.layout.borrow_to);
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
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
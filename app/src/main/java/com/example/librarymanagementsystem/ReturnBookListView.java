package com.example.librarymanagementsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.librarymanagementsystem.ui.view_students.ViewStudentsFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReturnBookListView extends AppCompatActivity {

    IssueBookDB issueBookDB;
    ArrayList<String> bookID;
    //ArrayList<String> rollno;
    ArrayList<String> due_date;
    Button returnBackBtn;

    String rollNo;
    private CustomBaseAdapter_ customBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_return_book_admin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        // Getting rollno from the view student from Admin
        Intent intent = getIntent();
        rollNo = intent.getStringExtra("RollNo");

        Log.d("ROLL NO (Return book ListView):", rollNo);

        bookID = new ArrayList<String>();
//        rollno = new ArrayList<String>();
        due_date = new ArrayList<String>();

        issueBookDB = new IssueBookDB(getApplicationContext());

        Cursor cursor = issueBookDB.getAllIssuedBook(rollNo);
        if (cursor == null) {
            bookID.add("No Books");
        } else {
            while (cursor.moveToNext()) {
                bookID.add(cursor.getString(0));
                due_date.add(cursor.getString(1));
            }
        }

        customBaseAdapter = new CustomBaseAdapter_();
        ListView listView = findViewById(R.id.return_list_view);
        listView.setAdapter(customBaseAdapter);
        customBaseAdapter.notifyDataSetChanged();

        returnBackBtn = findViewById(R.id.returnBackBtn);

        returnBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Admin_Dashboard.class);
                startActivity(intent);
            }
        });

    }

    class CustomBaseAdapter_ extends BaseAdapter {

        @Override
        public int getCount() {
            return bookID.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_return_book_list_view, parent, false);
            }
            TextView bookIDView = convertView.findViewById(R.id.Return_BookID_List_View);
            Button removeBtn = convertView.findViewById(R.id.deleteBtn_returnBook);
            Button viewFineBtn = convertView.findViewById(R.id.checkFine_returnBook);


            viewFineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String dueDate = due_date.get(position);

                    //Get the current date
                    Date currentDate = new Date();

                    // Convert the due_date string to a Date object
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date formattedDueDate = null;
                    try {
                        formattedDueDate = dateFormat.parse(dueDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Calculate the difference between the current date and the due date in milliseconds
                    long differenceMillis = currentDate.getTime() - formattedDueDate.getTime();


                    // Convert the difference to days
                    int daysLate = (int) (differenceMillis / (1000 * 60 * 60 * 24));

                    double totalFee;

                    if (daysLate < 0) {
                        totalFee = 0;
                    }
                    else{
                        // Calculate the fee per day (e.g. 150 PKR per day)
                        double feePerDay = 150.0;
                        totalFee = daysLate * feePerDay;
                    }
                    Log.d("TOTAL FEE:", String.valueOf(totalFee));


                    Toast.makeText(ReturnBookListView.this, "Fine: "+totalFee, Toast.LENGTH_SHORT).show();

                }
            });

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String bookToRemove = bookID.get(position);
                    boolean checkbookdelete = issueBookDB.deleteIssueBook(rollNo, bookToRemove);

                    Log.d("BookID (Return book listview)", bookToRemove);

                    if (checkbookdelete) {
                        bookID.remove(position);
                        due_date.remove(position);
                        notifyDataSetChanged();

                        //Increment Book Quantity (Student) and decrement Issued Books (Book)
                        StudentDB studentDB = new StudentDB(getApplicationContext());
                        BooksDB booksDB = new BooksDB(getApplicationContext());

                        booksDB.incrementBookQuantity(bookToRemove);
                        studentDB.decrementIssuedBooks(rollNo);

                        Toast.makeText(getApplicationContext(), "Book Returned!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Book NOT Returned!", Toast.LENGTH_SHORT).show();
                    }
                }

            });

            bookIDView.setText(bookID.get(position));

            return convertView;
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Admin_Dashboard.class);
        startActivity(intent);
        finish();
    }
}



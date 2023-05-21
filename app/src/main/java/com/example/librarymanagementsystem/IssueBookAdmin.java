package com.example.librarymanagementsystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem.ui.home.HomeFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IssueBookAdmin extends AppCompatActivity {

    EditText et_bookID, et_student_rollno, et_due_date;
    Button issueBtn, gobackBtn;
    int student_book_issued = 0;
    int book_limit = 0;
    StudentDB db;

    String bookID_txt;
    String student_rollno_txt;
    String due_date ;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_issue_book_admin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);


        Intent intent = getIntent();
        String bookID = intent.getStringExtra("BookID");

        //Getting Book Limit from SharedPreferences
        SharedPreferences sp = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        book_limit = Integer.parseInt(sp.getString("Book Limit", "0"));

        Log.d("CHECK BOOK LIMIT: ",String.valueOf(book_limit));


        et_bookID = findViewById(R.id.issue_bookId);
        //Auto Filling the Book ID
        et_bookID.setText(bookID);

        et_student_rollno = findViewById(R.id.issue_rollno);
        et_due_date = findViewById(R.id.issue_date);


        et_due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dataPickerDialog = new DatePickerDialog(IssueBookAdmin.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                c.set(year,monthOfYear, dayOfMonth);
                                Date selectedDate = c.getTime();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                et_due_date.setText(sdf.format(selectedDate));

                                selectedDate = c.getTime();
                            }
                        },year,month,day);
                dataPickerDialog.show();
            }
        });





        issueBtn = (Button)findViewById(R.id.issueBookBtn);

        issueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(IssueBookAdmin.this, "ISSUE BUTTON WORKING", Toast.LENGTH_SHORT).show();


                bookID_txt = et_bookID.getText().toString();
                student_rollno_txt = et_student_rollno.getText().toString();
                due_date = et_due_date.getText().toString();

//                if (bookID.isEmpty()){
//                    Toast.makeText(IssueBookAdmin.this, "ISSUE BUTTON WORKING", Toast.LENGTH_SHORT).show();
//                }

                Log.d("BOOK ID",bookID_txt);
                Log.d("ROLL NO",student_rollno_txt);
                Log.d("DATE",due_date);

//
//                Log.d("",);
//
//
                db = new StudentDB(getApplicationContext());
                Cursor cursor = db.getStudentIssueBook(student_rollno_txt);

                if (cursor != null && cursor.moveToFirst()) {
                    student_book_issued = cursor.getInt(cursor.getColumnIndexOrThrow("bookIssued"));
                }

                Log.d("CHECK STUDENT: ", String.valueOf(student_book_issued));

                if (cursor != null) {
                    cursor.close();
                }

                if (book_limit > student_book_issued) {
                    // The book can be issued
                    IssueBookDB issue_db = new IssueBookDB(getApplicationContext());
                    boolean check = false;
                    try {
                        check = issue_db.insertIssueBook(bookID, student_rollno_txt, due_date);
                    } catch (Exception e) {
                        Log.e("INSERT ISSUE BOOK", "Error inserting issue book", e);
                    }

                    Log.d("CHECK ISSUE BOOK: ", String.valueOf(check));

                    if (check) {
                        // Increment Student Issue Book and Decrement Book quantity
                        BooksDB booksDB = new BooksDB(getApplicationContext());
                        StudentDB studentDB = new StudentDB(getApplicationContext());

                        booksDB.decrementBookQuantity(bookID);
                        studentDB.incrementIssuedBooks(student_rollno_txt);

                        Toast.makeText(IssueBookAdmin.this, "Book Issued Successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(IssueBookAdmin.this, "Book cannot be Issued", Toast.LENGTH_SHORT).show();
                    }
                }




            }
        });

        gobackBtn = (Button)findViewById(R.id.issueGoBack);

        gobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(IssueBookAdmin.this, Admin_Dashboard.class);
                //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });

    }






//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}

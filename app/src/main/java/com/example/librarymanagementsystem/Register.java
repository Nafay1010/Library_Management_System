package com.example.librarymanagementsystem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText name, dept, contactNo, rollNo, password;
    Button registerbtn, goBackbtn;
    StudentDB DB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        name = findViewById(R.id.Name);
        dept = findViewById(R.id.Dept);
        contactNo = findViewById(R.id.ContactNo);
        rollNo = findViewById(R.id.RollNo);
        password = findViewById(R.id.password);

        DB = new StudentDB(this);

        registerbtn = findViewById(R.id.Register);
        goBackbtn = findViewById(R.id.Login);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name_txt = name.getText().toString();
                String dept_txt = dept.getText().toString();
                String contactNo_txt = contactNo.getText().toString();
                String rollNo_txt = rollNo.getText().toString();
                String password_txt = password.getText().toString();

                String password_hash = PasswordHasher.sha256String(password_txt);

                Log.d("HASH REGISTER:",password_hash);

                Boolean checkInsertData = DB.insertstudent(rollNo_txt, name_txt, contactNo_txt, dept_txt, password_hash, 0);
                if(checkInsertData==true)
                {
                    Toast.makeText(getApplicationContext(), "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else
                    Toast.makeText(getApplicationContext(), "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
            }
        });

        goBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


    }
}

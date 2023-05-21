package com.example.librarymanagementsystem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.logging.ConsoleHandler;
import android.os.Handler;

import com.example.librarymanagementsystem.ui.profile.ProfileFragment;

import java.util.logging.LogRecord;

public class StartActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // SESSION LOGIC
//                SharedPreferences sp = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
//                String user = sp.getString("User","empty");
//                String pass = sp.getString("Password","empty");
//
//                if (user != "empty" && pass != "empty"){
//                    Intent intent = new Intent(StartActivity.this, Student_Dashboard.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//
//                }
//                else if (user == "admin"){
//                    Intent intent = new Intent(StartActivity.this, Admin_Dashboard.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }
//                else {
//                    Intent intent = new Intent(StartActivity.this, Login.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }

                Intent intent = new Intent(StartActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            }
        }, 3000);
    }
}

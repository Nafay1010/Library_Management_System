package com.example.librarymanagementsystem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText rollNo, password;
    Button registerBtn, loginBtn;
    StudentDB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        rollNo = findViewById(R.id.RollNo);
        password = findViewById(R.id.password);

        registerBtn = findViewById(R.id.Register);
        loginBtn = findViewById(R.id.Login);


        DB = new StudentDB(this);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rollNo_txt = rollNo.getText().toString();
                String password_txt = password.getText().toString();
                String password_hash = PasswordHasher.sha256String(password_txt);

                Log.d("HASH LOGIN: ", password_hash);


                if(rollNo_txt.equals("admin") && password_hash.equals(PasswordHasher.sha256String("admin"))){

                    Intent  i = new Intent(getApplicationContext(), Admin_Dashboard.class);
                    Toast.makeText(Login.this, "Successfully Logged In!", Toast.LENGTH_SHORT).show();

                    SharedPreferences sp = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("User", rollNo_txt);
                    editor.putString("Password", password_hash);
                    editor.putString("Book Limit", "3");
                    editor.commit();

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }
                else
                {
                    Cursor res = DB.getpassword(rollNo_txt);
                    if(res.getCount() == 0){
                        Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //password exist!
                        //comparing passwords
                        while(res.moveToNext()){
                            int index;
                            index = res.getColumnIndexOrThrow("password");
                            String DB_password = res.getString(index);


                            Log.d("PASS ENTERED:", password_hash);
                            Log.d("PASS FROM DB:", DB_password);


                            if(password_hash.equals(DB_password))
                            {
                                Toast.makeText(Login.this, "Successfully Login!", Toast.LENGTH_SHORT).show();
                                // Go to student dashboard
                                Intent i = new Intent(getApplicationContext(), Student_Dashboard.class);

                                SharedPreferences sp = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("User", rollNo_txt);
                                editor.putString("Password", password_hash);
                                editor.commit();

                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("rollNo", rollNo_txt);
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                }

            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });
    }


}
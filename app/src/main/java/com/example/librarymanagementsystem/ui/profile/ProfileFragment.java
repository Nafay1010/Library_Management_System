package com.example.librarymanagementsystem.ui.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.librarymanagementsystem.Login;
import com.example.librarymanagementsystem.R;
import com.example.librarymanagementsystem.StudentDB;
import com.example.librarymanagementsystem.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Button signout;
    SharedPreferences sp;
    String username = "";
    String phone = "";
    String dept = "";

    StudentDB DB;


    @SuppressLint("Range")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Get the rollNO from the shared Preference file
        sp = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        String rollNO = sp.getString("User","******");

        DB = new StudentDB(getContext());
        Cursor cursor = DB.getstudent(rollNO);

        if (cursor != null && cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndex("name"));
//            email = cursor.getString(cursor.getColumnIndex("email"));
            phone = cursor.getString(cursor.getColumnIndex("contactNo"));
            dept = cursor.getString(cursor.getColumnIndex("dept"));
        }

        if (cursor != null) {
            cursor.close();
        }



        final TextView tv_username = binding.userName;
        final TextView tv_rollNO = binding.userRollno;
        final TextView tv_email = binding.userEmail;
        final TextView tv_phone = binding.userPhone;
        final TextView tv_dept = binding.userDept;

        tv_username.setText(username);
        tv_rollNO.setText(rollNO);
        tv_email.setText(rollNO+"@nu.edu.pk");
        tv_phone.setText(phone);
        tv_dept.setText(dept);



        signout = (Button)binding.userSignout;
        binding.userSignout.setOnClickListener(view -> {


            // Removing data from shared preference
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("User");
            editor.remove("Password");
            editor.apply();

            Intent i = new Intent(getActivity(), Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
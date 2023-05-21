package com.example.librarymanagementsystem.ui.view_students;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.librarymanagementsystem.IssueBookDB;
import com.example.librarymanagementsystem.R;
import com.example.librarymanagementsystem.ReturnBookListView;
import com.example.librarymanagementsystem.StudentDB;
import com.example.librarymanagementsystem.databinding.FragmentViewStudentsBinding;

import java.util.ArrayList;

public class ViewStudentsFragment extends Fragment {

    private FragmentViewStudentsBinding binding;

    StudentDB DB;
    ArrayList<String> Names;
    ArrayList<String> RollNo;

    ArrayList<String> Dept;

    ArrayList<String> ContactNo;

    ArrayList<String> Password;

    ArrayList<Integer> BookIssued;

    private CustomBaseAdapter mAdapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentViewStudentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView = binding.ViewStudentsListView;

        DB = new StudentDB(getContext());
        Cursor cursor = DB.getAllstudents();

        Names = new ArrayList<String>();
        RollNo = new ArrayList<String>();
        Dept = new ArrayList<String>();
        ContactNo = new ArrayList<String>();
        Password = new ArrayList<String>();
        BookIssued = new ArrayList<Integer>();


        while (cursor.moveToNext()) {
            RollNo.add(cursor.getString(0));
            Names.add(cursor.getString(1));
            ContactNo.add(cursor.getString(2));
            Dept.add(cursor.getString(3));
            Password.add(cursor.getString(4));
            BookIssued.add(cursor.getInt(5));
        }


        mAdapter = new CustomBaseAdapter();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), RollNo.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    class CustomBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Names.size();
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
                        .inflate(R.layout.activity_view_student_list_view, parent, false);
            }
            TextView NameView = convertView.findViewById(R.id.Name_ListView);
            TextView RollNoView = convertView.findViewById(R.id.RollNo_ListView);
            TextView DeptView = convertView.findViewById(R.id.Dept_ListView);
            Button detailsStudentBtn = convertView.findViewById(R.id.viewBtn_admin);
            Button deleteStudentBtn = convertView.findViewById(R.id.deleteBtn_viewStudent);
            Button returnBookStudentBtn = convertView.findViewById(R.id.returnBookBtn_viewStudent);

            detailsStudentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("Name: "+Names.get(position)+"\n");
                    sb.append("Roll No: "+RollNo.get(position)+"\n");
                    sb.append("Department: "+Dept.get(position)+"\n");
                    sb.append("Contact No: "+ContactNo.get(position)+"\n");
                    sb.append("Password: "+Password.get(position)+"\n");
                    sb.append("Total Book Issued: "+BookIssued.get(position)+"\n");

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Student Details");
                    builder.setMessage(sb.toString());
                    builder.show();
                }
            });

            returnBookStudentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    String rollNo = RollNo.get(position);

                    Log.d("ROLL NO (view student):", rollNo);

                    Intent i = new Intent(getContext(), ReturnBookListView.class);
                    i.putExtra("RollNo", rollNo);
                    startActivity(i);

                }
            });

            deleteStudentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checkstudentdelete = DB.deletestudent(RollNo.get(position));
                    if(checkstudentdelete == true){
                        RollNo.remove(position);
                        Names.remove(position);
                        Dept.remove(position);
                        ContactNo.remove(position);
                        Password.remove(position);
                        BookIssued.remove(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Student Record Deleted!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Student Record NOT Deleted!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            NameView.setText(Names.get(position));
            RollNoView.setText(RollNo.get(position));
            DeptView.setText(Dept.get(position));

            return convertView;
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
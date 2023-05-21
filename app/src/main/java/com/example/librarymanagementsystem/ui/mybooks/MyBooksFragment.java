package com.example.librarymanagementsystem.ui.mybooks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.librarymanagementsystem.BooksDB;
import com.example.librarymanagementsystem.IssueBookDB;
import com.example.librarymanagementsystem.R;
import com.example.librarymanagementsystem.ReturnBookListView;
import com.example.librarymanagementsystem.StudentDB;
import com.example.librarymanagementsystem.databinding.FragmentMybooksBinding;
import com.example.librarymanagementsystem.ui.view_students.ViewStudentsFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyBooksFragment extends Fragment {

    private FragmentMybooksBinding binding;

    IssueBookDB issueBookDB;
    BooksDB booksDB;
    ArrayList<String> BookTitle;
    ArrayList<String> BookID;
    ArrayList<String> DueDate;

    MyBooksAdapter myBooksAdapter;

    SharedPreferences sp;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMybooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView = binding.mybooksListView;

        BookTitle = new ArrayList<String>();

        BookID = new ArrayList<String>();
        DueDate = new ArrayList<String>();

        // Get the rollNO from the shared Preference file
        sp = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        String rollNO = sp.getString("User","******");

        issueBookDB = new IssueBookDB(getContext());
        Cursor cursor = issueBookDB.getMyIssuedBook(rollNO);

        while (cursor.moveToNext()) {
            BookID.add(cursor.getString(0));
            DueDate.add(cursor.getString(1));
        }

        BooksDB booksDB = new BooksDB(getContext());

        for (String id : BookID) {
            Cursor booksC = booksDB.getBook(id);

            if (booksC != null && booksC.moveToFirst() ){
                BookTitle.add(booksC.getString(1));
            }

        }





        myBooksAdapter = new MyBooksAdapter();
        listView.setAdapter(myBooksAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //onclick a
//            }
//        });


        return root;
    }

    class MyBooksAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return BookID.size();
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
                        .inflate(R.layout.activity_my_book_list_view, parent, false);
            }
            TextView BookTitleView = convertView.findViewById(R.id.Mybooks_book_title_ListView);
            TextView BookIDView = convertView.findViewById(R.id.Mybooks_bookID_ListView);
            TextView DueDateView = convertView.findViewById(R.id.Mybooks_due_date_ListView);

            BookTitleView.setText(BookTitle.get(position));
            BookIDView.setText(BookID.get(position));
            DueDateView.setText(DueDate.get(position));


            return convertView;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
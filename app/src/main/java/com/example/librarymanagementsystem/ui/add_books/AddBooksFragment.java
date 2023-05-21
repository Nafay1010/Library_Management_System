package com.example.librarymanagementsystem.ui.add_books;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.librarymanagementsystem.BooksDB;
import com.example.librarymanagementsystem.Login;
import com.example.librarymanagementsystem.R;
import com.example.librarymanagementsystem.databinding.FragmentAddBooksBinding;

public class AddBooksFragment extends Fragment {

    public FragmentAddBooksBinding binding;

    EditText bookID, title, author, genre, quantity;
    BooksDB DB;
    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DB = new BooksDB(getContext());

        bookID = root.findViewById(R.id.bookId);
        title = root.findViewById(R.id.title);
        author = root.findViewById(R.id.author);
        genre = root.findViewById(R.id.genre);
        quantity = root.findViewById(R.id.quantity);


        Button updateBtn = root.findViewById(R.id.updateBookBtn);


        updateBtn.setOnClickListener(view1 -> {
            String bookID_txt = bookID.getText().toString();
            String title_txt = title.getText().toString();
            String author_txt = author.getText().toString();
            String genre_txt = genre.getText().toString();
            int quantity_int = Integer.parseInt(quantity.getText().toString());

            boolean checkBookUpdate = DB.updateBook(bookID_txt, title_txt, author_txt, genre_txt, quantity_int, "false");
            if(checkBookUpdate==true)
            {
                Toast.makeText(getContext(), "Book Updated Successfully!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getContext(), "Books NOT Updated!", Toast.LENGTH_SHORT).show();

        });

        Button addBtn = root.findViewById(R.id.addBookBtn);

        addBtn.setOnClickListener(view1 -> {

            String bookID_txt = bookID.getText().toString();
            String title_txt = title.getText().toString();
            String author_txt = author.getText().toString();
            String genre_txt = genre.getText().toString();
            int quantity_int = Integer.parseInt(quantity.getText().toString());

            boolean checkBookInsert = DB.insertBook(bookID_txt, title_txt, author_txt, genre_txt, quantity_int, "false");
            if(checkBookInsert==true)
            {
                Toast.makeText(getContext(), "New Book Added Successfully", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getContext(), "New Books NOT Added", Toast.LENGTH_SHORT).show();

        });
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
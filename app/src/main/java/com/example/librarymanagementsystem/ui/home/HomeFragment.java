package com.example.librarymanagementsystem.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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

import com.example.librarymanagementsystem.BooksDB;
import com.example.librarymanagementsystem.IssueBookAdmin;
import com.example.librarymanagementsystem.IssueBookDB;
import com.example.librarymanagementsystem.Login;
import com.example.librarymanagementsystem.R;
import com.example.librarymanagementsystem.StartActivity;
import com.example.librarymanagementsystem.databinding.FragmentHomeBinding;
import com.example.librarymanagementsystem.ui.bookmark.BookmarkFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    BooksDB DB;

    ArrayList<String> bookID;

    ArrayList<String> title;

    ArrayList<String> author;

    ArrayList<String> genre;

    ArrayList<Integer> quantity;

    ArrayList<String> soldout;

    private CustomBaseAdapter mAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView = binding.ViewHomeListView;

        bookID = new ArrayList<String>();
        title = new ArrayList<String>();
        author = new ArrayList<String>();
        genre = new ArrayList<String>();
        quantity = new ArrayList<Integer>();
        soldout = new ArrayList<String>();

        DB = new BooksDB(getContext());

        Cursor cursor = DB.getAllBooks();
        if(cursor == null){
            title.add("Empty BookShelf");
            author.add(" ");
            genre.add(" ");
        }
        else{
            while (cursor.moveToNext()){
                bookID.add(cursor.getString(0));
                title.add(cursor.getString(1));
                author.add(cursor.getString(2));
                genre.add(cursor.getString(3));
                quantity.add(cursor.getInt(4));
                soldout.add(cursor.getString(5));
            }

            mAdapter = new CustomBaseAdapter();
            listView.setAdapter(mAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getContext(), title.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return root;
    }

    class CustomBaseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return title.size();
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
                        .inflate(R.layout.activity_book_list_view, parent, false);
            }
            TextView TitleView = convertView.findViewById(R.id.Book_Title_List_View);
            TextView AuthorView = convertView.findViewById(R.id.Book_Author_List_View);
            Button ViewBtn = convertView.findViewById(R.id.viewBtn_admin);
            Button IssueBtn = convertView.findViewById(R.id.issueBtn_admin);
            Button DeleteBtn = convertView.findViewById(R.id.deleteBtn_admin);

            DeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checkbookdelete = DB.deleteBook(bookID.get(position));
                    if(checkbookdelete == true){
                        title.remove(position);
                        author.remove(position);
                        bookID.remove(position);
                        genre.remove(position);
                        quantity.remove(position);
                        soldout.remove(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Book Deleted!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Book NOT Deleted!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            IssueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ID = bookID.get(position);
                    int quantity;

                    //Getting book quantity
                    BooksDB db = new BooksDB(getContext());
                    Cursor cursor = db.getBook(ID);

                    Log.d("ISSUE BOOK:", ID);

                    if (cursor !=null && cursor.moveToFirst()){
                        quantity = cursor.getColumnIndexOrThrow("quantity");

//                        Log.d("QUANTITY:", String.valueOf(quantity));

                        if (quantity > 0){
                            Intent intent = new Intent(getContext(), IssueBookAdmin.class);
                            intent.putExtra("BookID", ID);
                            startActivity(intent);
                        }
                        //Checking the quantity if equals to zero return a toast
                        else{
                            Toast.makeText(getContext(), "Book Not Available!", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            });


            ViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("ID: "+bookID.get(position)+"\n");
                    sb.append("Name: "+title.get(position)+"\n");
                    sb.append("Author: "+author.get(position)+"\n");
                    sb.append("Genre: "+genre.get(position)+"\n");
                    sb.append("Books Left: "+quantity.get(position)+"\n");
                    sb.append("Soldout: "+soldout.get(position));

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Book Details");
                    builder.setMessage(sb.toString());
                    builder.show();
                }
            });

            TitleView.setText(title.get(position));
            AuthorView.setText(author.get(position));

            return convertView;
        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }
}
package com.example.librarymanagementsystem.ui.bookmark;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.librarymanagementsystem.BookmarkDB;
import com.example.librarymanagementsystem.R;
import com.example.librarymanagementsystem.Student_Dashboard;
import com.example.librarymanagementsystem.databinding.FragmentBookmarkBinding;
import com.example.librarymanagementsystem.ui.home_student.HomeStudentFragment;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    private FragmentBookmarkBinding binding;

    BookmarkDB bookmarkDB;
    private String rollNo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the rollNo value from the Intent
        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            rollNo = intent.getStringExtra("rollNo");
            Log.d("rollno", rollNo);
        }
    }

    ImageButton bookmark;

    ArrayList<String> bookID;

    ArrayList<String> title;

    ArrayList<String> author;

    ArrayList<String> genre;

    ArrayList<Integer> quantity;

    ArrayList<String> soldout;


    private CustomBaseAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentBookmarkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final ListView listView = binding.ViewBookmarkListView;
        bookmarkDB = new BookmarkDB(getContext());

        Cursor cursor = bookmarkDB.getAllBookmarks(rollNo);
        title = new ArrayList<String>();
        author = new ArrayList<String>();
        bookID = new ArrayList<String>();
        genre = new ArrayList<String>();
        quantity = new ArrayList<Integer>();
        soldout = new ArrayList<String>();

        if(cursor.getCount() == 0){
            title.add("Empty BookShelf");
            author.add(" ");
        }
        else{


            while (cursor.moveToNext()){
                bookID.add(cursor.getString(1));
                title.add(cursor.getString(2));
                author.add(cursor.getString(3));
                genre.add(cursor.getString(4));
                quantity.add(cursor.getInt(5));
                soldout.add(cursor.getString(6));
            }

            mAdapter = new CustomBaseAdapter();
            listView.setAdapter(mAdapter);

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
                        .inflate(R.layout.activity_book_bookmark_list_view, parent, false);
            }
            TextView TitleView = convertView.findViewById(R.id.Book_Title_List_View);
            TextView AuthorView = convertView.findViewById(R.id.Book_Author_List_View);
            Button IssueBtn = convertView.findViewById(R.id.issueBtn);
            Button ViewBtn = convertView.findViewById(R.id.viewBtn_student);
            ImageButton bookmark = convertView.findViewById(R.id.BookmarkRemove);


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

            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checkbookmarkdelete = bookmarkDB.deleteBookmark(bookID.get(position), rollNo);
                    if(checkbookmarkdelete == true){
                        title.remove(position);
                        author.remove(position);
                        bookID.remove(position);
                        genre.remove(position);
                        quantity.remove(position);
                        soldout.remove(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Bookmark Deleted!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Bookmark NOT Deleted!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            IssueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Buy Button Clicked for " + title.get(position), Toast.LENGTH_SHORT).show();
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
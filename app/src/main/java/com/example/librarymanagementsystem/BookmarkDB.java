package com.example.librarymanagementsystem;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookmarkDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "LMS.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Bookmark";
    SQLiteDatabase db;

    public BookmarkDB(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, bookID TEXT, title TEXT, author TEXT, genre TEXT, quantity INTEGER, soldout TEXT, rollNo TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(DB);
    }

    public boolean tableExists(String tableName) {
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public boolean insertBookmark(String bookID, String title, String author, String genre, int quantity, String soldout, String rollNo){
        if(!tableExists(TABLE_NAME)) {
            onCreate(db);
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE bookID = ? AND rollNo = ?", new String[]{bookID, rollNo});
        if(cursor.getCount() == 0){
            //record doesnt exist
            ContentValues contentValues = new ContentValues();
            contentValues.put("bookID", bookID);
            contentValues.put("title", title);
            contentValues.put("author", author);
            contentValues.put("genre", genre);
            contentValues.put("quantity", quantity);
            contentValues.put("soldout", soldout);
            contentValues.put("rollNo", rollNo);
            long result = db.insert(TABLE_NAME, null, contentValues);
            if(result==-1)
                return false;
            else
                return true;
        }
        else
            return false;

    }

    public boolean deleteBookmark(String bookID, String rollNo){
        if(!tableExists(TABLE_NAME)) {
            onCreate(db);
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE bookID = ? AND rollNo = ?", new String[]{bookID, rollNo});
        if(cursor.getCount() > 0){
            long result = db.delete(TABLE_NAME, "bookID = ? AND rollNo = ?", new String[]{bookID, rollNo});
            return result != -1;
        } else {
            return false;
        }
    }

    public Cursor getAllBookmarks(String rollNo){
        if(!tableExists(TABLE_NAME)) {
            onCreate(db);
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where rollNo = ?", new String[]{rollNo});
        return cursor;
    }


}
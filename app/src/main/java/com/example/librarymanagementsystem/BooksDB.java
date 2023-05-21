package com.example.librarymanagementsystem;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class BooksDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "LMS.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Books";
    SQLiteDatabase db;

    public BooksDB(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        Log.d("BooksDB", "Creating database...");
        try {
            DB.execSQL("CREATE table " + TABLE_NAME + " (bookID TEXT PRIMARY KEY, title TEXT, author TEXT, genre TEXT, quantity INTEGER, soldout String)");
            Log.d("BooksDB", "Database created successfully.");
        } catch (SQLException e) {
            Log.e("BooksDB", "Error creating database", e);
        }
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


    public boolean insertBook(String bookID, String title, String author, String genre, int quantity, String soldout){
        if(!tableExists(TABLE_NAME)) {
            onCreate(db);
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("bookID", bookID);
        contentValues.put("title", title);
        contentValues.put("author", author);
        contentValues.put("genre", genre);
        contentValues.put("quantity", quantity);
        contentValues.put("soldout", soldout);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }

    public boolean updateBook(String bookId, String title, String author, String genre, int quantity, String soldout){
        if(!tableExists(TABLE_NAME)){
            onCreate(db);
            return false;
        }
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", title);
        contentValues.put("author", author);
        contentValues.put("genre", genre);
        contentValues.put("quantity", quantity);
        contentValues.put("soldout", soldout);

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE bookId = ?", new String[]{bookId});
        if(cursor.getCount() > 0){
            long result = db.update(TABLE_NAME, contentValues, "bookId=?", new String[]{bookId});
            return result != -1;
        } else {
            return false;
        }
    }

    public boolean deleteBook(String bookId){
        if(!tableExists(TABLE_NAME)){
            onCreate(db);
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE bookId = ?", new String[]{bookId});
        if(cursor.getCount() > 0){
            long result = db.delete(TABLE_NAME, "bookId=?", new String[]{bookId});
            return result != -1;
        } else {
            return false;
        }
    }


    public Cursor getBook(String bookId){
        if(!tableExists(TABLE_NAME)){
            onCreate(db);
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where bookID = ?", new String[]{bookId});
        return cursor;
    }


    public Cursor getAllBooks(){
        if(!tableExists(TABLE_NAME)){
            onCreate(db);
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }

    @SuppressLint("Range")
    public void decrementBookQuantity(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!tableExists(TABLE_NAME)) {
            onCreate(db);
            return;
        }
        Cursor cursor = db.rawQuery("SELECT quantity FROM " + TABLE_NAME + " WHERE bookID = ?", new String[]{bookId});
        int quantity = 0;
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity - 1);
        db.update(TABLE_NAME, values, "bookID = ?", new String[]{bookId});
    }

    @SuppressLint("Range")
    public void incrementBookQuantity(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!tableExists(TABLE_NAME)) {
            onCreate(db);
            return;
        }
        Cursor cursor = db.rawQuery("SELECT quantity FROM " + TABLE_NAME + " WHERE bookID = ?", new String[]{bookId});
        int quantity = 0;
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity + 1);
        db.update(TABLE_NAME, values, "bookID = ?", new String[]{bookId});
    }


//    public void decrementBookQuantity(String bookId){
//        if(!tableExists(TABLE_NAME)){
//            onCreate(db);
//            return;
//        }
//        Cursor cursor = db.rawQuery("UPDATE "+ TABLE_NAME + " SET"+ " quantity"+ "="+" quantity" + " -1"+" WHERE bookID = ?", new String[]{bookId});
//    }

}
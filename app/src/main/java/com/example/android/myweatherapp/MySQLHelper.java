package com.example.android.myweatherapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MySQLHelper extends SQLiteOpenHelper {

    String TABLE_NAME = "tblLocation";

    String db_create_query = "" +
            "CREATE TABLE " + TABLE_NAME +"(" +
            " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " title TEXT " +")" +
            "";


    public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(db_create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void inserToDB(String name) {

        try {
            String insertQuery = "INSERT INTO " + TABLE_NAME +
                    "(title)" +
                    "VALUES( '" + name + "' )";

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(insertQuery);
            db.close();

        }catch(Exception e){

        }


    }

//    public void updateToDB(Search movie) {
//
//
//        try {
//            String updateQuery = "UPDATE " + TABLE_NAME + "SET " +
//                    "title ='" + movie.getTitle() + "'" +
//                    "year ='" + movie.getYear() + "'" +
//                    "type ='" + movie.getType() + "'" +
//                    "poster ='" + movie.getPoster() + "'" +
//                    " WHERE ImdbID='" + movie.getImdbID() + "'" ;
//
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.execSQL(updateQuery);
//            db.close();
//
//        }catch(Exception e){
//
//        }
//
//
//    }


    public Boolean isExist(String name){

        Boolean blResult = false;

        SQLiteDatabase db = this. getReadableDatabase();

        Cursor cursor = null;
        String sql ="SELECT _id FROM "+TABLE_NAME+" WHERE title='"+name+"'";
        cursor= db.rawQuery(sql,null);
        Log.i("MYTAG", "Cursor Count : " + cursor.getCount());

        if(cursor.getCount()>0){
            //PID Found
            blResult = true;
        }else{
            //PID Not Found
            blResult = false;
        }
        cursor.close();
        return  blResult;


    }

    public List<String> getLocations() {

        List<String> items= new ArrayList<>();

        SQLiteDatabase db = this. getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title  from " + TABLE_NAME, null);

        while (cursor.moveToNext()) {
            items.add(cursor.getString(0));
        }

        db.close();
        return items;
    }
}
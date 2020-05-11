package com.terryyessfung.whatsins.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "WHATSINS.DB";
    public static final String TABLE_NAME = "USER";
   // cloumns
    public static final String _id = "_id";
    public static final String token = "token";




    public DatabaseHelper(Context context) {
        super(context, DB_NAME ,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE " + TABLE_NAME + "(" + _id + " TEXT PRIMARY KEY, " + token + " TEXT NOT NULL);" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

}

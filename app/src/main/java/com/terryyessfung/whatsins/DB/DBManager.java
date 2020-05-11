package com.terryyessfung.whatsins.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private static DBManager mDBManager;
    private static DatabaseHelper mDatabaseHelper;
    private static SQLiteDatabase mDatabase;


//    private DBManager open() throws SQLException{
//        mDatabaseHelper = new DatabaseHelper(mContext);
//        mDatabase = mDatabaseHelper.getWritableDatabase();
//        return this;
//    }
    public static DBManager getInstance(Context context) throws SQLException{
        mDBManager = new DBManager();
        mDatabaseHelper = new DatabaseHelper(context);
        mDatabase = mDatabaseHelper.getWritableDatabase();
        return mDBManager;
    }

    public String getUid(){
        Cursor cursor =  mDBManager.fetchUserToken();
        if(cursor.getCount() > 0){
            String uid =  cursor.getString(0);
            mDBManager.close();
            return uid;
        }
       return null;
    }

    private void close(){
        mDatabaseHelper.close();
    }

    public void insertUserToken(String uid, String token){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper._id,uid);
        contentValues.put(DatabaseHelper.token, token);
        mDatabase.insert(DatabaseHelper.TABLE_NAME,null,contentValues);
    }

    private Cursor fetchUserToken(){
        String[] colums = new String[]{DatabaseHelper._id,DatabaseHelper.token};
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_NAME, colums,null,null,null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    private int updateUserToken(String uid, String token){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.token,token);
        //int i = mDatabase.update(DatabaseHelper.TABLE_NAME,contentValues,DatabaseHelper._id + " = "+ uid,null);
        int i = mDatabase.update(DatabaseHelper.TABLE_NAME,contentValues,DatabaseHelper._id + " = "+ uid,null);
        return i;
    }

    public void deleteUserToken(String uid){
        mDatabase.delete(DatabaseHelper.TABLE_NAME,DatabaseHelper._id+" = " + "'" +  uid + "'", null);

    }


}

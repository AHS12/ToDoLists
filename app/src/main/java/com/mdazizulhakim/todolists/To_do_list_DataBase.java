package com.mdazizulhakim.todolists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MD AZIZUL HAKIM on 04/05/2017.
 */

public class To_do_list_DataBase extends SQLiteOpenHelper {

    public To_do_list_DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "To_do_Database";
    private static final String TABLE_NAME_UNDONE = "Undone";
    private static final String TABLE_NAME_DONE = "Done";


    private static final String COLUMN1 = "ID";
    private static final String COLUMN2 = "TASK";
    private static final String COLUMN3 = "DATE";


    @Override
    public void onCreate(SQLiteDatabase db) {

        String query_table_undone = "CREATE TABLE " + TABLE_NAME_UNDONE + "(" + COLUMN1 + " INTEGER PRIMARY KEY," + COLUMN2 + " TEXT NOT NULL," + COLUMN3 + " LONG)";
        String query_table_done = "CREATE TABLE " + TABLE_NAME_DONE + " (" + COLUMN1 + " INTEGER," + COLUMN2 + " TEXT NOT NULL," + COLUMN3 + " LONG)";


        try {
            db.execSQL(query_table_undone);
        }catch (SQLException e){
            e.printStackTrace();
        }

        try {
            db.execSQL(query_table_done);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query_drop_undone = "DROP IF EXISTS " + TABLE_NAME_UNDONE;
        String query_drop_done = "DROP IF EXISTS " + TABLE_NAME_DONE;
        db.execSQL(query_drop_undone);
        db.execSQL(query_drop_done);
        onCreate(db);

    }

    public boolean addToTable_Undone(String ID, String TASK, Long DATE) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN1, ID);
        values.put(COLUMN2, TASK);
        values.put(COLUMN3, DATE);

        long check = db.insert(TABLE_NAME_UNDONE, null, values);

        if (check == -1) return false;
        else return true;
    }

    public boolean addToTable_done(String ID, String TASK, Long DATE) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN1, ID);
        values.put(COLUMN2, TASK);
        values.put(COLUMN3, DATE);

        //copy query = "INSERT INTO done SELECT *from undone WHERE id = 2"

        long check = db.insert(TABLE_NAME_DONE, null, values);

        if (check == -1) return false;
        else return true;
    }


    public Cursor Display_Table_Undone() {
        SQLiteDatabase db = getReadableDatabase();
        //query = "SELECT *FROM " + TABLE_NAME+" ORDER BY DATE DESC";
        Cursor cursor = db.query(TABLE_NAME_UNDONE, new String[]{COLUMN1, COLUMN2, COLUMN3}, null, null, null, null, COLUMN3 + " DESC");
        //db.close();
        return cursor;
    }

    public Cursor Display_Table_done() {
        SQLiteDatabase db = getReadableDatabase();
        //query = "SELECT *FROM " + TABLE_NAME+" ORDER BY DATE DESC";
        Cursor cursor = db.query(TABLE_NAME_DONE, new String[]{COLUMN1, COLUMN2, COLUMN3}, null, null, null, null, COLUMN3 + " DESC");
        //db.close();
        return cursor;
    }

    public boolean Delete_Table_undone(String ID){
        SQLiteDatabase db = getWritableDatabase();
        int check = db.delete(TABLE_NAME_UNDONE,"ID = ?",new String[]{ID});

        if (check > 0) return true;
        else return false;
    }

    public void Delete_Table_done(){
        SQLiteDatabase db = getWritableDatabase();

      //  db.delete(TABLE_NAME_DONE,null,null);
        //db.execSQL("DELETE * FROM "+TABLE_NAME_DONE);
        db.execSQL("DELETE FROM "+TABLE_NAME_DONE);
        db.execSQL("VACUUM");//this will clear all allocated spaces.
    }




}

package com.example.listview_sample;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "PersonDB";
    public static final String TB_NAME = "PersonTB";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "AGE";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +TB_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, AGE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, int age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, name);
        cv.put(COL3, age);
        long res = db.insert(TB_NAME, null, cv);
        return  res != -1;
    }

    public ArrayList<PersonInfo> getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PersonInfo> arrayList = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM " + TB_NAME, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int age = cursor.getInt(2);

            PersonInfo pi = new PersonInfo(id, name, age);
            arrayList.add(pi);
        }
        return arrayList;
    }

    public boolean updateData(int id, String name, int age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL1, id);
        cv.put(COL2, name);
        cv.put(COL3, age);
        db.update(TB_NAME, cv, COL1+" = ?", new String[]{String.valueOf(id)});
        return true;
    }

    public Integer deleteRow(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TB_NAME,COL1+" = ?", new String[]{String.valueOf(id)});
    }

}

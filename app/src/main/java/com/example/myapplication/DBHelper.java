package com.example.myapplication;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "contactDb";
    public static final String TABLE_AVTO = "avtomobil";
    public static final String TABLE_POLZOVATELI = "polzovateli";

    public static final String KEY_LOGIN = "login";
    public static final String KEY_PAROL = "parol";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAZVANIE = "nazvanie";
    public static final String KEY_CENA = "cena";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_AVTO + "(" + KEY_ID
                + " integer primary key," + KEY_NAZVANIE + " text," + KEY_CENA + " text"  + ")");

        db.execSQL("create table " + TABLE_POLZOVATELI + "(" + KEY_ID
                + " integer primary key," + KEY_LOGIN + " text," + KEY_PAROL + " text"  + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_AVTO);
        db.execSQL("drop table if exists " + TABLE_POLZOVATELI);

        onCreate(db);

    }
}
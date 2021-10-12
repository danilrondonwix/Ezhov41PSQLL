package com.example.myapplication;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "contactDb";
    public static final String TABLE_AVTO = "avtomobil";

    public static final String KEY_ID = "_id";
    public static final String KEY_MARKA = "marka";
    public static final String KEY_KYZOV = "kyzov";
    public static final String KEY_GOD = "god";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_AVTO + "(" + KEY_ID
                + " integer primary key," + KEY_MARKA + " text," + KEY_KYZOV + " text," +  KEY_GOD + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_AVTO);

        onCreate(db);

    }
}
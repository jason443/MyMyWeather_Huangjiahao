package com.huangjiahao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASUS on 2016/7/23.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static MyDatabaseHelper instance = null;

    public static final String CREATE_PROVINCE = "create table Province(" +
            "id integer primary key autoincrement," +
            "province text)";

    public static final String CREATE_CITY = "create table City(" +
            "id integer primary key autoincrement," +
            "city text," +
            "province text)";

    public static MyDatabaseHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        if(instance == null) {
            synchronized (MyDatabaseHelper.class) {
                if(instance == null) {
                    instance = new MyDatabaseHelper(context, name, factory, version);
                }
            }
        }
        return instance;
    }

    private MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}

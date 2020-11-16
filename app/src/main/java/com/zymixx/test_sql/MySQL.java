package com.zymixx.test_sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MySQL extends SQLiteOpenHelper {

    private  static final int VERSION = 1;
    private  static final String DATABASES_NAME = "my_db";

    public  MySQL (Context context) {
        super(context, DATABASES_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE my_DB (id INTEGER primary key autoincrement, " +
                "text varchar (250), titleNote varchar (22) DEFAULT 'Title')");
        db.execSQL("CREATE TABLE config_DB (name VARCHAR (30), corent INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

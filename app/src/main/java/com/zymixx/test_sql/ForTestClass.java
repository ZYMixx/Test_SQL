package com.zymixx.test_sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class ForTestClass extends AppCompatActivity {
    MySQL mySQL;
    SQLiteDatabase db;



    public void main(Context context){

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd hh:mm");



        try {
            MySQL mySQL = new MySQL(context);
            SQLiteDatabase db = mySQL.getWritableDatabase();
            db.execSQL("INSERT INTO recycle_bin_DB VALUES (null, 'запись из трая 1', 'без тайтла', '"+formatForDateNow.format(date)+"')");
            db.execSQL("INSERT INTO recycle_bin_DB VALUES (null, 'запись из трая 2', 'без тайтла', '"+formatForDateNow.format(date)+"')");
            db.execSQL("INSERT INTO recycle_bin_DB VALUES (null, 'запись из трая 3', 'без тайтла', '"+formatForDateNow.format(date)+"')");
            db.execSQL("INSERT INTO my_DB VALUES (null, 'запись из трая 4', 'без тайтла', '"+formatForDateNow.format(date)+"')");
            db.execSQL("INSERT INTO my_DB VALUES (null, 'запись из трая 5', 'без тайтла', '"+formatForDateNow.format(date)+"')");
            db.execSQL("UPDATE config_DB SET corent = 1 where name = 'fust_dealite'");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

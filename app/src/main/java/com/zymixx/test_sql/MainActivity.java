package com.zymixx.test_sql;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    MySQL mySQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context;
        runDB();
    }

    public void runDB() {
        TextView textView = findViewById(R.id.textForDB);
        mySQL = new MySQL(this);

        Log.e("XLog", "My changes 1");
        Log.e("XLog", "My changes 2");

        try {
            SQLiteDatabase db = mySQL.getWritableDatabase();
            db.execSQL("CREATE TABLE my_DB (id INTEGER primary key autoincrement, " +
                    "text varchar (250), titleNote varchar (22) DEFAULT 'Title', createTime varchar (24))");
            db.execSQL("CREATE TABLE config_DB (name VARCHAR (30), corent INTEGER)");
            Log.e("XLog", "My changes 3");
            db.execSQL("CREATE TABLE recycle_bin_DB (id INTEGER primary key autoincrement, " +
                    "text varchar (250), titleNote varchar (22) DEFAULT 'Title', createTime varchar (24)) ");
            db.close();
        } catch (Exception ex) {

            Log.e("XLog", "My changes 4");
            ex.printStackTrace();   //  TODO Так не делай. Дог будет не в потоке Error а в потоке Info/
            Log.e("XLog", "Error", ex); // TODO Лучше так.
            ControllerLog.error(ex);    // TODO Так идеально, если у тебя отдельный класс отвечает за логирование. Там сможешь кешировать логи и выводить их на экран приложения или отправлять на сервер например.
        }

        try {
            Log.e("XLog", "My changes 5");
            SQLiteDatabase db = mySQL.getWritableDatabase();
            db.execSQL("DROP TABLE my_DB");
            db.execSQL("DROP TABLE config_DB");
            db.execSQL("DROP TABLE recycle_bin_DB");
            db.execSQL("CREATE TABLE my_DB (id INTEGER primary key autoincrement, " +
                    "text varchar (250), titleNote varchar (22) DEFAULT 'Title', createTime varchar (24))");
            db.execSQL("CREATE TABLE config_DB (name VARCHAR (30), corent INTEGER)");
            db.execSQL("CREATE TABLE recycle_bin_DB (id INTEGER primary key autoincrement, " +
                    "text varchar (250), titleNote varchar (22) DEFAULT 'Title', createTime varchar (24)) ");
            db.execSQL("INSERT INTO config_DB values ('font', 18 )");
            db.execSQL("INSERT INTO config_DB values ('auto_save', 0 )");
            db.execSQL("INSERT INTO config_DB values ('fust_dealite', 0 )");
            System.out.println("СОЗДАЛА БДБДБДБ");
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ForTestClass forTestClass = new ForTestClass();
        forTestClass.main(this);
    }
    public void clic_boton(View view) {
        Intent intent = new Intent(MainActivity.this, Scroll.class);
        startActivity(intent);
    }

    public void forTestMetod (){}
}
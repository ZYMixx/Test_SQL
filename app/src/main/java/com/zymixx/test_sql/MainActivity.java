package com.zymixx.test_sql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    MySQL mySQL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context;

        runDB();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1: System.out.println("Нажат 111");
            case 2: System.out.println("Нажат 222");
            case 3: System.out.println("Нажат 333");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void runDB(){
        TextView textView = findViewById(R.id.textForDB);
        mySQL = new MySQL(this);

        try {

            SQLiteDatabase db = mySQL.getWritableDatabase();
            db.execSQL("DROP TABLE my_DB");
            db.execSQL("CREATE TABLE my_DB (id INTEGER primary key autoincrement, " +
                    "text varchar (250), titleNote varchar (22) DEFAULT 'Title')");
           // db.execSQL("INSERT INTO my_DB values (null, 'Текс для первой записи' )");
           // db.execSQL("INSERT INTO my_DB values (null, 'ого это вторая запись' )");
           // db.execSQL("INSERT INTO my_DB values (null, 'Все любят троичку а а а' )");
            String[] columns = {"text"};
            Cursor cursor = db.query("my_DB", columns, null, null, null, null, null);

            cursor.moveToFirst();
            textView.setText(cursor.getString(0));
            for (int i = 0; i < 10 ; i++) {
                System.out.println(cursor.getCount());
            }
            cursor.close();

        }catch (Exception ex) {ex.printStackTrace();}
    }

    public void clic_boton (View view){
        Intent intent = new Intent(MainActivity.this, Scroll.class);
      startActivity(intent);

    }
}
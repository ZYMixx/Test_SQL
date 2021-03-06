package com.zymixx.test_sql;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

public class NotesTextSpace extends AppCompatActivity {
    MySQL mySQL;
    SQLiteDatabase db;
    EditText editText;
  //  EditText editTextForListenrt;
    int textSizeFromSQL;

    //для смены заголовка
    EditText themeChanger;
    String newTitle = "nullTitle";
    FrameLayout themeChanger_frame;

    FrameLayout into_notes_frameLayout;
    FrameLayout notes_frameLayout;

    NestedScrollView nestedScrollView;

    int countLVLfromScroll;
    String textFromSQL = "данные не полученны";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notestextspace);
        countLVLfromScroll = (int) getIntent().getSerializableExtra("COUNT LVL");
        createGUInotes ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println(item.getItemId());
       switch (item.getItemId()){
           case (android.R.id.home):
               mySQL = new MySQL(NotesTextSpace.this);
               try {
                   db = mySQL.getWritableDatabase();
                   String column [] = {"id", "text", "titleNote"};
                   Cursor cursor = db.query("my_DB", column, "id = " + countLVLfromScroll, null,null,null,null);
                   cursor.moveToFirst();
                   textFromSQL = cursor.getString(1);
                   cursor.close();
               }catch (Exception ex) {ex.printStackTrace();}

               if (textFromSQL.equals(String.valueOf(editText.getText()))){
                   return false;
               } else {
                   if (Scroll.auto_save == 1) {
                       onSaveClicked();
                       this.finish();
                   } else {
                   AlertDialog.Builder builder = new AlertDialog.Builder(NotesTextSpace.this);
                   builder.setMessage("Сохранить изменения?");
                   builder.setPositiveButton("SAVE",  new OnExitSaveClicListener());
                   builder.setNegativeButton("CANCEL",  new OnExitSaveClicListener());
                   builder.create();
                   builder.show();}
               }
               return true;

           case (R.id.save_menu_item): onSaveClicked ();
           this.finish();
           return true;
           case (R.id.change_title): changeTitleCkicked (); return true;
       }
        return super.onOptionsItemSelected(item);
    }

    private class OnExitSaveClicListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case (-1):
                    onSaveClicked();
                    NotesTextSpace.this.finish();
                    System.out.println("Нажато Уес");
                    break;
                    //выходим с сохранением
                case (-2): NotesTextSpace.this.finish(); //выходим без сохранения
                    System.out.println("Нажато Ноу");
                    break;
            }

        }
    }


    @Override
    public void onBackPressed() {
        mySQL = new MySQL(NotesTextSpace.this);
        try {
            db = mySQL.getWritableDatabase();
            String column [] = {"id", "text", "titleNote"};
            Cursor cursor = db.query("my_DB", column, "id = " + countLVLfromScroll, null,null,null,null);
            cursor.moveToFirst();
            textFromSQL = cursor.getString(1);
            cursor.close();
        }catch (Exception ex) {ex.printStackTrace();}

        if (textFromSQL.equals(String.valueOf(editText.getText()))){

            this.finish();
        } else {
            if (Scroll.auto_save == 1) {
                onSaveClicked();
                this.finish();}
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotesTextSpace.this);
                builder.setMessage("Сохранить изменения?");
                builder.setPositiveButton("SAVE", new OnExitSaveClicListener());
                builder.setNegativeButton("CANCEL", new OnExitSaveClicListener());
                builder.create();
                builder.show();
                this.removeDialog(builder.hashCode());
            }
        }
    }

    public void createGUInotes (){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nestedScrollView = findViewById(R.id.notes_NestedScrollView);

        mySQL = new MySQL(this);
        try {
            db = mySQL.getWritableDatabase();
            String column [] = {"id", "text", "titleNote"};
            String textSize [] = {"corent"};
            Cursor cursor = db.query("my_DB", column, "id = " + countLVLfromScroll, null,null,null,null);
            cursor.moveToFirst();
            Cursor cursorTextSize = db.query("config_DB", textSize, "name = 'font'", null, null, null, null);
            cursorTextSize.moveToFirst();
            textSizeFromSQL = cursorTextSize.getInt(0);
            textFromSQL = cursor.getString(1);
            getSupportActionBar().setTitle(cursor.getString(2));
            cursor.close();
        }catch (Exception ex) {ex.printStackTrace();}


       into_notes_frameLayout = new FrameLayout(this);
       into_notes_frameLayout = (FrameLayout) findViewById(R.id.into_notes_frameLayout);
      // editTextForListenrt = new EditText(this);
       editText = new EditText(this);
       editText.setLayoutParams(Scroll.getParms(0,0,0,0));

       if (textFromSQL.equals("новая запись")) {editText.setHint("Новая запись");
       } else {
       editText.setText(textFromSQL);
       editText.setSelection(textFromSQL.length()); }

       into_notes_frameLayout.addView(editText);
       editText.setTextColor(Color.BLACK);
       editText.setTextSize(textSizeFromSQL);
       editText.setBackgroundColor(80000000); //делает прозрачный фон чтобы убрать синию линию


    }

    public void onSaveClicked (){
        String textString = editText.getText().toString();

        mySQL = new MySQL(NotesTextSpace.this);
        try {
            db = mySQL.getWritableDatabase();
            db.execSQL("UPDATE my_DB SET text = '" + textString + "' where id = " + countLVLfromScroll);
            if (newTitle != "nullTitle") {
            db.execSQL("UPDATE my_DB SET titleNote = '" + newTitle + "' where id = " + countLVLfromScroll);}
        }catch (Exception ex) {ex.printStackTrace();}
    }
    public void changeTitleCkicked () {
        themeChanger = new EditText(this);
        themeChanger_frame = new FrameLayout(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Сменить заголовок");
        themeChanger_frame.setPadding(50,0,50,0);
        themeChanger_frame.addView(themeChanger);
        Typeface typeface = Typeface.DEFAULT_BOLD;
        themeChanger.setTypeface(typeface);
        themeChanger.setLayoutParams(Scroll.getParms(0,0,0,0)); //делает её адекватного вида
        builder.setView(themeChanger_frame);
        builder.setPositiveButton("SAVE",  new OnTitleChangeClicListener());
        builder.setNegativeButton("CANCEL",  new OnTitleChangeClicListener());




        builder.show();

      



    }


    public class OnTitleChangeClicListener implements DialogInterface.OnClickListener  {


        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case (-1): getSupportActionBar().setTitle(themeChanger.getText());
                    newTitle = String.valueOf(themeChanger.getText());

                    mySQL = new MySQL(NotesTextSpace.this);
                    try {
                        db = mySQL.getWritableDatabase();
                        if (newTitle != "nullTitle") {
                            db.execSQL("UPDATE my_DB SET titleNote = '" + newTitle + "' where id = " + countLVLfromScroll);}
                        String column [] = {"id", "text", "titleNote"};
                        Cursor cursor = db.query("my_DB", column, "id = " + countLVLfromScroll, null,null,null,null);
                        cursor.moveToFirst();
                        getSupportActionBar().setTitle(cursor.getString(2));
                        cursor.close();
                    }catch (Exception ex) {ex.printStackTrace();}
                    break;
                case (-2):break;
            }

        }
    }



    /* layout_marginLeft*/


}

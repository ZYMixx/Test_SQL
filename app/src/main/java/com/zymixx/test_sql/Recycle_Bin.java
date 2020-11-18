package com.zymixx.test_sql;

import android.animation.Animator;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Recycle_Bin extends AppCompatActivity {

    ArrayMap<Button, Integer> button_id_map = new ArrayMap<>();

    LinearLayout inFrame_frameLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_bin_layout);
        makeGUI ();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rb_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MySQL mySQL = new MySQL(Recycle_Bin.this);
        SQLiteDatabase db = mySQL.getWritableDatabase();
        switch (item.getItemId()){
            case R.id.delete_all:
                        try {
                            db.execSQL("DELETE FROM recycle_Bin_DB where id > 0" );
                            db.close();
                        } catch (Exception e) { e.printStackTrace(); }
                        Recycle_Bin.this.recreate();
        }
        return super.onOptionsItemSelected(item);
    }

    public void makeGUI (){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.recycle_bin);

        inFrame_frameLayout = findViewById(R.id.recycle_bin_frameLayout_inFrame);
        MySQL mySQL = new MySQL(this);
        SQLiteDatabase db = mySQL.getWritableDatabase();
        int id_recycle = 2;
        try {
           String [] column = {"id", "text", "titleNote ", "createTime"};
           Cursor cursor = db.query("recycle_bin_DB", column, null, null, null, null, null);
           while (cursor.moveToNext()) {
               createRecycleBinNote(cursor.getInt(0),cursor.getString(1), cursor.getString(2) ,cursor.getString(3));
           }

        }catch (Exception ex){ex.printStackTrace();}

    }

    public void createRecycleBinNote (int id, String text, String title, String createTime) {
        FrameLayout test_Frame = new FrameLayout(this);
        test_Frame.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, Scroll.dHeight/8));
        test_Frame.setPadding(12,0,12,10);


        Button button = new Button(this);
        TextView textView_1 = new TextView(this);
        TextView textView_2 = new TextView(this);

        textView_1.setTextSize(24);
        textView_1.setText(text);
        textView_1.setGravity(Gravity.CENTER | Gravity.LEFT);
        textView_1.setPadding(16,15,0,30);
        textView_1.setElevation(6); // чтобы кнопка была за текстм

        textView_2.setText(createTime);
        textView_2.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        textView_2.setPadding(0,0,20,6);
        textView_2.setElevation(6);

        button.setAlpha(0.8f);
        button.setLayoutParams(test_Frame.getLayoutParams());
        button.setOnClickListener(new OnButtonClick());


        button_id_map.put(button, id);

        test_Frame.addView(button);
        test_Frame.addView(textView_1);
        test_Frame.addView(textView_2);
        inFrame_frameLayout.addView(test_Frame);

    }

    public class OnButtonClick implements ViewGroup.OnClickListener{
        @Override
        public void onClick(View v) {
            final int idNote = button_id_map.get(v);
            AlertDialog.Builder builder = new AlertDialog.Builder(Recycle_Bin.this);
            String [] yes_or_not = {"Востановить","Удалить"};
            builder.setItems(yes_or_not, new OnRestoreClick() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int id_note = idNote;

                    MySQL mySQL = new MySQL(Recycle_Bin.this);
                    SQLiteDatabase db = mySQL.getWritableDatabase();
                    switch (which){
                        case 1:
                            try {
                            db.execSQL("DELETE FROM recycle_Bin_DB where id = " + id_note );
                            db.close();
                        } catch (Exception e) { e.printStackTrace(); }
                            Recycle_Bin.this.recreate();
                            System.out.println("закончил удаление из бд");
                           break;

                            //востановить
                        case 0:
                            try {
                                String [] column = {"id", "text", "titleNote ", "createTime"};
                                Cursor cursor = db.query("recycle_bin_DB", column, "id = " + id_note, null, null, null, null);
                                cursor.moveToFirst();
                                String text = cursor.getString(1);
                                String title = cursor.getString(2);
                                String createTime = cursor.getString(3);
                                db.execSQL("INSERT INTO my_DB (id, text, titleNote, createTime) VALUES (null, '" + text +"', '" + title+"', '" + createTime +"')");
                                db.execSQL("DELETE FROM recycle_Bin_DB where id = " + id_note );
                                db.close();
                            } catch (Exception e) { e.printStackTrace();}
                            Recycle_Bin.this.recreate();
                            break;
                            //удалить
                    }

                }
            });
            v.setBackgroundColor(Color.rgb(200,100,100));
            v.setAlpha(0.6f);

            builder.setOnCancelListener( new OnDialogClick(v));
            builder.show();

        }
    }




    public class OnDialogClick implements DialogInterface.OnCancelListener{

        View button;

        OnDialogClick(View v){
            v = this.button;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            Recycle_Bin.this.recreate();
        }
    }

    public class OnRestoreClick implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }

    }

}



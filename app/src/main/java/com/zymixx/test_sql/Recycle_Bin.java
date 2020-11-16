package com.zymixx.test_sql;

import android.animation.Animator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Recycle_Bin extends AppCompatActivity {

    LinearLayout inFrame_frameLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_bin_layout);
        makeGUI ();

    }

    public void makeGUI (){
        getSupportActionBar().setTitle(R.string.recycle_bin);
        inFrame_frameLayout = findViewById(R.id.recycle_bin_frameLayout_inFrame);
        MySQL mySQL = new MySQL(this);
        SQLiteDatabase db = mySQL.getWritableDatabase();
        int id_recycle = 2;
        try {
           String [] column = {"id", "text", "titleNote ", "createTime"};
           Cursor cursor = db.query("recycle_bin_DB", column, null, null, null, null, null);
           while (cursor.moveToNext()) {
               createRecycleBinNote(cursor.getString(1), cursor.getString(2) ,cursor.getString(3));
           }

        }catch (Exception ex){ex.printStackTrace();}

    }

    public void createRecycleBinNote (String text, String title, String createTime) {
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


        test_Frame.addView(button);
        test_Frame.addView(textView_1);
        test_Frame.addView(textView_2);
        inFrame_frameLayout.addView(test_Frame);

    }

}



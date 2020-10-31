package com.zymixx.test_sql;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;



public class Scroll extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NestedScrollView main_nestedScrollView;
    FrameLayout main_frameLayout;
    FrameLayout into_main_frameLayout1;
    ImageView plus;
    ImageView imageView;
    ArrayList<Notes> notesArrayList = new ArrayList<Notes>();
    ArrayList<ImageView> imageForRemove = new ArrayList<ImageView>();
    ArrayList<TextView> textViewsForRemove = new ArrayList<TextView>();
    MySQL mySQL;
    SQLiteDatabase db;
    static int countNotes = 0;
    static int nextNotes = 1;
    ViewGroup.LayoutParams LPforNotes;
    String coverTitle;

    DrawerLayout drawer;
    NavigationView navigationView;

    //для вычесления размера записей на экране
    int dWidth;
    int otstup;
    int notesSIze;


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_drawer_scroll);
        countNotes = 0;
        makeGUI();
        createStartNotesFromSQL ();


    /*    RunnableForDrawer runnableForDrawer = new RunnableForDrawer();
        Thread thread = new Thread(runnableForDrawer);
        thread.start();*/ //готовый ранейбл для тестов и прочего




        final DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        final NavigationView navigationView =  findViewById(R.id.nav_view);
       final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.open, R.string.closs) {

           boolean needCheng = false;
            @Override
            public void onDrawerOpened(View drawerView) {
                System.out.println("МЕНЯ ОТКРЫЛИ");
                drawer.bringChildToFront(navigationView);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                    System.out.println("МЕНЯ zacrili");
                    drawer.bringChildToFront(main_nestedScrollView);
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                System.out.println("Меняются какие то статы");
                if (!needCheng){
                    drawer.bringChildToFront(navigationView);
                    needCheng = true;}
                super.onDrawerStateChanged(newState);



            }

           @Override
           public void onDrawerSlide(View drawerView, float slideOffset) {
               System.out.println("ДРАВЙВЕР СЛАЙД");
               if (needCheng){
               drawer.bringChildToFront(main_nestedScrollView);
                   needCheng = false;}
                super.onDrawerSlide(drawerView, slideOffset);
           }
       };
        drawer.addDrawerListener(toggle);


//        navigationView.setNavigationItemSelectedListener(this);

    }

    public class RunnableForDrawer implements Runnable {
        @Override
        public void run() {
            boolean start = true;
            while (start){
            try {
            Thread.sleep(1000);
            }catch (Exception ex)  {}

                System.out.println("Состояние " );

         }
       }
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        repaintALL();
    }
    public void repaintALL (){

        try {
        for (ImageView image : imageForRemove){
            image.setAlpha(0f);
        }
        for (TextView textView : textViewsForRemove){
            textView.setAlpha(0f);
        }
        plus.setAlpha(0f);
        } catch (Exception ex) {ex.printStackTrace();}

        imageForRemove.removeAll(imageForRemove);
        nextNotes = 1;
        countNotes = 0;
      //  leftMarg = 220;
        makeGUI();
        createStartNotesFromSQL ();

    }

    public void createStartNotesFromSQL (){
        nextNotes = 0;
        mySQL = new MySQL(this);
        try {
            db = mySQL.getWritableDatabase();
            String column [] = {"id", "text"};
            Cursor cursor = db.query("my_DB", column, null, null,null,null,null);
            cursor.moveToFirst();
            int countNotes = cursor.getCount();
            while (cursor.moveToNext())
            cursor.getString(1);
            for (int i = 0; i < countNotes ; i++) {
                createNotes();
                plusSetParms();
            }
        }catch (Exception ex) {ex.printStackTrace();}


    }


    public void makeGUI (){

        drawer = findViewById(R.id.drawer_layout);
        navigationView = new NavigationView(this);
        navigationView = findViewById(R.id.nav_view);
        main_nestedScrollView = new NestedScrollView(Scroll.this);
        main_nestedScrollView = (NestedScrollView) findViewById(R.id.my_scroll);
        main_frameLayout = new FrameLayout(Scroll.this);
        main_frameLayout = (FrameLayout) findViewById(R.id.main_frameLayout);
        into_main_frameLayout1 = new FrameLayout(Scroll.this);
        into_main_frameLayout1 = (FrameLayout) findViewById(R.id.into_main_frameLayout1);


        main_frameLayout.setLayoutParams(getParms(0, 0, 0, 0));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;



        plus = new ImageView(this);
        plus.setImageResource(R.drawable.plus);
        plus.setOnClickListener((new onClick()));
        plus.setOnLongClickListener(new OnLongClick());
        into_main_frameLayout1.addView(plus);

        dWidth = screenWidth;
        otstup = dWidth / 6 / 5;
        notesSIze = (dWidth - otstup * 5) / 4;
        LPforNotes = getParms(otstup, 50, notesSIze, notesSIze);
        plusSetParms(); //вычесляем размеер для записей




    }



    public void delete_notes (int notes_lvl) {

        FrameLayout frameLayoutForDialog = new FrameLayout(this);
        frameLayoutForDialog.setLayoutParams(getParms(0,0, 600,300));

        TextView textView1 = new TextView(this);
        textView1.setText("Первая запись");
        TextView textView2 = new TextView(this);
        textView1.setText("Вторая запись");

        textView1.setLayoutParams(getParms(20, 40, 200, 200));
        textView2.setLayoutParams(getParms(20, 240, 200, 200));

       frameLayoutForDialog.addView(textView1);
        frameLayoutForDialog.addView(textView2);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    //    builder.setTitle("Удалить запись?");

        String stringItems[] = {"Удалить запись", "Переместить", "Создать папку"};


        builder.setItems(stringItems, new OnDeleteNoteListener(notes_lvl, builder));

      //  builder.setPositiveButton("YES",  new OnDeleteNoteListener (notes_lvl));
      //  builder.setNegativeButton("NO",  new OnDeleteNoteListener (notes_lvl));
        builder.show();

    } //создаёт диалоговое меню для удаления

    public class OnDeleteNoteListener implements DialogInterface.OnClickListener  {
        int note_lvl;
        AlertDialog.Builder builder;
        OnDeleteNoteListener (int note, AlertDialog.Builder builder) {
            this.note_lvl = note;
            this.builder = builder;
        } //принимает айди удаляемой записи для БД
        @Override
        public void onClick(DialogInterface dialog, int which) {
            for (int i = 0; i < 10 ; i++) {
                System.out.println(which);
            }
            switch (which){
                    case (-1):

                    try {
                        mySQL = new MySQL(getApplicationContext());
                        db = mySQL.getWritableDatabase();
                        db.execSQL("DELETE FROM my_DB WHERE ID = " + note_lvl);
                        repaintALL ();}
                    catch (Exception ex){ex.printStackTrace();} //удаляет запись

                    break;
                case (-2): break;

                case (0):
                    builder.setPositiveButton("YES",  new OnDeleteNoteListener (note_lvl, builder));
                    builder.setNegativeButton("NO",  new OnDeleteNoteListener (note_lvl, builder));

                    builder.setTitle("Точно удалить?");
                    builder.show();

                    break; //удалить
                case (1): break; //Переместить
                case (2): break; //Создать папку
            }

        }
    } //лисанер диалогового меню при долгом нажатии


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    public void plusSetParms (){
        plus.setLayoutParams(getLParmsNotes(otstup, otstup*2, notesSIze, notesSIze));
    }

    public class OnLongClick implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {

            Notes selectNote;
            try {
                mySQL = new MySQL(Scroll.this);
                db = mySQL.getWritableDatabase();
                if (v == plus) {
                } else {
                    for (Notes note : notesArrayList) {
                        if (v == note.imageView2) {
                            selectNote = note;
                            delete_notes(selectNote.countLVL);
                        }

                    }
                }
            }catch (Exception ex) {ex.printStackTrace();}
            return false;
        }

    }

    public class onClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Notes selectNote;
            if (v == plus) {
                    createNotesForSQL();

                plusSetParms();
                } else {
                    for (Notes note : notesArrayList) {
                        if (v == note.imageView2) {
                            selectNote = note;
                            Intent intent = new Intent(Scroll.this, NotesTextSpace.class );
                            intent.putExtra("COUNT LVL", note.countLVL);
                            startActivity(intent);
                    break;
                        }
                }
            }
        }

    }


    public void createNotes() {

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.zzz);
        imageView.setLayoutParams(plus.getLayoutParams());
                            imageView.setAlpha(1f);
        TextView textView = new TextView(this);
        textView.setLayoutParams(plus.getLayoutParams());
        String textForCover = "null";
     //   textView.setText(getNomberMethod(countNotes));
        //textView.setGravity(4);
        textView.setTextSize(20);
        textView.setLineSpacing(0f, 0.8f);
        textView.setTextColor(Color.RED);
        textViewsForRemove.add(textView);

        Typeface typeface = Typeface.DEFAULT_BOLD;
        textView.setTypeface(typeface);

        Notes notes = new Notes();
        imageView.setOnClickListener(new onClick());
        imageView.setOnLongClickListener(new OnLongClick());



        mySQL = new MySQL(this);
        try {
            db = mySQL.getWritableDatabase();
            String column [] = {"id", "text", "titleNote"};
            Cursor cursor = db.query("my_DB", column, null, null,null,null,null);
           cursor.moveToPosition(nextNotes);
           notes.countLVL = cursor.getInt(0);
            coverTitle = cursor.getString(2);
           textForCover = cursor.getString(1);
            for (int i = 0; i < 3; i++) {
                System.out.println("КУРСОР ЗДЕСЬ " + cursor.getPosition());
            }
            cursor.close();
            StringBuffer stringBuffer = new StringBuffer(textForCover);
            stringBuffer.setLength(18);
            String finalTextForCover = stringBuffer.toString();
            if (textForCover.length() > 18){
                finalTextForCover = finalTextForCover + "...";}
            finalTextForCover = finalTextForCover.replaceAll("\n", " "); //заменем абзатцы пробелами
            textView.setText(finalTextForCover);
            for (int i = 0; i < 10; i++) {
                System.out.println(finalTextForCover);
            }
            if (!coverTitle.equals("Title")){
                textView.setText(coverTitle);
                textView.setTextColor(Color.WHITE);
            }
        // int corent_AutoInc = Integer.parseInt(cursor.getString(0));
          //  db.execSQL("UPDATE my_DB SET id = " + (countNotes - 1) + " where id = " + nextNotes);
            nextNotes++;
        }catch (Exception ex) {ex.printStackTrace();}


        notes.imageView2 = imageView;
        notesArrayList.add(notes);
        into_main_frameLayout1.addView(imageView);
        into_main_frameLayout1.addView(textView);
        imageForRemove.add(imageView);

    }

    public void createNotesForSQL() {

        mySQL = new MySQL(this);
    try {

        db = mySQL.getWritableDatabase();
        db.execSQL("INSERT INTO my_DB (id, text) values (null, 'новая запись')");
      //  String column [] = {"id", "text"};
      //  Cursor cursor = db.query("my_DB", column, null, null,null,null,null);
      //  cursor.moveToLast();
    //    int corent_AutoInc = Integer.parseInt(cursor.getString(0));
   //     db.execSQL("UPDATE my_DB SET id =" + countNotes + "where id = " + corent_AutoInc);
    }catch (Exception ex) {ex.printStackTrace();}
        createNotes();
    }


    public static FrameLayout.LayoutParams getParms (int left, int top, int height, int width){
        boolean allParm = true;
        FrameLayout.LayoutParams params = new FrameLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (left == 0 && top == 0 && height == 0 && width == 0) { // не присваевает значение, а возвращает мач.перент
            allParm = false;
        }
        if (allParm){
            params.leftMargin =  left; // типа координаты в контейнере по X
            params.topMargin = top; // типа координаты в контейнере по Y
            params.height = height;
            params.width =  width;
        }
        return params; // возврощает паметры во фрейме
    } //возвращает заданые лаяут параметры одним обьектом

    public  FrameLayout.LayoutParams getLParmsNotes(int left, int top, int height, int width){
        top = top * ((countNotes/4) + 1);
        left = (notesSIze + otstup ) * (countNotes%4+1) - notesSIze;
        if (countNotes > 3) {
            top = (notesSIze + otstup) * (countNotes/4) + otstup*2;
        }
        countNotes++;
        FrameLayout.LayoutParams params = new FrameLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            params.leftMargin =  left; // типа координаты в контейнере по X
            params.topMargin = top; // типа координаты в контейнере по Y
            params.height = height;
            params.width =  width;

        return params; // возврощает паметры во фрейме
    } //возвращает заданые лаяут параметры одним обьектом

   public class Notes {
        public int countLVL;
        public ImageView imageView2;

        Notes(){
            ImageView imageView2 = new ImageView(Scroll.this);
        }
    }


}

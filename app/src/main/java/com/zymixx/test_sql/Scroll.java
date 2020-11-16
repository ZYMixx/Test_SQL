package com.zymixx.test_sql;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Scroll extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NestedScrollView main_nestedScrollView;
    FrameLayout main_frameLayout;
    FrameLayout into_main_frameLayout1;
    ImageView plus;
    ImageView imageView;
    ImageView imageBack;
    ArrayList<Notes> notesArrayList = new ArrayList<Notes>();
    ArrayList<ImageView> imageForRemove = new ArrayList<ImageView>();
    ArrayList<TextView> textViewsForRemove = new ArrayList<TextView>();
    ArrayList<ImageView> backImageForRemove = new ArrayList<>();
    MySQL mySQL;
    SQLiteDatabase db;
    static int countNotes = 0;
    static int nextNotes = 1;
    ViewGroup.LayoutParams LPforNotes;
    String coverTitle;
    FloatingActionButton floatingActionButton;

    ArrayList<Integer> notesCountForReplace = new ArrayList<Integer>();

    DrawerLayout drawer;
    NavigationView navigationView;

    //для вычесления размера записей на экране
    static int dWidth;
    static int dHeight;
    int otstup;
    int notesSIze;

    //статические переменые с настроек
    public static int auto_save;
    public static int fust_dealite;

    int strangInt; //delite first

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
        createStartNotesFromSQL();


    /*    RunnableForDrawer runnableForDrawer = new RunnableForDrawer();
        Thread thread = new Thread(runnableForDrawer);
        thread.start();*/ //готовый ранейбл для тестов и прочего


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()) {
                    case (R.id.Serch):
                        System.out.println("Поиск");
                        intent = new Intent(Scroll.this, Search.class);
                        startActivity(intent);
                        drawer.closeDrawers();
                        return true;
                    case (R.id.Setting):
                        System.out.println("Настройки");
                        intent = new Intent(Scroll.this, Settings.class);
                        startActivity(intent);
                        drawer.closeDrawers();
                        return true;
                    case (R.id.Recycle_bin):
                        System.out.println("Мусорка");
                        intent = new Intent(Scroll.this, Recycle_Bin.class);
                        startActivity(intent);
                        drawer.closeDrawers();
                        return true;
                }
                drawer.closeDrawers();
                return false;
            }


        });
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.open, R.string.closs) {

            @Override
            public View.OnClickListener getToolbarNavigationClickListener() {
                return super.getToolbarNavigationClickListener();
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                getMenuInflater().inflate(R.menu.drawer_menu, item.getSubMenu());


                switch (item.getItemId()) {

                }

                return super.onOptionsItemSelected(item);
            }

            boolean needCheng = false;

            @Override
            public void onDrawerOpened(View drawerView) {
                System.out.println("МЕНЯ ОТКРЫЛИ");
                drawer.bringChildToFront(navigationView);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawer.bringChildToFront(main_nestedScrollView);
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (!needCheng) {
                    drawer.bringChildToFront(navigationView);
                    needCheng = true;
                }
                super.onDrawerStateChanged(newState);

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (needCheng) {
                    drawer.bringChildToFront(main_nestedScrollView);
                    needCheng = false;
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };


        drawer.addDrawerListener(toggle);


//        navigationView.setNavigationItemSelectedListener(this);

    } //запускает Drawer и рисует GUI

    public class RunnableForDrawer implements Runnable {
        @Override
        public void run() {
            boolean start = true;
            while (start) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                }

                System.out.println("Состояние ");

            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        repaintALL();
    }

    public void repaintALL() {

        try {
            for (ImageView image : imageForRemove) {
                image.setAlpha(0f);
            }
            for (TextView textView : textViewsForRemove) {
                textView.setAlpha(0f);
            }
            plus.setAlpha(0f);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        notesArrayList.removeAll(notesArrayList);
        imageForRemove.removeAll(imageForRemove);
        nextNotes = 1;
        countNotes = 0;

        makeGUI();
        createStartNotesFromSQL();

    } // запускает перересовку компонентов из SQL и удаляет всё из масивов

    public void createStartNotesFromSQL() {
        nextNotes = 0;
        mySQL = new MySQL(this);
        try {
            db = mySQL.getWritableDatabase();
            String[] column = {"id", "text"};
            Cursor cursor = db.query("my_DB", column, null, null, null, null, null);
            cursor.moveToFirst();
            int countNotes = cursor.getCount();
            while (cursor.moveToNext())
                cursor.getString(1);
            for (int i = 0; i < countNotes; i++) {
                createNotes();
                plusSetParms();
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } //запускает создания записей и даёт им текст из SQL

    public void makeGUI() {

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        main_nestedScrollView = findViewById(R.id.my_scroll);
        main_frameLayout = findViewById(R.id.main_frameLayout);
        into_main_frameLayout1 = findViewById(R.id.into_main_frameLayout1);
        floatingActionButton = findViewById(R.id.fabab);
        floatingActionButton.setExpanded(true);
        floatingActionButton.hide(); //скрываем кнопку для отмены переноса
        main_frameLayout.setLayoutParams(getParms(0, 0, 0, 0));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenheight = displayMetrics.heightPixels;

        plus = new ImageView(this);
        plus.setImageResource(R.drawable.plus);
        plus.setOnClickListener((new onClick()));
        plus.setOnLongClickListener(new OnLongClick());
        into_main_frameLayout1.addView(plus);

        dWidth = screenWidth;
        dHeight = screenheight;
        otstup = dWidth / 6 / 5;
        notesSIze = (dWidth - otstup * 5) / 4;
        LPforNotes = getParms(otstup, 50, notesSIze, notesSIze);
        plusSetParms(); //вычесляем размеер для записей

        updateConfig(); // принимает из БД данные об автосохранении и быстром удалении

    }

    public void updateConfig() {
        MySQL mySQL = new MySQL(Scroll.this);
        try {
            SQLiteDatabase db = mySQL.getWritableDatabase();
            String[] column = {"corent"};
            Cursor cursor = db.query("config_DB", column, "name = 'auto_save'", null, null, null, null);
            cursor.moveToFirst();
            auto_save = cursor.getInt(0);
            cursor.close();
            Cursor cursor1 = db.query("config_DB", column, "name = 'fust_dealite'", null, null, null, null);
            cursor1.moveToFirst();
            fust_dealite = cursor1.getInt(0);

            cursor1.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // принимает из БД данные об автосохранении и быстром удалении

    public void delete_notes(int notes_lvl) {

        FrameLayout frameLayoutForDialog = new FrameLayout(this);
        frameLayoutForDialog.setLayoutParams(getParms(0, 0, 600, 300));

        TextView textView1 = new TextView(this);
        textView1.setText("Первая запись");
        TextView textView2 = new TextView(this);
        textView1.setText("Вторая запись");

        textView1.setLayoutParams(getParms(20, 40, 200, 200));
        textView2.setLayoutParams(getParms(20, 240, 200, 200));

        frameLayoutForDialog.addView(textView1);
        frameLayoutForDialog.addView(textView2);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] stringItems = {"Удалить запись", "Переместить", "Создать папку"};
        builder.setItems(stringItems, new OnDeleteNoteListener(notes_lvl, builder));

        builder.show();

    } //создаёт диалоговое меню для удаления

    public class OnDeleteNoteListener implements DialogInterface.OnClickListener {
        int note_lvl;
        AlertDialog.Builder builder;

        OnDeleteNoteListener(int note, AlertDialog.Builder builder) {
            this.note_lvl = note;
            this.builder = builder;
        } //принимает айди удаляемой записи для БД

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case (-1):

                    try {
                        mySQL = new MySQL(getApplicationContext());
                        db = mySQL.getWritableDatabase();
                        String[] column = {"id", "text", "titleNote", "createTime"};
                        Cursor cursor = db.query("my_DB", column, "id = " + note_lvl, null, null, null, null);
                        cursor.moveToFirst();
                        String text_forRecycle = cursor.getString(1);
                        String title_forRecycle = cursor.getString(2);
                        String createTime_forRecycle = cursor.getString(3);
                        db.execSQL("INSERT INTO recycle_bin_DB VALUES (null, '" + text_forRecycle + "' , '" + title_forRecycle + "', '" +
                                createTime_forRecycle +"' )");
                        db.execSQL("DELETE FROM my_DB WHERE ID = " + note_lvl);
                        repaintALL();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } //удаляет запись

                    break;
                case (-2):
                    break;

                case (0):

                    if (fust_dealite == 1) {
                        try {
                            mySQL = new MySQL(getApplicationContext());
                            db = mySQL.getWritableDatabase();
                            String[] column = {"id", "text", "titleNote", "createTime"};
                            Cursor cursor = db.query("my_DB", column, "id = " + note_lvl, null, null, null, null);
                            cursor.moveToFirst();
                            String text_forRecycle = cursor.getString(1);
                            String title_forRecycle = cursor.getString(2);
                            String createTime_forRecycle = cursor.getString(3);
                            db.execSQL("INSERT INTO recycle_bin_DB VALUES (null, '" + text_forRecycle + "' , '" + title_forRecycle + "', '" +
                                    createTime_forRecycle +"' )");
                            db.execSQL("DELETE FROM my_DB WHERE ID = " + note_lvl);

                            repaintALL();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } //удаляет запись
                    } else {
                        builder.setPositiveButton("YES", new OnDeleteNoteListener(note_lvl, builder));
                        builder.setNegativeButton("NO", new OnDeleteNoteListener(note_lvl, builder));
                        builder.setTitle("Точно удалить?");
                        builder.show();
                    }

                    break; //удалить
                case (1):
                    moveNoteInNewPosition(note_lvl);
                    break; //Переместить
                case (2):
                    break; //Создать папку
            }

        }
    } //лисанер диалогового меню при долгом нажатии


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    public void plusSetParms() {
        plus.setLayoutParams(getLParmsNotes(otstup, otstup * 2, notesSIze, notesSIze));
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
                        Intent intent = new Intent(Scroll.this, NotesTextSpace.class);
                        intent.putExtra("COUNT LVL", note.countLVL);
                        startActivity(intent);
                        break;
                    }
                }
            }
        }

    }

    public void moveNoteInNewPosition(int noteLVL) {
        final ScaleAnimation startScaleAnimation = new ScaleAnimation(1, 0.9f, 1f, 0.9f, 75, 75);
        startScaleAnimation.setFillAfter(true);
        startScaleAnimation.setDuration(300);
        for (Notes note : notesArrayList) {
            imageBack = new ImageView(Scroll.this);
            imageBack.setLayoutParams(note.imageView2.getLayoutParams());
            imageBack.setBackgroundColor(Color.rgb(70, 40, 225)); //цвет всех записей
            imageBack.setAlpha(0.35f);
            backImageForRemove.add(imageBack);
            note.backImageForReplace = imageBack;

            if (note.countLVL == noteLVL) {
                strangInt = note.countLVL;
                imageBack.setBackgroundColor(Color.rgb(220, 10, 10));
            }//Цвет записи для перемещеиния
            into_main_frameLayout1.addView(imageBack);
            note.imageView2.startAnimation(startScaleAnimation);
            for (TextView textView : textViewsForRemove) {
                textView.startAnimation(startScaleAnimation);
            }

            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notes noteOldPlace = new Notes();
                    Notes noteNewPlace = new Notes(); //здесь можно создать пустой обькст Ноте
                    for (Notes note : notesArrayList) {
                        if (note.countLVL == strangInt) {
                            noteOldPlace = note;
                        }
                        if (note.backImageForReplace == v) {
                            noteNewPlace = note;
                        }
                    }
                    mySQL = new MySQL(Scroll.this);
                    System.out.println("СТАРТУЮ скуэль код");
                    try {
                        db = mySQL.getWritableDatabase();
                        String[] column = {"id", "text", "titleNote", "createTime"};
                        Cursor cursor = db.query("my_DB", column, "id = " + noteNewPlace.countLVL, null, null, null, null);
                        cursor.moveToFirst();
                        int newPlaceID = cursor.getInt(0);
                        String newPlaceText = cursor.getString(1);
                        String newPlaceTitle = cursor.getString(2);
                        String newPlaceCreateTime = cursor.getString(3);
                        cursor = db.query("my_DB", column, "id = " + noteOldPlace.countLVL, null, null, null, null);
                        cursor.moveToFirst();
                        int oldPlaceID = cursor.getInt(0);
                        String oldPlaceText = cursor.getString(1);
                        String oldPlaceTitle = cursor.getString(2);
                        String oldPlaceCreateTime = cursor.getString(3);
                        db.execSQL("DELETE FROM my_DB where id = " + noteNewPlace.countLVL + " or " + "id = " + noteOldPlace.countLVL);
                        db.execSQL("INSERT INTO my_DB (id, text, titleNote, createTime) VALUES (" + noteNewPlace.countLVL + ",'" + oldPlaceText + "', '" +
                                oldPlaceTitle + "', '" + oldPlaceCreateTime +"' )");
                        db.execSQL("INSERT INTO my_DB VALUES (" + noteOldPlace.countLVL + " ,' " + newPlaceText + " ', '" +
                                newPlaceTitle + "', '" + oldPlaceCreateTime +"' )");
                        cursor.close();
                        System.out.println("закончил СКУЭЛЬ");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    ScaleAnimation reversEndAnimation = new ScaleAnimation(0.92f, 1f, 0.92f, 1f, 100, 100);
                    reversEndAnimation.setDuration(200);
                    reversEndAnimation.setFillAfter(true);

                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.35f, 0.1f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(250);

                    repaintALL();


                    for (final ImageView imBC : backImageForRemove) {
                        imBC.bringToFront();
                        imBC.setOnClickListener(null);
                        imBC.startAnimation(alphaAnimation);
                        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) { }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                for (ImageView imgg : backImageForRemove) {
                                    into_main_frameLayout1.removeView(imgg);
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }

                    for (final ImageView imgAnimate : imageForRemove) {
                        imgAnimate.startAnimation(reversEndAnimation);
                    }
                    getSupportActionBar().setTitle(R.string.app_name);
                }
            });


        }

        getSupportActionBar().setTitle("..переместить запись..");
        floatingActionButton.show();

    }


    public void createNotes() {
        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.zzz);
        imageView.setLayoutParams(plus.getLayoutParams());
        imageView.setAlpha(1f);
        TextView textView = new TextView(this);
        textView.setLayoutParams(plus.getLayoutParams());
        String textForCover = "null";
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
            String[] column = {"id", "text", "titleNote"};
            Cursor cursor = db.query("my_DB", column, null, null, null, null, null);
            cursor.moveToPosition(nextNotes);
            notes.countLVL = cursor.getInt(0);
            coverTitle = cursor.getString(2);
            textForCover = cursor.getString(1);
            cursor.close();
            StringBuffer stringBuffer = new StringBuffer(textForCover);
            stringBuffer.setLength(18);
            String finalTextForCover = stringBuffer.toString();
            if (textForCover.length() > 18) {
                finalTextForCover = finalTextForCover + "...";
            }
            finalTextForCover = finalTextForCover.replaceAll("\n", " "); //заменем абзатцы пробелами
            textView.setText(finalTextForCover);

            if (!coverTitle.equals("Title")) {
                textView.setText(coverTitle);
                textView.setTextColor(Color.WHITE);
            }
            nextNotes++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        notes.imageView2 = imageView;
        notesArrayList.add(notes);
        into_main_frameLayout1.addView(imageView);
        into_main_frameLayout1.addView(textView);
        imageForRemove.add(imageView);
        imageView.bringToFront();
        textView.bringToFront();

        System.out.println(Scroll.auto_save);
        System.out.println(Scroll.fust_dealite);

    }

    public void createNotesForSQL() {

        mySQL = new MySQL(this);
        try {
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd hh:mm"); // дата создания
            db = mySQL.getWritableDatabase();
            db.execSQL("INSERT INTO my_DB (id, text, createTime) values (null, 'новая запись', '"+ formatForDateNow.format(date)+"')");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        createNotes();
    }


    public static FrameLayout.LayoutParams getParms(int left, int top, int height, int width) {
        boolean allParm = true;
        FrameLayout.LayoutParams params = new FrameLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (left == 0 && top == 0 && height == 0 && width == 0) { // не присваевает значение, а возвращает мач.перент
            allParm = false;
        }
        if (allParm) {
            params.leftMargin = left; // типа координаты в контейнере по X
            params.topMargin = top; // типа координаты в контейнере по Y
            params.height = height;
            params.width = width;
        }
        return params; // возврощает паметры во фрейме
    } //возвращает заданые лаяут параметры одним обьектом

    public FrameLayout.LayoutParams getLParmsNotes(int left, int top, int height, int width) {
        top = top * ((countNotes / 4) + 1);
        left = (notesSIze + otstup) * (countNotes % 4 + 1) - notesSIze;
        if (countNotes > 3) {
            top = (notesSIze + otstup) * (countNotes / 4) + otstup * 2;
        }
        countNotes++;
        FrameLayout.LayoutParams params = new FrameLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        params.leftMargin = left; // типа координаты в контейнере по X
        params.topMargin = top; // типа координаты в контейнере по Y
        params.height = height;
        params.width = width;

        return params; // возврощает паметры во фрейме
    } //возвращает заданые лаяут параметры одним обьектом

    public class Notes {
        public int countLVL;
        public ImageView imageView2;
        public ImageView backImageForReplace;

        Notes() {
            ImageView imageView2 = new ImageView(Scroll.this);
        }
    }

}

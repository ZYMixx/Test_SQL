package com.zymixx.test_sql;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    TextView textViewSB_string;
    TextView textViewSB_int;
    static int corentTS_forSQL = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        makeGUI();

    }

    public void makeGUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.settings);
        LinearLayout linearLayout = findViewById(R.id.setting_LinearLayout);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.settings_items,
                android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listView.setAdapter(adapter);
        listView.setPadding(0, 0, 0, 5);
        listView.setClipToPadding(false);
        listView.setDividerHeight(5); //разделитель ячеек

        final ListView secondListView = new ListView(this);
        final ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,
                new String[]{"автосохранение", "быстрое удалиние"});
        secondAdapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        secondListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        secondListView.setAdapter(secondAdapter);
        secondListView.setDividerHeight(5);
        secondListView.setPadding(0, 0, 0, 5);
        secondListView.setClipToPadding(false);

        linearLayout.addView(listView);
        linearLayout.addView(secondListView);

        if (Scroll.auto_save == 1) {
            secondListView.setItemChecked(0, true);
        }
        if (Scroll.fust_dealite == 1) {
            secondListView.setItemChecked(1, true);
        }

        System.out.println("State now " + Scroll.auto_save);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(view.getId() + " " + position + " " + id);


                switch (position) {


                    case 0:
                        MySQL mySQL = new MySQL(Settings.this);
                        int textSizeFromSQL = 12;
                        try {
                            SQLiteDatabase db = mySQL.getReadableDatabase();
                            String textSize[] = {"corent"};
                            Cursor cursorTextSize = db.query("config_DB", textSize, "name = 'font'", null, null, null, null);
                            cursorTextSize.moveToFirst();
                            textSizeFromSQL = cursorTextSize.getInt(0);
                            cursorTextSize.close();
                            db.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                        Typeface typeface = Typeface.DEFAULT_BOLD;
                        textViewSB_string = new TextView(Settings.this);
                        textViewSB_int = new TextView(Settings.this);
                        textViewSB_int.setGravity(Gravity.CENTER);
                        textViewSB_int.setText(String.valueOf(textSizeFromSQL));
                        textViewSB_string.setTextSize(textSizeFromSQL);
                        textViewSB_string.setTextColor(Color.BLACK);
                        textViewSB_int.setTypeface(typeface);
                        textViewSB_string.setTextColor(Color.BLACK);
                        // textViewSB_string.setTypeface(typeface);
                        textViewSB_string.setGravity(Gravity.CENTER);
                        textViewSB_string.setText("Пример текста");
                        SeekBar seekBar = new SeekBar(Settings.this);
                        LinearLayout linearLayoutSB = new LinearLayout(Settings.this);
                        linearLayoutSB.setOrientation(LinearLayout.VERTICAL);
                        linearLayoutSB.setPadding(20, 40, 20, 0);
                        seekBar.setMax(26);
                        seekBar.setProgress(textSizeFromSQL - 10);
                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                textViewSB_int.setText(String.valueOf(seekBar.getProgress() + 10));
                                textViewSB_string.setTextSize(seekBar.getProgress() + 10);

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                setCorentTextSize(seekBar.getProgress() + 10);
                            }
                        }); // Увеличивает размер шрифта текста в приложении
                        seekBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
                        linearLayoutSB.addView(textViewSB_int);
                        linearLayoutSB.addView(textViewSB_string);
                        linearLayoutSB.addView(seekBar);
                        builder.setView(linearLayoutSB);
                        builder.setPositiveButton("SAVE", new OnFrontChangeListener());
                        builder.setNegativeButton("CANCEL", new OnFrontChangeListener());
                        builder.show();


                        break; //шрифт
                    case 1:
                        break; //цвет
                    case 2:
                        break; // Заменить (автосохранение)
                }
            }

        });
        secondListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(position);


                System.out.println(view.getId() + " " + position + " " + id + " click " + parent.getId());
                SparseBooleanArray chosen = ((ListView) parent).getCheckedItemPositions();
                System.out.println("раз " + chosen.valueAt(0));
                System.out.println("два " + chosen.valueAt(1));

                if(secondListView.isItemChecked(0)){Scroll.auto_save = 1;} else {Scroll.auto_save = 0;} //autosave 0
                if(secondListView.isItemChecked(1)){ Scroll.fust_dealite = 1;} else {  Scroll.fust_dealite = 0;}

                MySQL mySQL = new MySQL(Settings.this);
                try {
                    SQLiteDatabase db = mySQL.getWritableDatabase();
                    db.execSQL("UPDATE 'config_DB' SET 'corent' = " + Scroll.auto_save + " WHERE name = 'auto_save'");
                    db.execSQL("UPDATE 'config_DB' SET 'corent' = " + Scroll.fust_dealite + " WHERE name = 'fust_dealite'");
                    db.close();
                    System.out.println("Scroll.auto_save " + Scroll.auto_save);
                    System.out.println("Scroll.fust_dealite " + Scroll.fust_dealite);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }
        });




    }


    public void setCorentTextSize(int dannie) {
        corentTS_forSQL = dannie;
    }

    public class OnFrontChangeListener implements DialogInterface.OnClickListener {
        int progres;

        OnFrontChangeListener() {
            this.progres = corentTS_forSQL;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case (-1):
                    try {
                        MySQL mySQL = new MySQL(Settings.this);
                        SQLiteDatabase db = mySQL.getWritableDatabase();
                        db.execSQL("UPDATE config_DB SET corent = " + corentTS_forSQL + " WHERE name = 'font'");
                        System.out.println("Всё сделано");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;    //save
                case (-2):
                    break;   //noSave
            }
        }
    }
}



package com.zymixx.test_sql;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

public class Settings extends AppCompatActivity {

    static int hhh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        makeGUI();

    }

    public void makeGUI (){
        getSupportActionBar().setTitle(R.string.settings);
        LinearLayout linearLayout = findViewById(R.id.setting_LinearLayout);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.settings_items,
                android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listView.setAdapter(adapter);
        listView.setPadding(0,0,0,5);
        listView.setClipToPadding(false);
        listView.setDividerHeight(5); //разделитель ячеек

        ListView secondListView = new ListView(this);
        ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,
                new String[]{"автосохранение", "быстрое удалиние"});
        secondListView.setAdapter(secondAdapter);
        secondListView.setDividerHeight(5);
        secondListView.setPadding(0,0,0,5);
        secondListView.setClipToPadding(false);

        linearLayout.addView(listView);
        linearLayout.addView(secondListView);

    }
}



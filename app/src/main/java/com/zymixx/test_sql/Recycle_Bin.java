package com.zymixx.test_sql;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Recycle_Bin extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_bin_layout);
        makeGUI ();
    }

    public void makeGUI (){
        getSupportActionBar().setTitle(R.string.recycle_bin);

    }
}


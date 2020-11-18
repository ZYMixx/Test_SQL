package com.zymixx.test_sql;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Search extends AppCompatActivity {

    FrameLayout into_frameLayout;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView(R.layout.search_layout);
        makeGUI();
    }

    public void makeGUI (){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search);

        into_frameLayout = findViewById(R.id.into_search_frameLayout);
        into_frameLayout.setBackgroundColor(Color.BLACK);
        into_frameLayout.setOnTouchListener(new OnTTT());
    }

    private void  animateRevealColorFromCoordinates(float x, float y) {
        float finalRadius = (float) Math.hypot(into_frameLayout.getWidth(), into_frameLayout.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(into_frameLayout, (int)x, (int)y, 0, finalRadius);
        into_frameLayout.setBackgroundColor(Color.RED);
        anim.start();
    }

    public class OnTTT implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (v.getId() == R.id.into_search_frameLayout) {
                    animateRevealColorFromCoordinates(event.getRawX(), event.getRawY());
                }
            }
            return false;        }
    }
}

package com.example.ServeSpeed;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ServeSpeed.R;

public class HelpVideo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_video);
    }



    public void dismiss(View v)
    {
        finish();
    }

    public void dontShowAgain(View v)
    {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("servespeed",0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("dontShowAgain",true);
        editor.commit();
        finish();
    }
}

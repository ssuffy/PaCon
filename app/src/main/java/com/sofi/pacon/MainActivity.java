package com.sofi.pacon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Button buttonNewDay = findViewById(R.id.newDay);
        buttonNewDay.setOnClickListener(openNewDayActivity());

        final Button buttonNewActivity = findViewById(R.id.newActivity);
        buttonNewActivity.setOnClickListener(openNewActivityActivity());

    }

    public View.OnClickListener openNewDayActivity() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewDayActivity.class);
                startActivity(intent);
            }
        };
    }

    public View.OnClickListener openNewActivityActivity() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewActivityActivity.class);
                startActivity(intent);
            }
        };
    }
}

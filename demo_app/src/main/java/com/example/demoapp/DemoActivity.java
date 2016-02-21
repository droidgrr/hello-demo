package com.example.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.hellodemo.HelloMainActivity;
import com.example.hellodemo.HelloMath;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        int c = HelloMath.sum(12, 24);

        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)
            {
                Intent intent = new Intent(DemoActivity.this, HelloMainActivity.class);
                startActivity(intent);
            }
        });

    }
}

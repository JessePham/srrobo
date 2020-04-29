package com.example.srrobo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FrontPage extends AppCompatActivity {

    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        enter = findViewById(R.id.enterBtn);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterApp();
            }
        });

    }

    private void enterApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

package com.example.shopping_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class infoDisplayScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_display);

        SharedPreferences preferences = getSharedPreferences("PREF_DATABASE", MODE_PRIVATE);
        String displayInfo = preferences.getString("display", "");

        TextView displayInfoName = findViewById(R.id.textViewName);
        displayInfoName.setText(displayInfo);

        Button btnLogout = findViewById(R.id.buttonLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoMainScreen = new Intent(infoDisplayScreen.this, Login.class);
                startActivity(gotoMainScreen);
            }
        });

    }
}

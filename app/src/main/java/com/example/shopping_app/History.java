package com.example.shopping_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;



import com.example.shopping_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History extends AppCompatActivity {

    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        lv = (ListView) findViewById(R.id.listView);

        List<String> arrayListFilled = new ArrayList<String>();

        for (int i = 0; i < ScrollingActivity.classDateHistory.dateHistory.size(); ++i){
            arrayListFilled.add("Purchased: " + ScrollingActivity.classDateHistory.itemName.get(i) + ": " + ScrollingActivity.classDateHistory.dateHistory.get(i));
        }

        Log.d("datetime clicked", ScrollingActivity.classDateHistory.dateHistory.toString());
        Log.d("stringdate clicked", arrayListFilled.toString());

        //ScrollingActivity.classDateHistory.dateHistory;

        // First parameter is context of the activity
        // Second parameter is the type of list view
        // Third parameter is the array
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayListFilled);

        lv.setAdapter(arrayAdapter);
    }


}
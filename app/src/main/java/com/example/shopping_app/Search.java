package com.example.shopping_app;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.CDATASection;

import java.util.ArrayList;

public class Search<adapter> extends AppCompatActivity {

    public class ShopData
    {
        public String name;
        public int imageResource;
        public String description;
        public int cost;
        public boolean purchased;

        public ShopData(String nm, int res, String desc, int cst)
        {
            name = nm;
            imageResource = res;
            description = desc;
            cost = cst;
            purchased = false;
        };
    };

    private ArrayAdapter adapter;


    ScrollingActivity.ShopData data[] = {
            new ScrollingActivity.ShopData("Bone", R.drawable.bone, "Good chew toy.", 1, "01-05-2012",5),
            new ScrollingActivity.ShopData("Carrot", R.drawable.carrot, "Good chew.", 1, "01-07-2015",3),
            new ScrollingActivity.ShopData("Dog", R.drawable.dog, "Chews toy.", 2, "07-11-2013",4),
            new ScrollingActivity.ShopData("Flame", R.drawable.flame, "It burns.", 1, "08-23-2019",3),
            new ScrollingActivity.ShopData("Grapes", R.drawable.grapes, "Your eat them.", 1, "01-07-2020",5),
            new ScrollingActivity.ShopData("House", R.drawable.house, "As opposed to home.", 100, "01-23-2014",7),
            new ScrollingActivity.ShopData("Lamp", R.drawable.lamp, "It lights.", 2, "02-17-2017",8),
            new ScrollingActivity.ShopData("Mouse", R.drawable.mouse, "Not a rat.", 1, "08-15-2018",9),
            new ScrollingActivity.ShopData("Nail", R.drawable.nail, "Hammer required.", 1, "01-22-2019",9),
            new ScrollingActivity.ShopData("Penguin", R.drawable.penguin, "Find Batman.", 10, "12-05-2017",4),
            new ScrollingActivity.ShopData("Rocks", R.drawable.rocks, "Rolls.", 1, "01-05-2012",6),
            new ScrollingActivity.ShopData("Star", R.drawable.star, "Like the sun but farther away.", 25, "09-05-2016",10),
            new ScrollingActivity.ShopData("Toad", R.drawable.toad, "Like a frog.", 1, "01-11-2014",11),
            new ScrollingActivity.ShopData("Van", R.drawable.van, "Has four wheels.", 10, "07-19-2019",5),
            new ScrollingActivity.ShopData("Wheat", R.drawable.wheat, "Some breads have it.", 1, "06-28-2015",7),
            new ScrollingActivity.ShopData("Yak", R.drawable.yak, "Yakity Yak Yak.", 15, "12-12-2016",9),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        ListView list = findViewById(R.id.theList);
        EditText searchBar = findViewById(R.id.searchList);

        ArrayList<String> names = new ArrayList<>();

        for (int i = 0; i < data.length; ++i)
        {
            names.add(data[i].name);
        }
        Log.d("list", "names: " + names);
        //adapter = new ArrayAdapter(this, R.layout.item_list, names);
        list.setAdapter(adapter);
    }

}

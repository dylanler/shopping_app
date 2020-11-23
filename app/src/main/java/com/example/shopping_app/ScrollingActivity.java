package com.example.shopping_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
class Item
{
    @PrimaryKey
    @NonNull
    public String thename;

    @ColumnInfo
    public int itemcost;

    @ColumnInfo
    public boolean itempurchased;

    @ColumnInfo
    public int itemcount;

}

// Database
// Interface is sort of like a class but not a true class
// It is like an extention of a class
@Dao
interface ItemDao
{
    @Query("SELECT * FROM item")
    //it will return the list down here
    List<Item> getAll();

    @Query("SELECT * FROM item WHERE thename LIKE :n LIMIT 1")
    //the :n will be referred to String n below
    Item findByName(String n);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Item... users);

    @Delete
    void delete(Item user);


}


@Database(entities = {Item.class, UserCredentials.class}, version = 1, exportSchema = false )
abstract  class AppDatabase extends RoomDatabase
{
    public abstract  ItemDao itemDao();
    public abstract UserCredDao userCredDao();
}

public class ScrollingActivity extends AppCompatActivity {

    public static AppDatabase db = null;


    public int balanceAmount = 0;
    public int itemPurchasedPrice = 0;

    public static class classDateHistory {
        public static List<String> dateHistory = new ArrayList<String>();
        public static List<String> itemName = new ArrayList<String>();
    }


    public static class TransactionHistory
    {
        public String name;
        public List<SimpleDateFormat> dateHistory = new ArrayList<SimpleDateFormat>();

        public TransactionHistory(String nm, List dateHist)
        {
            name = nm;
            this.dateHistory = dateHist;
        }

        public List<SimpleDateFormat> getArray(){
            return dateHistory;
        }
    }

    public static class ShopData
    {
        public String name;
        public int imageResource;
        public String description;
        public int cost;
        public boolean purchased;
        public String date;
        public int itemCount;

        public ShopData(String nm, int res, String desc, int cst, String dt, int itmCount)
        {
            name = nm;
            imageResource = res;
            description = desc;
            cost = cst;
            purchased = false;
            date = dt;
            itemCount = itmCount;
        };

        public String getName() {
            return name;
        }
    };

    //private ArrayList<ShopData> shopDataArrayList;

    private void sortArrayList() {
        Collections.sort(data, new Comparator<ShopData>() {
            @Override
            public int compare(ShopData o1, ShopData o2) {
                int sortedResults;
                sortedResults = o1.name.compareTo(o2.name);
                Log.d("debug" , "o1: " + o1);
                Log.d("debug", "o2: " + o2);
                Log.d("sortedResults", "Value: " + sortedResults);

                return sortedResults;
            }
        });
        //mRecyclerViewAdapter.notifyDataSetChanged();
    };

    private void sortArrayListCost() {
        Collections.sort(data, new Comparator<ShopData>() {
            @Override
            public int compare(ShopData o1, ShopData o2) {
                int sortedResults;
                sortedResults = o1.name.compareTo(o2.name);
                Log.d("debug" , "o1: " + o1);
                Log.d("debug", "o2: " + o2);
                Log.d("sortedResults", "Value: " + sortedResults);

                return sortedResults;
            }
        });
        //mRecyclerViewAdapter.notifyDataSetChanged();
    };

    private void sortArrayListDate() {
        Collections.sort(data, new Comparator<ShopData>() {
            @Override
            public int compare(ShopData o1, ShopData o2) {
                int sortedResults;
                sortedResults = o1.cost - o2.cost;
                Log.d("debug" , "o1: " + o1);
                Log.d("debug", "o2: " + o2);
                Log.d("sortedResults", "Value: " + sortedResults);

                return sortedResults;
            }
        });
        //mRecyclerViewAdapter.notifyDataSetChanged();
    };

    /*
    public class BalanceData
    {
        public double balance;
        public double purchasedPrice;

        public BalanceData(double bal, double purchasedP)
        {
            balance = bal;
            purchasedPrice = purchasedP;
        }
    }
    */

    ArrayList<ShopData> data = new ArrayList<ShopData>(
            Arrays.asList(
            new ShopData("Bone", R.drawable.bone, "Good chew toy.", 1, "01-05-2012",5),
            new ShopData("Carrot", R.drawable.carrot, "Good chew.", 1, "01-07-2015",3),
            new ShopData("Dog", R.drawable.dog, "Chews toy.", 2, "07-11-2013",4),
            new ShopData("Flame", R.drawable.flame, "It burns.", 1, "08-23-2019",3),
            new ShopData("Grapes", R.drawable.grapes, "Your eat them.", 1, "01-07-2020",5),
            new ShopData("House", R.drawable.house, "As opposed to home.", 100, "01-23-2014",7),
            new ShopData("Lamp", R.drawable.lamp, "It lights.", 2, "02-17-2017",8),
            new ShopData("Mouse", R.drawable.mouse, "Not a rat.", 1, "08-15-2018",9),
            new ShopData("Nail", R.drawable.nail, "Hammer required.", 1, "01-22-2019",9),
            new ShopData("Penguin", R.drawable.penguin, "Find Batman.", 10, "12-05-2017",4),
            new ShopData("Rocks", R.drawable.rocks, "Rolls.", 1, "01-05-2012",6),
            new ShopData("Star", R.drawable.star, "Like the sun but farther away.", 25, "09-05-2016",10),
            new ShopData("Toad", R.drawable.toad, "Like a frog.", 1, "01-11-2014",11),
            new ShopData("Van", R.drawable.van, "Has four wheels.", 10, "07-19-2019",5),
            new ShopData("Wheat", R.drawable.wheat, "Some breads have it.", 1, "06-28-2015",7),
            new ShopData("Yak", R.drawable.yak, "Yakity Yak Yak.", 15, "12-12-2016",9)
            )
    );

    class CustomDialogClass extends Dialog
    {
        public int m_id;
        public LinearLayout m_view;
        public CustomDialogClass(@NonNull Context context) {
            super(context);
        }


        @Override //overides the Dialog's class of onCreate function
        protected void onCreate(Bundle savedInstanceState)
        {
            Log.d("Dialog", "Starting");
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); //I want a dialog bar but I don't want a title because I'm creating my own title
            setContentView(R.layout.shop_dialog);

            final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            final String url ="http://10.0.2.2:5005/buy";

            Button yes = this.findViewById(R.id.yesBuyButton);
            yes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view)
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Log.d("datetime clicked", dateFormat.format(new Date()));
                    classDateHistory.dateHistory.add(dateFormat.format(new Date()));
                    classDateHistory.itemName.add(data.get(m_id).name);
                    Log.d("time purchased", classDateHistory.dateHistory.toString());
                    Log.d("item purchased", classDateHistory.itemName.toString());

                    int id = m_id;

                    //reduce itemCount
                    data.get(id).itemCount = data.get(id).itemCount - 1;
                    int currentItemCount = data.get(id).itemCount;


                    Log.d("m_id", "Value: " + Float.toString(id));
                    Log.d("Dialog", "Purchase Clicked");

                    itemPurchasedPrice = data.get(id).cost;
                    Log.d("itemCost", "Cost: " + Float.toString(itemPurchasedPrice));

                    int tmpBalance = balanceAmount - itemPurchasedPrice;

                    balanceAmount = tmpBalance;
                    Log.d("balanceAmount", "Value: " + Float.toString(balanceAmount));

                    int viewIDtoRemove = m_id;
                    Log.d("viewIDtoRemove", "Value: " + Float.toString(viewIDtoRemove));
                    Log.d("view", "Value: " + m_view.getChildAt(m_id));

                    //remove item view if item count is zero
                    int toRemove = data.indexOf(data.get(id).name);
                    Log.d("view", "Value: " + toRemove);

                    if(currentItemCount == 0){
                        m_view.removeViewAt(id);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Item Count: " + currentItemCount, Toast.LENGTH_SHORT).show();
                    }

                    //final TextView balanceAmountView = findViewById(R.id.balanceAmountView);
                    //balanceAmountView.setText( "$" + tmpBalance);
                    Log.d("ADebugTag", "tmpBalance: " + tmpBalance);

                    final Item item = new Item();
                    item.thename = data.get(id).name;
                    item.itemcost = data.get(id).cost;
                    item.itempurchased = true;

                    //inserting to database to record down items that has been purchased
                    Thread save = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            db.itemDao().insertAll(item);
                        }
                    });
                    save.start();

                    JSONObject postparams = new JSONObject();
                    try {
                        postparams.put("name", data.get(id).name);
                        postparams.put("price", "$" + data.get(id).cost);
                        postparams.put("description", data.get(id).description);
                        postparams.put("item_count", data.get(id).itemCount);
                        postparams.put("date", data.get(id).date);
                        postparams.put("item_purchased", true);
                        postparams.put("purchased_date", dateFormat.format(new Date()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("ADebugTag", "postparams: " + postparams);

                    JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, postparams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });

                    queue.add(jsonObject);
                    //recreate();
                    dismiss();
                }
            });


            Button no = this.findViewById(R.id.noBuyButton);
            no.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    Log.d("Dialog", "Cancel Clicked");
                    dismiss();
                }

            });
        }

        public void SetDetails(int id)
        {
            m_id = id;

            TextView nameText = this.findViewById(R.id.nameText);
            nameText.setText(data.get(id).name);

            TextView costText = this.findViewById(R.id.costText);
            costText.setText("$" + data.get(id).cost);

            TextView descriptionText = this.findViewById(R.id.descriptionText);
            descriptionText.setText(data.get(id).description);

            ImageView objectImage = this.findViewById(R.id.imageView);
            objectImage.setImageResource(data.get(id).imageResource);

        }
    }

    public CustomDialogClass customDialog = null;

    public void ShopClicked(int id)
    {
        customDialog.show();
        customDialog.SetDetails(id);

        //code to change data of customDialog

    }

    private void InitShopItems()
    {
        //EditText searchFilter = findViewById(R.id.searchBar);

        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout layout = findViewById(R.id.layoutScrollView);
        
        customDialog.m_view = layout;

        for (int i = 0; i < data.size(); ++i)
        {
            Item item = db.itemDao().findByName(data.get(i).name);
            if (item != null && item.itempurchased) continue;

            final View myShopItem = inflater.inflate(R.layout.shop_item, null);
            final int tmp_id = i;

            View.OnClickListener click = new View.OnClickListener(){
                public int id = tmp_id;

                public void onClick(View view)
                {
                    Log.d("Dialog", "Clicked");
                    ShopClicked(id);
                }
            };

            Button nameButton = myShopItem.findViewById(R.id.nameButton);
            nameButton.setText(data.get(i).name);
            nameButton.setOnClickListener(click);

            Button costButton = myShopItem.findViewById(R.id.costButton);
            costButton.setText("$" + data.get(i).cost);
            costButton.setOnClickListener(click);

            ImageButton imageButton = myShopItem.findViewById(R.id.imageButton);
            imageButton.setImageResource(data.get(i).imageResource);
            imageButton.setOnClickListener(click);

            TextView dateAddedView = myShopItem.findViewById(R.id.dateAdded);
            dateAddedView.setText("Date added: " + data.get(i).date);


            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    layout.addView(myShopItem);
                }
            });

            //layout.addView(myShopItem);
        }
    }

    private void Populate() {
        Thread load = new Thread(new Runnable() {
            @Override
            public void run(){
                InitShopItems();
            }
        });

        load.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Log.d("pre-sorted array", "Value: " + data);

        //UNCOMMENT ALL UNUSED ITEMS
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */



        db = Room.databaseBuilder(this, AppDatabase.class, "ItemDatabase").build();

        Button btnSort = findViewById(R.id.buttonSort);
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout layout = findViewById(R.id.layoutScrollView);
                layout.removeAllViewsInLayout();
                Populate();

                Log.d("sort-preclicked", "Value: " + data.get(16).name);
                sortArrayList();
                Log.d("sort-postclicked", "Value: " + data.get(16).name);


                /*

                Collections.sort(data, new Comparator<ShopData>() {
                    @Override
                    public int compare(data lhs, data rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });

                */
            }
        });

        Button btnSort2 = findViewById(R.id.buttonSort2);
        btnSort2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout layout = findViewById(R.id.layoutScrollView);
                layout.removeAllViewsInLayout();
                Populate();

                Log.d("sort-preclicked", "Value: " + data.get(16).name);
                sortArrayListCost();
                Log.d("sort-postclicked", "Value: " + data.get(16).name);

            }
        });

        Button btnSort3 = findViewById(R.id.buttonSort3);
        btnSort3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout layout = findViewById(R.id.layoutScrollView);
                layout.removeAllViewsInLayout();
                Populate();

                Log.d("sort-preclicked", "Value: " + data.get(16).name);
                sortArrayListDate();
                Log.d("sort-postclicked", "Value: " + data.get(16).name);

            }
        });

        Button gotoLoginScreen = findViewById(R.id.gotoLoginScreen);
        gotoLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoLoginScreen = new Intent(ScrollingActivity.this, Login.class);
                startActivity(gotoLoginScreen);
            }
        });

        Button gotoSearchScreen = findViewById(R.id.searchButton);
        gotoSearchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoSearchScreen = new Intent(ScrollingActivity.this, Search.class);
                startActivity(gotoSearchScreen);
            }
        });

        Button gotoTransactionHistory = findViewById(R.id.buttonTransactHistory);
        gotoTransactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoTransactionHistory = new Intent(ScrollingActivity.this, History.class);
                startActivity(gotoTransactionHistory);
            }
        });

        final TextView balanceView = findViewById(R.id.balanceView);
        final TextView balanceAmountView = findViewById(R.id.balanceAmountView);
        final Button addMoney = findViewById(R.id.addMoneyButton);
        final Button sampleBtn = findViewById(R.id.buttonSample);
        final int amountToAdd = 100;

        balanceAmountView.setText( "$" + balanceAmount);

        if(getIntent().hasExtra("state")){
            if (getIntent().getStringExtra("state").equals("success")){
                addMoney.setEnabled(true);
            }else{
                addMoney.setEnabled(false);
            }
        }else{
            addMoney.setEnabled(false);
        }


        addMoney.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int tmpBalance = balanceAmount + amountToAdd;
                Log.d("ADebugTag", "tmpBalance: " + tmpBalance);
                balanceAmountView.setText( "$" + tmpBalance);
                balanceAmount = tmpBalance;
                Log.d("ADebugTag", "tmpBalance: " + balanceAmount);

            }
        });

        sampleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addMoney.setEnabled(true);
            }
        });

        customDialog = new CustomDialogClass(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:5005/list";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject reader = new JSONObject((response));
                            JSONArray lst = reader.getJSONArray("list");

                            Log.d("ADebugTag", "jsonList: " + lst);

                            for(int i = 0; i < lst.length(); ++i)
                            {
                                JSONObject obj = lst.getJSONObject(i);

                                String name = obj.getString("name");
                                String image = obj.getString("image");
                                String desc = obj.getString("description");
                                String date = obj.getString("date");
                                int itemCount = obj.getInt("itemCount");
                                int cost = obj.getInt("cost");

                                int ir = 0;
                                try{
                                    Field idField = R.drawable.class.getDeclaredField(image);
                                    ir = idField.getInt(idField);
                                } catch(Exception e)
                                {}
                                data.add(new ShopData(name, ir, desc, cost, date, itemCount));

                            }
                        } catch(JSONException e) {}
                        Populate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Populate();
                    }

                });

        queue.add(stringRequest);

        Populate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.shopping_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

@Entity
class UserCredentials
{
    @PrimaryKey
    @NonNull
    public String username;

    @ColumnInfo
    public String password;

    @ColumnInfo
    public String email;

}

@Dao
interface UserCredDao
{
    @Query("SELECT * FROM UserCredentials")
        //it will return the list down here
    List<UserCredentials> getAll();

    @Query("SELECT * FROM UserCredentials WHERE username LIKE :n LIMIT 1")
        //the :n will be referred to String n below
    UserCredentials findByUsername(String n);

    @Insert
    void insertNewUserCreds(UserCredentials... users);

    @Delete
    void deleteUserCreds(UserCredentials user);
}

/*
@Database(entities = {UserCredentials.class}, version = 1, exportSchema = false )
abstract  class AppDatabase extends RoomDatabase
{
    public abstract  UserCredentials userCred();
}
*/

public class Register extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://10.0.2.2:5005/";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        final EditText registerName = findViewById(R.id.editNewName);
        final EditText registerPassword = findViewById(R.id.editNewPassword);
        final EditText registerEmail = findViewById(R.id.editNewEmail);

        Button btnRegisterNew = findViewById(R.id.buttonNewRegister);



        btnRegisterNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /* SHARE PREFERENCE METHOD FOR REGISTERING USERS

                SharedPreferences myPreferences = getSharedPreferences("PREF_DATABASE", MODE_PRIVATE);

                String newName = registerName.getText().toString();
                String newPassword = registerPassword.getText().toString();
                String newEmail = registerEmail.getText().toString();

                SharedPreferences.Editor prefEditor = myPreferences.edit();

                prefEditor.putString(newName, newName);
                prefEditor.commit();
                prefEditor.putString(newPassword, newPassword);
                prefEditor.commit();
                prefEditor.putString(newName + newPassword + "data", newName + "\n" + newEmail);
                prefEditor.commit();
                */

                // DATABASE METHOD FOR REGISTERING USERS

                /*
                final UserCredentials userCred = new UserCredentials();

                userCred.username = registerName.getText().toString();
                userCred.password = registerPassword.getText().toString();
                userCred.email = registerEmail.getText().toString();

                Thread saveUserCred = new Thread(new Runnable () {
                   @Override
                   public void run(){
                       ScrollingActivity.db.userCredDao().insertNewUserCreds(userCred);
                   }
                });

                saveUserCred.start();

                String newName = userCred.username;
                String newPassword = userCred.password;
                String newEmail = userCred.email;

                Log.d("debug", "name: " + newName);
                Log.d("debug", "pwd: " + newPassword);
                Log.d("debug", "email: " + newEmail);
                */

                JSONObject postparams = new JSONObject();
                try {
                    postparams.put("subject", "register");
                    postparams.put("username", registerName.getText().toString());
                    postparams.put("password", registerPassword.getText().toString());
                    postparams.put("email", registerEmail.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("ADebugTag", "postparams: " + postparams);

                JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                Log.d("ADebugTag", "register_response: " + response);

                                if(response.toString().equals("{\"register\":true}")){
                                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                                    Intent gotoMainScreen = new Intent(Register.this, Login.class);
                                    startActivity(gotoMainScreen);
                                }
                                else if(response.toString().equals("{\"register\":\"duplicate\"}")){
                                    Toast.makeText(getApplicationContext(), "Username already registered", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Registration failed. Credentials might be already registered.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });

                queue.add(jsonObject);

                Log.d("ADebugTag", "jsonObject_register: " + jsonObject);
            }
        });
    }
}

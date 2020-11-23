package com.example.shopping_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopping_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://10.0.2.2:5005/";

        //create EditText and Buttons

        final EditText edtUsername = findViewById(R.id.editUsername);
        final EditText edtPwd = findViewById(R.id.editUserPassword);
        Button btnLogin = findViewById(R.id.buttonLogin);
        Button btnRegister = findViewById(R.id.buttonRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = edtUsername.getText().toString();
                final String password = edtPwd.getText().toString();


                /* SHARED PREFERENCE METHOD FOR LOGIN
                SharedPreferences myPreferences = getSharedPreferences("PREF_DATABASE", MODE_PRIVATE);

                String userPrivateInfo = myPreferences.getString(username + password + "data", "Credentials not accurate");
                SharedPreferences.Editor prefEditor = myPreferences.edit();
                prefEditor.putString("display", userPrivateInfo);
                prefEditor.commit();

                Log.d("Variable", "Value: " + userPrivateInfo);

                */

                // DATABASE METHOD FOR LOGIN

                /*

                Thread checkLogin = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserCredentials loginText = ScrollingActivity.db.userCredDao().findByUsername(username);
                        Log.d("ADebugTag", "UserCreds: " + loginText);
                        Log.d("ADebugTag", "Username: " + loginText.username);
                        Log.d("ADebugTag", "Password: " + loginText.password);
                        Log.d("ADebugTag", "Email: " + loginText.email);

                        if(loginText == null || TextUtils.isEmpty(loginText.email) || TextUtils.isEmpty(loginText.password)) {
                            Intent gotoLoginScreen = new Intent(Login.this, Login.class);
                            startActivity(gotoLoginScreen);
                        }
                        else{
                            if(password == loginText.password ) {
                                Intent gotoDisplayScreen = new Intent(Login.this, ScrollingActivity.class);
                                startActivity(gotoDisplayScreen);
                            }
                        }
                    }
                });

                checkLogin.start();

                */

                JSONObject postparams = new JSONObject();
                try {
                    postparams.put("subject", "login");
                    postparams.put("username", edtUsername.getText().toString());
                    postparams.put("password", edtPwd.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("ADebugTag", "postparams: " + postparams);

                JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                Log.d("ADebugTag", "login_response: " + response);

                                if(response.toString().equals("{\"records_found\":true}")){
                                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();

                                    Intent gotoDisplayScreen = new Intent(Login.this, ScrollingActivity.class);
                                    gotoDisplayScreen.putExtra("state", "success");
                                    startActivity(gotoDisplayScreen);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Username or password invalid", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });

                queue.add(jsonObject);

                Log.d("ADebugTag", "jsonObject_login: " + jsonObject);




            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Log.d("Variable", "Value: " + Float.toString(id));
                Log.d("Dialog", "Register Clicked");

                Intent gotoRegisterScreen = new Intent(Login.this, Register.class);
                startActivity(gotoRegisterScreen);
            }
        });


    }
}

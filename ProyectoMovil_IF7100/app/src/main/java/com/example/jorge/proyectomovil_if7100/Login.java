package com.example.jorge.proyectomovil_if7100;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Login extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url_all_login = "http://ingsftprojectbd.esy.es/androidconnect/LogIn.php";
    private static final String TAG_SUCCESS = "success";

    Button btnEntar;
    EditText etxUser,etxPass;
    String username, pass;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etxUser=(EditText)findViewById(R.id.etxUser);
        etxPass=(EditText)findViewById(R.id.etxPassword);
        btnEntar=(Button)findViewById(R.id.btnEntrar);
        btnEntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=etxUser.getText().toString();
                pass=etxPass.getText().toString();
                new LoadAllProducts().execute();
            }
        });
        getSupportActionBar().hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    class LoadAllProducts extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Buscando. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair("cedula",username));
            params.add(new BasicNameValuePair("pass",pass));
            JSONObject json = jParser.makeHttpRequest(url_all_login, "POST", params);

            try {
                success = json.getInt(TAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success==1){
                        Intent intent = new Intent(Login.this, HomePage.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Error de credenciales", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}

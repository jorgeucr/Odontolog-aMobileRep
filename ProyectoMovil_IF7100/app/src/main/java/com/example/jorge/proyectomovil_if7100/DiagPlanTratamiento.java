package com.example.jorge.proyectomovil_if7100;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DiagPlanTratamiento extends ActionBarActivity {


    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> empresaList;
    private static String url_all_plan_tratamiento = "http://ingsftprojectbd.esy.es/androidconnect/get_planTratamiento_post.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "tratamientos";
    private static final String TAG_PLAN = "tratamiento";

    TextView txvTratamiento, txvNombrePacienteTratamiento;
    JSONArray products = null;
    String nombrePaciente, tratamientoPaciente;
    int idPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diag_plan_tratamiento);

        txvTratamiento=(TextView)findViewById(R.id.txvTratamiento);
        txvNombrePacienteTratamiento=(TextView)findViewById(R.id.txvNombrePacienteTratamiento);

        Bundle bundle = getIntent().getExtras();
        nombrePaciente = bundle.getString("nombreCompletoPaciente");
        idPaciente = bundle.getInt("idPaciente");

        txvNombrePacienteTratamiento.setText(nombrePaciente);
        getSupportActionBar().hide();
        new LoadAllProducts().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diag_plan_tratamiento, menu);
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
            pDialog = new ProgressDialog(DiagPlanTratamiento.this);
            pDialog.setMessage("Buscando. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            List params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair("idPaciente",String.valueOf(idPaciente)));
            JSONObject json = jParser.makeHttpRequest(url_all_plan_tratamiento, "POST", params);
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    products = json.getJSONArray(TAG_PRODUCTS);
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        tratamientoPaciente=c.getString(TAG_PLAN);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    txvTratamiento.setText(tratamientoPaciente);
                }
            });
        }
    }
}

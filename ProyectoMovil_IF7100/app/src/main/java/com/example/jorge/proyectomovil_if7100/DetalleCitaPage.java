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


public class DetalleCitaPage extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> empresaList;
    private static String url_all_detalle = "http://ingsftprojectbd.esy.es/androidconnect/get_paciente_id.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "pacientes";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_ID = "cedula";
    private static final String TAG_APELLIDOS = "apellidosPaciente";
    JSONArray products = null;
    String horaCita, nombrePaciente, apellidosPaciente, cedula;
    int idPaciente;
    TextView txvNombrePacienteCita, txvCedulaPacienteCita, txvHoraCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cita_page);
        Bundle bundle = getIntent().getExtras();
        horaCita = bundle.getString("hora");
        idPaciente = bundle.getInt("idPaciente");
        txvCedulaPacienteCita=(TextView)findViewById(R.id.txvCedulaPacienteCita);
        txvHoraCita=(TextView)findViewById(R.id.txvHoraCita);
        txvNombrePacienteCita=(TextView)findViewById(R.id.txvNombrePacienteCita);
        getSupportActionBar().hide();
        new LoadAllProducts().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_cita_page, menu);
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

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetalleCitaPage.this);
            pDialog.setMessage("Buscando. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            List params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair("idPaciente",String.valueOf(idPaciente)));
            JSONObject json = jParser.makeHttpRequest(url_all_detalle, "POST", params);
            Log.d("All Products: ", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    products = json.getJSONArray(TAG_PRODUCTS);
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        nombrePaciente = c.getString(TAG_NOMBRE);
                        cedula=c.getString(TAG_ID);
                        apellidosPaciente=c.getString(TAG_APELLIDOS);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    txvNombrePacienteCita.setText(nombrePaciente+" "+apellidosPaciente);
                    txvCedulaPacienteCita.setText(cedula);
                    txvHoraCita.setText(horaCita);
                }
            });
        }
    }
}

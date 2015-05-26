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
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Reportes extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> empresaList;
    private static String url_all_reportes = "http://ingsftprojectbd.esy.es/androidconnect/get_paciente_post.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "pacientes";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_ID = "id";
    private static final String TAG_APELLIDOS = "apellidos";
    JSONArray products = null;
    String nombrePaciente, apellidosPaciente;
    int idPaciente;
    Button btnDPlanTratamiento, btnDatosPersonales, btnTratamientoEfectuado;
    ImageButton btnBuscar;
    EditText etxCedulaPaciente;
    TextView txvNombrePaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        etxCedulaPaciente=(EditText)findViewById(R.id.etxCedulaPaciente);
        txvNombrePaciente=(TextView)findViewById(R.id.txvNombrePaciente);

        btnBuscar=(ImageButton)findViewById(R.id.btnBuscar);
        btnDPlanTratamiento=(Button)findViewById(R.id.btnDPlanTratamiento);
        btnDatosPersonales=(Button)findViewById(R.id.btnDatosPersoanles);
        btnTratamientoEfectuado=(Button)findViewById(R.id.btnTratamientoEfectuado);


        btnDPlanTratamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreCompleto = nombrePaciente+" "+apellidosPaciente;

                Bundle bundle = new Bundle();
                bundle.putString("nombreCompletoPaciente", nombreCompleto);
                bundle.putInt("idPaciente", idPaciente);

                Intent intent = new Intent(Reportes.this, DiagPlanTratamiento.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadAllProducts().execute();
            }
        });
        getSupportActionBar().hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reportes, menu);
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
            pDialog = new ProgressDialog(Reportes.this);
            pDialog.setMessage("Buscando. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            String cedulaIngresada=etxCedulaPaciente.getText().toString();
            List params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair("cedula",cedulaIngresada));
            JSONObject json = jParser.makeHttpRequest(url_all_reportes, "POST", params);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    products = json.getJSONArray(TAG_PRODUCTS);
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        nombrePaciente = c.getString(TAG_NOMBRE);
                        idPaciente=c.getInt(TAG_ID);
                        apellidosPaciente=c.getString(TAG_APELLIDOS);
                    }
                }else{
                    nombrePaciente = null;

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
                    if(nombrePaciente!=null){
                        txvNombrePaciente.setText(nombrePaciente+" "+apellidosPaciente);
                        btnDatosPersonales.setEnabled(true);
                        btnTratamientoEfectuado.setEnabled(true);
                        btnDPlanTratamiento.setEnabled(true);
                    }else{
                        txvNombrePaciente.setText("El paciente no existe");
                        btnDatosPersonales.setEnabled(false);
                        btnTratamientoEfectuado.setEnabled(false);
                        btnDPlanTratamiento.setEnabled(false);
                    }
                }
            });
        }
    }

}

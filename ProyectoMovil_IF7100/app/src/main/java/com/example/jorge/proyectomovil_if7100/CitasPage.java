package com.example.jorge.proyectomovil_if7100;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class CitasPage extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> empresaList;
    private static String url_all_citas = "http://ingsftprojectbd.esy.es/androidconnect/get_cita_fecha.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "citas";
    private static final String TAG_NOMBRE = "idCita";
    private static final String TAG_HORA = "hora";
    private static final String TAG_ID_PACIENTE = "idPaciente";
    JSONArray products = null;
    TextView txvFecha;
    ListView lvwCitas;
    String fechaActual,hora;
    LinkedList<Citas> listaCitas;
    Citas cita;
    int idCita, idPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lvwCitas=(ListView)findViewById(R.id.lvwCitas);
        setContentView(R.layout.activity_citas_page);
        listaCitas=new LinkedList<>();

        txvFecha=(TextView)findViewById(R.id.txvFecha);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fechaActual=sdf.format(new Date());
        txvFecha.setText(fechaActual);
        getSupportActionBar().hide();
        new LoadAllProducts().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_citas_page, menu);
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
            pDialog = new ProgressDialog(CitasPage.this);
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
            params.add(new BasicNameValuePair("fecha",fechaActual));
            JSONObject json = jParser.makeHttpRequest(url_all_citas, "POST", params);
            Log.d("All Products: ", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    products = json.getJSONArray(TAG_PRODUCTS);
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        cita=new Citas();
                        idCita = Integer.parseInt(c.getString(TAG_NOMBRE));
                        idPaciente=Integer.parseInt(c.getString(TAG_ID_PACIENTE));
                        hora=String.valueOf(c.get(TAG_HORA));
                        cita.setHora(hora);
                        cita.setId(idCita);
                        cita.setIdPaciente(idPaciente);
                        listaCitas.add(cita);

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
                   cargarListView();
                }
            });
        }
    }

    public void cargarListView(){
        String[] arregloHoras = new String [listaCitas.size()];
        int contador=0;
        for (Citas cita : listaCitas) {

            arregloHoras[contador]=cita.getHora();
            contador++;
        }

        lvwCitas = (ListView)findViewById(R.id.lvwCitas);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arregloHoras);
        lvwCitas.setAdapter(adaptador);
        lvwCitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position,
                                    long arg) {
                String hora = lvwCitas.getItemAtPosition(position).toString();
                for (Citas cita : listaCitas) {

                    if (cita.getHora().equals(hora)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("hora", hora);
                        bundle.putInt("idPaciente", cita.getIdPaciente());

                        Intent intent = new Intent(CitasPage.this, DetalleCitaPage.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}

package kumo.kbase_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kumo.kbase_android.adapters.PrefijosAdapter;
import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Configuracion;
import kumo.kbase_android.model.xPrefijo_Telf;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.Cultura;

public class RegistroTelefonoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_telefono_fragment);

        Bundle extras = getIntent().getExtras();


        final AutoCompleteTextView mTelefono = (AutoCompleteTextView) findViewById(R.id.registro_telefono);

        final TextView mCodigo_Acceso = (TextView)findViewById(R.id.registro_codigo_acceso);
        mCodigo_Acceso.setText(extras.get("Codigo_Acceso").toString());


        final Spinner mPrefijo = ( Spinner )findViewById( R.id.registro_prefijo );
        List<xPrefijo_Telf> prefijos = Cultura.obt_prefijos();
        PrefijosAdapter adapter = new PrefijosAdapter(this.getBaseContext(),android.R.layout.select_dialog_singlechoice,prefijos);

        mPrefijo.setAdapter(adapter);

        Button mSiguiente = (Button) findViewById(R.id.registro_to_telefono);
        mSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                xPrefijo_Telf prefijo = (xPrefijo_Telf) mPrefijo.getSelectedItem();
                String telefono = mTelefono.getText().toString();
                String codigo_acceso = mCodigo_Acceso.getText().toString();

                if (!TextUtils.isEmpty(telefono)) {

                    HashMap<String, String> params = new HashMap<String, String>();

                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("_codigo_acceso", codigo_acceso);
                    jsonObject.addProperty("_prefijo", prefijo.iX);
                    jsonObject.addProperty("_movil", telefono);

                    try {
                        GsonRequest<Configuracion[]> getPersons =
                                new GsonRequest<Configuracion[]>(Request.Method.POST,findViewById(android.R.id.content), Constantes.USUARIO__OBT_APLICACIONES, Configuracion[].class,params,jsonObject,

                                        new Response.Listener<Configuracion[]>() {
                                            @Override
                                            public void onResponse(Configuracion[] response) {
                                                List<Configuracion> persons = Arrays.asList(response);
                                            }
                                        }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("objeto",error.getMessage());
                                        // TODO deal with error
                                    }
                                });

                        HttpCola.getInstance(getBaseContext()).addToRequestQueue(getPersons);

                    } catch (Exception e) {
                        Log.d("Error",e.getMessage());
                    }
                }
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

}

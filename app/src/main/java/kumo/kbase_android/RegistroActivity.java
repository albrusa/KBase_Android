package kumo.kbase_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import java.util.HashMap;

import kumo.kbase_android.fragments.AplicacionesFragment;
import kumo.kbase_android.fragments.CodigoAccesoFragment;
import kumo.kbase_android.fragments.CrearClaveFragment;
import kumo.kbase_android.fragments.IntroducirClaveFragment;
import kumo.kbase_android.fragments.IntroducirCodigoValidacionFragment;
import kumo.kbase_android.fragments.SeleccionarPasswordFragment;
import kumo.kbase_android.fragments.TelefonoFragment;
import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Configuracion;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.model.boolean_Api;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.QuickstartPreferences;

public class RegistroActivity extends AppCompatActivity
        implements
        CodigoAccesoFragment.onCodigoAccesoFragmentInteractionListener,
        TelefonoFragment.OnTelefonoFragmentInteractionListener,
        AplicacionesFragment.OnAplicacionesFragmentInteractionListener,
        SeleccionarPasswordFragment.OnSeleccionarPasswordFragmentInteractionListener,
        IntroducirCodigoValidacionFragment.onIntroducirCodigoValidacionFragmentInteraction,
        IntroducirClaveFragment.onIntroducirClaveFragmentInteraction,
        CrearClaveFragment.onCrearClaveFragmentInteractionListener
        {

    public Configuracion mConfiguracion;
    public String mCodigo_Acceso;
    public String mTelefono;
    public String mPrefijo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean si_Registro = sharedPreferences.getString(QuickstartPreferences.USUARIO_REGISTRADO,"0") == "0";

        if(si_Registro) {

            setContentView(R.layout.registro_activity);

            Fragment fragment = new CodigoAccesoFragment();

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.registro_contenido, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
        }else{
            saltarRegistro();
        }
    }

    public void onCodigoAccesoFragmentInteractionListener(String _codigo_acceso) {
        Log.d("Interaction", _codigo_acceso);

        mCodigo_Acceso = _codigo_acceso;

        Fragment fragment = new TelefonoFragment().newInstance(_codigo_acceso);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter,R.anim.pop_exit)
                .replace(R.id.registro_contenido, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();

        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }

    public void OnTelefonoFragmentInteractionListener(String _codigo_acceso, String _prefijo, String _telefono) {

        Log.d("Interaction", _codigo_acceso);

        mPrefijo = _prefijo;
        mTelefono = _telefono;

        Fragment fragment = new AplicacionesFragment().newInstance(_codigo_acceso,_prefijo,_telefono);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter,R.anim.pop_exit)
                .replace(R.id.registro_contenido, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }

    public void OnAplicacionesFragmentInteractionListener(String _estado, Configuracion _configuracion) {

        if( _estado == "Ok") {

            mConfiguracion = _configuracion;

            Fragment fragment = new SeleccionarPasswordFragment().newInstance(mConfiguracion);

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.registro_contenido, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();

            Log.d("Interaction", _configuracion.Id_Aplicacion);
        }else{
            if(_estado == "Ko"){
                Fragment fragment = new TelefonoFragment().newInstance(mCodigo_Acceso);

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter,R.anim.pop_exit)
                        .replace(R.id.registro_contenido, fragment)
                        .commit();

            }
        }

    }

    public void OnSeleccionarPasswordFragmentInteractionListener(String _tipo) {

        Fragment fragment;

        if(_tipo == "crear")
        {
            fragment = new IntroducirCodigoValidacionFragment().newInstance(mConfiguracion);
        }
        else
        {
            fragment = new IntroducirClaveFragment().newInstance(mConfiguracion);
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter,R.anim.pop_exit)
                .replace(R.id.registro_contenido, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();

        Log.d("Interaction", "asd");
    }


    public void onIntroducirCodigoValidacionFragmentInteraction(String _codigo_validacion){

        Fragment fragment = new CrearClaveFragment().newInstance(mConfiguracion,_codigo_validacion);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter,R.anim.pop_exit)
                .replace(R.id.registro_contenido, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    public void onIntroducirClaveFragmentInteraction(Usuario _usuario){

        guardar_Usuario(mConfiguracion, _usuario);


        Log.d("Interaction", "asd");
    }

    public void onCrearClaveFragmentInteractionListener(Usuario _usuario){

        guardar_Usuario(mConfiguracion, _usuario);

        Log.d("Interaction", "asd");
    }


    public void guardar_Usuario(Configuracion _configuracion, Usuario _usuario){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(QuickstartPreferences.USUARIO_REGISTRADO,"1").apply();

        String token = sharedPreferences.getString(QuickstartPreferences.ID_TOKEN,"");

        try {

            HashMap<String, String> params = new HashMap<String, String>();

            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("_id_aplicacion", mConfiguracion.Id_Aplicacion);
            jsonObject.addProperty("_id_usuario", mConfiguracion.Id_Usuario);
            jsonObject.addProperty("_id_usuario_clase", mConfiguracion.Id_Usuario_Clase);
            jsonObject.addProperty("_token", token);

            GsonRequest<boolean_Api> getPersons =
                    new GsonRequest<boolean_Api>(Request.Method.POST, findViewById(android.R.id.content), Constantes.USUARIO__REGISTRAR, boolean_Api.class,params,jsonObject,

                            new Response.Listener<boolean_Api>() {
                                @Override
                                public void onResponse(boolean_Api response) {

                                    if (response.Valor)
                                    {
                                        saltarRegistro();
                                    }
                                    else{

                                        Log.d("ValidacionFragment","codigo erroneo");
                                    }



                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("objeto", error.getMessage());
                            // TODO deal with error
                        }
                    });

            HttpCola.getInstance(this.getBaseContext()).addToRequestQueue(getPersons);

        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }



    }


    public void saltarRegistro(){

        Intent intent = new Intent(this,ConversacionesListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



        @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean si_Registro = sharedPreferences.getString(QuickstartPreferences.USUARIO_REGISTRADO,"0") == "0";

        if(!si_Registro) {

            saltarRegistro();
        }

    }

    @Override
    public void onBackPressed() {

         Fragment fragment_activo = (Fragment)getSupportFragmentManager().findFragmentById(R.id.registro_contenido);

        Log.d("Interaction", fragment_activo.getClass().getName());

        if(fragment_activo instanceof SeleccionarPasswordFragment){

            Fragment fragment = new TelefonoFragment().newInstance(mCodigo_Acceso);

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter,R.anim.pop_exit)
                    .replace(R.id.registro_contenido, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();

        }else{
            super.onBackPressed();
        }





    }
}

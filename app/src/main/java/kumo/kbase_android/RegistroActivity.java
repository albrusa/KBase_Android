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
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import kumo.kbase_android.db.DatabaseHelper;
import kumo.kbase_android.fragments.registro.AplicacionesFragment;
import kumo.kbase_android.fragments.registro.CodigoAccesoFragment;
import kumo.kbase_android.fragments.registro.CrearClaveFragment;
import kumo.kbase_android.fragments.registro.IntroducirClaveFragment;
import kumo.kbase_android.fragments.registro.IntroducirCodigoValidacionFragment;
import kumo.kbase_android.fragments.registro.SeleccionarPasswordFragment;
import kumo.kbase_android.fragments.registro.TelefonoFragment;
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

    public boolean si_Usuarios = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean si_Registro = sharedPreferences.getString(QuickstartPreferences.USUARIO_REGISTRADO,"0") == "0";

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            si_Registro = bundle.getString("si_Registro", "0").equals("1");

            if(si_Registro){
                si_Usuarios = true;
            }
        }
        si_Registro = false;


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

    @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean si_Registro = sharedPreferences.getString(QuickstartPreferences.USUARIO_REGISTRADO,"0") == "0";
        //boolean si_Registro = true;

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            si_Registro = bundle.getString("si_Registro", "0").equals("1");
            if(si_Registro){
                si_Usuarios = true;
            }
        }

        if(!si_Registro) {

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

        //guardar_Usuario(mConfiguracion, _usuario);
        guardar_Usuario_NoRegistro(mConfiguracion, _usuario);


        Log.d("Interaction", "asd");
    }

    public void onCrearClaveFragmentInteractionListener(Usuario _usuario){

        guardar_Usuario(mConfiguracion, _usuario);

        Log.d("Interaction", "asd");
    }


    public void guardar_Usuario_NoRegistro(final Configuracion _configuracion, final Usuario _usuario) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(QuickstartPreferences.USUARIO_REGISTRADO,"1").apply();

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(getBaseContext(), DatabaseHelper.class);

        try {
            Dao<Usuario, Integer> usuarioDao = databaseHelper.getUsuarioDao();

            _usuario.Aplicacion = _configuracion.Aplicacion;
            _usuario.Ruta_Imagen_Aplicacion = _configuracion.Ruta_Imagen_Aplicacion;


            if(usuarioDao.isTableExists()){

                List<Usuario> l_usuarios = usuarioDao.queryBuilder().where().eq("Id",_usuario.Id).query();

                if(l_usuarios.size() == 0){
                    usuarioDao.create(_usuario);

                }

            }else{

                usuarioDao.create(_usuario);
            }

            saltarRegistro();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void guardar_Usuario(final Configuracion _configuracion, final Usuario _usuario){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(QuickstartPreferences.USUARIO_REGISTRADO,"1").apply();

        String token = sharedPreferences.getString(QuickstartPreferences.ID_TOKEN,"");

        //Cal comentar despres
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(getBaseContext(), DatabaseHelper.class);
        try {
            /*Dao<Usuario, Integer> usuarioDao = databaseHelper.getUsuarioDao();
            Dao<Configuracion, Integer> configuracionDao = databaseHelper.getConfiguracionDao();

            if(usuarioDao.isTableExists()) {
                databaseHelper.dropUsuario();
            }else{
                databaseHelper.createUsuario();
            }

            if(configuracionDao.isTableExists()) {
                databaseHelper.dropConfiguracion();
            }else{
                databaseHelper.createConfiguracion();
            }*/



        }catch(Exception e){


        }

        try {

            HashMap<String, String> params = new HashMap<String, String>();

            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("_id_aplicacion", mConfiguracion.Id_Aplicacion);
            jsonObject.addProperty("_id_usuario", mConfiguracion.Id_Usuario);
            jsonObject.addProperty("_id_usuario_clase", mConfiguracion.Id_Usuario_Clase);
            jsonObject.addProperty("_token", token);

            GsonRequest<boolean_Api> getPersons =
                    new GsonRequest<boolean_Api>(Request.Method.POST, Constantes.USUARIO__REGISTRAR, boolean_Api.class,params,jsonObject,

                            new Response.Listener<boolean_Api>() {
                                @Override
                                public void onResponse(boolean_Api response) {

                                    if (response.Valor)
                                    {

                                        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(getBaseContext(), DatabaseHelper.class);

                                        try {
                                            Dao<Usuario, Integer> usuarioDao = databaseHelper.getUsuarioDao();

                                            _usuario.Aplicacion = _configuracion.Aplicacion;
                                            _usuario.Ruta_Imagen_Aplicacion = _configuracion.Ruta_Imagen_Aplicacion;


                                            if(usuarioDao.isTableExists()){

                                                List<Usuario> l_usuarios = usuarioDao.queryBuilder().where().eq("Id",_usuario.Id).query();

                                                if(l_usuarios.size() == 0){
                                                    usuarioDao.create(_usuario);
                                                }

                                            }else{

                                                usuarioDao.create(_usuario);
                                            }
                                            saltarRegistro();

                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
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

        Intent intent = new Intent(this,UsuariosListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
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

            if(fragment_activo instanceof CodigoAccesoFragment && si_Usuarios){

                Intent intent = new Intent(getBaseContext(), UsuariosListActivity.class);
                Bundle b = new Bundle();

                startActivity(intent);

            }else{

                super.onBackPressed();
            }
        }
    }
}

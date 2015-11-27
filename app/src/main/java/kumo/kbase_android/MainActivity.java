package kumo.kbase_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import kumo.kbase_android.adapters.MainTabAdapter;
import kumo.kbase_android.fragments.conversacionesList.ConversacionesListFragment;
import kumo.kbase_android.fragments.documentosList.DocumentosListFragment;
import kumo.kbase_android.fragments.informacionesList.InformacionesListFragment;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.Cookies;
import kumo.kbase_android.utils.ObjectPreference;
import kumo.kbase_android.utils.QuickstartPreferences;


public class MainActivity extends AppCompatActivity
 implements ConversacionesListFragment.OnConversacionesListFragmentInteractionListener,
        DocumentosListFragment.OnDocumentosListFragmentInteractionListener,
        InformacionesListFragment.OnInformacionesListFragmentInteractionListener
    {

    private Usuario mUsuario;
    private ObjectPreference objectPreference;
    private AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        //overridePendingTransition(R.anim.enter, R.anim.hold);

        Bundle bundle = this.getIntent().getExtras();

        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mId_Usuario = sharedPreferences.getString(QuickstartPreferences.USUARIO_ACTIVO,"0");

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        try {
            Dao<Usuario, Integer> usuarioDao = databaseHelper.getUsuarioDao();

            List<Usuario> l_usuarios = usuarioDao.queryBuilder().where().eq("Id",mId_Usuario).query();

            if(l_usuarios.size() > 0){
                mUsuario = l_usuarios.get(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        objectPreference = new ObjectPreference();
        objectPreference.init(getBaseContext());
        Cookies cookie = objectPreference.getComplexPreference();

        mUsuario = cookie.getObject(QuickstartPreferences.USUARIO_ACTIVO, Usuario.class);

        int tab_seleccionado = cookie.getObject(QuickstartPreferences.TAB_ACTIVO, int.class);

        if(mUsuario.Id_Clase.equals("BB60C904-701F-43CE-8E63-5E4F9D528E93")) {

            setContentView(R.layout.main_activity);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(new MainTabAdapter(
                    getSupportFragmentManager(), mUsuario, getApplicationContext()));

            viewPager.setCurrentItem(tab_seleccionado);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.appbartabs);
            tabLayout.setupWithViewPager(viewPager);



        }else{

            setContentView(R.layout.main_pro_activity);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ConversacionesListFragment conversacionesFragment = ConversacionesListFragment.newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_contenido, conversacionesFragment)
                    .commit();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


    @Override
    public void onPause(){
        super.onPause();
       // overridePendingTransition(R.anim.enter,R.anim.hold);
    }

    @Override
    public void onStop(){
        super.onStop();

       timerHandler.postDelayed(timerRunnable, 30000);

        // this.finish();
    }

    @Override
    protected void onDestroy(){
        timerHandler.removeCallbacks(timerRunnable);
        super.onDestroy();super.finish();
    }

    public void OnConversacionesListFragmentInteractionListener(String _id_conversacion, String _nombre, String _imagen){


        Intent intent = new Intent(getBaseContext(), MensajesListActivity.class);
        Bundle b = new Bundle();

        b.putString("Id_Conversacion", _id_conversacion);
        b.putString("Nombre", _nombre);
        b.putString("Imagen", _imagen);
        intent.putExtras(b);

        Cookies cookie = objectPreference.getComplexPreference();
        if (cookie != null) {
            cookie.putObject(QuickstartPreferences.TAB_ACTIVO, 1);
            cookie.commit();
        }


        startActivity(intent);
        Log.d("Interaction", "asd");
    }

    public void OnDocumentosListFragmentInteractionListener(){

        Log.d("Interaction", "asd");
    }

    public void OnInformacionesListFragmentInteractionListener(String _id_noticia, String _titulo, String _texto){

        Intent intent = new Intent(getBaseContext(), InformacionActivity.class);
        Bundle b = new Bundle();

        b.putString("Id_Conversacion", _id_noticia);
        b.putString("Titulo", _titulo);
        b.putString("Texto", _texto);
        intent.putExtras(b);

        Cookies cookie = objectPreference.getComplexPreference();
        if (cookie != null) {
            cookie.putObject(QuickstartPreferences.TAB_ACTIVO, 0);
            cookie.commit();
        }

        startActivity(intent);
        Log.d("Interaction", "asd");
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            mActivity.finish();
            timerHandler.postDelayed(this, 500);
        }
    };
}

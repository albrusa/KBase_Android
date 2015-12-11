package kumo.kbase_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import kumo.kbase_android.adapters.MainTabAdapter;
import kumo.kbase_android.fragments.conversacionesList.ConversacionesListFragment;
import kumo.kbase_android.fragments.documentosList.DocumentosListFragment;
import kumo.kbase_android.fragments.informacionesList.InformacionesListFragment;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.BaseAppCompatActivity;
import kumo.kbase_android.utils.Cookies;
import kumo.kbase_android.utils.ObjectPreference;
import kumo.kbase_android.utils.QuickstartPreferences;


public class MainActivity extends BaseAppCompatActivity
 implements ConversacionesListFragment.OnConversacionesListFragmentInteractionListener,
        DocumentosListFragment.OnDocumentosListFragmentInteractionListener,
        InformacionesListFragment.OnInformacionesListFragmentInteractionListener
    {

    private Usuario mUsuario;
    private ObjectPreference objectPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //overridePendingTransition(R.anim.enter, R.anim.hold);

        Bundle bundle = this.getIntent().getExtras();


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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Configuracion:
                return abrir_configuracion();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean abrir_configuracion(){
        Intent intent = new Intent(getBaseContext(), ConfiguracionActivity.class);

        Cookies cookie = objectPreference.getComplexPreference();
        if (cookie != null) {
            cookie.putObject(QuickstartPreferences.TAB_ACTIVO, 1);
            cookie.commit();
        }


        startActivity(intent);

        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        //timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        //timerHandler.removeCallbacks(timerRunnable);
        super.onDestroy();
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
}

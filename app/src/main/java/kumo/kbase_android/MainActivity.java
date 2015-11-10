package kumo.kbase_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import kumo.kbase_android.adapters.MainTabAdapter;
import kumo.kbase_android.db.DatabaseHelper;
import kumo.kbase_android.fragments.conversacionesList.ConversacionesListFragment;
import kumo.kbase_android.fragments.documentosList.DocumentosListFragment;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.QuickstartPreferences;


public class MainActivity extends AppCompatActivity
 implements ConversacionesListFragment.OnConversacionesListFragmentInteractionListener,
        DocumentosListFragment.OnDocumentosListFragmentInteractionListener
    {

    private String mId_Usuario;
    private Usuario mUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //overridePendingTransition(R.anim.enter, R.anim.hold);

        Bundle bundle = this.getIntent().getExtras();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
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
        }



        if(mUsuario.Id_Clase.equals("BB60C904-701F-43CE-8E63-5E4F9D528E93")) {

            setContentView(R.layout.main_activity);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(new MainTabAdapter(
                    getSupportFragmentManager(), mUsuario));

            TabLayout tabLayout = (TabLayout) findViewById(R.id.appbartabs);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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
    protected void onDestroy(){
        super.onDestroy();
    }

    public void OnConversacionesListFragmentInteractionListener(String _id_conversacion){


        Intent intent = new Intent(getBaseContext(), MensajesListActivity.class);
        Bundle b = new Bundle();

        b.putString("Id_Conversacion", _id_conversacion);
        intent.putExtras(b);

        startActivity(intent);
        Log.d("Interaction", "asd");
    }

    public void OnDocumentosListFragmentInteractionListener(){

        Log.d("Interaction", "asd");
    }
}

package kumo.kbase_android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import kumo.kbase_android.db.DatabaseHelper;
import kumo.kbase_android.fragments.conversacionesList.MensajesListFragment;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.QuickstartPreferences;

public class MensajesListActivity extends AppCompatActivity implements MensajesListFragment.OnMensajesListFragmentInteractionListener{

    private String mId_Usuario;
    private Usuario mUsuario;

    private String mId_Conversacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajes_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = this.getIntent().getExtras();
        mId_Conversacion = bundle.getString("Id_Conversacion");

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


        Fragment fragment = new MensajesListFragment().newInstance(mUsuario.Id_Aplicacion,mUsuario.Id,mUsuario.Id_Clase, mId_Conversacion);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_contenido, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();


    }

        @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        //overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);
    }


    public void OnMensajesListFragmentInteractionListener(){


    }
}

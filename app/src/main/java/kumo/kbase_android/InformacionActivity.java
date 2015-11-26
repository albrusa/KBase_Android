package kumo.kbase_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import kumo.kbase_android.fragments.conversacionesList.MensajesListFragment;
import kumo.kbase_android.fragments.informacionesList.InformacionFragment;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.Cookies;
import kumo.kbase_android.utils.ObjectPreference;
import kumo.kbase_android.utils.QuickstartPreferences;

public class InformacionActivity extends AppCompatActivity implements MensajesListFragment.OnMensajesListFragmentInteractionListener {

    private Usuario mUsuario;

    private String mTitulo;
    private String mTexto;

    private String mId_Noticia;

    private ObjectPreference objectPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajes_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = this.getIntent().getExtras();
        mId_Noticia = bundle.getString("Id_Noticia");
        mTitulo = bundle.getString("Titulo");
        mTexto = bundle.getString("Texto");


        objectPreference = new ObjectPreference();
        objectPreference.init(getBaseContext());
        Cookies cookie = objectPreference.getComplexPreference();

        mUsuario = cookie.getObject(QuickstartPreferences.USUARIO_ACTIVO, Usuario.class);

        Fragment fragment = new InformacionFragment().newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase, mId_Noticia,mTitulo,mTexto);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_contenido, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
        }


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
    public void onPause(){
        super.onPause();
        //this.finish();
        // overridePendingTransition(R.anim.enter,R.anim.hold);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        //super.onBackPressed();
//        getFragmentManager().popBackStack();
        //finish();
        //overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);
    }


    public void OnMensajesListFragmentInteractionListener(){


    }

}

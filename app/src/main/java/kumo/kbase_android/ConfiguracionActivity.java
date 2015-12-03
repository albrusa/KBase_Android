package kumo.kbase_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import kumo.kbase_android.fragments.configuracion.ConfiguracionFragment;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.Cookies;
import kumo.kbase_android.utils.ObjectPreference;
import kumo.kbase_android.utils.QuickstartPreferences;


public class ConfiguracionActivity extends AppCompatActivity
{

    private Usuario mUsuario;
    private ObjectPreference objectPreference;
    private AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        objectPreference = new ObjectPreference();
        objectPreference.init(getBaseContext());
        Cookies cookie = objectPreference.getComplexPreference();

        mUsuario = cookie.getObject(QuickstartPreferences.USUARIO_ACTIVO, Usuario.class);

        setContentView(R.layout.configuracion_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        new ConfiguracionFragment();
        Fragment fragment = ConfiguracionFragment.newInstance();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.configuracion_contenido, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //this.finish();
        NavUtils.navigateUpFromSameTask(this);
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
        super.onDestroy();super.finish();
    }
}

package kumo.kbase_android;

import android.os.Bundle;

import kumo.kbase_android.fragments.usuariosList.PinFragment;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.BaseAppCompatActivity;
import kumo.kbase_android.utils.Cookies;
import kumo.kbase_android.utils.ObjectPreference;
import kumo.kbase_android.utils.QuickstartPreferences;


public class PinActivity extends BaseAppCompatActivity
{

    private Usuario mUsuario;
    private ObjectPreference objectPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objectPreference = new ObjectPreference();
        objectPreference.init(getBaseContext());
        Cookies cookie = objectPreference.getComplexPreference();

        mUsuario = cookie.getObject(QuickstartPreferences.USUARIO_ACTIVO, Usuario.class);

        setContentView(R.layout.pin_activity);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        PinFragment pinFragment = PinFragment.newInstance(mUsuario.PIN);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.pin_contenido, pinFragment)
                .commit();


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
        super.onDestroy();
    }
}

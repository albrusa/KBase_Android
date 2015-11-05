package kumo.kbase_android;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import kumo.kbase_android.fragments.AplicacionesFragment;
import kumo.kbase_android.fragments.CodigoAccesoFragment;
import kumo.kbase_android.fragments.CrearClaveFragment;
import kumo.kbase_android.fragments.IntroducirCodigoValidacionFragment;
import kumo.kbase_android.fragments.SeleccionarPasswordFragment;
import kumo.kbase_android.fragments.TelefonoFragment;
import kumo.kbase_android.model.Configuracion;

public class RegistroActivity extends AppCompatActivity
        implements
        CodigoAccesoFragment.onCodigoAccesoFragmentInteraction,
        TelefonoFragment.OnTelefonoFragmentInteractionListener,
        AplicacionesFragment.OnAplicacionesFragmentInteractionListener,
        SeleccionarPasswordFragment.OnSeleccionarPasswordFragmentInteractionListener,
        IntroducirCodigoValidacionFragment.onIntroducirCodigoValidacionFragmentInteraction
        {

    public Configuracion mConfiguracion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);

        Fragment fragment = new CodigoAccesoFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.registro_contenido, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(fragment.getClass().getName())
        .commit();
    }

    public void onCodigoAccesoFragmentInteraction(String codigo_acceso) {
        Log.d("Interaction", codigo_acceso);

        Fragment fragment = new TelefonoFragment().newInstance(codigo_acceso);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.registro_contenido, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(fragment.getClass().getName())
                .commit();

        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }

    public void OnTelefonoFragmentInteractionListener(String codigo_acceso, String prefijo, String telefono) {

        Log.d("Interaction", codigo_acceso);

        Fragment fragment = new AplicacionesFragment().newInstance(codigo_acceso,prefijo,telefono);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.registro_contenido, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(fragment.getClass().getName())
                .commit();
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }

    public void OnAplicacionesFragmentInteractionListener(Configuracion _configuracion) {

        mConfiguracion = _configuracion;

        Fragment fragment = new SeleccionarPasswordFragment().newInstance(mConfiguracion);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.registro_contenido, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(fragment.getClass().getName())
                .commit();

        Log.d("Interaction", _configuracion.Id_Aplicacion);

    }

    public void OnSeleccionarPasswordFragmentInteractionListener(String _tipo) {

        Fragment fragment;

        if(_tipo == "crear")
        {
            fragment = new IntroducirCodigoValidacionFragment().newInstance(mConfiguracion);
        }
        else
        {
            fragment = new IntroducirCodigoValidacionFragment().newInstance(mConfiguracion);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.registro_contenido, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(fragment.getClass().getName())
                .commit();

        Log.d("Interaction", "asd");
    }


    public void onIntroducirCodigoValidacionFragmentInteraction(String _codigo_validacion){

        Fragment fragment = new CrearClaveFragment().newInstance(mConfiguracion,_codigo_validacion);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.registro_contenido, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

        @Override
    protected void onResume(){
        super.onResume();
    }
}

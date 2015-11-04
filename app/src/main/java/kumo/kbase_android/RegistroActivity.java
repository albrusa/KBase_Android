package kumo.kbase_android;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import kumo.kbase_android.fragments.AplicacionesFragment;
import kumo.kbase_android.fragments.CodigoAccesoFragment;
import kumo.kbase_android.fragments.TelefonoFragment;

public class RegistroActivity extends AppCompatActivity
        implements
        CodigoAccesoFragment.onCodigoAccesoFragmentInteraction,
        TelefonoFragment.OnTelefonoFragmentInteractionListener,
        AplicacionesFragment.OnAplicacionesFragmentInteractionListener{

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

    public void OnAplicacionesFragmentInteractionListener(String codigo_acceso, String prefijo, String telefono) {

        Log.d("Interaction", codigo_acceso);

    }




        @Override
    protected void onResume(){
        super.onResume();
    }
}

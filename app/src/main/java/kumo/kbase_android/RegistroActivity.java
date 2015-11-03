package kumo.kbase_android;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import kumo.kbase_android.fragments.CodigoAccesoFragment;
import kumo.kbase_android.fragments.TelefonoFragment;

public class RegistroActivity extends AppCompatActivity implements CodigoAccesoFragment.OnFragmentInteractionListener,TelefonoFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);

        Fragment fragment = new CodigoAccesoFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.registro_contenido, fragment)
                .commit();
    }

    public void onFragmentInteraction(Uri uri) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }


        @Override
    protected void onResume(){
        super.onResume();
    }
}

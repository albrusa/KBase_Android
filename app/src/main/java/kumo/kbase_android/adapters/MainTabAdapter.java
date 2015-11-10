package kumo.kbase_android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kumo.kbase_android.fragments.conversacionesList.ConversacionesListFragment;
import kumo.kbase_android.fragments.documentosList.DocumentosListFragment;
import kumo.kbase_android.model.Usuario;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class MainTabAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] =
            new String[] { "Tab Uno", "Tab Dos", "Tab Tres"};

    private Usuario mUsuario;
    private DocumentosListFragment documentosFragment;
    private ConversacionesListFragment conversacionesFragment;
    private ConversacionesListFragment conversacionesFragment2;


    public MainTabAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainTabAdapter(FragmentManager fm, Usuario _usuario) {
        super(fm);
        mUsuario = _usuario;

        documentosFragment = DocumentosListFragment.newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase);
        conversacionesFragment = ConversacionesListFragment.newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase);
        conversacionesFragment2 = ConversacionesListFragment.newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase);

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        switch(position) {
            case 0:
                //f = ConversacionesListFragment.newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase);
                f = conversacionesFragment;
                break;
            case 1:
                f = documentosFragment;
                break;
            case 2:
                f = conversacionesFragment2;
                break;
        }

        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

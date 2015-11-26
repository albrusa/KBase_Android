package kumo.kbase_android.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kumo.kbase_android.R;
import kumo.kbase_android.fragments.conversacionesList.ConversacionesListFragment;
import kumo.kbase_android.fragments.documentosList.DocumentosListFragment;
import kumo.kbase_android.fragments.informacionesList.InformacionesListFragment;
import kumo.kbase_android.model.Usuario;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class MainTabAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;

    private String tabTitles[] = new String[3];

    private Usuario mUsuario;
    private DocumentosListFragment documentosFragment;
    private ConversacionesListFragment conversacionesFragment;
    private InformacionesListFragment informacionesFragment;
    private Context mContext;


    public MainTabAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainTabAdapter(FragmentManager fm, Usuario _usuario, Context _context) {
        super(fm);
        mUsuario = _usuario;
        mContext = _context;

        tabTitles[0] = mContext.getResources().getString(R.string.Main_Informaciones);
        tabTitles[1] = mContext.getResources().getString(R.string.Main_Conversaciones);
        tabTitles[2] = mContext.getResources().getString(R.string.Main_Documentos);

        documentosFragment = DocumentosListFragment.newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase);
        conversacionesFragment = ConversacionesListFragment.newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase);
        informacionesFragment = InformacionesListFragment.newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase);

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
                f = informacionesFragment;

                break;
            case 1:
                f = conversacionesFragment;

                break;
            case 2:
                f = documentosFragment;
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

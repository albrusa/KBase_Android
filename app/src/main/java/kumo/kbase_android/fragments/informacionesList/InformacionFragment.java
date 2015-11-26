package kumo.kbase_android.fragments.informacionesList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kumo.kbase_android.R;


public class InformacionFragment extends Fragment {

    private static final String ID_APLICACION = "id_aplicacion";
    private static final String ID = "id";
    private static final String ID_CLASE = "id_clase";
    private static final String ID_NOTICIA = "id_noticia";
    private static final String TITULO = "titulo";
    private static final String TEXTO = "texto";


    private String mId_Aplicacion;
    private String mId;
    private String mId_Clase;
    private String mId_Noticia;
    private String mTitulo;
    private String mTexto;

    private TextView vTitulo;
    private TextView vTexto;

    public static InformacionFragment newInstance(String _id_Aplicacion, String _id, String _id_Clase, String _id_noticia, String _titulo, String _texto) {
        InformacionFragment fragment = new InformacionFragment();
        Bundle args = new Bundle();
        args.putString(ID_APLICACION, _id_Aplicacion);
        args.putString(ID, _id);
        args.putString(ID_CLASE, _id_Clase);
        args.putString(ID_NOTICIA, _id_noticia);
        args.putString(TITULO, _titulo);
        args.putString(TEXTO, _texto);
        fragment.setArguments(args);
        return fragment;
    }

    public InformacionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId_Aplicacion = getArguments().getString(ID_APLICACION);
            mId = getArguments().getString(ID);
            mId_Clase = getArguments().getString(ID_CLASE);
            mId_Noticia = getArguments().getString(ID_NOTICIA);
            mTitulo = getArguments().getString(TITULO);
            mTexto = getArguments().getString(TEXTO);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view =  inflater.inflate(R.layout.informacion_fragment, container, false);

        vTexto = (TextView) _view.findViewById(R.id.texto);
        vTitulo = (TextView) _view.findViewById(R.id.titulo);

        vTitulo.setText(mTitulo);
        vTexto.setText(mTexto);

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

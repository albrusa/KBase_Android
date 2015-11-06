package kumo.kbase_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import kumo.kbase_android.R;
import kumo.kbase_android.model.Configuracion;
import kumo.kbase_android.utils.Constantes;

public class CodigoAccesoFragment extends Fragment {

    private onCodigoAccesoFragmentInteractionListener mListener;


    private AutoCompleteTextView vCodigo_Acceso;
    private Button vSiguiente;

    private Configuracion mConfiguracion;
    private View mView;

    public static CodigoAccesoFragment  newInstance(Configuracion _configuracion) {
        CodigoAccesoFragment fragment = new CodigoAccesoFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.BUNDLE_CONFIGURACION,_configuracion);
        fragment.setArguments(args);
        return fragment;
    }

    public CodigoAccesoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mConfiguracion = (Configuracion)getArguments().getSerializable(Constantes.BUNDLE_CONFIGURACION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view =  inflater.inflate(R.layout.registro_usuario_fragment, container, false);

        vCodigo_Acceso = (AutoCompleteTextView) _view.findViewById(R.id.registro_codigo_acceso);


        //Cal comentar

        vCodigo_Acceso.setText("nmonfulleda");

        //Fi cal comentar

        vSiguiente  = (Button) _view.findViewById(R.id.registro_to_telefono);

        vSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codigo_Acceso = vCodigo_Acceso.getText().toString();
                if (!TextUtils.isEmpty(codigo_Acceso)) {
                    if (mListener != null) {
                        mListener.onCodigoAccesoFragmentInteractionListener(codigo_Acceso);
                    }
                }
            }
        });

        return _view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (onCodigoAccesoFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface onCodigoAccesoFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onCodigoAccesoFragmentInteractionListener(String codigo_acceso);
    }

}

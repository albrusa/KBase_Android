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

public class IntroducirClaveFragment extends Fragment {

    private onIntroducirClaveFragmentInteraction mListener;

    private AutoCompleteTextView vClave;
    private Button vSiguiente;

    private Configuracion mConfiguracion;

    public static IntroducirClaveFragment newInstance(Configuracion _configuracion) {
        IntroducirClaveFragment fragment = new IntroducirClaveFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.BUNDLE_CONFIGURACION,_configuracion);
        fragment.setArguments(args);
        return fragment;
    }

    public IntroducirClaveFragment() {
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
        View _view =  inflater.inflate(R.layout.registro_introducir_clave_fragment, container, false);

        vClave = (AutoCompleteTextView) _view.findViewById(R.id.registro_codigo_acceso);


        //Cal comentar

        vClave.setText("nmonfulleda");

        //Fi cal comentar

        vSiguiente  = (Button) _view.findViewById(R.id.registro_to_telefono);

        vSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String clave = vClave.getText().toString();
                if (!TextUtils.isEmpty(clave)) {
                    if (mListener != null) {
                        mListener.onIntroducirClaveFragmentInteraction();
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
            mListener = (onIntroducirClaveFragmentInteraction) getActivity();
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


    public interface onIntroducirClaveFragmentInteraction {
        // TODO: Update argument type and name
        public void onIntroducirClaveFragmentInteraction();
    }

}

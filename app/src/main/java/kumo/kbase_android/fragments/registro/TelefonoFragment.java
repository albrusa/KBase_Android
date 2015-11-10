package kumo.kbase_android.fragments.registro;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.adapters.PrefijosAdapter;
import kumo.kbase_android.model.xPrefijo_Telf;
import kumo.kbase_android.utils.Cultura;

public class TelefonoFragment extends Fragment {

    private OnTelefonoFragmentInteractionListener mListener;

    private static final String CODIGO_ACCESO = "codigo_acceso";

    private String mCodigo_Acceso;

    private AutoCompleteTextView vTelefono;
    private Spinner vPrefijo;
    private TextView vCodigo_Acceso;



    public static TelefonoFragment newInstance(String codigo_acceso) {
        TelefonoFragment fragment = new TelefonoFragment();
        Bundle args = new Bundle();
        args.putString(CODIGO_ACCESO, codigo_acceso);
        fragment.setArguments(args);
        return fragment;
    }

    public TelefonoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCodigo_Acceso = getArguments().getString(CODIGO_ACCESO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view =  inflater.inflate(R.layout.registro_telefono_fragment, container, false);

        vTelefono = (AutoCompleteTextView) _view.findViewById(R.id.registro_telefono);

        vCodigo_Acceso = (TextView)_view.findViewById(R.id.registro_codigo_acceso);
        vCodigo_Acceso.setText(mCodigo_Acceso);


        vPrefijo = ( Spinner )_view.findViewById( R.id.registro_prefijo );
        List<xPrefijo_Telf> prefijos = Cultura.obt_prefijos();
        PrefijosAdapter adapter = new PrefijosAdapter(_view.getContext(),android.R.layout.select_dialog_singlechoice,prefijos);

        vPrefijo.setAdapter(adapter);

        //Cal comentar a la versió final

        for (int i = 0; i < vPrefijo.getCount(); i++) {
            if (((xPrefijo_Telf)vPrefijo.getItemAtPosition(i)).iX.equals("34")) {
                vPrefijo.setSelection(i);
                break;
            }
        }
        vTelefono.setText("650595821");

        //Fi del comentari


        Button mSiguiente = (Button) _view.findViewById(R.id.registro_to_telefono);
        mSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prefijo = ((xPrefijo_Telf) vPrefijo.getSelectedItem()).iX;
                String telefono = vTelefono.getText().toString();
                String codigo_acceso = vCodigo_Acceso.getText().toString();

                if (!TextUtils.isEmpty(telefono)) {

                    if (mListener != null) {
                        mListener.OnTelefonoFragmentInteractionListener(codigo_acceso,prefijo,telefono);
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
            mListener = (OnTelefonoFragmentInteractionListener) getActivity();
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


    public interface OnTelefonoFragmentInteractionListener {
        // TODO: Update argument type and name
        public void OnTelefonoFragmentInteractionListener(String codigo_acceso, String prefijo, String telefono);
    }

}

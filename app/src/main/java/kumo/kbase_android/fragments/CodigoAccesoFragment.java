package kumo.kbase_android.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import kumo.kbase_android.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CodigoAccesoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CodigoAccesoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CodigoAccesoFragment extends Fragment {

    private onCodigoAccesoFragmentInteraction mListener;
    private AutoCompleteTextView mCodigo_Acceso;
    private Button mSiguiente;

    public static CodigoAccesoFragment newInstance() {
        CodigoAccesoFragment fragment = new CodigoAccesoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CodigoAccesoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view =  inflater.inflate(R.layout.registro_usuario_fragment, container, false);

        mCodigo_Acceso = (AutoCompleteTextView) _view.findViewById(R.id.registro_codigo_acceso);
        mSiguiente  = (Button) _view.findViewById(R.id.registro_to_telefono);

        mSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codigo_Acceso = mCodigo_Acceso.getText().toString();
                if (!TextUtils.isEmpty(codigo_Acceso)) {
                    if (mListener != null) {
                        mListener.onCodigoAccesoFragmentInteraction(codigo_Acceso);
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
            mListener = (onCodigoAccesoFragmentInteraction) getActivity();
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


    public interface onCodigoAccesoFragmentInteraction {
        // TODO: Update argument type and name
        public void onCodigoAccesoFragmentInteraction(String codigo_acceso);
    }

}

package kumo.kbase_android.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kumo.kbase_android.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TelefonoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TelefonoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TelefonoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CODIGO_ACCESO = "codigo_acceso";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mCodigo_Acceso;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
            mListener = (OnFragmentInteractionListener) getActivity();
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

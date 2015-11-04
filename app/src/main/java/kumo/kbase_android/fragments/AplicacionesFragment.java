package kumo.kbase_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.adapters.PrefijosAdapter;
import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Configuracion;
import kumo.kbase_android.model.xPrefijo_Telf;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.Cultura;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AplicacionesFragment.OnAplicacionesFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AplicacionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AplicacionesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CODIGO_ACCESO = "codigo_acceso";
    private static final String PREFIJO = "prefijo";
    private static final String TELEFONO = "telefono";

    // TODO: Rename and change types of parameters
    private String mCodigo_Acceso;
    private String mPrefijo;
    private String mTelefono;

    private AutoCompleteTextView vTelefono;
    private Spinner vPrefijo;
    private TextView vCodigo_Acceso;

    private OnAplicacionesFragmentInteractionListener mListener;

    public static AplicacionesFragment newInstance(String codigo_acceso, String prefijo, String telefono) {
        AplicacionesFragment fragment = new AplicacionesFragment();
        Bundle args = new Bundle();
        args.putString(CODIGO_ACCESO, codigo_acceso);
        args.putString(PREFIJO, prefijo);
        args.putString(TELEFONO, telefono);
        fragment.setArguments(args);
        return fragment;
    }

    public AplicacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCodigo_Acceso = getArguments().getString(CODIGO_ACCESO);
            mPrefijo = getArguments().getString(PREFIJO);
            mTelefono = getArguments().getString(TELEFONO);
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

        Button mSiguiente = (Button) _view.findViewById(R.id.registro_to_telefono);
        mSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        View _view = getView();

        HashMap<String, String> params = new HashMap<String, String>();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_codigo_acceso", mCodigo_Acceso);
        jsonObject.addProperty("_prefijo", mPrefijo);
        jsonObject.addProperty("_movil", mTelefono);

        try {
            GsonRequest<Configuracion[]> getPersons =
                    new GsonRequest<Configuracion[]>(Request.Method.POST, _view.findViewById(android.R.id.content), Constantes.USUARIO__OBT_APLICACIONES, Configuracion[].class,params,jsonObject,

                            new Response.Listener<Configuracion[]>() {
                                @Override
                                public void onResponse(Configuracion[] response) {
                                    List<Configuracion> persons = Arrays.asList(response);
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("objeto", error.getMessage());
                            // TODO deal with error
                        }
                    });

            HttpCola.getInstance(_view.getContext()).addToRequestQueue(getPersons);

        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnAplicacionesFragmentInteractionListener) getActivity();
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


    public interface OnAplicacionesFragmentInteractionListener {
        // TODO: Update argument type and name
        public void OnAplicacionesFragmentInteractionListener(String codigo_acceso, String prefijo, String telefono);
    }

}

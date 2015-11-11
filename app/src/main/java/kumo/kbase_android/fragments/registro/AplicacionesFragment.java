package kumo.kbase_android.fragments.registro;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Configuracion;
import kumo.kbase_android.utils.Constantes;


public class AplicacionesFragment extends Fragment {

    private static final String CODIGO_ACCESO = "codigo_acceso";
    private static final String PREFIJO = "prefijo";
    private static final String TELEFONO = "telefono";

    private String mCodigo_Acceso;
    private String mPrefijo;
    private String mTelefono;

    private Button vSiguiente;
    private ProgressBar vProgreso;

    private OnAplicacionesFragmentInteractionListener mListener;

    public static AplicacionesFragment newInstance(String _codigo_acceso, String _prefijo, String _telefono) {
        AplicacionesFragment fragment = new AplicacionesFragment();
        Bundle args = new Bundle();
        args.putString(CODIGO_ACCESO, _codigo_acceso);
        args.putString(PREFIJO, _prefijo);
        args.putString(TELEFONO, _telefono);
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
        View _view =  inflater.inflate(R.layout.registro_aplicaciones_fragment, container, false);

        vSiguiente  = (Button) _view.findViewById(R.id.registro_to_telefono);
        vProgreso  = (ProgressBar) _view.findViewById(R.id.circular_progress);

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        final View _view = getView();

        HashMap<String, String> params = new HashMap<>();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_codigo_acceso", mCodigo_Acceso);
        jsonObject.addProperty("_prefijo", mPrefijo);
        jsonObject.addProperty("_movil", mTelefono);

        try {
            GsonRequest<Configuracion[]> getPersons =
                    new GsonRequest<>(Request.Method.POST, _view.findViewById(android.R.id.content), Constantes.USUARIO__OBT_APLICACIONES, Configuracion[].class,params,jsonObject,

                            new Response.Listener<Configuracion[]>() {
                                @Override
                                public void onResponse(Configuracion[] response) {
                                    List<Configuracion> l_configuraciones = Arrays.asList(response);

                                    LinearLayout layout = (LinearLayout) _view.findViewById(R.id.Registro_layout);

                                    if (l_configuraciones.size() == 1) {
                                        mListener.OnAplicacionesFragmentInteractionListener("Ok", l_configuraciones.get(0));
                                    } else {

                                        vSiguiente.setVisibility(View.VISIBLE);
                                        vProgreso.setVisibility(View.GONE);

                                        for (Configuracion config : l_configuraciones) {

                                            TextView text = new TextView(getContext());
                                            text.setTextColor(Color.BLACK);
                                            text.setText(config.Aplicacion);

                                            layout.addView(text);
                                        }
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("objeto", error.getMessage());
                            mListener.OnAplicacionesFragmentInteractionListener("Ko", null);
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
        void OnAplicacionesFragmentInteractionListener(String _estado, Configuracion _configuracion);
    }

}

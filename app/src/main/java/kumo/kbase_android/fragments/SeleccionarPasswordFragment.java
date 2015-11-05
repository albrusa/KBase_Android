package kumo.kbase_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import java.util.HashMap;

import kumo.kbase_android.R;
import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Configuracion;
import kumo.kbase_android.model.boolean_Api;
import kumo.kbase_android.utils.Constantes;

public class SeleccionarPasswordFragment extends Fragment {

    private OnSeleccionarPasswordFragmentInteractionListener mListener;

    private Button vCrear;
    private Button vIntroducir;

    private Configuracion mConfiguracion;
    private View mView;



    public static SeleccionarPasswordFragment newInstance(Configuracion _configuracion) {
        SeleccionarPasswordFragment fragment = new SeleccionarPasswordFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.BUNDLE_CONFIGURACION,_configuracion);
        fragment.setArguments(args);
        return fragment;
    }

    public SeleccionarPasswordFragment() {
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
        View _view =  inflater.inflate(R.layout.registro_seleccionar_password_fragment, container, false);
        mView = _view;

        vCrear  = (Button) _view.findViewById(R.id.crear_password);
        vIntroducir  = (Button) _view.findViewById(R.id.introducir_password);

        vCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    HashMap<String, String> params = new HashMap<String, String>();

                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("_id_aplicacion", mConfiguracion.Id_Aplicacion);
                    jsonObject.addProperty("_id_usuario", mConfiguracion.Id_Usuario);
                    jsonObject.addProperty("_id_usuario_Clase", mConfiguracion.Id_Usuario_Clase);

                    GsonRequest<boolean_Api> getPersons =
                            new GsonRequest<boolean_Api>(Request.Method.POST, mView.findViewById(android.R.id.content), Constantes.USUARIO__GENERAR_CODIGO_VERIFICACION, boolean_Api.class,params,jsonObject,

                                    new Response.Listener<boolean_Api>() {
                                        @Override
                                        public void onResponse(boolean_Api response) {
                                            Log.d("respuesta", Boolean.toString(response.Valor));

                                            mListener.OnSeleccionarPasswordFragmentInteractionListener("crear");
                                        }
                                    }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("objeto", error.getMessage());
                                    // TODO deal with error
                                }
                            });

                    HttpCola.getInstance(mView.getContext()).addToRequestQueue(getPersons);

                } catch (Exception e) {
                    Log.d("Error",e.getMessage());
                }
            }
        });

        vIntroducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnSeleccionarPasswordFragmentInteractionListener("introducir");
            }
        });



        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        final View _view = getView();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnSeleccionarPasswordFragmentInteractionListener) getActivity();
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


    public interface OnSeleccionarPasswordFragmentInteractionListener {
        public void OnSeleccionarPasswordFragmentInteractionListener(String _tipo);
    }

}

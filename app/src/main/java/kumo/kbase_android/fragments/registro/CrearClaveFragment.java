package kumo.kbase_android.fragments.registro;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
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
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.model.boolean_Api;
import kumo.kbase_android.utils.Constantes;

public class CrearClaveFragment extends Fragment {

    private final static String CODIGO_VALIDACION = "codigo_validacion";

    private onCrearClaveFragmentInteractionListener mListener;


    private AutoCompleteTextView vClave;
    private AutoCompleteTextView vRepetir_Clave;
    private Button vSiguiente;


    private Configuracion mConfiguracion;
    private String mCodigo_Validacion;
    private View mView;
    private String mClave;


    public static CrearClaveFragment newInstance(Configuracion _configuracion, String _codigo_validacion) {
        CrearClaveFragment fragment = new CrearClaveFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.BUNDLE_CONFIGURACION,_configuracion);
        args.putString(CODIGO_VALIDACION, _codigo_validacion);
        fragment.setArguments(args);
        return fragment;
    }

    public CrearClaveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mConfiguracion = (Configuracion)getArguments().getSerializable(Constantes.BUNDLE_CONFIGURACION);
            mCodigo_Validacion = getArguments().getString(CODIGO_VALIDACION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view =  inflater.inflate(R.layout.registro_crear_clave_fragment, container, false);

        mView = _view;

        vClave = (AutoCompleteTextView) _view.findViewById(R.id.registro_clave);
        vRepetir_Clave = (AutoCompleteTextView) _view.findViewById(R.id.registro_repetir_clave);


        //Cal comentar

        vClave.setText("112233");
        vRepetir_Clave.setText("112233");

        //Fi cal comentar

        vSiguiente  = (Button) _view.findViewById(R.id.registro_to_telefono);

        vSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String clave = vClave.getText().toString();
                String repetir_clave = vRepetir_Clave.getText().toString();

                if (!TextUtils.isEmpty(clave) && !TextUtils.isEmpty(repetir_clave)) {

                    if(clave.equals(repetir_clave)) {

                        mClave = clave;

                        try {

                            HashMap<String, String> params = new HashMap<String, String>();

                            final JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("_id_aplicacion", mConfiguracion.Id_Aplicacion);
                            jsonObject.addProperty("_id_usuario", mConfiguracion.Id_Usuario);
                            jsonObject.addProperty("_id_usuario_Clase", mConfiguracion.Id_Usuario_Clase);
                            jsonObject.addProperty("_clave", clave);
                            jsonObject.addProperty("_codigo_verificacion", mCodigo_Validacion);

                            GsonRequest<boolean_Api> getPersons =
                                    new GsonRequest<boolean_Api>(Request.Method.POST, Constantes.USUARIO__ASIGNAR_CLAVE, boolean_Api.class,params,jsonObject,

                                            new Response.Listener<boolean_Api>() {
                                                @Override
                                                public void onResponse(boolean_Api response) {
                                                    if (response.Valor)
                                                    {

                                                        try {

                                                            HashMap<String, String> params = new HashMap<String, String>();

                                                            final JsonObject jsonObject = new JsonObject();
                                                            jsonObject.addProperty("_id_aplicacion", mConfiguracion.Id_Aplicacion);
                                                            jsonObject.addProperty("_id_usuario", mConfiguracion.Id_Usuario);
                                                            jsonObject.addProperty("_id_usuario_clase", mConfiguracion.Id_Usuario_Clase);
                                                            jsonObject.addProperty("_clave", mClave);

                                                            GsonRequest<Usuario> getPersons =
                                                                    new GsonRequest<Usuario>(Request.Method.POST,  Constantes.USUARIO__AUTENTIFICAR, Usuario.class,params,jsonObject,

                                                                            new Response.Listener<Usuario>() {
                                                                                @Override
                                                                                public void onResponse(Usuario response) {
                                                                                    Usuario usuario = response;


                                                                                    if (usuario != null)
                                                                                    {
                                                                                        mListener.onCrearClaveFragmentInteractionListener(usuario);
                                                                                    }
                                                                                    else{

                                                                                        Log.d("ValidacionFragment", "codigo erroneo");
                                                                                    }


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
                                                    else{

                                                        Log.d("CrearClave", "error");
                                                    }


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
                    }else{

                    }
                }else{

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
            mListener = (onCrearClaveFragmentInteractionListener) getActivity();
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


    public interface onCrearClaveFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onCrearClaveFragmentInteractionListener(Usuario usuario);
    }

}

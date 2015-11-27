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
import kumo.kbase_android.utils.Constantes;

public class IntroducirClaveFragment extends Fragment {

    private onIntroducirClaveFragmentInteraction mListener;

    private AutoCompleteTextView vClave;
    private Button vSiguiente;

    private Configuracion mConfiguracion;
    private View mView;

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

        mView = _view;

        vClave = (AutoCompleteTextView) _view.findViewById(R.id.registro_clave);


        //Cal comentar

        vClave.setText("nmonfulleda");

        //Fi cal comentar

        vSiguiente  = (Button) _view.findViewById(R.id.registro_to_telefono);

        vSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String clave = vClave.getText().toString();
                if (!TextUtils.isEmpty(clave)) {


                    try {

                        HashMap<String, String> params = new HashMap<String, String>();

                        final JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("_id_aplicacion", mConfiguracion.Id_Aplicacion);
                        jsonObject.addProperty("_id_usuario", mConfiguracion.Id_Usuario);
                        jsonObject.addProperty("_id_usuario_clase", mConfiguracion.Id_Usuario_Clase);
                        jsonObject.addProperty("_clave", clave);

                        GsonRequest<Usuario> getPersons =
                                new GsonRequest<Usuario>(Request.Method.POST,  Constantes.USUARIO__AUTENTIFICAR, Usuario.class,params,jsonObject,

                                        new Response.Listener<Usuario>() {
                                            @Override
                                            public void onResponse(Usuario response) {
                                                Usuario usuario = response;

                                                if (usuario != null)
                                                {
                                                    mListener.onIntroducirClaveFragmentInteraction(usuario);
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
        public void onIntroducirClaveFragmentInteraction(Usuario _usuario);
    }

}

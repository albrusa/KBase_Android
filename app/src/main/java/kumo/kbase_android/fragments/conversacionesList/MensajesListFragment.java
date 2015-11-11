package kumo.kbase_android.fragments.conversacionesList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.adapters.MensajesListAdapter;
import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Mensaje;
import kumo.kbase_android.utils.Constantes;


public class MensajesListFragment extends Fragment {

    private static final String ID_APLICACION = "id_aplicacion";
    private static final String ID = "id";
    private static final String ID_CLASE = "id_clase";
    private static final String ID_CONVERSACION = "id_conversacion";

    private String mId_Aplicacion;
    private String mId;
    private String mId_Clase;
    private String mId_Conversacion;

    private RecyclerView recView;
    private MensajesListAdapter adaptador;

    private OnMensajesListFragmentInteractionListener mListener;

    public static MensajesListFragment newInstance(String _id_Aplicacion, String _id, String _id_Clase, String _id_conversacion) {
        MensajesListFragment fragment = new MensajesListFragment();
        Bundle args = new Bundle();
        args.putString(ID_APLICACION, _id_Aplicacion);
        args.putString(ID, _id);
        args.putString(ID_CLASE, _id_Clase);
        args.putString(ID_CONVERSACION, _id_conversacion);
        fragment.setArguments(args);
        return fragment;
    }

    public MensajesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId_Aplicacion = getArguments().getString(ID_APLICACION);
            mId = getArguments().getString(ID);
            mId_Clase = getArguments().getString(ID_CLASE);
            mId_Conversacion = getArguments().getString(ID_CONVERSACION);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view =  inflater.inflate(R.layout.mensajes_list_fragment, container, false);

        recView  = (RecyclerView) _view.findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        final View _view = getView();

        HashMap<String, String> params = new HashMap<String, String>();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id_aplicacion", mId_Aplicacion);
        jsonObject.addProperty("_id_usuario", mId);
        jsonObject.addProperty("_id_usuario_clase", mId_Clase);
        jsonObject.addProperty("_id_conversacion", mId_Conversacion);

        try {
            GsonRequest<Mensaje[]> getPersons =
                    new GsonRequest<Mensaje[]>(Request.Method.POST, _view.findViewById(android.R.id.content), Constantes.CONVERSACIONES_OBT_MENSAJES, Mensaje[].class,params,jsonObject,

                            new Response.Listener<Mensaje[]>() {
                                @Override
                                public void onResponse(Mensaje[] response) {
                                    List<Mensaje> l_mensajes = Arrays.asList(response);

                                    adaptador = new MensajesListAdapter(l_mensajes);

                                    recView.setAdapter(adaptador);

                                    recView.setLayoutManager(
                                            new LinearLayoutManager(_view.getContext(), LinearLayoutManager.VERTICAL, false));



                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("objeto", error.getMessage());
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
            mListener = (OnMensajesListFragmentInteractionListener) getActivity();
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


    public interface OnMensajesListFragmentInteractionListener {
        public void OnMensajesListFragmentInteractionListener();
    }

}
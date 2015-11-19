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
import kumo.kbase_android.adapters.ConversacionesListAdapter;
import kumo.kbase_android.decorators.DividerItemDecoration;
import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Conversacion;
import kumo.kbase_android.utils.Constantes;


public class ConversacionesListFragment extends Fragment {

    private static final String ID_APLICACION = "id_aplicacion";
    private static final String ID = "id";
    private static final String ID_CLASE = "id_clase";

    private String mId_Aplicacion;
    private String mId;
    private String mId_Clase;

    private RecyclerView recView;
    private ConversacionesListAdapter adaptador;

    private List<Conversacion> l_conversaciones;

    private OnConversacionesListFragmentInteractionListener mListener;

    public static ConversacionesListFragment newInstance(String _id_Aplicacion, String _id, String _id_Clase) {
        ConversacionesListFragment fragment = new ConversacionesListFragment();
        Bundle args = new Bundle();
        args.putString(ID_APLICACION, _id_Aplicacion);
        args.putString(ID, _id);
        args.putString(ID_CLASE, _id_Clase);
        fragment.setArguments(args);
        return fragment;
    }

    public ConversacionesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId_Aplicacion = getArguments().getString(ID_APLICACION);
            mId = getArguments().getString(ID);
            mId_Clase = getArguments().getString(ID_CLASE);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view =  inflater.inflate(R.layout.conversaciones_list_fragment, container, false);

        recView  = (RecyclerView) _view.findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        recView.setLayoutManager(
                new LinearLayoutManager(_view.getContext(), LinearLayoutManager.VERTICAL, false));

        recView.addItemDecoration(
                new DividerItemDecoration(getActivity(), R.drawable.divider,76,10));

        adaptador = new ConversacionesListAdapter(getContext());
        recView.setAdapter(adaptador);

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnConversacionesListFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }

        final View _view = getView();

        HashMap<String, String> params = new HashMap<String, String>();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id_aplicacion", mId_Aplicacion);
        jsonObject.addProperty("_id_usuario", mId);
        jsonObject.addProperty("_id_usuario_clase", mId_Clase);

        try {
            GsonRequest<Conversacion[]> getPersons =
                    new GsonRequest<Conversacion[]>(Request.Method.POST, _view.findViewById(android.R.id.content), Constantes.CONVERSACIONES_OBT_CONVERSACIONES, Conversacion[].class,params,jsonObject,

                            new Response.Listener<Conversacion[]>() {
                                @Override
                                public void onResponse(Conversacion[] response) {
                                    l_conversaciones = Arrays.asList(response);

                                    /*adaptador = new ConversacionesListAdapter(l_conversaciones);



                                    recView.setAdapter(adaptador);*/

                                    adaptador.updateData(l_conversaciones);




                                    adaptador.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Log.d("DemoRecView", "Pulsado el elemento " + recView.getChildViewHolder(v));
                                            Log.d("DemoRecView", "Pulsado el elemento " + recView.getChildItemId(v));

                                            Conversacion conversacion_seleccionado = l_conversaciones.get(recView.getChildViewHolder(v).getAdapterPosition());

                                            if (conversacion_seleccionado != null) {
                                                mListener.OnConversacionesListFragmentInteractionListener(conversacion_seleccionado.Id);
                                            }
                                        }
                                    });

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnConversacionesListFragmentInteractionListener {
        public void OnConversacionesListFragmentInteractionListener(String _id_conversacion);
    }

}

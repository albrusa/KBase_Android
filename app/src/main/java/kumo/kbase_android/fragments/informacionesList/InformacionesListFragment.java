package kumo.kbase_android.fragments.informacionesList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLException;
import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.adapters.InformacionesListAdapter;
import kumo.kbase_android.dao.BaseDao;
import kumo.kbase_android.dao.InformacionesDao;
import kumo.kbase_android.model.Informacion;
import kumo.kbase_android.model.e_Informaciones;
import kumo.kbase_android.utils.ReceiverManager;


public class InformacionesListFragment extends Fragment {

    private static final String ID_APLICACION = "id_aplicacion";
    private static final String ID = "id";
    private static final String ID_CLASE = "id_clase";

    private String mId_Aplicacion;
    private String mId;
    private String mId_Clase;

    private RecyclerView recView;
    private InformacionesListAdapter adaptador;

    private BroadcastReceiver mReceiverInformacionesDone;
    private InformacionesDao informacionesDao;

    private ReceiverManager mReceiverManager;

    private List<Informacion> l_informaciones;

    private OnInformacionesListFragmentInteractionListener mListener;

    public static InformacionesListFragment newInstance(String _id_Aplicacion, String _id, String _id_Clase) {
        InformacionesListFragment fragment = new InformacionesListFragment();
        Bundle args = new Bundle();
        args.putString(ID_APLICACION, _id_Aplicacion);
        args.putString(ID, _id);
        args.putString(ID_CLASE, _id_Clase);
        fragment.setArguments(args);
        return fragment;
    }

    public InformacionesListFragment() {
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
        View _view =  inflater.inflate(R.layout.informaciones_list_fragment, container, false);

        recView  = (RecyclerView) _view.findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        adaptador = new InformacionesListAdapter(getContext());

        recView.setLayoutManager(
                new LinearLayoutManager(_view.getContext(), LinearLayoutManager.VERTICAL, false));

        recView.setAdapter(adaptador);


        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();


        mReceiverManager = ReceiverManager.init(getContext());

        IntentFilter informacionesDone = new IntentFilter(ReceiverManager.OBT_INFORMACIONES_DONE);

        try {
            informacionesDao = informacionesDao.init(getContext(), BaseDao.getInstance(getContext()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mReceiverInformacionesDone = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    l_informaciones = informacionesDao.obt_Informaciones_db(mId_Aplicacion, mId, mId_Clase);

                    adaptador.updateData(l_informaciones);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        };

        if (!mReceiverManager.isReceiverRegistered(mReceiverInformacionesDone)) {
            mReceiverManager.registerReceiver(mReceiverInformacionesDone, informacionesDone);
        }

        try {
            l_informaciones = informacionesDao.obt_Informaciones(mId_Aplicacion, mId, mId_Clase);

            adaptador.updateData(l_informaciones);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("DemoRecView", "Pulsado el elemento " + recView.getChildViewHolder(v));
                Log.d("DemoRecView", "Pulsado el elemento " + recView.getChildItemId(v));

                Informacion informacion_seleccionado = l_informaciones.get(recView.getChildViewHolder(v).getAdapterPosition());

                if (informacion_seleccionado != null) {

                    if (informacion_seleccionado.c_Tipo == e_Informaciones.Noticia.getValue())

                        mListener.OnInformacionesListFragmentInteractionListener(informacion_seleccionado.Id, informacion_seleccionado.Titulo, informacion_seleccionado.Descripcion);
                }
            }
        });

        /*final View _view = getView();

        HashMap<String, String> params = new HashMap<String, String>();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id_aplicacion", mId_Aplicacion);
        jsonObject.addProperty("_id_usuario", mId);
        jsonObject.addProperty("_id_usuario_clase", mId_Clase);

        try {
            GsonRequest<Informacion[]> getPersons =
                    new GsonRequest<Informacion[]>(Request.Method.POST, Constantes.INFORMACIONES_OBT_INFORMACIONES, Informacion[].class,params,jsonObject,

                            new Response.Listener<Informacion[]>() {
                                @Override
                                public void onResponse(Informacion[] response) {
                                    l_informaciones = Arrays.asList(response);

                                    adaptador.updateData(l_informaciones);

                                    adaptador.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Log.d("DemoRecView", "Pulsado el elemento " + recView.getChildViewHolder(v));
                                            Log.d("DemoRecView", "Pulsado el elemento " + recView.getChildItemId(v));

                                            Informacion informacion_seleccionado = l_informaciones.get(recView.getChildViewHolder(v).getAdapterPosition());

                                            if (informacion_seleccionado != null) {

                                                if(informacion_seleccionado.c_Tipo == e_Informaciones.Noticia.getValue())

                                                mListener.OnInformacionesListFragmentInteractionListener(informacion_seleccionado.Id, informacion_seleccionado.Titulo, informacion_seleccionado.Descripcion);
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
        }*/
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnInformacionesListFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause(){
        if(mReceiverManager!=null) {
            if (mReceiverManager.isReceiverRegistered(mReceiverInformacionesDone)) {
                mReceiverManager.unregisterReceiver(mReceiverInformacionesDone);
            }
        }
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnInformacionesListFragmentInteractionListener {
        public void OnInformacionesListFragmentInteractionListener(String _id_informacion, String _titulo, String _texto);
    }

}

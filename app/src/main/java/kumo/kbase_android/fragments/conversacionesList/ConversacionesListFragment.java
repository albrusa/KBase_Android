package kumo.kbase_android.fragments.conversacionesList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLException;
import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.adapters.ConversacionesListAdapter;
import kumo.kbase_android.dao.BaseDao;
import kumo.kbase_android.dao.ConversacionesDao;
import kumo.kbase_android.decorators.DividerItemDecoration;
import kumo.kbase_android.model.Conversacion;
import kumo.kbase_android.utils.ReceiverManager;


public class ConversacionesListFragment extends Fragment {

    private static final String ID_APLICACION = "id_aplicacion";
    private static final String ID = "id";
    private static final String ID_CLASE = "id_clase";

    private String mId_Aplicacion;
    private String mId;
    private String mId_Clase;

    private RecyclerView recView;
    private ConversacionesListAdapter adaptador;
    private BroadcastReceiver mReceiverConversacionesDone;
    private ConversacionesDao conversacionesDao;

    private ReceiverManager mReceiverManager;

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

        mReceiverManager = ReceiverManager.init(getContext());

        IntentFilter conversacionesDone = new IntentFilter(ReceiverManager.OBT_CONVERSACIONES_DONE);

        try {
            conversacionesDao = conversacionesDao.init(getContext(),BaseDao.getInstance(getContext()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mReceiverConversacionesDone = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    l_conversaciones = conversacionesDao.obt_Conversaciones_db(mId_Aplicacion, mId, mId_Clase);

                    adaptador.updateData(l_conversaciones);
                }
                    catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        };

        if (!mReceiverManager.isReceiverRegistered(mReceiverConversacionesDone)) {
            mReceiverManager.registerReceiver(mReceiverConversacionesDone, conversacionesDone);
        }

        try {
            l_conversaciones = conversacionesDao.obt_Conversaciones(mId_Aplicacion, mId, mId_Clase);

            adaptador.updateData(l_conversaciones);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Conversacion conversacion_seleccionado = l_conversaciones.get(recView.getChildViewHolder(v).getAdapterPosition());

                if (conversacion_seleccionado != null) {
                    mListener.OnConversacionesListFragmentInteractionListener(conversacion_seleccionado.Id, conversacion_seleccionado.Nombre, conversacion_seleccionado.Imagen);
                }
            }
        });


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
    }

    @Override
    public void onPause(){
        if(mReceiverManager!=null) {
            if (mReceiverManager.isReceiverRegistered(mReceiverConversacionesDone)) {
                mReceiverManager.unregisterReceiver(mReceiverConversacionesDone);
            }
        }
        super.onPause();

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnConversacionesListFragmentInteractionListener {
        public void OnConversacionesListFragmentInteractionListener(String _id_conversacion, String _nombre, String _imagen);
    }

}

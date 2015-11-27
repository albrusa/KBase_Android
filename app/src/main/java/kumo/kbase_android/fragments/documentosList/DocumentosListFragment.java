package kumo.kbase_android.fragments.documentosList;

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
import kumo.kbase_android.adapters.DocumentosListAdapter;
import kumo.kbase_android.dao.BaseDao;
import kumo.kbase_android.dao.DocumentosDao;
import kumo.kbase_android.model.Documento;
import kumo.kbase_android.utils.ReceiverManager;


public class DocumentosListFragment extends Fragment {

    private static final String ID_APLICACION = "id_aplicacion";
    private static final String ID = "id";
    private static final String ID_CLASE = "id_clase";

    private String mId_Aplicacion;
    private String mId;
    private String mId_Clase;

    private RecyclerView recView;
    private DocumentosListAdapter adaptador;
    private BroadcastReceiver mReceiverDocumentosDone;
    private DocumentosDao documentosDao;

    private ReceiverManager mReceiverManager;

    private List<Documento> l_documentos;

    private OnDocumentosListFragmentInteractionListener mListener;

    public static DocumentosListFragment newInstance(String _id_Aplicacion, String _id, String _id_Clase) {
        DocumentosListFragment fragment = new DocumentosListFragment();
        Bundle args = new Bundle();
        args.putString(ID_APLICACION, _id_Aplicacion);
        args.putString(ID, _id);
        args.putString(ID_CLASE, _id_Clase);
        fragment.setArguments(args);
        return fragment;
    }

    public DocumentosListFragment() {
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
        View _view =  inflater.inflate(R.layout.documentos_list_fragment, container, false);

        recView  = (RecyclerView) _view.findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        adaptador = new DocumentosListAdapter(getContext());

        recView.setLayoutManager(
                new LinearLayoutManager(_view.getContext(), LinearLayoutManager.VERTICAL, false));

        recView.setAdapter(adaptador);



        /*List<Conversacion> l_conversaciones = new ArrayList<Conversacion>();

        adaptador = new ConversacionesListAdapter(l_conversaciones);

        recView.setAdapter(adaptador);

        recView.setLayoutManager(
                new LinearLayoutManager(_view.getContext(), LinearLayoutManager.VERTICAL, false));
*/
        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();


        mReceiverManager = ReceiverManager.init(getContext());

        IntentFilter documentosDone = new IntentFilter(ReceiverManager.OBT_DOCUMENTOS_DONE);

        try {
            documentosDao = documentosDao.init(getContext(), BaseDao.getInstance(getContext()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mReceiverDocumentosDone = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    l_documentos = documentosDao.obt_Documentos_db(mId_Aplicacion, mId, mId_Clase);

                    adaptador.updateData(l_documentos);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        };

        if (!mReceiverManager.isReceiverRegistered(mReceiverDocumentosDone)) {
            mReceiverManager.registerReceiver(mReceiverDocumentosDone, documentosDone);
        }

        try {
            l_documentos = documentosDao.obt_Documentos(mId_Aplicacion, mId, mId_Clase);

            adaptador.updateData(l_documentos);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*final View _view = getView();

        HashMap<String, String> params = new HashMap<String, String>();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id_aplicacion", mId_Aplicacion);
        jsonObject.addProperty("_id_usuario", mId);
        jsonObject.addProperty("_id_usuario_clase", mId_Clase);

        try {
            GsonRequest<Documento[]> getPersons =
                    new GsonRequest<Documento[]>(Request.Method.POST, Constantes.DOCUMENTOS_OBT_DOCUMENTOS, Documento[].class,params,jsonObject,

                            new Response.Listener<Documento[]>() {
                                @Override
                                public void onResponse(Documento[] response) {
                                    List<Documento> l_documentos = Arrays.asList(response);

                                    //adaptador = new DocumentosListAdapter(l_documentos);
                                    adaptador.updateData(l_documentos);



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
            mListener = (OnDocumentosListFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mReceiverManager.unregisterReceiver(mReceiverDocumentosDone);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnDocumentosListFragmentInteractionListener {
        public void OnDocumentosListFragmentInteractionListener();
    }

}

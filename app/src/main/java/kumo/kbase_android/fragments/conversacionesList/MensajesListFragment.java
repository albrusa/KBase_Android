package kumo.kbase_android.fragments.conversacionesList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.adapters.MensajesListAdapter;
import kumo.kbase_android.dao.BaseDao;
import kumo.kbase_android.dao.MensajesDao;
import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Mensaje;
import kumo.kbase_android.model.boolean_Api;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.DownloadTask;
import kumo.kbase_android.utils.ReceiverManager;


public class MensajesListFragment extends Fragment {

    private static final String ID_APLICACION = "id_aplicacion";
    private static final String ID = "id";
    private static final String ID_CLASE = "id_clase";
    private static final String ID_CONVERSACION = "id_conversacion";

    private String mId_Aplicacion;
    private String mId;
    private String mId_Clase;
    private String mId_Conversacion;

    private String mMensaje;

    private RecyclerView recView;
    private MensajesListAdapter adaptador;
    private BroadcastReceiver mReceiverMensajesDone;
    private MensajesDao mensajesDao;

    private List<Mensaje> l_mensajes;

    private ReceiverManager mReceiverManager;

    private EditText vMensaje;
    private ImageButton vEnviar;

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

        LinearLayoutManager llm = new LinearLayoutManager(_view.getContext(), LinearLayoutManager.VERTICAL,false);

        llm.setStackFromEnd(true);

        recView.setLayoutManager(llm);

        adaptador = new MensajesListAdapter(getContext(), mId);

        recView.setAdapter(adaptador);

        vEnviar = (ImageButton) _view.findViewById(R.id.enviar_btn);
        vMensaje = (EditText) _view.findViewById(R.id.mensaje_txt);

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        final View _view = getView();


        mReceiverManager = ReceiverManager.init(getContext());

        IntentFilter conversacionesDone = new IntentFilter(ReceiverManager.OBT_MENSAJES_DONE);

        try {
            mensajesDao = mensajesDao.init(getContext(), BaseDao.getInstance(getContext()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mReceiverMensajesDone = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    l_mensajes = mensajesDao.obt_Mensajes_db(mId_Aplicacion, mId, mId_Clase, mId_Conversacion);

                    adaptador.updateData(l_mensajes);

                    recView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recView.scrollToPosition(adaptador.getItemCount());
                        }
                    }, 100);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        };

        if (!mReceiverManager.isReceiverRegistered(mReceiverMensajesDone)) {
            mReceiverManager.registerReceiver(mReceiverMensajesDone, conversacionesDone);
        }

        try {
            l_mensajes = mensajesDao.obt_Mensajes(mId_Aplicacion, mId, mId_Clase, mId_Conversacion);

            adaptador.updateData(l_mensajes);

            recView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recView.scrollToPosition(adaptador.getItemCount());
                }
            }, 100);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        vEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enviar_mensaje();
            }
        });


        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Mensaje mensaje_seleccionado = l_mensajes.get(recView.getChildViewHolder(v).getAdapterPosition());

                if(!mensaje_seleccionado.Id_Archivo.equals("00000000-0000-0000-0000-000000000000")) {

                    File target = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mensaje_seleccionado.Mensaje);
                    target.setWritable(true);
                    String url = Constantes.ARCHIVOS_OBT_ARCHIVO + "?_id_aplicacion=" + mId_Aplicacion + "&_id_usuario=" + mId + "&_id_usuario_clase=" + mId_Clase + "&_id_archivo=" + mensaje_seleccionado.Id_Archivo;


                    new DownloadTask(getContext(), target, "hola").execute(url);
                }

            }
        });

       /* vMensaje.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                recView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recView.smoothScrollToPosition(adaptador.getItemCount());
                    }
                }, 150);

                return false;
            }
        });*/
    }


    public void obt_mensajes()
    {
        final View _view = getView();

        HashMap<String, String> params = new HashMap<String, String>();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id_aplicacion", mId_Aplicacion);
        jsonObject.addProperty("_id_usuario", mId);
        jsonObject.addProperty("_id_usuario_clase", mId_Clase);
        jsonObject.addProperty("_id_conversacion", mId_Conversacion);

        try {
            GsonRequest<Mensaje[]> obt_mensajes =
                    new GsonRequest<Mensaje[]>(Request.Method.POST, Constantes.CONVERSACIONES_OBT_MENSAJES, Mensaje[].class,params,jsonObject,

                            new Response.Listener<Mensaje[]>() {
                                @Override
                                public void onResponse(Mensaje[] response) {
                                    List<Mensaje> l_mensajes = Arrays.asList(response);

                                    adaptador.updateData(l_mensajes);

                                    recView.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            recView.scrollToPosition(adaptador.getItemCount());
                                        }
                                    }, 100);
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("objeto", error.getMessage());
                        }
                    });

            HttpCola.getInstance(_view.getContext()).addToRequestQueue(obt_mensajes);

        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
    }

    public void enviar_mensaje(){

        final View _view = getView();

        String mensaje = vMensaje.getText().toString();
        if (!TextUtils.isEmpty(mensaje)) {
            try {

                HashMap<String, String> params = new HashMap<String, String>();

                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("_id_aplicacion", mId_Aplicacion);
                jsonObject.addProperty("_id_usuario", mId);
                jsonObject.addProperty("_id_usuario_clase", mId_Clase);
                jsonObject.addProperty("_id_conversacion", mId_Conversacion);
                jsonObject.addProperty("_mensaje", mensaje);

                GsonRequest<boolean_Api> enviar_mensaje =
                        new GsonRequest<boolean_Api>(Request.Method.POST, Constantes.CONVERSACIONES_ENVIAR_MENSAJE, boolean_Api.class,params,jsonObject,

                                new Response.Listener<boolean_Api>() {
                                    @Override
                                    public void onResponse(boolean_Api response) {

                                        vMensaje.setText("");
                                        obt_mensajes();
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("objeto", error.getMessage());
                            }
                        });

                HttpCola.getInstance(_view.getContext()).addToRequestQueue(enviar_mensaje);

            } catch (Exception e) {
                Log.d("Error",e.getMessage());
            }
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

    @Override
    public void onPause(){
        super.onPause();
        if (mReceiverManager.isReceiverRegistered(mReceiverMensajesDone)) {
            mReceiverManager.unregisterReceiver(mReceiverMensajesDone);
        }
    }


    public interface OnMensajesListFragmentInteractionListener {
        public void OnMensajesListFragmentInteractionListener();
    }

}

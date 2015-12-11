package kumo.kbase_android.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ReceiverManager {

    private static List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();
    private static ReceiverManager ref;
    private Context context;



    public static final String OBT_CONVERSACIONES_DONE = "es.kumo.kbase.obt_Conversaciones_request_done";
    public static final String OBT_MENSAJES_DONE = "es.kumo.kbase.obt_Mensajes_request_done";
    public static final String OBT_DOCUMENTOS_DONE = "es.kumo.kbase.obt_Documentos_request_done";
    public static final String OBT_INFORMACIONES_DONE = "es.kumo.kbase.obt_Informaciones_request_done";

    public static final String USUARIO_SI_ACTIVO_DONE = "es.kumo.kbase.Usuario_si_Activo_request_done";
    public static final String MODIFICAR_USUARIO_DONE = "es.kumo.kbase.obt_Informaciones_request_done";

    public static final String DESTRUIR_TODAS_ACTIVITIES = "es.kumo.kbase.destruir_todas_activities";


    private ReceiverManager(Context context){
        this.context = context;
    }

    public static synchronized ReceiverManager init(Context context) {
        if (ref == null) ref = new ReceiverManager(context);
        return ref;
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter intentFilter){
        receivers.add(receiver);
        Intent intent = context.registerReceiver(receiver, intentFilter);
        Log.i(getClass().getSimpleName(), "registered receiver: " + receiver + "  with filter: " + intentFilter);
        Log.i(getClass().getSimpleName(), "receiver Intent: "+intent);
        return intent;
    }

    public boolean isReceiverRegistered(BroadcastReceiver receiver){
        boolean registered = receivers.contains(receiver);
        Log.i(getClass().getSimpleName(), "is receiver "+receiver+" registered? "+registered);
        return registered;
    }

    public void unregisterReceiver(BroadcastReceiver receiver){
        if (isReceiverRegistered(receiver)){
            receivers.remove(receiver);
            context.unregisterReceiver(receiver);
            Log.i(getClass().getSimpleName(), "unregistered receiver: "+receiver);
        }
    }
}
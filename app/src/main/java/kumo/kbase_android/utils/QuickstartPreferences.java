package kumo.kbase_android.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dev_2 on 16/10/2015.
 */
public final class QuickstartPreferences {

    public static final String DEFAULT_PREFERENCE = "kGlobal_Preferences";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String ID_TOKEN = "idToken";

    public static final String USUARIO_REGISTRADO = "usuarioRegistrado";
    public static final String USUARIO_ACTIVO = "usuarioActivo";

    public static final String TAB_ACTIVO = "tabActivo";

    public SharedPreferences settings;

    public static Context globalContext;

    /*public QuickstartPreferences(Context _context){

        settings = _context.getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static void init(Context _context){
        globalContext = _context;
    }

    public static SharedPreferences getSettings(Context _activity){
        globalContext = _activity;
        return globalContext.getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_PRIVATE);
    }*/
}

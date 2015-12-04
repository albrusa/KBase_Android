package kumo.kbase_android.dao;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.model.boolean_Api;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.ReceiverManager;

/**
 * Created by dev_2 on 27/11/2015.
 */
public class UsuariosDao{

    private static UsuariosDao mInstance;
    private static BaseDao bdao;
    private static Context mContext;
    private static Dao<Usuario, Integer> usuarioDao;

    private UsuariosDao(Context _context, BaseDao _bdao) throws SQLException{
        this.mContext = _context;
        this.bdao = _bdao;
        this.usuarioDao = this.bdao.getDataBase().getUsuarioDao();
    }

    public static synchronized UsuariosDao init(Context _context,BaseDao _bdao) throws SQLException {
        if (mInstance == null) mInstance = new UsuariosDao(_context,_bdao);
        return mInstance;
    }


    public static List<Usuario> obt_Usuarios() throws SQLException {
        return  usuarioDao.queryForAll();
    }


    public void si_activo(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase) throws SQLException {

        HashMap<String, String> params = new HashMap<String, String>();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id_aplicacion", _id_Aplicacion);
        jsonObject.addProperty("_id_usuario", _id_Usuario);
        jsonObject.addProperty("_id_usuario_clase", _id_Usuario_Clase);


        try {
            GsonRequest<boolean_Api> obt_mensajes =
                    new GsonRequest<boolean_Api>(Request.Method.POST, Constantes.USUARIO_SI_ACTIVO, boolean_Api.class,params,jsonObject,

                            new Response.Listener<boolean_Api>() {
                                @Override
                                public void onResponse(boolean_Api response) {

                                    Intent i = new Intent(ReceiverManager.USUARIO_SI_ACTIVO_DONE);

                                    i.putExtra("Valor", response.Valor);

                                    mContext.sendBroadcast(i);
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("objeto", error.getMessage());
                        }
                    });

            HttpCola.getInstance(mContext).addToRequestQueue(obt_mensajes);

        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
    }


    public void modificar(final Usuario _usuario) throws SQLException {

        HashMap<String, String> params = new HashMap<String, String>();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id_aplicacion", _usuario.Id_Aplicacion);
        jsonObject.addProperty("_id_usuario", _usuario.Id);
        jsonObject.addProperty("_id_usuario_clase", _usuario.Id_Clase);
        jsonObject.addProperty("_nombre", _usuario.Nombre);
        jsonObject.addProperty("_apellidos", _usuario.Apellidos);
        jsonObject.addProperty("_tlf_movil", _usuario.Tlf_Movil);
        jsonObject.addProperty("_prefijo", _usuario.Prefijo);
        jsonObject.addProperty("_correo", _usuario.Correo);


        try {
            GsonRequest<boolean_Api> obt_mensajes =
                    new GsonRequest<boolean_Api>(Request.Method.POST, Constantes.USUARIO_MODIFICAR_USUARIO, boolean_Api.class,params,jsonObject,

                            new Response.Listener<boolean_Api>() {
                                @Override
                                public void onResponse(boolean_Api response) {
                                    if(response.Valor) {
                                        /*try {
                                            usuarioDao.update(_usuario);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }*/
                                        Intent i = new Intent(ReceiverManager.MODIFICAR_USUARIO_DONE);

                                        mContext.sendBroadcast(i);
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("objeto", error.getMessage());
                        }
                    });

            HttpCola.getInstance(mContext).addToRequestQueue(obt_mensajes);

        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
    }

    public void drop(){
        try {

            TableUtils.dropTable(bdao.getDataBase().getConnectionSource(), Usuario.class, true);
            TableUtils.createTable( bdao.getDataBase().getConnectionSource(), Usuario.class);

        } catch (SQLException e) {

        }
    }

    public void create(){
        try {

            TableUtils.createTable( bdao.getDataBase().getConnectionSource(), Usuario.class);

        } catch (SQLException e) {

        }
    }

    public void clean(){
        try {

            TableUtils.clearTable(bdao.getDataBase().getConnectionSource(), Usuario.class);

        } catch (SQLException e) {

        }
    }
}

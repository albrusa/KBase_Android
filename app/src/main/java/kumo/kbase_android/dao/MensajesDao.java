package kumo.kbase_android.dao;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Mensaje;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.Helpers;
import kumo.kbase_android.utils.ReceiverManager;

/**
 * Created by dev_2 on 27/11/2015.
 */
public class MensajesDao {

        private static MensajesDao mInstance;
        private static BaseDao bdao;
        private static Context mContext;
        private static Dao<Mensaje, Integer> mensajeDao;
        private List<Mensaje> l_mensajes;

        private String mId_Usuario;
        private String mId_Usuario_Clase;
        private String mId_Aplicacion;
        private String mId_Conversacion;

        private MensajesDao(Context _context, BaseDao _bdao) throws SQLException{
            this.mContext = _context;
            this.bdao = _bdao;
            this.mensajeDao = this.bdao.getDataBase().getMensajeDao();
        }

        public static synchronized MensajesDao init(Context _context,BaseDao _bdao) throws SQLException {
            if (mInstance == null) mInstance = new MensajesDao(_context,_bdao);
            return mInstance;
        }


        public List<Mensaje> obt_Mensajes(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase, String _id_Conversacion) throws SQLException {

            l_mensajes = obt_Mensajes_db(_id_Aplicacion, _id_Usuario, _id_Usuario_Clase, _id_Conversacion);

            if(Helpers.isNetworkAvailable(mContext, Helpers.conexiones)){

                obt_Mensajes_request(_id_Aplicacion, _id_Usuario, _id_Usuario_Clase, _id_Conversacion);

            }

            return l_mensajes;

        }

        public void obt_Mensajes_request(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase, String _id_Conversacion) throws SQLException {

            HashMap<String, String> params = new HashMap<String, String>();

            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("_id_aplicacion", _id_Aplicacion);
            jsonObject.addProperty("_id_usuario", _id_Usuario);
            jsonObject.addProperty("_id_usuario_clase", _id_Usuario_Clase);
            jsonObject.addProperty("_id_conversacion", _id_Conversacion);

            mId_Usuario = _id_Usuario.toLowerCase();
            mId_Usuario_Clase = _id_Usuario_Clase.toLowerCase();
            mId_Aplicacion = _id_Aplicacion.toLowerCase();
            mId_Conversacion = _id_Conversacion.toLowerCase();

           /* if(l_mensajes.size()> 0){
                Mensaje mensaje = l_mensajes.get(l_mensajes.size() - 1);

                SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                jsonObject.addProperty("_fecha", mDateFormat.format(mensaje.Creado));
            }*/

            try {
                GsonRequest<Mensaje[]> obt_mensajes =
                        new GsonRequest<Mensaje[]>(Request.Method.POST, Constantes.CONVERSACIONES_OBT_MENSAJES, Mensaje[].class,params,jsonObject,

                                new Response.Listener<Mensaje[]>() {
                                    @Override
                                    public void onResponse(Mensaje[] response) {

                                        List<Mensaje> l_mensajes = Arrays.asList(response);

                                        //clean();
                                        try {

                                            DeleteBuilder<Mensaje, Integer> dbuilder = mensajeDao.deleteBuilder();
                                            dbuilder.where().like("Id_0", mId_Conversacion).and().like("Id_Aplicacion", mId_Aplicacion);
                                            mensajeDao.delete(dbuilder.prepare());

                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }


                                        for (Mensaje mensaje : l_mensajes) {

                                            try {
                                                mensajeDao.create(mensaje);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        Intent i = new Intent(ReceiverManager.OBT_MENSAJES_DONE);
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

        public List<Mensaje> obt_Mensajes_db(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase, String _id_Conversacion) throws SQLException {

             return  mensajeDao.queryBuilder().where().like("Id_0", _id_Conversacion).and().like("Id_Aplicacion", _id_Aplicacion).query();

        }

        public void drop(){
            try {

                TableUtils.dropTable( bdao.getDataBase().getConnectionSource(), Mensaje.class, true);
                TableUtils.createTable( bdao.getDataBase().getConnectionSource(), Mensaje.class);

            } catch (SQLException e) {

            }
        }

        public void create(){
            try {

                TableUtils.createTable( bdao.getDataBase().getConnectionSource(), Mensaje.class);

            } catch (SQLException e) {

            }
        }

        public void clean(){
            try {

                TableUtils.clearTable(bdao.getDataBase().getConnectionSource(), Mensaje.class);

            } catch (SQLException e) {

            }
        }



}

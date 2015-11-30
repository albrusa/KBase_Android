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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kumo.kbase_android.httpRequest.GsonRequest;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.model.Conversacion;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.Helpers;
import kumo.kbase_android.utils.ReceiverManager;

/**
 * Created by dev_2 on 27/11/2015.
 */
public class ConversacionesDao {

        private static ConversacionesDao mInstance;
        private static BaseDao bdao;
        private static Context mContext;
        private static Dao<Conversacion, Integer> conversacionDao;


        private ConversacionesDao(Context _context,BaseDao _bdao) throws SQLException{
            this.mContext = _context;
            this.bdao = _bdao;
            this.conversacionDao = this.bdao.getDataBase().getConversacionDao();
        }

        public static synchronized ConversacionesDao init(Context _context,BaseDao _bdao) throws SQLException {
            if (mInstance == null) mInstance = new ConversacionesDao(_context,_bdao);
            return mInstance;
        }


        public List<Conversacion> obt_Conversaciones(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase) throws SQLException {

            if(Helpers.isNetworkAvailable(mContext, Helpers.conexiones)){

                obt_Conversaciones_request(_id_Aplicacion, _id_Usuario, _id_Usuario_Clase);

            }

            return obt_Conversaciones_db(_id_Aplicacion, _id_Usuario, _id_Usuario_Clase);

        }

        public void obt_Conversaciones_request(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase) throws SQLException {

            HashMap<String, String> params = new HashMap<String, String>();

            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("_id_aplicacion", _id_Aplicacion);
            jsonObject.addProperty("_id_usuario", _id_Usuario);
            jsonObject.addProperty("_id_usuario_clase", _id_Usuario_Clase);

            try {
                GsonRequest<Conversacion[]> request =
                        new GsonRequest<Conversacion[]>(Request.Method.POST, Constantes.CONVERSACIONES_OBT_CONVERSACIONES, Conversacion[].class,params,jsonObject,

                                new Response.Listener<Conversacion[]>() {
                                    @Override
                                    public void onResponse(Conversacion[] response) {
                                        List<Conversacion> l_conversaciones = Arrays.asList(response);

                                        clean();

                                        for (Conversacion conversacion : l_conversaciones) {

                                                try {
                                                    conversacionDao.create(conversacion);
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }
                                        }

                                        Intent i = new Intent(ReceiverManager.OBT_CONVERSACIONES_DONE);
                                        mContext.sendBroadcast(i);

                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("objeto", error.getMessage());
                            }
                        });

                HttpCola.getInstance(mContext).addToRequestQueue(request);

            } catch (Exception e) {
                Log.d("Error",e.getMessage());
            }
        }

        public List<Conversacion> obt_Conversaciones_db(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase) throws SQLException {

            return  conversacionDao.queryForAll();
            //return conversacionDao.queryBuilder().where().eq("Id","44154DFF-DCED-4877-9D25-00EF75AA4485").query();
        }

        public void drop(){
            try {

                TableUtils.dropTable( bdao.getDataBase().getConnectionSource(), Conversacion.class, true);
                TableUtils.createTable( bdao.getDataBase().getConnectionSource(), Conversacion.class);

            } catch (SQLException e) {

            }
        }

        public void create(){
            try {

                TableUtils.createTable( bdao.getDataBase().getConnectionSource(), Conversacion.class);

            } catch (SQLException e) {

            }
        }

        public void clean(){
            try {

                TableUtils.clearTable(bdao.getDataBase().getConnectionSource(), Conversacion.class);

            } catch (SQLException e) {

            }
        }



}

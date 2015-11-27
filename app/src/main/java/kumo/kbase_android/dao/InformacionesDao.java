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
import kumo.kbase_android.model.Informacion;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.Helpers;
import kumo.kbase_android.utils.ReceiverManager;

/**
 * Created by dev_2 on 27/11/2015.
 */
public class InformacionesDao {

        private static InformacionesDao mInstance;
        private static BaseDao bdao;
        private static Context mContext;
        private static Dao<Informacion, Integer> informacionDao;


        private InformacionesDao(Context _context, BaseDao _bdao) throws SQLException{
            this.mContext = _context;
            this.bdao = _bdao;
            this.informacionDao = this.bdao.getDataBase().getInformacionDao();
        }

        public static synchronized InformacionesDao init(Context _context,BaseDao _bdao) throws SQLException {
            if (mInstance == null) mInstance = new InformacionesDao(_context,_bdao);
            return mInstance;
        }


        public List<Informacion> obt_Informaciones(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase) throws SQLException {

            if(Helpers.isNetworkAvailable(mContext, Helpers.conexiones)){

                obt_Informaciones_request(_id_Aplicacion, _id_Usuario, _id_Usuario_Clase);

            }

            return obt_Informaciones_db(_id_Aplicacion, _id_Usuario, _id_Usuario_Clase);

        }

        public void obt_Informaciones_request(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase) throws SQLException {
            HashMap<String, String> params = new HashMap<String, String>();

            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("_id_aplicacion", _id_Aplicacion);
            jsonObject.addProperty("_id_usuario", _id_Usuario);
            jsonObject.addProperty("_id_usuario_clase", _id_Usuario_Clase);

            try {
                GsonRequest<Informacion[]> getPersons =
                        new GsonRequest<Informacion[]>(Request.Method.POST, Constantes.INFORMACIONES_OBT_INFORMACIONES, Informacion[].class,params,jsonObject,

                                new Response.Listener<Informacion[]>() {
                                    @Override
                                    public void onResponse(Informacion[] response) {
                                        List<Informacion> l_informaciones = Arrays.asList(response);

                                        clean();

                                        for (Informacion informacion : l_informaciones) {

                                            try {
                                                informacionDao.create(informacion);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        Intent i = new Intent(ReceiverManager.OBT_INFORMACIONES_DONE);
                                        mContext.sendBroadcast(i);

                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("objeto", error.getMessage());
                            }
                        });

                HttpCola.getInstance(mContext).addToRequestQueue(getPersons);

            } catch (Exception e) {
                Log.d("Error",e.getMessage());
            }

        }

        public List<Informacion> obt_Informaciones_db(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase) throws SQLException {

            return  informacionDao.queryForAll();
        }

        public void drop(){
            try {

                TableUtils.dropTable( bdao.getDataBase().getConnectionSource(), Informacion.class, true);
                TableUtils.createTable( bdao.getDataBase().getConnectionSource(), Informacion.class);

            } catch (SQLException e) {

            }
        }

        public void create(){
            try {

                TableUtils.createTable( bdao.getDataBase().getConnectionSource(), Informacion.class);

            } catch (SQLException e) {

            }
        }

        public void clean(){
            try {

                TableUtils.clearTable(bdao.getDataBase().getConnectionSource(), Informacion.class);

            } catch (SQLException e) {

            }
        }



}

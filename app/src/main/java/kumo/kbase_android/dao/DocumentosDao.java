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
import kumo.kbase_android.model.Documento;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.Helpers;
import kumo.kbase_android.utils.ReceiverManager;

/**
 * Created by dev_2 on 27/11/2015.
 */
public class DocumentosDao {

        private static DocumentosDao mInstance;
        private static BaseDao bdao;
        private static Context mContext;
        private static Dao<Documento, Integer> documentoDao;

        private String mId_Usuario;
        private String mId_Usuario_Clase;
        private String mId_Aplicacion;

        private DocumentosDao(Context _context, BaseDao _bdao) throws SQLException{
            this.mContext = _context;
            this.bdao = _bdao;
            this.documentoDao = this.bdao.getDataBase().getDocumentoDao();
        }

        public static synchronized DocumentosDao init(Context _context,BaseDao _bdao) throws SQLException {
            if (mInstance == null) mInstance = new DocumentosDao(_context,_bdao);
            return mInstance;
        }


        public List<Documento> obt_Documentos(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase) throws SQLException {

            if(Helpers.isNetworkAvailable(mContext, Helpers.conexiones)){

                obt_Documentos_request(_id_Aplicacion, _id_Usuario, _id_Usuario_Clase);

            }

            return obt_Documentos_db(_id_Aplicacion, _id_Usuario, _id_Usuario_Clase);

        }

        public void obt_Documentos_request(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase) throws SQLException {

            HashMap<String, String> params = new HashMap<String, String>();

            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("_id_aplicacion", _id_Aplicacion);
            jsonObject.addProperty("_id_usuario", _id_Usuario);
            jsonObject.addProperty("_id_usuario_clase", _id_Usuario_Clase);

            mId_Usuario = _id_Usuario.toLowerCase();
            mId_Usuario_Clase = _id_Usuario_Clase.toLowerCase();
            mId_Aplicacion = _id_Aplicacion.toLowerCase();

            try {
                GsonRequest<Documento[]> getPersons =
                        new GsonRequest<Documento[]>(Request.Method.POST, Constantes.DOCUMENTOS_OBT_DOCUMENTOS, Documento[].class,params,jsonObject,

                                new Response.Listener<Documento[]>() {
                                    @Override
                                    public void onResponse(Documento[] response) {
                                        List<Documento> l_documentos = Arrays.asList(response);

                                        //clean();

                                        try {

                                            DeleteBuilder<Documento, Integer> dbuilder = documentoDao.deleteBuilder();
                                            dbuilder.where().like("Id_Usuario", mId_Usuario).and().like("Id_Usuario_Clase", mId_Usuario_Clase).and().like("Id_Aplicacion",mId_Aplicacion);
                                            documentoDao.delete(dbuilder.prepare());

                                            //documentoDao.deleteBuilder().where().eq("Id_Usuario", mId_Usuario).and().eq("Id_Usuario_Clase",mId_Usuario_Clase).and().eq("Id_Aplicacion",mId_Aplicacion).query();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }


                                        for (Documento documento : l_documentos) {

                                            try {
                                                documentoDao.create(documento);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        Intent i = new Intent(ReceiverManager.OBT_DOCUMENTOS_DONE);
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

        public List<Documento> obt_Documentos_db(String _id_Aplicacion, String _id_Usuario, String _id_Usuario_Clase) throws SQLException {

            //return  documentoDao.queryForAll();
            return documentoDao.queryBuilder().where().like("Id_Usuario", _id_Usuario).and().like("Id_Usuario_Clase", _id_Usuario_Clase).and().like("Id_Aplicacion", _id_Aplicacion).query();
        }

        public void drop(){
            try {

                TableUtils.dropTable( bdao.getDataBase().getConnectionSource(), Documento.class, true);
                TableUtils.createTable( bdao.getDataBase().getConnectionSource(), Documento.class);

            } catch (SQLException e) {

            }
        }

        public void create(){
            try {

                TableUtils.createTable( bdao.getDataBase().getConnectionSource(), Documento.class);

            } catch (SQLException e) {

            }
        }

        public void clean(){
            try {

                TableUtils.clearTable(bdao.getDataBase().getConnectionSource(), Documento.class);

            } catch (SQLException e) {

            }
        }



}

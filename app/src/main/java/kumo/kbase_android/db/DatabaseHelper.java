package kumo.kbase_android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import kumo.kbase_android.R;
import kumo.kbase_android.model.Configuracion;
import kumo.kbase_android.model.Conversacion;
import kumo.kbase_android.model.Documento;
import kumo.kbase_android.model.Informacion;
import kumo.kbase_android.model.Mensaje;
import kumo.kbase_android.model.Usuario;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /************************************************
     * Suggested Copy/Paste code. Everything from here to the done block.
     ************************************************/

    private static final String DATABASE_NAME = "kbase.db";
    private static final int DATABASE_VERSION = 11;

    private Dao<Usuario, Integer> UsuarioDao;
    private Dao<Configuracion, Integer> ConfiguracionDao;
    private Dao<Conversacion, Integer> ConversacionDao;
    private Dao<Mensaje, Integer> MensajeDao;
    private Dao<Documento, Integer> DocumentoDao;
    private Dao<Informacion, Integer> InformacionDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);

    }

    /************************************************
     * Suggested Copy/Paste Done
     ************************************************/

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.dropTable(connectionSource, Usuario.class, true);
            TableUtils.dropTable(connectionSource, Configuracion.class, true);
            TableUtils.dropTable(connectionSource, Conversacion.class, true);
            TableUtils.dropTable(connectionSource, Mensaje.class, true);
            TableUtils.dropTable(connectionSource, Documento.class, true);
            TableUtils.dropTable(connectionSource, Informacion.class, true);

            TableUtils.createTable(connectionSource, Usuario.class);
            TableUtils.createTable(connectionSource, Configuracion.class);
            TableUtils.createTable(connectionSource, Conversacion.class);
            TableUtils.createTable(connectionSource, Mensaje.class);
            TableUtils.createTable(connectionSource, Documento.class);
            TableUtils.createTable(connectionSource, Informacion.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            TableUtils.dropTable(connectionSource, Usuario.class, true);
            TableUtils.dropTable(connectionSource, Configuracion.class, true);
            TableUtils.dropTable(connectionSource, Conversacion.class, true);
            TableUtils.dropTable(connectionSource, Mensaje.class, true);
            TableUtils.dropTable(connectionSource, Documento.class, true);
            TableUtils.dropTable(connectionSource, Informacion.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }


    public ConnectionSource getConnectionSource(){
        return connectionSource;
    }

    /*public void dropUsuario(){
        try {

            TableUtils.dropTable(connectionSource, Usuario.class, true);
            TableUtils.createTable(connectionSource, Usuario.class);

        } catch (SQLException e) {

        }
    }

    public void createUsuario(){
        try {

            TableUtils.createTable(connectionSource, Usuario.class);

        } catch (SQLException e) {

        }
    }




    public void dropConfiguracion(){
        try {

           TableUtils.dropTable(connectionSource, Configuracion.class, true);
            TableUtils.createTable(connectionSource, Configuracion.class);

        } catch (SQLException e) {

        }
    }

    public void createConfiguracion(){
        try {

            TableUtils.createTable(connectionSource, Configuracion.class);

        } catch (SQLException e) {

        }
    }

    public void limpiar_bbdd(){
        try {
            TableUtils.clearTable(connectionSource, Usuario.class);
            TableUtils.clearTable(connectionSource, Configuracion.class);

        } catch (SQLException e) {

        }
    }
*/

    public Dao<Usuario, Integer> getUsuarioDao() throws SQLException {
        if(UsuarioDao == null) {
            UsuarioDao = getDao(Usuario.class);
        }
        return UsuarioDao;
    }

    public Dao<Configuracion, Integer> getConfiguracionDao() throws SQLException {
        if(ConfiguracionDao == null) {
            ConfiguracionDao = getDao(Configuracion.class);
        }
        return ConfiguracionDao;
    }

    public Dao<Conversacion, Integer> getConversacionDao() throws SQLException {
        if(ConversacionDao == null) {
            ConversacionDao = getDao(Conversacion.class);
        }
        return ConversacionDao;
    }

    public Dao<Mensaje, Integer> getMensajeDao() throws SQLException {
        if(MensajeDao == null) {
            MensajeDao = getDao(Mensaje.class);
        }
        return MensajeDao;
    }

    public Dao<Documento, Integer> getDocumentoDao() throws SQLException {
        if(DocumentoDao == null) {
            DocumentoDao = getDao(Documento.class);
        }
        return DocumentoDao;
    }

    public Dao<Informacion, Integer> getInformacionDao() throws SQLException {
        if(InformacionDao == null) {
            InformacionDao = getDao(Informacion.class);
        }
        return InformacionDao;
    }
}
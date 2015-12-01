package kumo.kbase_android.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import kumo.kbase_android.model.Usuario;

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

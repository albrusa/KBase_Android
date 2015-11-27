package kumo.kbase_android.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import kumo.kbase_android.model.Usuario;

/**
 * Created by dev_2 on 27/11/2015.
 */
public class UsuariosDao{

        private static BaseDao bdao;
        private static Context mContext;
        private static Dao<Usuario, Integer> usuarioDao;

        public static void init (BaseDao _bdao) throws SQLException {
            bdao = _bdao;

            usuarioDao = bdao.getDataBase().getUsuarioDao();
        }



        public static List<Usuario> obt_Usuarios() throws SQLException {
            return  usuarioDao.queryForAll();
        }
}

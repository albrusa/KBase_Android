package kumo.kbase_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kumo.kbase_android.adapters.UsuariosListAdapter;
import kumo.kbase_android.db.DatabaseHelper;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.QuickstartPreferences;

public class UsuariosListActivity extends AppCompatActivity{

    private RecyclerView recView;
    private List<Usuario> l_usuarios;
    private UsuariosListAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuarios_list_activity);

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        l_usuarios =  new ArrayList<Usuario>();

        List<Usuario> l_usuarios2 = new ArrayList<Usuario>();

        Usuario usur = new Usuario();
        usur.Nombre = "Paciente";
        usur.Aplicacion = "Paciente";
        usur.Id = "44154DFF-DCED-4877-9D25-00EF75AA4485";
        usur.Id_Clase = "BB60C904-701F-43CE-8E63-5E4F9D528E93";
        usur.Id_Aplicacion = "59135511-BF4F-4A50-A013-950734A087FF";
        usur.Imagen_Perfil = "";

        try {
            Dao<Usuario, Integer> usuarioDao = databaseHelper.getUsuarioDao();

            l_usuarios2 = usuarioDao.queryBuilder().where().eq("Id","44154DFF-DCED-4877-9D25-00EF75AA4485").query();

            if(l_usuarios2.size() == 0){
                usuarioDao.create(usur);
            }
            l_usuarios = usuarioDao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        adaptador = new UsuariosListAdapter(l_usuarios);

        recView.setAdapter(adaptador);

        recView.setLayoutManager(
                new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));


        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("DemoRecView", "Pulsado el elemento " + recView.getChildViewHolder(v));
                Log.d("DemoRecView", "Pulsado el elemento " + recView.getChildItemId(v));

                Usuario usuario_seleccionado = l_usuarios.get(recView.getChildViewHolder(v).getAdapterPosition());

                if(usuario_seleccionado != null) {

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    Bundle b = new Bundle();

                    b.putString("Id_Usuario", usuario_seleccionado.Id);
                    intent.putExtras(b);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    sharedPreferences.edit().putString(QuickstartPreferences.USUARIO_ACTIVO,usuario_seleccionado.Id).apply();

                    startActivity(intent);
                }
            }
        });
    }

        @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}

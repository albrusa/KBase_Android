package kumo.kbase_android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kumo.kbase_android.adapters.UsuariosListAdapter;
import kumo.kbase_android.dao.BaseDao;
import kumo.kbase_android.dao.UsuariosDao;
import kumo.kbase_android.db.DatabaseHelper;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.Cookies;
import kumo.kbase_android.utils.ObjectPreference;
import kumo.kbase_android.utils.QuickstartPreferences;

public class UsuariosListActivity extends AppCompatActivity{

    private RecyclerView recView;
    private List<Usuario> l_usuarios;
    private UsuariosListAdapter adaptador;
    private ObjectPreference objectPreference;
    private Button vRegistro;
    private Button vNotificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuarios_list_activity);

        objectPreference = new ObjectPreference();
        objectPreference.init(getBaseContext());



        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        l_usuarios =  new ArrayList<Usuario>();

        List<Usuario> l_usuarios2 = new ArrayList<Usuario>();
        List<Usuario> l_usuarios3 = new ArrayList<Usuario>();

        Usuario usur2 = new Usuario();
        usur2.Nombre = "Paciente";
        usur2.Aplicacion = "localhost";
        usur2.Id = "44154DFF-DCED-4877-9D25-00EF75AA4485"; //8D75DB91-3315-4232-86DC-458F01426760
        usur2.Id_Clase = "BB60C904-701F-43CE-8E63-5E4F9D528E93";
        usur2.Id_Aplicacion = "59135511-BF4F-4A50-A013-950734A087FF"; //EBB7F446-03E5-41CE-A8C3-EF3EC9A8A1E6
        usur2.Imagen_Perfil = "";

        Usuario usur = new Usuario();
        usur.Nombre = "Paciente";
        usur.Aplicacion = "kGlobal test";
        usur.Id = "8D75DB91-3315-4232-86DC-458F01426760";
        usur.Id_Clase = "BB60C904-701F-43CE-8E63-5E4F9D528E93";
        usur.Id_Aplicacion = "EBB7F446-03E5-41CE-A8C3-EF3EC9A8A1E6";
        usur.Imagen_Perfil = "";


        try {

            UsuariosDao.init(BaseDao.getInstance(this.getBaseContext()));

            Dao<Usuario, Integer> usuarioDao = databaseHelper.getUsuarioDao();

            l_usuarios3 = usuarioDao.queryBuilder().where().eq("Id","44154DFF-DCED-4877-9D25-00EF75AA4485").query();
            l_usuarios2 = usuarioDao.queryBuilder().where().eq("Id","8D75DB91-3315-4232-86DC-458F01426760").query();

            if(l_usuarios2.size() == 0){
                usuarioDao.create(usur);
            }
            if(l_usuarios3.size() == 0){
                usuarioDao.create(usur2);
            }
            //l_usuarios = usuarioDao.queryForAll();

            l_usuarios = UsuariosDao.obt_Usuarios();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        adaptador = new UsuariosListAdapter(l_usuarios);

        recView.setAdapter(adaptador);

        recView.setLayoutManager(
                new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));

        vRegistro = (Button) findViewById(R.id.registro);
        vNotificacion = (Button) findViewById(R.id.notificacion);



    }

        @Override
    protected void onResume(){
        super.onResume();

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("DemoRecView", "Pulsado el elemento " + recView.getChildViewHolder(v));
                Log.d("DemoRecView", "Pulsado el elemento " + recView.getChildItemId(v));

                Usuario usuario_seleccionado = l_usuarios.get(recView.getChildViewHolder(v).getAdapterPosition());

                if (usuario_seleccionado != null) {

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    Bundle b = new Bundle();

                    b.putString("Id_Usuario", usuario_seleccionado.Id);
                    intent.putExtras(b);

                /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                sharedPreferences.edit().putString(QuickstartPreferences.USUARIO_ACTIVO,usuario_seleccionado.Id).apply();*/

                    Cookies cookie = objectPreference.getComplexPreference();
                    if (cookie != null) {
                        cookie.putObject(QuickstartPreferences.USUARIO_ACTIVO, usuario_seleccionado);
                        cookie.putObject(QuickstartPreferences.TAB_ACTIVO, 1);
                        cookie.commit();
                    }

                    startActivity(intent);
                }
            }
        });

        vRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), RegistroActivity.class);
                Bundle b = new Bundle();

                b.putString("si_Registro", "1");
                intent.putExtras(b);

                startActivity(intent);
            }
        });

        vNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendNotification("hola");
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, UsuariosListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mr_ic_settings_dark)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(0)
                .setSound(defaultSoundUri)
                .setVisibility(-1)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}

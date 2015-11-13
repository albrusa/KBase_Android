package kumo.kbase_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kumo.kbase_android.db.DatabaseHelper;
import kumo.kbase_android.fragments.conversacionesList.MensajesListFragment;
import kumo.kbase_android.httpRequest.MultipartUtility;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.QuickstartPreferences;

public class MensajesListActivity extends AppCompatActivity implements MensajesListFragment.OnMensajesListFragmentInteractionListener {

    private String mId_Usuario;
    private Usuario mUsuario;

    private String mId_Conversacion;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajes_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Bundle bundle = this.getIntent().getExtras();
        mId_Conversacion = bundle.getString("Id_Conversacion");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mId_Usuario = sharedPreferences.getString(QuickstartPreferences.USUARIO_ACTIVO, "0");

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        try {
            Dao<Usuario, Integer> usuarioDao = databaseHelper.getUsuarioDao();

            List<Usuario> l_usuarios = usuarioDao.queryBuilder().where().eq("Id",mId_Usuario).query();

            if(l_usuarios.size() > 0){
                mUsuario = l_usuarios.get(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        Fragment fragment = new MensajesListFragment().newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase, mId_Conversacion);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_contenido, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conversacion_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.anyadir_menu:
                displayPopupWindow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayPopupWindow() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup,null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT,150,true);

        popupWindow.setAnimationStyle(R.style.PopUpWindowAnimation);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(popupView, Gravity.TOP, 0, toolbar.getHeight() + 60);

        ImageButton camara = (ImageButton) popupView.findViewById(R.id.imageButton1);

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

       /* PopupWindow popup = new PopupWindow(getBaseContext());
        View layout = getLayoutInflater().inflate(R.layout.popup, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

       // final PopupWindow popup = new PopupWindow(layout, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        //final PopupWindow popup = new PopupWindow(layout, ActionBar.LayoutParams.MATCH_PARENT,100,true);



        popup.setContentView(layout);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAtLocation(layout, Gravity.TOP, 0, toolbar.getHeight());*/
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, 100);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Test");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Mensajes", "Oops! Failed create "
                        + "Test" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        new UploadFileToServer().execute();
    }






        @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        //overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);
    }


    public void OnMensajesListFragmentInteractionListener(){


    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            Log.d("prog",progress.toString());
        }

        //

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(){
            try {
                MultipartUtility multipart = new MultipartUtility(Constantes.CONVERSACIONES_ENVIAR_ARCHIVO, "UTF-8");

                File filePath = new File(fileUri.getPath());

                multipart.addFormField("_id_configuracion", mUsuario.Id_Aplicacion);
                multipart.addFormField("_id_usuario", mUsuario.Id);
                multipart.addFormField("_id_usuario_clase", mUsuario.Id_Clase);
                multipart.addFormField("_id_conversacion", mId_Conversacion);
                multipart.addFormField("_nombre_fichero", filePath.getName());

                multipart.addFilePart("fileUpload", filePath);

                List<String> response = multipart.finish();

                System.out.println("SERVER REPLIED:");

                for (String line : response) {
                    System.out.println(line);
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }

            return "asd";

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
        }

    }


}

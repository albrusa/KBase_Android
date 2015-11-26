package kumo.kbase_android;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kumo.kbase_android.fragments.conversacionesList.MensajesListFragment;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.httpRequest.LruBitmapCache;
import kumo.kbase_android.httpRequest.MultipartUtility;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.CircularNetworkImageView;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.Cookies;
import kumo.kbase_android.utils.ObjectPreference;
import kumo.kbase_android.utils.QuickstartPreferences;

public class MensajesListActivity extends AppCompatActivity implements MensajesListFragment.OnMensajesListFragmentInteractionListener {

    private static final int CAMERA = 100;
    private static final int GALERIA = 200;
    private static final int ARCHIVO = 300;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private String mId_Usuario;
    private Usuario mUsuario;

    private CircularNetworkImageView vImagen;
    private TextView vTitulo;

    private String mImagen;
    private String mNombre;

    private String mId_Conversacion;
    private ImageLoader mImageLoader;

    private View mPopupView;
    private PopupWindow mPopupWindow;

    private Uri fileUri;
    private ObjectPreference objectPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajes_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int height =(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        mPopupView = layoutInflater.inflate(R.layout.popup,null);
        mPopupWindow = new PopupWindow(mPopupView, ActionBar.LayoutParams.MATCH_PARENT,height,true);


        Bundle bundle = this.getIntent().getExtras();
        mId_Conversacion = bundle.getString("Id_Conversacion");
        mNombre = bundle.getString("Nombre");
        mImagen = bundle.getString("Imagen");

        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
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
        }*/

        objectPreference = new ObjectPreference();
        objectPreference.init(getBaseContext());
        Cookies cookie = objectPreference.getComplexPreference();

        mUsuario = cookie.getObject(QuickstartPreferences.USUARIO_ACTIVO, Usuario.class);


        CircularNetworkImageView vImagen = (CircularNetworkImageView) findViewById(R.id.Toolbar_Imagen);
        TextView vTitulo = (TextView) findViewById(R.id.toolbar_Titulo);

        ImageLoader.ImageCache imageCache = new LruBitmapCache(getBaseContext());

        mImageLoader = HttpCola.getInstance(getBaseContext()).getImageLoader();

        if(mImagen != null && mImagen != "")
        {
            vImagen.setImageUrl(Constantes.HTTP_KMED_SERVER+mImagen, mImageLoader);
        }

       vTitulo.setText(mNombre);


        Fragment fragment = new MensajesListFragment().newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase, mId_Conversacion);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_contenido, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
        }


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

        mPopupWindow.setAnimationStyle(R.style.PopUpWindowAnimation);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(mPopupView, Gravity.TOP, 0, toolbar.getHeight() + 60);

        ImageButton camara = (ImageButton) mPopupView.findViewById(R.id.camera_btn_popup);
        ImageButton galeria = (ImageButton) mPopupView.findViewById(R.id.galeria_btn_popup);
        //ImageButton archivos = (ImageButton) popupView.findViewById(R.id.archivos_btn_popup);

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturarImagen();
            }
        });

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGalleria();
            }
        });
    }

    private void capturarImagen() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA);
    }

    public void abrirGalleria()
    {
        Intent intent = new Intent();
        intent.setType("image/*");


        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Intent intent =  new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(intent, ""), GALERIA);
        //startActivityForResult(intent, GALERIA);

    }

    public void abrirArchivos()
    {
        Intent intent = new Intent();
        intent.setType("file/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Intent intent =  new Intent(Intent.ACTION_PICK, android.provider.DocumentsProvider.Media);

        startActivityForResult(Intent.createChooser(intent, ""), ARCHIVO);
       //startActivityForResult(intent, GALERIA);

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
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } /*else if (type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        }*/ else {
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

        if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {

                File archivo = new File(fileUri.getPath());

                new UploadFileToServer(archivo, CAMERA).execute();
            }
             else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }else{
            if(requestCode == GALERIA){

                if (resultCode == RESULT_OK) {
                    fileUri = data.getData();


                    /*try {
                        Bitmap bitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    //File archivo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileUri.getPath());

                    File archivo = new File(fileUri.getPath());

                    new UploadFileToServer(archivo, GALERIA).execute();
                }
                else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getApplicationContext(),
                            "User cancelled image capture", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }

            }

        }


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
    public void onPause(){
        super.onPause();
        //this.finish();
        // overridePendingTransition(R.anim.enter,R.anim.hold);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        //super.onBackPressed();
//        getFragmentManager().popBackStack();
        //finish();
        //overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);
    }


    public void OnMensajesListFragmentInteractionListener(){


    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        private final File mArchivo;
        private final int mOrigen;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
        }

        UploadFileToServer(File _archivo, int _tipo) {

            mArchivo = _archivo;
            mOrigen = _tipo;
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

               // File filePath = new File(fileUri.getPath());

                multipart.addFormField("_id_configuracion", mUsuario.Id_Aplicacion);
                multipart.addFormField("_id_usuario", mUsuario.Id);
                multipart.addFormField("_id_usuario_clase", mUsuario.Id_Clase);
                multipart.addFormField("_id_conversacion", mId_Conversacion);
                multipart.addFormField("_nombre_fichero", mArchivo.getName());

                if(mOrigen == CAMERA) {
                    multipart.addFilePart("fileUpload", mArchivo);
                }else{
                    if(mOrigen == GALERIA) {

                        File filePath = new File(fileUri.getPath());

                        multipart.addFilePart("fileUpload", filePath.getName(), fileUri, getBaseContext());
                    }
                }

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

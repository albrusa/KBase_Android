package kumo.kbase_android;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kumo.kbase_android.fragments.conversacionesList.MensajesListFragment;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.httpRequest.LruBitmapCache;
import kumo.kbase_android.httpRequest.MultipartUtility;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.BaseAppCompatActivity;
import kumo.kbase_android.utils.CircularNetworkImageView;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.Cookies;
import kumo.kbase_android.utils.MediaHelper;
import kumo.kbase_android.utils.ObjectPreference;
import kumo.kbase_android.utils.QuickstartPreferences;

public class MensajesListActivity extends BaseAppCompatActivity implements MensajesListFragment.OnMensajesListFragmentInteractionListener {

    private String mId_Usuario;
    private Usuario mUsuario;

    private CircularNetworkImageView vImagen;
    private TextView vTitulo;

    private String mId_Conversacion;

    private View mPopupView;
    private PopupWindow mPopupWindow;

    private Uri fileUri;

    private boolean isActivityForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajes_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        isActivityForResult = false;

        int height =(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        mPopupView = layoutInflater.inflate(R.layout.popup,null);
        mPopupWindow = new PopupWindow(mPopupView, ActionBar.LayoutParams.MATCH_PARENT,height,true);


        Bundle bundle = this.getIntent().getExtras();
        mId_Conversacion = bundle.getString("Id_Conversacion");
        String mNombre = bundle.getString("Nombre");
        String mImagen = bundle.getString("Imagen");

        ObjectPreference objectPreference = new ObjectPreference();
        objectPreference.init(getBaseContext());
        Cookies cookie = objectPreference.getComplexPreference();

        mUsuario = cookie.getObject(QuickstartPreferences.USUARIO_ACTIVO, Usuario.class);


        CircularNetworkImageView vImagen = (CircularNetworkImageView) findViewById(R.id.Toolbar_Imagen);
        TextView vTitulo = (TextView) findViewById(R.id.toolbar_Titulo);

        ImageLoader.ImageCache imageCache = new LruBitmapCache(getBaseContext());

        ImageLoader mImageLoader = HttpCola.getInstance(getBaseContext()).getImageLoader();

        if(mImagen != null && !mImagen.equals(""))
        {
            if(mImagen.contains("imagen_perfil_defecto.png")){
                vImagen.setImageResource(R.drawable.imagen_perfil_defecto);
            }else{
                vImagen.setImageUrl(mImagen, mImageLoader);
            }

            //vImagen.setImageUrl(Constantes.HTTP_KMED_SERVER+ mImagen, mImageLoader);
        }

       vTitulo.setText(mNombre);


        new MensajesListFragment();
        Fragment fragment = MensajesListFragment.newInstance(mUsuario.Id_Aplicacion, mUsuario.Id, mUsuario.Id_Clase, mId_Conversacion);

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

        fileUri = MediaHelper.getOutputMediaFileUri(MediaHelper.MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        isActivityForResult = true;

        // start the image capture Intent
        startActivityForResult(intent, MediaHelper.CAMERA);
    }

    public void abrirGalleria()
    {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Intent intent =  new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        isActivityForResult = true;

        startActivityForResult(Intent.createChooser(intent, ""), MediaHelper.GALERIA);
        //startActivityForResult(intent, GALERIA);

    }

    public void abrirArchivos()
    {
        Intent intent = new Intent();
        intent.setType("file/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Intent intent =  new Intent(Intent.ACTION_PICK, android.provider.DocumentsProvider.Media);

        startActivityForResult(Intent.createChooser(intent, ""), MediaHelper.ARCHIVO);
       //startActivityForResult(intent, GALERIA);

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

        if (requestCode == MediaHelper.CAMERA) {
            if (resultCode == RESULT_OK) {

               // File archivo = new File(fileUri.getPath());

                new UploadFileToServer(fileUri, MediaHelper.CAMERA).execute();

                //new UploadFileToServer(archivo, MediaHelper.CAMERA).execute();
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
            if(requestCode == MediaHelper.GALERIA){

                if (resultCode == RESULT_OK) {

                    new UploadFileToServer(data.getData(), MediaHelper.GALERIA).execute();

                    /*File original = new File(data.getData().getPath());
                    String mime = MediaHelper.GetMimeType(this,data.getData());

                    String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mime);

                    File archivo;

                    try {
                        fileUri = MediaHelper.getOutputMediaFileUri(MediaHelper.MEDIA_TYPE_IMAGE, extension);

                        archivo = new File(fileUri.getPath());

                        MediaHelper.copy(original,archivo);

                        new UploadFileToServer(archivo, MediaHelper.GALERIA).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

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

        if(isActivityForResult) {
            mMemoryBoss.isInBackground = false;
            isActivityForResult = false;
        }
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

        private final Uri mUri;
        private File mArchivo;
        private final int mOrigen;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
        }

        UploadFileToServer(File _archivo, int _tipo) {

            mArchivo = _archivo;
            mUri = null;
            mOrigen = _tipo;
        }

        UploadFileToServer(Uri _uri, int _tipo) {

            mUri = _uri;
            mArchivo = null;
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
                //multipart.addFormField("_nombre_fichero", mArchivo.getName());

                if(mOrigen == MediaHelper.CAMERA) {

                    mArchivo = new File(fileUri.getPath());

                    multipart.addFormField("_nombre_fichero", mArchivo.getName());

                    multipart.addFilePart("fileUpload", mArchivo);
                }else{
                    if(mOrigen == MediaHelper.GALERIA) {

                        String mime = MediaHelper.GetMimeType(getBaseContext(), mUri);

                        String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mime);
                        String nombre = MediaHelper.GetName(getBaseContext(),mUri);

                        multipart.addFormField("_nombre_fichero", nombre);

                        multipart.addFilePart("fileUpload", nombre, mUri, getBaseContext());
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

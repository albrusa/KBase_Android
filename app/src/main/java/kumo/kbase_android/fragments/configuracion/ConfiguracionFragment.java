package kumo.kbase_android.fragments.configuracion;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kumo.kbase_android.R;
import kumo.kbase_android.adapters.PrefijosAdapter;
import kumo.kbase_android.dao.BaseDao;
import kumo.kbase_android.dao.UsuariosDao;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.httpRequest.MultipartUtility;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.model.xPrefijo_Telf;
import kumo.kbase_android.utils.Constantes;
import kumo.kbase_android.utils.Cookies;
import kumo.kbase_android.utils.Cultura;
import kumo.kbase_android.utils.MediaHelper;
import kumo.kbase_android.utils.ObjectPreference;
import kumo.kbase_android.utils.QuickstartPreferences;


public class ConfiguracionFragment extends Fragment {

    private ObjectPreference objectPreference;

    private AutoCompleteTextView vTelefono;
    private AutoCompleteTextView vNombre;
    private AutoCompleteTextView vApellidos;
    private AutoCompleteTextView vCorreo;
    private NetworkImageView vImagen;

    private Button vGuardar;
    private ImageButton vEditarImagen;
    private Uri fileUri;
    private ImageLoader mImageLoader;

    private Usuario mUsuario;

    private Spinner vPrefijo;

    private UsuariosDao usuariosDao;

    public static ConfiguracionFragment newInstance() {
        ConfiguracionFragment fragment = new ConfiguracionFragment();
        return fragment;
    }

    public ConfiguracionFragment() {

        objectPreference = new ObjectPreference();
        objectPreference.init(getContext());
        Cookies cookie = objectPreference.getComplexPreference();

        mUsuario = cookie.getObject(QuickstartPreferences.USUARIO_ACTIVO, Usuario.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view =  inflater.inflate(R.layout.configuracion_fragment, container, false);

        vTelefono = (AutoCompleteTextView) _view.findViewById(R.id.configuracion_telefono);
        vNombre = (AutoCompleteTextView) _view.findViewById(R.id.configuracion_nombre);
        vApellidos = (AutoCompleteTextView) _view.findViewById(R.id.configuracion_apellidos);
        vCorreo = (AutoCompleteTextView) _view.findViewById(R.id.configuracion_correo);

        vImagen = (NetworkImageView) _view.findViewById(R.id.configuracion_imagen);

        vGuardar = (Button) _view.findViewById(R.id.configuracion_guardar);
        vEditarImagen = (ImageButton) _view.findViewById(R.id.configuracion_editar_imagen);

        vPrefijo = ( Spinner )_view.findViewById( R.id.configuracion_prefijo );
        List<xPrefijo_Telf> prefijos = Cultura.obt_prefijos();
        PrefijosAdapter adapter = new PrefijosAdapter(_view.getContext(),android.R.layout.select_dialog_singlechoice,prefijos);


        vPrefijo.setAdapter(adapter);

        //Cal comentar a la versió final



        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        vTelefono.setText(mUsuario.Tlf_Movil);
        vNombre.setText(mUsuario.Nombre);
        vApellidos.setText(mUsuario.Apellidos);
        vCorreo.setText(mUsuario.Correo);

        for (int i = 0; i < vPrefijo.getCount(); i++) {
            if (((xPrefijo_Telf)vPrefijo.getItemAtPosition(i)).iX.equals(mUsuario.Prefijo)) {
                vPrefijo.setSelection(i);
                break;
            }
        }

        mImageLoader = HttpCola.getInstance(getContext()).getImageLoader();

        if(mUsuario.Imagen_Perfil != null && !mUsuario.Imagen_Perfil.equals(""))
        {
            if(mUsuario.Imagen_Perfil.contains("imagen_perfil_defecto.png")){
                vImagen.setImageResource(R.drawable.imagen_perfil_defecto);
            }else{
                //vImagen.setImageUrl(Constantes.HTTP_KMED_SERVER+t.Imagen, mImageLoader);

               String imagen_perfil = Constantes.HTTP_IMAGENES_SERVER+"/" + mUsuario.Id_Aplicacion + "/l/"+ mUsuario.Imagen_Perfil.replace (mUsuario.Id_Aplicacion  +"/", "");

                vImagen.setImageUrl( imagen_perfil, mImageLoader);
            }
        }

        vGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = vNombre.getText().toString();
                String apellidos = vApellidos.getText().toString();
                String telefono = vTelefono.getText().toString();
                String correo = vCorreo.getText().toString();
                String prefijo = ((xPrefijo_Telf) vPrefijo.getSelectedItem()).iX;

                boolean si_ok = true;

                if (nombre.isEmpty()) {
                    vNombre.setError("enter a valid email address");
                    si_ok = false;
                } else {
                    vNombre.setError(null);
                }

                if (apellidos.isEmpty()) {
                    vApellidos.setError("enter a valid email address");
                    si_ok = false;
                } else {
                    vApellidos.setError(null);
                }

                if (telefono.isEmpty() || !Patterns.PHONE.matcher(telefono).matches()) {
                    vTelefono.setError("enter a valid email address");
                    si_ok = false;
                } else {
                    vTelefono.setError(null);
                }

                if (correo.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    vCorreo.setError("enter a valid email address");
                    si_ok = false;
                } else {
                    vCorreo.setError(null);
                }

                if (si_ok) {

                    mUsuario.Nombre = nombre;
                    mUsuario.Apellidos = apellidos;
                    mUsuario.Correo = correo;
                    mUsuario.Prefijo = prefijo;
                    mUsuario.Tlf_Movil = telefono;


                    try {
                        usuariosDao = usuariosDao.init(getContext(), BaseDao.getInstance(getContext()));

                        usuariosDao.modificar(mUsuario);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }


            }
        });

        vEditarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturarImagen();
            }
        });
    }

    private void capturarImagen() {
        fileUri = MediaHelper.getOutputMediaFileUri(MediaHelper.MEDIA_TYPE_IMAGE);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();


        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, MediaHelper.CAMERA);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            fileUri = savedInstanceState.getParcelable("file_uri");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MediaHelper.CAMERA) {
            if (resultCode == getActivity().RESULT_OK) {
                final boolean si_Camera;
                if (data == null) {
                    si_Camera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        si_Camera = false;
                    } else {
                        si_Camera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                if(si_Camera)
                {
                    File archivo = new File(fileUri.getPath());
                    new UploadFileToServer(archivo, MediaHelper.CAMERA).execute();

                }else{
                    fileUri = data.getData();
                    File archivo = new File(fileUri.getPath());
                    new UploadFileToServer(archivo, MediaHelper.GALERIA).execute();
                }
               // new UploadFileToServer(archivo, MediaHelper.CAMERA).execute();
            }
            else if (resultCode == getActivity().RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
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
            Log.d("prog", progress.toString());
        }

        //

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(){
            try {
                MultipartUtility multipart = new MultipartUtility(Constantes.USUARIO_MODIFICAR_IMAGEN_PERFIL, "UTF-8");

                // File filePath = new File(fileUri.getPath());

                multipart.addFormField("_id_configuracion", mUsuario.Id_Aplicacion);
                multipart.addFormField("_id_usuario", mUsuario.Id);
                multipart.addFormField("_id_usuario_clase", mUsuario.Id_Clase);

                if(mOrigen == MediaHelper.CAMERA) {
                    multipart.addFilePart("fileUpload", mArchivo);
                }else{
                    if(mOrigen == MediaHelper.GALERIA) {

                        File filePath = new File(fileUri.getPath());

                        multipart.addFilePart("fileUpload", filePath.getName(), fileUri, getContext());
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

            Random ran = new Random();
            int x = ran.nextInt(6) + 5;

            String imagen_perfil = Constantes.HTTP_IMAGENES_SERVER+"/" + mUsuario.Id_Aplicacion + "/l/"+ mUsuario.Imagen_Perfil.replace (mUsuario.Id_Aplicacion  +"/", "")+"?"+x;

            vImagen.setImageUrl(imagen_perfil, mImageLoader);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

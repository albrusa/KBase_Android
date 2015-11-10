package kumo.kbase_android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.httpRequest.LruBitmapCache;
import kumo.kbase_android.model.Usuario;
import kumo.kbase_android.utils.Constantes;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class UsuariosListAdapter extends RecyclerView.Adapter<UsuariosListAdapter.AdapterElementoViewHolder> implements View.OnClickListener{
    private List<Usuario> l_usuarios;
    private View.OnClickListener listener;

    public UsuariosListAdapter(List<Usuario> _l_usuarios) {
        this.l_usuarios = _l_usuarios;
    }

    public static class AdapterElementoViewHolder
            extends RecyclerView.ViewHolder{

        private TextView mNombre_Aplicacion;
        private TextView mNombre_Usuario;
        RequestQueue mRequestQueue;
        NetworkImageView mImagen;
        ImageLoader mImageLoader;


        public AdapterElementoViewHolder(View itemView) {
            super(itemView);

            mNombre_Aplicacion = (TextView)itemView.findViewById(R.id.Nombre_Aplicacion);
            mNombre_Usuario = (TextView)itemView.findViewById(R.id.Nombre_Usuario);
            mImagen = (NetworkImageView) itemView.findViewById(R.id.Imagen);


        }

        public void bindTitular(Usuario t) {
            mNombre_Usuario.setText(t.Nombre);
            mNombre_Aplicacion.setText(t.Aplicacion);

            ImageLoader.ImageCache imageCache = new LruBitmapCache(itemView.getContext());

            mImageLoader = HttpCola.getInstance(itemView.getContext()).getImageLoader();

            if(t.Imagen_Perfil != null && t.Imagen_Perfil != "")
            {
                mImagen.setImageUrl(Constantes.HTTP_KMED_SERVER+t.Imagen_Perfil, mImageLoader);
            }
        }

    }

    @Override
    public AdapterElementoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.usuario_list_item, viewGroup, false);

        AdapterElementoViewHolder tvh = new AdapterElementoViewHolder(itemView);

        itemView.setOnClickListener(this);

        return tvh;
    }

    @Override
    public void onBindViewHolder(AdapterElementoViewHolder viewHolder, int pos) {
        Usuario item = l_usuarios.get(pos);

        viewHolder.bindTitular(item);
    }

    @Override
    public int getItemCount() {
        return l_usuarios.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {

        if(listener != null)
            listener.onClick(view);
    }
}


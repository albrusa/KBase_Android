package kumo.kbase_android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
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

    public UsuariosListAdapter(Context _context) {
        this.l_usuarios = new ArrayList<>();
    }

    public static class AdapterElementoViewHolder
            extends RecyclerView.ViewHolder{

        private TextView vNombre_Aplicacion;
        private TextView vNombre_Usuario;
        RequestQueue mRequestQueue;
        NetworkImageView vImagen;
        ImageLoader mImageLoader;


        public AdapterElementoViewHolder(View itemView) {
            super(itemView);

            vNombre_Aplicacion = (TextView)itemView.findViewById(R.id.Nombre_Aplicacion);
            vNombre_Usuario = (TextView)itemView.findViewById(R.id.Nombre_Usuario);
            vImagen = (NetworkImageView) itemView.findViewById(R.id.Imagen);


        }

        public void bindTitular(Usuario t) {
            vNombre_Usuario.setText(t.Nombre);
            vNombre_Aplicacion.setText(t.Aplicacion);

            ImageLoader.ImageCache imageCache = new LruBitmapCache(itemView.getContext());

            mImageLoader = HttpCola.getInstance(itemView.getContext()).getImageLoader();

            if(t.Imagen_Perfil != null && t.Imagen_Perfil != "")
            {
                vImagen.setImageUrl(Constantes.HTTP_KMED_SERVER+t.Imagen_Perfil, mImageLoader);

            }
        }

    }

    @Override
    public AdapterElementoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.usuario_list_item, viewGroup, false);

        AdapterElementoViewHolder tvh = new AdapterElementoViewHolder(itemView);

        if(tvh.vImagen != null){
            tvh.vImagen.setImageDrawable(null);
        }

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


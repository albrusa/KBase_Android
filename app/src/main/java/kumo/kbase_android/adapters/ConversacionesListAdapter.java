package kumo.kbase_android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.httpRequest.HttpCola;
import kumo.kbase_android.httpRequest.LruBitmapCache;
import kumo.kbase_android.model.Conversacion;
import kumo.kbase_android.utils.CircularNetworkImageView;
import kumo.kbase_android.utils.Constantes;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class ConversacionesListAdapter extends RecyclerView.Adapter<ConversacionesListAdapter.AdapterElementoViewHolder> implements View.OnClickListener{
    private List<Conversacion> l_conversaciones;
    private View.OnClickListener listener;
    private ImageLoader mImageLoader;

    public ConversacionesListAdapter(List<Conversacion> _l_conversaciones) {
        this.l_conversaciones = _l_conversaciones;
    }

    public ConversacionesListAdapter(Context _context) {
        this.l_conversaciones = new ArrayList<>();
    }

    public static class AdapterElementoViewHolder
            extends RecyclerView.ViewHolder {

        private TextView vMensaje;
        private TextView vNombre_Usuario;
        CircularNetworkImageView vImagen;
        TextView vFecha;


        RequestQueue mRequestQueue;

        ImageLoader mImageLoader;


        public AdapterElementoViewHolder(View itemView) {
            super(itemView);

            vMensaje = (TextView)itemView.findViewById(R.id.Mensaje);
            vNombre_Usuario = (TextView)itemView.findViewById(R.id.Nombre_Usuario);
            vImagen = (CircularNetworkImageView) itemView.findViewById(R.id.Imagen);
            vFecha = (TextView)itemView.findViewById(R.id.Fecha);

        }

        public void bindTitular(Conversacion t) {
            vNombre_Usuario.setText(t.Nombre);
            vMensaje.setText(t.Mensaje);
            vFecha.setText(t.Dia + " " + t.Hora);

            ImageLoader.ImageCache imageCache = new LruBitmapCache(itemView.getContext());

            mImageLoader = HttpCola.getInstance(itemView.getContext()).getImageLoader();

           /* vImagen.setImageDrawable(null);
            vImagen.setImageBitmap(null);
            vImagen.setImageURI(null);*/

            if(t.Imagen != null && !t.Imagen.equals(""))
            {
                if(t.Imagen.contains("imagen_perfil_defecto.png")){
                    vImagen.setImageResource(R.drawable.imagen_perfil_defecto);
                }else{
                    //vImagen.setImageUrl(Constantes.HTTP_KMED_SERVER+t.Imagen, mImageLoader);

                    t.Imagen = Constantes.HTTP_IMAGENES_SERVER+"/" + t.Id_Aplicacion + "/s/"+ t.Imagen.replace (t.Id_Aplicacion +"/", "");

                    vImagen.setImageUrl(t.Imagen, mImageLoader);
                }
            }
        }
    }


    @Override
    public AdapterElementoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.conversaciones_list_item, viewGroup, false);

        AdapterElementoViewHolder tvh = new AdapterElementoViewHolder(itemView);

        tvh.vImagen.setImageDrawable(null);
        tvh.vImagen.setImageBitmap(null);
        tvh.vImagen.setImageURI(null);

        itemView.setOnClickListener(this);

        return tvh;
    }

    public void updateData(List<Conversacion> _conversaciones) {
        l_conversaciones.clear();
        l_conversaciones.addAll(_conversaciones);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AdapterElementoViewHolder viewHolder, int pos) {
        Conversacion item = l_conversaciones.get(pos);

        viewHolder.bindTitular(item);
    }

    @Override
    public int getItemCount() {
        return l_conversaciones.size();
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


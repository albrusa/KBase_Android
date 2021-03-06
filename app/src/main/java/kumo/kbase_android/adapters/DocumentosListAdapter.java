package kumo.kbase_android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.model.Documento;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class DocumentosListAdapter extends RecyclerView.Adapter<DocumentosListAdapter.AdapterElementoViewHolder> implements View.OnClickListener{
    private List<Documento> l_documentos;
    private View.OnClickListener listener;
    private static Context mContext;

    public DocumentosListAdapter(List<Documento> _l_documentos) {
        this.l_documentos = _l_documentos;
    }

    public DocumentosListAdapter(Context _context) {

        this.mContext = _context;
        this.l_documentos = new ArrayList<>();
    }

    public static class AdapterElementoViewHolder
            extends RecyclerView.ViewHolder {

        private TextView vMensaje;
        private TextView vNombre_Usuario;
        private ImageView vImagen;
        private TextView vFecha;

        private DateFormat mDateFormat;
        private DateFormat mTimeFormat;

        private Date mToday;

        private RequestQueue mRequestQueue;
        private ImageLoader mImageLoader;


        public AdapterElementoViewHolder(View itemView) {
            super(itemView);

            mToday = new Date();

            mDateFormat = android.text.format.DateFormat.getMediumDateFormat(mContext);
            mTimeFormat = android.text.format.DateFormat.getTimeFormat(mContext);
           // mDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            vMensaje = (TextView)itemView.findViewById(R.id.Mensaje);
            vNombre_Usuario = (TextView)itemView.findViewById(R.id.Nombre_Usuario);
            vImagen = (ImageView) itemView.findViewById(R.id.Imagen);
            vFecha = (TextView)itemView.findViewById(R.id.Fecha);

        }

        public void bindTitular(Documento t) {
            vNombre_Usuario.setText(t.Nombre);
            vMensaje.setText(t.Propietario);

            String hoy = mDateFormat.format(mToday);
            String fecha = mDateFormat.format(t.Fecha);



            if(hoy.equals(fecha)){
                vFecha.setText(mTimeFormat.format(t.Fecha));
            }else{
                vFecha.setText(fecha + " "+ mTimeFormat.format(t.Fecha));
            }



            if(t.Imagen != null && t.Imagen != "") {

                t.Imagen = t.Imagen.replace(".png","");

                int id = mContext.getResources().getIdentifier(t.Imagen, "drawable", mContext.getPackageName());
                if(id!= 0) {
                    vImagen.setImageResource(id);
                }

            }
           /* ImageLoader.ImageCache imageCache = new LruBitmapCache(itemView.getContext());

            mImageLoader = HttpCola.getInstance(itemView.getContext()).getImageLoader();

            if(t.Imagen != null && t.Imagen != "")
            {
                mImagen.setImageUrl(Constantes.HTTP_KMED_SERVER+t.Imagen, mImageLoader);
            }*/


        }
    }

    @Override
    public AdapterElementoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.documentos_list_item, viewGroup, false);

        AdapterElementoViewHolder tvh = new AdapterElementoViewHolder(itemView);

        itemView.setOnClickListener(this);

        if(tvh.vImagen != null){
            tvh.vImagen.setImageDrawable(null);
        }

        return tvh;
    }

    public void updateData(List<Documento> _documentos) {
        l_documentos.clear();
        l_documentos.addAll(_documentos);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AdapterElementoViewHolder viewHolder, int pos) {
        Documento item = l_documentos.get(pos);

        viewHolder.bindTitular(item);
    }

    @Override
    public int getItemCount() {
        return l_documentos.size();
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


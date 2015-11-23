package kumo.kbase_android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.model.Mensaje;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class MensajesListAdapter extends RecyclerView.Adapter<MensajesListAdapter.AdapterElementoViewHolder> implements View.OnClickListener{
    private List<Mensaje> l_mensajes;
    private String mId_Usuario;

    private int position_ant = -1;


    private View.OnClickListener listener;

    public MensajesListAdapter(List<Mensaje> _l_mensajes ,String _id_Usuario) {
        this.l_mensajes = _l_mensajes;
        this.mId_Usuario = _id_Usuario;
    }

    public static class AdapterElementoViewHolder
            extends RecyclerView.ViewHolder {

        private TextView mMensaje;
        private TextView mNombre_Usuario;
        RequestQueue mRequestQueue;
        NetworkImageView mImagen;
        ImageLoader mImageLoader;


        public AdapterElementoViewHolder(View itemView) {
            super(itemView);

            mMensaje = (TextView)itemView.findViewById(R.id.Mensaje);
          /*  mNombre_Usuario = (TextView)itemView.findViewById(R.id.Nombre_Usuario);
            mImagen = (NetworkImageView) itemView.findViewById(R.id.Imagen);*/

        }

        public void bindTitular(Mensaje t) {
            //mNombre_Usuario.setText(t.Nombre);
            mMensaje.setText(t.Mensaje);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        Mensaje item = l_mensajes.get(position);

        if(item.Id_Propietario.toUpperCase().equals(mId_Usuario.toUpperCase())){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public AdapterElementoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView;

        switch (viewType) {
            case 0: itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensajes_list_item_propio, viewGroup, false);

                    if(position_ant != viewType) {
                        LinearLayout list = (LinearLayout) itemView.findViewById(R.id.main_layout);
                        list.setBackgroundResource(R.drawable.balloon_outgoing_normal);
                    }
                break;
            case 1: itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensajes_list_item, viewGroup, false);

                if(position_ant != viewType) {
                    LinearLayout list = (LinearLayout) itemView.findViewById(R.id.main_layout);
                    list.setBackgroundResource(R.drawable.balloon_incoming_normal);
                }
           break;
            default:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.mensajes_list_item, viewGroup, false);
                break;
        }

        position_ant = viewType;

        AdapterElementoViewHolder tvh = new AdapterElementoViewHolder(itemView);

        itemView.setOnClickListener(this);

        return tvh;
    }

    @Override
    public void onBindViewHolder(AdapterElementoViewHolder viewHolder, int pos) {
        Mensaje item = l_mensajes.get(pos);

        viewHolder.bindTitular(item);
    }

    @Override
    public int getItemCount() {
        return l_mensajes.size();
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


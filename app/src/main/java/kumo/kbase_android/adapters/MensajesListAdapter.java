package kumo.kbase_android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.model.Mensaje;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class MensajesListAdapter extends RecyclerView.Adapter<MensajesListAdapter.AdapterElementoViewHolder> implements View.OnClickListener{
    private List<Mensaje> l_mensajes;
    private String mId_Usuario;

    private static Context mContext;

    private int position_ant = -1;


    private View.OnClickListener listener;

    public MensajesListAdapter(List<Mensaje> _l_mensajes ,String _id_Usuario) {
        this.l_mensajes = _l_mensajes;
        this.mId_Usuario = _id_Usuario;
    }

    public MensajesListAdapter(Context _context, String _id_Usuario) {
        this.mContext = _context;
        this.l_mensajes = new ArrayList<>();
        this.mId_Usuario = _id_Usuario;
    }

    public static class AdapterElementoViewHolder
            extends RecyclerView.ViewHolder {

        private TextView vMensaje;
        private TextView vFecha;

        private DateFormat mDateFormat;
        private DateFormat mTimeFormat;

        private Date mToday;

        private ImageView vImagen_Archivo;

        /*private RequestQueue mRequestQueue;
        private NetworkImageView mImagen;
        private ImageLoader mImageLoader;*/


        public AdapterElementoViewHolder(View itemView) {
            super(itemView);

            vMensaje = (TextView)itemView.findViewById(R.id.Mensaje);
            vFecha = (TextView)itemView.findViewById(R.id.Fecha);
            vImagen_Archivo  = (ImageView)itemView.findViewById(R.id.Imagen_Archivo);

            mToday = new Date();

            mDateFormat = android.text.format.DateFormat.getMediumDateFormat(mContext);
            mTimeFormat = android.text.format.DateFormat.getTimeFormat(mContext);
        }

        public void bindTitular(Mensaje t) {
            vMensaje.setText(t.Mensaje);

            String hoy = mDateFormat.format(mToday);
            String fecha = mDateFormat.format(t.Creado);

            if(hoy.equals(fecha)){
                vFecha.setText(mTimeFormat.format(t.Creado));
            }else{
                vFecha.setText(fecha + " "+ mTimeFormat.format(t.Creado));
            }

            if(vImagen_Archivo != null){
                if(t.Id_Archivo != null && !t.Id_Archivo.equals("") && !t.Id_Archivo.equals("00000000-0000-0000-0000-000000000000")) {
                    if (t.Imagen_Archivo != null && !t.Imagen_Archivo.equals("")) {

                       t.Imagen_Archivo = t.Imagen_Archivo.replace(".png", "");

                        int id = mContext.getResources().getIdentifier(t.Imagen_Archivo, "drawable", mContext.getPackageName());
                        if (id != 0) {
                            vImagen_Archivo.setImageResource(id);
                            vImagen_Archivo.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        Mensaje item = l_mensajes.get(position);

        Mensaje item_ant;

        if(position > 0){
            item_ant = l_mensajes.get(position-1);
        }else{
            item_ant = null;
        }

        int valor = 0;

        /*
         0 -> text meu inicial
         1 -> text meu no inicial

         2 -> imatge meva inicial
         3 -> imatge meva no inicial

         4 -> text meu inicial
         5 -> text meu no inicial

         6 -> imatge meva inicial
         7 -> imatge meva no inicial

         */

        if(item.Id_Propietario.toUpperCase().equals(mId_Usuario.toUpperCase()))
        {
            if(item_ant == null) {
                valor = 0;
            }else{
                if(item_ant.Id_Propietario.toUpperCase().equals(item.Id_Propietario.toUpperCase())){
                //if(item_ant.Id_Propietario.toUpperCase().equals(mId_Usuario.toUpperCase())){
                    valor = 1;
                }
                else
                {
                    valor = 0;
                }
            }

            if(item.Id_Archivo != null && !item.Id_Archivo.equals("") && !item.Id_Archivo.equals("00000000-0000-0000-0000-000000000000")) {
                if (item.Imagen_Archivo != null && !item.Imagen_Archivo.equals("")) {
                    valor+=2;
                }
            }
        }else
        {
            if(item_ant == null) {
                valor = 4;
            }else{
                if(item_ant.Id_Propietario.toUpperCase().equals(item.Id_Propietario.toUpperCase())){
                //if(item_ant.Id_Propietario.toUpperCase().equals(mId_Usuario.toUpperCase())){
                    valor =  5;
                }
                else
                {
                    valor = 4;
                }
            }

            if(item.Id_Archivo != null && !item.Id_Archivo.equals("") && !item.Id_Archivo.equals("00000000-0000-0000-0000-000000000000")) {
                if (item.Imagen_Archivo != null && !item.Imagen_Archivo.equals("")) {
                    valor+=2;
                }
            }
        }

        return valor;
    }


    /*
         0 -> text meu inicial
         1 -> text meu no inicial

         2 -> imatge meva inicial
         3 -> imatge meva no inicial

         4 -> text no meu inicial
         5 -> text no meu no inicial

         6 -> imatge no meva inicial
         7 -> imatge no meva no inicial

         */

    @Override
    public AdapterElementoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView;
        LinearLayout list;

        int pL,pT,pR,pB;


        switch (viewType) {
            case 0: itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensajes_list_item_propio, viewGroup, false);

                list = (LinearLayout) itemView.findViewById(R.id.main_layout);

                pL = list.getPaddingLeft();
                pT = list.getPaddingTop();
                pR = list.getPaddingRight();
                pB = list.getPaddingBottom();

                list.setBackgroundResource(R.drawable.balloon_outgoing_normal);
                list.setPadding(pL, pT, pR, pB);
                break;
            case 1: itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensajes_list_item_propio, viewGroup, false);
                break;

            case 2: itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensajes_list_imagen_item_propio, viewGroup, false);

                list = (LinearLayout) itemView.findViewById(R.id.main_layout);

                pL = list.getPaddingLeft();
                pT = list.getPaddingTop();
                pR = list.getPaddingRight();
                pB = list.getPaddingBottom();

                list.setBackgroundResource(R.drawable.balloon_outgoing_normal);
                list.setPadding(pL, pT, pR, pB);
                break;
            case 3: itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensajes_list_imagen_item_propio, viewGroup, false);
                break;

            case 4: itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensajes_list_item, viewGroup, false);

                list = (LinearLayout) itemView.findViewById(R.id.main_layout);

                pL = list.getPaddingLeft();
                pT = list.getPaddingTop();
                pR = list.getPaddingRight();
                pB = list.getPaddingBottom();

                list.setBackgroundResource(R.drawable.balloon_incoming_normal);
                list.setPadding(pL, pT, pR, pB);
           break;
            case 5: itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensajes_list_item, viewGroup, false);
                break;
            case 6: itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensajes_list_imagen_item, viewGroup, false);

                list = (LinearLayout) itemView.findViewById(R.id.main_layout);

                pL = list.getPaddingLeft();
                pT = list.getPaddingTop();
                pR = list.getPaddingRight();
                pB = list.getPaddingBottom();

                list.setBackgroundResource(R.drawable.balloon_incoming_normal);
                list.setPadding(pL, pT, pR, pB);
                break;
            case 7: itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensajes_list_imagen_item, viewGroup, false);
                break;
            default:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.mensajes_list_item, viewGroup, false);
                break;
        }

        position_ant = viewType;

        AdapterElementoViewHolder tvh = new AdapterElementoViewHolder(itemView);

        if(tvh.vImagen_Archivo != null){
            tvh.vImagen_Archivo.setImageDrawable(null);
        }

        itemView.setOnClickListener(this);

        return tvh;
    }

    public void updateData(List<Mensaje> _mensajes) {
        l_mensajes.clear();
        l_mensajes.addAll(_mensajes);
        notifyDataSetChanged();
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


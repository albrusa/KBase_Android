package kumo.kbase_android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import kumo.kbase_android.R;
import kumo.kbase_android.model.Informacion;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class InformacionesListAdapter extends RecyclerView.Adapter<InformacionesListAdapter.AdapterElementoViewHolder> implements View.OnClickListener{
    private List<Informacion> l_informaciones;
    private View.OnClickListener listener;
    private static Context mContext;

    public InformacionesListAdapter(Context _context) {

        this.mContext = _context;
        this.l_informaciones = new ArrayList<>();
    }

    public static class AdapterElementoViewHolder
            extends RecyclerView.ViewHolder {

        private TextView vMensaje;
        private TextView vNombre_Usuario;
        ImageView vImagen;
        TextView vFecha;

        DateFormat mDateFormat;


        public AdapterElementoViewHolder(View itemView) {
            super(itemView);

            mDateFormat = android.text.format.DateFormat.getMediumDateFormat(mContext);

            vMensaje = (TextView)itemView.findViewById(R.id.Mensaje);
            vNombre_Usuario = (TextView)itemView.findViewById(R.id.Nombre_Usuario);
            vImagen = (ImageView) itemView.findViewById(R.id.Imagen);
            vFecha = (TextView)itemView.findViewById(R.id.Fecha);

        }

        public void bindTitular(Informacion t) {
            vNombre_Usuario.setText(t.Titulo);
            vMensaje.setText(t.Descripcion);

            String fecha = "";

            if(t.Dia != null && !t.Dia.equals("")){
                fecha = t.Dia;
            }
            if(t.Hora != null && !t.Hora.equals("")){

                if(!fecha.equals("")) fecha += " ";

                fecha = t.Dia;
            }
            vFecha.setText(fecha);

            if(t.Imagen != null && !t.Imagen.equals("")) {

                t.Imagen = t.Imagen.replace(".png","").toLowerCase();

                int id = mContext.getResources().getIdentifier(t.Imagen, "drawable", mContext.getPackageName());

                vImagen.setImageResource(id);

            }
        }
    }

    @Override
    public AdapterElementoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.informaciones_list_item, viewGroup, false);

        AdapterElementoViewHolder tvh = new AdapterElementoViewHolder(itemView);

        if(tvh.vImagen != null){
            tvh.vImagen.setImageDrawable(null);
        }

        itemView.setOnClickListener(this);

        return tvh;
    }

    public void updateData(List<Informacion> _informaciones) {
        l_informaciones.clear();
        l_informaciones.addAll(_informaciones);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AdapterElementoViewHolder viewHolder, int pos) {
        Informacion item = l_informaciones.get(pos);


        viewHolder.bindTitular(item);
    }

    @Override
    public int getItemCount() {
        return l_informaciones.size();
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


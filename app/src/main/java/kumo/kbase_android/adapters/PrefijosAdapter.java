package kumo.kbase_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kumo.kbase_android.model.xPrefijo_Telf;
import kumo.kbase_android.utils.Cultura;

/**
 * Created by dev_2 on 02/11/2015.
 */
public class PrefijosAdapter extends ArrayAdapter<xPrefijo_Telf> {

    private Context mContext;
    private List<xPrefijo_Telf> mPrefijos;

    public PrefijosAdapter( Context cxt, int textViewResourceId, List<xPrefijo_Telf> values)
    {
        super(cxt, textViewResourceId, values);

        mContext = cxt;
        mPrefijos = Cultura.obt_prefijos();
    }

    @Override
    public int getCount() {
        return mPrefijos.size();
    }

    @Override
    public xPrefijo_Telf getItem(int index) {
        return mPrefijos.get(index);
    }


    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(android.R.layout.select_dialog_item, parent, false);
        TextView textView = (TextView) rowView;
        textView.setText(mPrefijos.get(position).vX);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.select_dialog_singlechoice, parent, false);
        TextView textView = (TextView) rowView;
        textView.setText(mPrefijos.get(position).vX);
        return textView;
    }

}

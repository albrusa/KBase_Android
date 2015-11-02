package kumo.kbase_android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kumo.kbase_android.model.xPrefijo_Telf;
import kumo.kbase_android.utils.Cultura;

/**
 * Created by dev_2 on 02/11/2015.
 */
public class PrefijosAdapter extends BaseAdapter {

    private Context mContext;
    private List<xPrefijo_Telf> mPrefijos;

    public PrefijosAdapter( Context cxt )
    {
        super();

        mContext = cxt;
        mPrefijos = Cultura.obt_prefijos();
    }

    @Override
    public int getCount() {
        return mPrefijos.size();
    }

    @Override
    public Object getItem(int index) {
        return mPrefijos.get(index);
    }


    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View recycleView, ViewGroup viewGroup) {
        TextView view;
        if( recycleView == null )
        {
            view = new TextView(m_Context);
            view.setPadding(30, 10, 10, 10);
        }
        else
        {
            view = (TextView)recycleView;
        }

        view.setText(m_Countries[index]);

        return view;
    }

}

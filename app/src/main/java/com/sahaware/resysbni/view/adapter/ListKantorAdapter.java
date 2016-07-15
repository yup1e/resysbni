package com.sahaware.resysbni.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.DataKantor;

import java.util.List;

/***** Adapter class extends with ArrayAdapter ******/
public class ListKantorAdapter extends ArrayAdapter<String>{

    private Context activity;
    private List data;
    public Resources res;
    DataKantor tempValues=null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public ListKantorAdapter(
            Context activitySpinner,
            int textViewResourceId,
            List objects,
            Resources resLocal
    )
    {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
        res      = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_rows, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (DataKantor) data.get(position);

        TextView nama = (TextView)row.findViewById(R.id.nama);
        TextView keterangan = (TextView)row.findViewById(R.id.keterangan);
        TextView id = (TextView)row.findViewById(R.id.idKantor);

            // Set values for spinner each row
            nama.setText(tempValues.getNama() + " (" + tempValues.getJarak()+ " KM)");
            keterangan.setText(tempValues.getAlamat());
            id.setText(tempValues.getIDKantor().toString());




        return row;
    }
}
package br.com.rotteny.yohrebanho.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.rotteny.yohrebanho.R;
import br.com.rotteny.yohrebanho.models.Ocorrencia;

/**
 * Created by ENCBACK on 27/07/2017.
 */

public class OcorrenciaListAdapter extends ArrayAdapter<Ocorrencia> {

    private Context context;
    private ArrayList<Ocorrencia> list;
    private int layout;

    public OcorrenciaListAdapter(@NonNull Context context, int layout, ArrayList<Ocorrencia> list) {
        super(context, layout, list);

        this.context = context;
        this.list = list;
        //R.layout.ocorrencia_list_item
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout,null);

        Ocorrencia ocorrencia = list.get(position);

        TextView txtOcorrencia = (TextView) convertView.findViewById(R.id.txtOcorrencia);
        txtOcorrencia.setText("Em: " + ocorrencia.getData() + ", " + ocorrencia.getOcorrencia());

        return convertView;
    }
}

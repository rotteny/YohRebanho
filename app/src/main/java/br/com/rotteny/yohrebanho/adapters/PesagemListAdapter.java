package br.com.rotteny.yohrebanho.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.rotteny.yohrebanho.R;
import br.com.rotteny.yohrebanho.models.Pesagem;

/**
 * Created by ENCBACK on 12/07/2017.
 */

public class PesagemListAdapter extends ArrayAdapter<Pesagem> {

    private Context context;
    private ArrayList<Pesagem> list;
    private int layout;

    public PesagemListAdapter(@NonNull Context context,int layout, ArrayList<Pesagem> list) {
        super(context, layout, list);

        this.context = context;
        this.list = list;
        //R.layout.pesagem_list_item
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout,null);

        Pesagem pesagem = list.get(position);

        TextView txtPesagem = (TextView) convertView.findViewById(R.id.txtPesagem);
        txtPesagem.setText("Em: " + pesagem.getData() + ", " + Double.toString(pesagem.getPeso()) + "Kg");

        return convertView;
    }
}

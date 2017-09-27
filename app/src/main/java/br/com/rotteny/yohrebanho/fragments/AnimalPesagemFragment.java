package br.com.rotteny.yohrebanho.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.rotteny.yohrebanho.AnimalViewActivity;
import br.com.rotteny.yohrebanho.MainActivity;
import br.com.rotteny.yohrebanho.R;
import br.com.rotteny.yohrebanho.adapters.PesagemListAdapter;
import br.com.rotteny.yohrebanho.handlers.DatabaseHelper;
import br.com.rotteny.yohrebanho.models.Pesagem;

/**
 * Created by ENCBACK on 26/07/2017.
 */

public class AnimalPesagemFragment extends Fragment {

    private static final String TAG = "Pesagens";

    private static Pesagem pesagem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_animal_view_pesagem, container, false);

        PesagemListAdapter adapter = new PesagemListAdapter(getContext(), R.layout.pesagem_list_item, (ArrayList<Pesagem>) AnimalViewActivity.listPesagem);

        ListView listView = (ListView) view.findViewById(R.id.listPesagem);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                pesagem = AnimalViewActivity.listPesagem.get(position);

                dialog.setTitle("Deseja apagar este registro?")
                        .setPositiveButton("Confirmar",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHelper dbHelper = new DatabaseHelper(getContext());

                                if(dbHelper.removeData(pesagem))
                                {
                                    Toast.makeText(getContext(), "Dados removidos...", Toast.LENGTH_LONG).show();

                                    // Reload activity
                                    Intent intent = getActivity().getIntent();
                                    getActivity().finish();
                                    getActivity().startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Erros db...", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar",null);

                dialog.show();
                return false;
            }
        });
        return view;
    }

}

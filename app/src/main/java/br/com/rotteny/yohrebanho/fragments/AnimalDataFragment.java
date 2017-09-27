package br.com.rotteny.yohrebanho.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.rotteny.yohrebanho.AnimalViewActivity;
import br.com.rotteny.yohrebanho.MainActivity;
import br.com.rotteny.yohrebanho.R;
import br.com.rotteny.yohrebanho.handlers.DatabaseHelper;
import br.com.rotteny.yohrebanho.models.Animal;

/**
 * Created by ENCBACK on 26/07/2017.
 */

public class AnimalDataFragment extends Fragment {

    private static final String TAG = "Dados";

    ImageView imageView;
    TextView txtDataNascimento, txtProprietario, txtBrincoPai, txtBrincoMae, txtSexo, txtPesagem;

    DatabaseHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper    = new DatabaseHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.tab_animal_view_data, container, false);

            imageView           = (ImageView) view.findViewById(R.id.imagem);

            txtDataNascimento   = (TextView) view.findViewById(R.id.txtDataNascimento);
            txtProprietario     = (TextView) view.findViewById(R.id.txtProprietario);
            txtBrincoPai        = (TextView) view.findViewById(R.id.txtBrincoPai);
            txtBrincoMae        = (TextView) view.findViewById(R.id.txtBrincoMae);
            txtSexo             = (TextView) view.findViewById(R.id.txtSexo);
            txtPesagem          = (TextView) view.findViewById(R.id.txtUltimaPesagem);

            imageView.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                            AnimalViewActivity.animal.getImagem(),
                            0,
                            AnimalViewActivity.animal.getImagem().length));

            if(AnimalViewActivity.animal.getAnimalPaiID() == null || AnimalViewActivity.animal.getAnimalPaiID() == 0) {
                txtBrincoPai.setText("Sem brinco de pai");
            }
            else {
                Animal animalPai = dbHelper.getAnimal(AnimalViewActivity.animal.getAnimalPaiID());
                txtBrincoPai.setText(animalPai.getBrinco());
            }

            if(AnimalViewActivity.animal.getAnimalMaeID() == null || AnimalViewActivity.animal.getAnimalMaeID() == 0) {
                txtBrincoMae.setText("Sem brinco de mãe");
            }
            else {
                Animal animalMae = dbHelper.getAnimal(AnimalViewActivity.animal.getAnimalMaeID());
                txtBrincoMae.setText(animalMae.getBrinco());
            }

            if(AnimalViewActivity.animal.getSexo().equals(Animal.SEXO_MACHO)) {
                txtSexo.setText(Animal.SEXO_DISPLAY_MACHO);
            }
            else {
                txtSexo.setText(Animal.SEXO_DISPLAY_FEMEA);
            }

            txtDataNascimento.setText(AnimalViewActivity.animal.getDataNascimento());

            txtProprietario.setText(AnimalViewActivity.usuario.getNome());

            txtPesagem.setText(Double.toString(AnimalViewActivity.animal.getPeso()) + " Kg");

            return view;
    }
}

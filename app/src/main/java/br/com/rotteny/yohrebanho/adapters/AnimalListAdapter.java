package br.com.rotteny.yohrebanho.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.rotteny.yohrebanho.R;
import br.com.rotteny.yohrebanho.models.Animal;
import br.com.rotteny.yohrebanho.handlers.DatabaseHelper;

/**
 * Created by ENCBACK on 06/07/2017.
 */

public class AnimalListAdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private ArrayList<Animal> animalList;

    public AnimalListAdapter(Context context, int layout, ArrayList<Animal> animalList) {
        this.context = context;
        this.layout = layout;
        this.animalList = animalList;
    }

    @Override
    public int getCount() {
        return animalList.size();
    }

    @Override
    public Object getItem(int position) {
        return animalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtBrinco, txtSexo, txtDataNascimento, txtPeso;

        public void settxtBrinco(String txtBrinco) {
            this.txtBrinco.setText(txtBrinco);
        }

        public void setTxtDataNascimento(String txtDataNascimento) {
            this.txtDataNascimento.setText(txtDataNascimento);
        }

        public void setImageView(byte[] imageView) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageView,0,imageView.length);
            this.imageView.setImageBitmap(bitmap);
        }
        public void setTxtSexo(String txtSexo) {
            this.txtSexo.setText(txtSexo);
        }


        public void setTxtPeso(double txtPeso) {
            this.txtPeso.setText(Double.toString(txtPeso) + " Kg");
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            holder.txtBrinco = (TextView) view.findViewById(R.id.txtBrinco);
            holder.txtSexo = (TextView) view.findViewById(R.id.txtSexo);
            holder.txtDataNascimento = (TextView) view.findViewById(R.id.txtDataNascimento);
            holder.txtPeso = (TextView) view.findViewById(R.id.txtPeso);
            holder.imageView = (ImageView) view.findViewById(R.id.imgAnimal);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        Animal animal = animalList.get(position);

        holder.settxtBrinco(animal.getBrinco());
        if(animal.getSexo().equals(Animal.SEXO_MACHO)){
            holder.setTxtSexo(Animal.SEXO_DISPLAY_MACHO);
        }
        else {
            holder.setTxtSexo(Animal.SEXO_DISPLAY_FEMEA);
        }

        holder.setTxtDataNascimento(animal.getDataNascimento());
        holder.setTxtPeso(animal.getPeso());
        holder.setImageView(animal.getImagem());

        return view;
    }
}

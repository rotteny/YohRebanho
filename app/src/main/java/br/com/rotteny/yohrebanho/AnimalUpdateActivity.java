package br.com.rotteny.yohrebanho;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.rotteny.yohrebanho.handlers.BitmapHandler;
import br.com.rotteny.yohrebanho.handlers.DatabaseHelper;
import br.com.rotteny.yohrebanho.handlers.DateHandler;
import br.com.rotteny.yohrebanho.models.Animal;
import br.com.rotteny.yohrebanho.models.Usuario;

/**
 * Created by ENCBACK on 07/07/2017.
 */

public class AnimalUpdateActivity extends AppCompatActivity {

    EditText editbrinco,editDataNascimento;
    public static TextView textbrincoPai, textbrincoMae, textProprietario;
    public static Usuario usuario;
    public static Animal animal, animalPai, animalMae;
    ImageView imageView;
    String sexo;

    static DatabaseHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_update_activity);

        init();
    }

    private void init() {
        textProprietario    = (TextView) findViewById(R.id.textView_proprietario);
        textbrincoPai       = (TextView) findViewById(R.id.textView_brinco_pai);
        textbrincoMae       = (TextView) findViewById(R.id.textView_brinco_mae);
        editbrinco          = (EditText) findViewById(R.id.editText_brinco);
        editDataNascimento  = (EditText) findViewById(R.id.editText_data_nascimento);
        dateMask();
        imageView           = (ImageView) findViewById(R.id.imagem);

        dbHelper            = new DatabaseHelper(this);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        Integer animalID = settings.getInt("animalID",0);
        animal = dbHelper.getAnimal(animalID);

        getSupportActionBar().setTitle(animal.getBrinco());

        int usuarioID   = settings.getInt("usuarioID", 0);
        if(usuarioID == 0)
            usuarioID   = animal.getUsuarioID();

        usuario         = dbHelper.getUsuario(usuarioID);

        textProprietario.setText(usuario.getNome());

        String imagem = settings.getString("imagem", "");
        if(!imagem.isEmpty()){
            Bitmap bitmap = BitmapHandler.decodeBase64(imagem);
            imageView.setImageBitmap(BitmapHandler.bitmapResize(bitmap));
        }
        else {
            imageView.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                            animal.getImagem(),
                            0,
                            animal.getImagem().length));
        }

        Integer animalPaiID = settings.getInt("animalPaiID", 0);
        if(animalPaiID != 0){
            animalPai = dbHelper.getAnimal(animalPaiID);
            textbrincoPai.setText(animalPai.getBrinco());
        }
        else {
            if(animal.getAnimalPaiID() != 0){
                animalPai = dbHelper.getAnimal(animal.getAnimalPaiID());
                textbrincoPai.setText(animalPai.getBrinco());
            }
        }

        Integer animalMaeID = settings.getInt("animalMaeID", 0);
        if(animalMaeID != 0){
            animalMae = dbHelper.getAnimal(animalMaeID);
            textbrincoMae.setText(animalMae.getBrinco());
        }
        else {
            if(animal.getAnimalMaeID() != 0) {
                animalMae = dbHelper.getAnimal(animal.getAnimalMaeID());
                textbrincoMae.setText(animalMae.getBrinco());
            }
        }

        String brinco = settings.getString("brinco", "");
        if(!brinco.isEmpty()) {
            editbrinco.setText(brinco);
        }
        else {
            editbrinco.setText(animal.getBrinco());
        }

        String dataNascimento = settings.getString("dataNascimento", "");
        if(!dataNascimento.isEmpty()) {
            editDataNascimento.setText(dataNascimento);
        }
        else {
            editDataNascimento.setText(animal.getDataNascimento());
        }

        sexo = settings.getString("sexo", "");
        if(!sexo.isEmpty()) {
            sexo = settings.getString("sexo", "");
        }
        else {
            sexo = animal.getSexo();
        }

        RadioButton radioButton;

        if(sexo.equals(Animal.SEXO_FEMEA)) {
            radioButton = (RadioButton) findViewById(R.id.radioSexoF);
        }
        else {
            radioButton = (RadioButton) findViewById(R.id.radioSexoM);
        }

        radioButton.setChecked(true);
    }

    public void saveAnimal(View v) {
        Animal animal;
        try {
            animal = this.animal;
            animal.setBrinco(editbrinco.getText().toString().trim().toUpperCase());
            animal.setUsuarioID(usuario.getID());
            animal.setSexo(sexo);
            animal.setDataNascimento(editDataNascimento.getText().toString().trim());
            animal.setAnimalPaiID(null);
            if(animalPai != null)
                animal.setAnimalPaiID(animalPai.getID());
            animal.setAnimalMaeID(null);
            if(animalMae != null)
                animal.setAnimalMaeID(animalMae.getID());
            animal.setImagem(BitmapHandler.imageViewToByte(imageView));
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Erro", "Falha ao salvar imagem");
            return;
        }

        if (animal.getBrinco().isEmpty()) {
            showMessage("Erro", "Brinco inválido");
            return;
        }

        Animal existe = dbHelper.getAnimalbrinco(animal.getBrinco(),animal.getID());
        if (existe != null && animal.getBrinco().equals(existe.getBrinco())) {
            showMessage("Erro", "Brinco já cadastrado");
            return;
        }

        if (!DateHandler.isDataValida(animal.getDataNascimento())) {
            showMessage("Erro", "Data de nascimento inválida");
            return;
        }

        boolean isInserted = MainActivity.dbHelper.updateData(animal);

        if (isInserted == true) {
            Toast.makeText(this, "Atualizado com sucesso!", Toast.LENGTH_LONG).show();

            Intent returnBtn = new Intent(this,AnimalViewActivity.class);
            startActivity(returnBtn);
        } else {
            Toast.makeText(this, "DB Erro...", Toast.LENGTH_LONG).show();
        }
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Animal.REQUEST_CODE_GALLERY) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,Animal.REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(),"Você não tem permissão para acessar o arquivo.", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void dateMask() {
        editDataNascimento.addTextChangedListener(
                new DateHandler.TextWacherMask(editDataNascimento));
    }

    public void imgSource(View v) {
        CharSequence[] items = {"Câmera", "Galeria"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Selecione sua image");
        dialog.setItems(items,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if( item == 0) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, Animal.REQUEST_IMAGE_CAPTURE);
                }
                else if(item == 1) {
                    ActivityCompat.requestPermissions(
                            AnimalUpdateActivity.this,
                            new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            Animal.REQUEST_CODE_GALLERY);
                }
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        Bitmap bitmap = null;
        if(resultCode == RESULT_OK && data != null) {

            if (requestCode == Animal.REQUEST_CODE_GALLERY) {
                uri = data.getData();

                try {

                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == Animal.REQUEST_IMAGE_CAPTURE) {
                //path from full size image
                Cursor cursor = this.getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{
                                MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.DATE_ADDED,
                                MediaStore.Images.ImageColumns.ORIENTATION
                        },
                        MediaStore.Images.Media.DATE_ADDED,
                        null,
                        "date_added DESC");

                if (cursor != null && cursor.moveToFirst()) {
                    uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    String photoPath = uri.toString();
                    cursor.close();
                    if (photoPath != null) {
                        bitmap = BitmapHandler.decodeSampledBitmap(photoPath,getWindowManager().getDefaultDisplay());//here is the bitmap of image full size
                    }
                }
            }

            imageView.setImageBitmap(BitmapHandler.bitmapResize(bitmap));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void selectUsuario(View v) {
        SelectUsuarioDialogFragment selectUsuarioDialogFragment = new SelectUsuarioDialogFragment();
        selectUsuarioDialogFragment.show(getFragmentManager(), "select");
    }

    public void selectMacho(View v) {
        SelectAnimalDialogFragment selectAnimalDialogFragment = new SelectAnimalDialogFragment();
        selectAnimalDialogFragment.setSexo(Animal.SEXO_MACHO);

        selectAnimalDialogFragment.show(getFragmentManager(), "selectMacho");
    }

    public void selectFemea(View v) {
        SelectAnimalDialogFragment selectAnimalDialogFragment = new SelectAnimalDialogFragment();
        selectAnimalDialogFragment.setSexo(Animal.SEXO_FEMEA);

        selectAnimalDialogFragment.show(getFragmentManager(), "selectFemea");
    }

    public void onRadioButtonClicked(View v) {
        // Is the button now checked?
        boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.radioSexoF:
                if (checked)
                    sexo = Animal.SEXO_FEMEA;
                break;
            case R.id.radioSexoM:
                if (checked)
                    sexo = Animal.SEXO_MACHO;
                break;
        }
    }

    public static class SelectUsuarioDialogFragment extends DialogFragment {
        ArrayList<Usuario> usuarioList;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

            usuarioList = new ArrayList<>();
            usuarioList.clear();
            usuarioList.add(MainActivity.principal);

            dbHelper = new DatabaseHelper(getActivity());
            Cursor response = dbHelper.getAllNotPrincipal();

            if(response != null)
            {
                while (response.moveToNext()) {
                    usuarioList.add(new Usuario(response));
                }
            }

            dialog.setTitle("Selecione o proprietário")
                    .setItems(toSequence(),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int position) {
                            usuario = usuarioList.get(position);
                            textProprietario.setText(usuario.getNome());
                        }
                    });

            return dialog.create();
        }

        private CharSequence[] toSequence(){
            List<String> listItems = new ArrayList<String>();

            for(Usuario u : usuarioList) {
                listItems.add(u.getNome());
            }

            return listItems.toArray(new CharSequence[listItems.size()]);
        }
    }

    public static class SelectAnimalDialogFragment extends DialogFragment {

        String sexo = Animal.SEXO_MACHO;

        ArrayList<Animal> animalList;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

            animalList = new ArrayList<>();
            animalList.clear();

            dbHelper = new DatabaseHelper(getActivity());
            Cursor response = dbHelper.getAllAnimalSexo(sexo,animal.getBrinco());

            dialog.setTitle("Selecione um animal");
            if(response != null && response.getCount() > 0) {

                while (response.moveToNext()) {
                    animalList.add(new Animal(response));
                }
                dialog.setItems(toSequence(),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        if(sexo.equals(Animal.SEXO_MACHO)) {
                            animalPai = animalList.get(position);
                            textbrincoPai.setText(animalPai.getBrinco());
                        }
                        else
                        {
                            animalMae = animalList.get(position);
                            textbrincoMae.setText(animalMae.getBrinco());
                        }
                    }
                });
            } else {
                dialog.setMessage("Nenhum animal disponível");
            }

            return dialog.create();
        }

        public void setSexo(String sexo) { this.sexo = sexo; }

        private CharSequence[] toSequence(){
            List<String> listItems = new ArrayList<String>();

            for(Animal a : animalList) {
                listItems.add(a.getBrinco());
            }

            return listItems.toArray(new CharSequence[listItems.size()]);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("usuarioID", usuario.getID());

        if(null != animalPai)
            editor.putString("animalPaibrinco", animalPai.getBrinco());
        if(null != animalMae)
            editor.putString("animalMaebrinco", animalMae.getBrinco());

        editor.putString("dataNascimento", editDataNascimento.getText().toString());
        editor.putString("sexo", sexo);
        editor.putString("imagem", BitmapHandler.encodeImageViewTobase64(imageView));

        // Commit the edits!
        editor.commit();
    }
}
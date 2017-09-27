package br.com.rotteny.yohrebanho;

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
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import br.com.rotteny.yohrebanho.handlers.BitmapHandler;
import br.com.rotteny.yohrebanho.handlers.DatabaseHelper;
import br.com.rotteny.yohrebanho.handlers.DateHandler;
import br.com.rotteny.yohrebanho.models.Animal;
import br.com.rotteny.yohrebanho.models.Usuario;

/**
 * Created by ENCBACK on 20/07/2017.
 */

public class UsuarioUpdateActivity  extends AppCompatActivity {

    public static final String PREFS_NAME = "UsuarioUpdateActivity";

    public static Usuario usuario;

    EditText editNome, editTelefone, editEmail;

    static DatabaseHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_update_activity);

        init();
    }

    private void init() {

        editNome        = (EditText) findViewById(R.id.editText_nome);
        editTelefone    = (EditText) findViewById(R.id.editText_telefone);
        editEmail       = (EditText) findViewById(R.id.editText_email);

        dbHelper        = new DatabaseHelper(this);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        int usuarioID   = settings.getInt("usuarioID", 0);
        usuario         = dbHelper.getUsuario(usuarioID);

        String nome = settings.getString("nome", "");
        if(!nome.isEmpty()){
            editNome.setText(nome);
        }
        else
        {
            editNome.setText(usuario.getNome());
        }

        String telefone = settings.getString("telefone", "");
        if(!telefone.isEmpty()){
            editTelefone.setText(telefone);
        }
        else
        {
            editTelefone.setText(usuario.getTelefone());
        }

        String email = settings.getString("email", "");
        if(!email.isEmpty()){
            editEmail.setText(email);
        }
        else
        {
            editEmail.setText(usuario.getEmail());
        }
    }

    public void saveUsuario(View v) {
        Usuario usuario;
        usuario = new Usuario(
                editNome.getText().toString().trim().toUpperCase(),
                editTelefone.getText().toString().trim().toUpperCase(),
                editEmail.getText().toString().trim()

        );

        if (usuario.getNome().isEmpty()) {
            showMessage("Erro", "Informe o nome");
            return;
        }

        if (usuario.getTelefone().isEmpty()) {
            showMessage("Erro", "Informe o telefone");
            return;
        }

        if (usuario.getEmail().isEmpty()) {
            showMessage("Erro", "Informe o e-mail");
            return;
        }

        usuario.setID(UsuarioUpdateActivity.usuario.getID());
        boolean isInserted = dbHelper.updateData(usuario);

        if (isInserted == true) {
            Toast.makeText(this, "Sucesso!", Toast.LENGTH_LONG).show();
            Intent returnBtn;

            if(usuario.getPrincipal() == 1) {
                returnBtn = new Intent(this,MainActivity.class);
            }
            else
            {
                returnBtn = new Intent(this,MainActivity.class);
            }

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
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("usuarioID", usuario.getID());
        editor.putString("nome", editNome.getText().toString());
        editor.putString("telefone", editTelefone.getText().toString());
        editor.putString("email", editEmail.getText().toString());

        // Commit the edits!
        editor.commit();
    }
}

package br.com.rotteny.yohrebanho.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.rotteny.yohrebanho.MainActivity;
import br.com.rotteny.yohrebanho.R;
import br.com.rotteny.yohrebanho.handlers.DatabaseHelper;
import br.com.rotteny.yohrebanho.models.Usuario;

/**
 * Created by ENCBACK on 20/07/2017.
 */

public class NovoUsuarioDialogFragment extends DialogFragment {
    LayoutInflater layoutInflater;
    View inflater = null;
    public static AlertDialog.Builder dialog;

    String title;

    EditText editNome, editTelefone, editEmail;

    static DatabaseHelper dbHelper = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        layoutInflater = getActivity().getLayoutInflater();
        inflater = layoutInflater.inflate(R.layout.dialog_usuario_add, null);

        editNome = (EditText) inflater.findViewById(R.id.editText_nome);
        editTelefone = (EditText) inflater.findViewById(R.id.editText_telefone);
        editEmail = (EditText) inflater.findViewById(R.id.editText_email);

        dialog = new AlertDialog.Builder(getActivity());

        dialog.setView(inflater)
                .setPositiveButton("Salvar",null)
                .setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });
        dialog.setTitle("Informe seus dados");
        return dialog.create();
    }

    public static void showNovoUsuario(FragmentManager fragmentManager) {
        NovoUsuarioDialogFragment novoUsuarioDialogFragment = new NovoUsuarioDialogFragment();
        novoUsuarioDialogFragment.setCancelable(false);

        novoUsuarioDialogFragment.show(fragmentManager, "principal");
    }

    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Usuario usuario;
                    usuario = new Usuario(
                            editNome.getText().toString().trim().toUpperCase(),
                            editTelefone.getText().toString().trim().toUpperCase(),
                            editEmail.getText().toString().trim(),
                            1);

                    if (usuario.getNome().isEmpty()) {
                        Toast.makeText(getActivity(), "Nome obrigatório", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (usuario.getTelefone().isEmpty()) {
                        Toast.makeText(getActivity(), "Telefone obrigatório", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (usuario.getEmail().isEmpty()) {
                        Toast.makeText(getActivity(), "E-mail obrigatório", Toast.LENGTH_LONG).show();
                        return;
                    }

                    dbHelper = new DatabaseHelper(getActivity());
                    boolean isInserted = dbHelper.insertData(usuario);

                    if (isInserted == true) {
                        Toast.makeText(getActivity(), "Sucesso!", Toast.LENGTH_LONG).show();
                        MainActivity.principal = dbHelper.getPrincipal();
                        dismiss();
                    } else {
                        Toast.makeText(getActivity(), "DB Erro...", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

}

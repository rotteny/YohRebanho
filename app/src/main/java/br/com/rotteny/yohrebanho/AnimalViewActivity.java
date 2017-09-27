package br.com.rotteny.yohrebanho;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.rotteny.yohrebanho.fragments.AnimalDataFragment;
import br.com.rotteny.yohrebanho.fragments.AnimalOcorrenciaFragment;
import br.com.rotteny.yohrebanho.fragments.AnimalPesagemFragment;
import br.com.rotteny.yohrebanho.handlers.DatabaseHelper;
import br.com.rotteny.yohrebanho.handlers.DateHandler;
import br.com.rotteny.yohrebanho.models.Animal;
import br.com.rotteny.yohrebanho.models.Ocorrencia;
import br.com.rotteny.yohrebanho.models.Pesagem;
import br.com.rotteny.yohrebanho.models.Usuario;

/**
 * Created by ENCBACK on 12/07/2017.
 */

public class AnimalViewActivity extends AppCompatActivity {

    static DatabaseHelper dbHelper = null;

    public static List<Pesagem> listPesagem = new ArrayList<>();
    public static List<Ocorrencia> listOcorrencia = new ArrayList<>();

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public static Animal animal;
    public static Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.animal_view_data);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_remove_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        dbHelper     = new DatabaseHelper(this);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        Integer animalID = settings.getInt("animalID",0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        animal              = dbHelper.getAnimal(animalID);

        getSupportActionBar().setTitle(animal.getBrinco());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usuario             = dbHelper.getUsuario(animal.getUsuarioID());

        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mSectionsPagerAdapter.addFragment(new AnimalDataFragment(),"Dados");
        mSectionsPagerAdapter.addFragment(new AnimalPesagemFragment(),"Pesagens");
        mSectionsPagerAdapter.addFragment(new AnimalOcorrenciaFragment(),"Ocorrências");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Cursor cursor;

        cursor = dbHelper.getAllPesagemAnimal(animal.getID());
        listPesagem = convertCursorPesagemToArrayList(cursor);

        cursor = dbHelper.getAllOcorrenciaAnimal(animal.getID());
        listOcorrencia = convertCursorOcocrrenciaToArrayList(cursor);

        dbHelper.close();
    }

    public static ArrayList<Pesagem> convertCursorPesagemToArrayList(Cursor cursor) {
        ArrayList<Pesagem> lista = new ArrayList<Pesagem>();

        lista.clear();
        if(cursor != null)
        {
            while (cursor.moveToNext()) {
                lista.add(new Pesagem(cursor));
            }
        }

        return lista;
    }

    public static ArrayList<Ocorrencia> convertCursorOcocrrenciaToArrayList(Cursor cursor) {
        ArrayList<Ocorrencia> lista = new ArrayList<Ocorrencia>();

        lista.clear();
        if(cursor != null)
        {
            while (cursor.moveToNext()) {
                lista.add(new Ocorrencia(cursor));
            }
        }

        return lista;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menuRemoveAnimal:
                removeAnimal();
                return false;
            case R.id.menuUpdateAnimal:
                Intent returnBtn = new Intent(getApplicationContext(),AnimalUpdateActivity.class);
                startActivity(returnBtn);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void removeAnimal() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Deseja apagar este registro?")
                .setPositiveButton("Confirmar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(MainActivity.dbHelper.removeData(animal))
                        {
                            Toast.makeText(getApplicationContext(), "Dados removidos...", Toast.LENGTH_LONG).show();

                            Intent returnBtn = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(returnBtn);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Erros db...", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar",null);

        dialog.show();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AnimalDataFragment();
                case 1:
                    return new AnimalPesagemFragment();
                case 2:
                    return new AnimalOcorrenciaFragment();
            }

            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    public void addPesagem(View v){
        NovaPesagemDialogFragment novaPesagemDialogFragment = new NovaPesagemDialogFragment();

        novaPesagemDialogFragment.show(getFragmentManager(), "pesagem");
    }

    public static class NovaPesagemDialogFragment extends DialogFragment {
        LayoutInflater layoutInflater;
        View inflater = null;

        EditText editPeso, editData;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            layoutInflater = getActivity().getLayoutInflater();
            inflater = layoutInflater.inflate(R.layout.dialog_pesagem_add, null);

            editPeso = (EditText) inflater.findViewById(R.id.editText_peso);
            editData = (EditText) inflater.findViewById(R.id.editText_data);
            editData.addTextChangedListener(new DateHandler.TextWacherMask(editData));
            editData.setText(DateHandler.getCurrentData());


            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

            dialog.setView(inflater)
                    .setPositiveButton("Salvar",null)
                    .setNegativeButton("Cancelar", null);
            dialog.setTitle("Incluir Pesagem");
            return dialog.create();
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
                        Pesagem pesagem;
                        pesagem = new Pesagem(
                                animal.getID(),
                                Double.parseDouble(editPeso.getText().toString().trim()),
                                editData.getText().toString().trim().toUpperCase());

                        if (pesagem.getPeso() <= 0) {
                            Toast.makeText(getActivity(), "Peso obrigatório", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (pesagem.getData().isEmpty()) {
                            Toast.makeText(getActivity(), "Data obrigatória", Toast.LENGTH_LONG).show();
                            return;
                        }

                        boolean isInserted = dbHelper.insertData(pesagem);

                        if (isInserted == true) {
                            Toast.makeText(getActivity(), "Sucesso!", Toast.LENGTH_LONG).show();
                            dismiss();

                            // Reload activity
                            Intent intent = getActivity().getIntent();
                            getActivity().finish();
                            getActivity().startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "DB Erro...", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }
    }

    public void addOcorrencia(View v){
        NovaOcorrenciaDialogFragment novaOcorrenciaDialogFragment= new NovaOcorrenciaDialogFragment();

        novaOcorrenciaDialogFragment.show(getFragmentManager(), "ocorrencia");
    }

    public static class NovaOcorrenciaDialogFragment extends DialogFragment {
        LayoutInflater layoutInflater;
        View inflater = null;

        EditText editOcorrencia, editData;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            layoutInflater = getActivity().getLayoutInflater();
            inflater = layoutInflater.inflate(R.layout.dialog_ocorrencia_add, null);

            editOcorrencia = (EditText) inflater.findViewById(R.id.editText_ocorrencia);
            editData = (EditText) inflater.findViewById(R.id.editText_data);
            editData.addTextChangedListener(new DateHandler.TextWacherMask(editData));
            editData.setText(DateHandler.getCurrentData());

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

            dialog.setView(inflater)
                    .setPositiveButton("Salvar",null)
                    .setNegativeButton("Cancelar", null);
            dialog.setTitle("Incluir Ocorrência");
            return dialog.create();
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
                        Ocorrencia ocorrencia = new Ocorrencia(
                                animal.getID(),
                                editOcorrencia.getText().toString().trim(),
                                editData.getText().toString().trim().toUpperCase());

                        if (ocorrencia.getOcorrencia().isEmpty()) {
                            Toast.makeText(getActivity(), "Ocorrencia obrigatório", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (ocorrencia.getData().isEmpty()) {
                            Toast.makeText(getActivity(), "Data obrigatória", Toast.LENGTH_LONG).show();
                            return;
                        }

                        boolean isInserted = dbHelper.insertData(ocorrencia);

                        if (isInserted == true) {
                            Toast.makeText(getActivity(), "Sucesso!", Toast.LENGTH_LONG).show();
                            dismiss();

                            // Reload activity
                            Intent intent = getActivity().getIntent();
                            getActivity().finish();
                            getActivity().startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "DB Erro...", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }
    }
}

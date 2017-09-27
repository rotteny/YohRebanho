package br.com.rotteny.yohrebanho;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.rotteny.yohrebanho.adapters.AnimalListAdapter;
import br.com.rotteny.yohrebanho.dialog.NovoUsuarioDialogFragment;
import br.com.rotteny.yohrebanho.models.Animal;
import br.com.rotteny.yohrebanho.handlers.DatabaseHelper;
import br.com.rotteny.yohrebanho.models.Usuario;

/**
 * Created by ENCBACK on 06/07/2017.
 */

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "YohRebanho";

    static DatabaseHelper dbHelper = null;
    public static Usuario principal;

    ListView listView;
    ArrayList<Animal> list;
    AnimalListAdapter adapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_list_activity);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menuUpdateUsuario:
                SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.clear().commit();

                editor.putInt("usuarioID", principal.getID());
                editor.commit();

                Intent returnBtn = new Intent(getApplicationContext(),UsuarioUpdateActivity.class);
                startActivity(returnBtn);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        dbHelper    = new DatabaseHelper(this);
        //dbHelper.update();

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        listView    = (ListView) findViewById(R.id.animalListView);
        list        = new ArrayList<>();
        adapter     = new AnimalListAdapter(this,R.layout.list_item_animal, list);

        listView.setAdapter(adapter);

        reloadList();
        principal   = dbHelper.getPrincipal();

        if(principal == null) {
            NovoUsuarioDialogFragment.showNovoUsuario(getFragmentManager());
        }

        dbHelper.close();
    }



    public void reloadList() {
        Cursor response = dbHelper.getAllAnimal();

        if(response != null)
        {
            list.clear();
            while (response.moveToNext()) {
                list.add(new Animal(response));
            }
        }
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.clear().commit();

                Animal animal = (Animal) adapter.getItem(position);

                editor.putInt("animalID", animal.getID());
                editor.commit();

                Intent intent = new Intent(getApplicationContext(),AnimalViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addAnimal(View v) {
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.clear().commit();

        editor.putInt("usuarioID", principal.getID());
        editor.commit();

        Intent intent = new Intent(this,AnimalAddActivity.class);
        startActivity(intent);
    }

    public void addPhoto(View v) {
        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);
    }


    public void clearDb(View arg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Deseja apagar todos os dados?");
        dialog.setPositiveButton("Confirmar",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MainActivity.dbHelper.update();

                    Toast.makeText(getApplicationContext(), "Todos os dados foram apagados...", Toast.LENGTH_LONG).show();

                    principal = null;
                    reloadList();
                    NovoUsuarioDialogFragment.showNovoUsuario(getFragmentManager());
                }
            });
        dialog.setNegativeButton("Cancelar",null);

        dialog.show();
    }

}

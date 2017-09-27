package br.com.rotteny.yohrebanho.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.rotteny.yohrebanho.models.Animal;
import br.com.rotteny.yohrebanho.models.Ocorrencia;
import br.com.rotteny.yohrebanho.models.Pesagem;
import br.com.rotteny.yohrebanho.models.Usuario;

/**
 * Created by ENCBACK on 03/07/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "YohRebanho.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Usuario.SQL_CREATE);
        db.execSQL(Animal.SQL_CREATE);
        db.execSQL(Pesagem.SQL_CREATE);
        db.execSQL(Ocorrencia.SQL_CREATE);
    }

    public void update() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(Usuario.SQL_DELETE);
        db.execSQL(Animal.SQL_DELETE);
        db.execSQL(Pesagem.SQL_DELETE);
        db.execSQL(Ocorrencia.SQL_DELETE);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(Usuario.SQL_DELETE);
        db.execSQL(Animal.SQL_DELETE);
        db.execSQL(Pesagem.SQL_DELETE);
        db.execSQL(Ocorrencia.SQL_DELETE);
        onCreate(db);
    }

    // USUARIO
    public boolean insertData(Usuario usuario) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Usuario.COLUMN_NAME_NOME,usuario.getNome());
        contentValues.put(Usuario.COLUMN_NAME_TELEFONE,usuario.getTelefone());
        contentValues.put(Usuario.COLUMN_NAME_EMAIL,usuario.getEmail());
        contentValues.put(Usuario.COLUMN_NAME_PRINCIPAL,usuario.getPrincipal());

        return insetDataIntoDatabase(Usuario.TABLE_NAME,contentValues);
    }

    public Cursor getAllNotPrincipal() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Usuario.TABLE_NAME +
                " WHERE " + Usuario.COLUMN_NAME_PRINCIPAL +
                " = 0 ORDER BY " + Usuario.COLUMN_NAME_NOME,null);
    }

    public Usuario getPrincipal() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Usuario.TABLE_NAME +
                " WHERE " + Usuario.COLUMN_NAME_PRINCIPAL +
                " = 1 ORDER BY " + Usuario._ID + " LIMIT 1" ,null);

        if(cursor.getCount() == 0)
            return null;

        cursor.moveToFirst();
        return new Usuario(cursor);
    }

    public Usuario getUsuario(Integer ID) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Usuario.TABLE_NAME +
                " WHERE " + Usuario._ID +
                " = ? ORDER BY " + Usuario._ID +
                " LIMIT 1" ,new String[] {Integer.toString(ID)});

        if(cursor.getCount() == 0)
            return null;

        cursor.moveToFirst();
        return new Usuario(cursor);
    }

    public boolean updateData(Usuario usuario) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Usuario.COLUMN_NAME_NOME,usuario.getNome());
        contentValues.put(Usuario.COLUMN_NAME_TELEFONE,usuario.getTelefone());
        contentValues.put(Usuario.COLUMN_NAME_EMAIL,usuario.getEmail());

        return updateDataIntoDatabase(
                Usuario.TABLE_NAME,
                contentValues,
                Usuario._ID + " = ?",
                new String[] { Integer.toString(usuario.getID()) });
    }

    public boolean removeData(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        return getReturn(
                db.delete(
                        Usuario.TABLE_NAME,
                        Usuario._ID + " = ?",
                        new String[] { usuario.getID().toString() }));
    }
    //! USUARIO

    // ANIMAL
    public boolean insertData(Animal animal) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Animal.COLUMN_NAME_BRINCO,animal.getBrinco());
        contentValues.put(Animal.COLUMN_NAME_USUARIO,animal.getUsuarioID());
        contentValues.put(Animal.COLUMN_NAME_SEXO,animal.getSexo());
        contentValues.put(Animal.COLUMN_NAME_NASCIMENTO,animal.getDataNascimento());
        contentValues.put(Animal.COLUMN_NAME_PESO,animal.getPeso());
        contentValues.put(Animal.COLUMN_NAME_PAI,animal.getAnimalPaiID());
        contentValues.put(Animal.COLUMN_NAME_MAE,animal.getAnimalMaeID());
        contentValues.put(Animal.COLUMN_NAME_IMAGEM,animal.getImagem());

        return insetDataIntoDatabase(Animal.TABLE_NAME,contentValues);
    }

    public Cursor getAllAnimal() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Animal.TABLE_NAME + " ORDER BY " + Animal.COLUMN_NAME_CRIACAO + " DESC",null);
    }

    public Cursor getAllAnimalSexo(String sexo) {
        if(!sexo.equals(Animal.SEXO_MACHO) && !sexo.equals(Animal.SEXO_FEMEA))
            return null;

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Animal.TABLE_NAME +
                " WHERE " + Animal.COLUMN_NAME_SEXO +
                " = ? ORDER BY " + Animal.COLUMN_NAME_BRINCO,
                new String[] {sexo});
    }

    public Cursor getAllAnimalSexo(String sexo, String notbrinco) {
        if(!sexo.equals(Animal.SEXO_MACHO) && !sexo.equals(Animal.SEXO_FEMEA))
            return null;

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Animal.TABLE_NAME +
                " WHERE " + Animal.COLUMN_NAME_SEXO +
                " = ? AND " + Animal.COLUMN_NAME_BRINCO +
                " <> ? ORDER BY " + Animal.COLUMN_NAME_BRINCO,
                new String[] {sexo,notbrinco});
    }

    public Animal getAnimal(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Animal.TABLE_NAME +
                " WHERE " + Animal._ID +
                " = ? LIMIT 1" ,new String[] { id.toString() });

        if(cursor.getCount() == 0)
            return null;

        cursor.moveToFirst();
        return new Animal(cursor);
    }

    public Animal getAnimalbrinco(String brinco, Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor;
        if(id != null)
            cursor = db.rawQuery("SELECT * FROM " + Animal.TABLE_NAME +
                    " WHERE " + Animal.COLUMN_NAME_BRINCO +
                    " = ? AND " + Animal._ID + " <> ? LIMIT 1" ,new String[] { brinco,id.toString() });
        else
            cursor = db.rawQuery("SELECT * FROM " + Animal.TABLE_NAME +
                    " WHERE " + Animal.COLUMN_NAME_BRINCO +
                    " = ? LIMIT 1" ,new String[] { brinco });

        if(cursor.getCount() == 0)
            return null;

        cursor.moveToFirst();
        return new Animal(cursor);
    }

    public boolean updateData(Animal animal) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Animal.COLUMN_NAME_BRINCO,animal.getBrinco());
        contentValues.put(Animal.COLUMN_NAME_USUARIO,animal.getUsuarioID());
        contentValues.put(Animal.COLUMN_NAME_SEXO,animal.getSexo());
        contentValues.put(Animal.COLUMN_NAME_NASCIMENTO,animal.getDataNascimento());
        contentValues.put(Animal.COLUMN_NAME_PESO,animal.getPeso());
        contentValues.put(Animal.COLUMN_NAME_PAI,animal.getAnimalPaiID());
        contentValues.put(Animal.COLUMN_NAME_MAE,animal.getAnimalMaeID());
        contentValues.put(Animal.COLUMN_NAME_IMAGEM,animal.getImagem());

        return updateDataIntoDatabase(
                Animal.TABLE_NAME,
                contentValues,
                Animal._ID + " = ?",
                new String[] { animal.getID().toString() });
    }

    public boolean removeData(Animal animal) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
                Pesagem.TABLE_NAME,
                Pesagem.COLUMN_NAME_ANIMAL + " = ?",
                new String[] { animal.getID().toString() });

        db.delete(
                Ocorrencia.TABLE_NAME,
                Ocorrencia.COLUMN_NAME_ANIMAL + " = ?",
                new String[] { animal.getID().toString() });

        return getReturn(
                db.delete(
                        Animal.TABLE_NAME,
                        Animal._ID + " = ?",
                        new String[] { animal.getID().toString() }));
    }
    //! ANIMAL

    // PESAGEM
    public boolean insertData(Pesagem pesagem) {
        ContentValues animalValues = new ContentValues();

        animalValues.put(Animal.COLUMN_NAME_PESO,pesagem.getPeso());
        updateDataIntoDatabase(Animal.TABLE_NAME,animalValues,Animal._ID + "= ?",new String[] {pesagem.getAnimalID().toString()});

        ContentValues contentValues = new ContentValues();

        contentValues.put(Pesagem.COLUMN_NAME_ANIMAL,pesagem.getAnimalID());
        contentValues.put(Pesagem.COLUMN_NAME_PESO,pesagem.getPeso());
        contentValues.put(Pesagem.COLUMN_NAME_DATA,pesagem.getData());

        return insetDataIntoDatabase(Pesagem.TABLE_NAME,contentValues);
    }

    protected boolean insetDataIntoDatabase(String table_name,ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        return getReturn(
                db.insert(
                        table_name,
                        null,
                        contentValues));
    }

    public Cursor getAllPesagemAnimal(Integer animalID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Pesagem.TABLE_NAME +
                " WHERE " + Pesagem.COLUMN_NAME_ANIMAL +
                " = ? ORDER BY " + Pesagem.COLUMN_NAME_CRIACAO +
                " DESC",new String[] { animalID.toString() });
    }

    public Pesagem getPesagem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Pesagem.TABLE_NAME + " WHERE " + Pesagem._ID + " = ? LIMIT 1" ,new String[] { id.toString() });

        if(cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();
        return new Pesagem(cursor);
    }

    public boolean removeData(Pesagem pesagem) {
        SQLiteDatabase db = this.getWritableDatabase();

        return getReturn(
                db.delete(
                        Pesagem.TABLE_NAME,
                        Pesagem._ID + " = ?",
                        new String[]{Integer.toString(pesagem.getID())}));
    }
    //! PESAGEM

    // OCORRENCIA

    public boolean insertData(Ocorrencia ocorrencia) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Ocorrencia.COLUMN_NAME_ANIMAL,ocorrencia.getAnimalID());
        contentValues.put(Ocorrencia.COLUMN_NAME_OCORRENCIA,ocorrencia.getOcorrencia());
        contentValues.put(Ocorrencia.COLUMN_NAME_DATA,ocorrencia.getData());

        return insetDataIntoDatabase(Ocorrencia.TABLE_NAME,contentValues);
    }

    public Cursor getAllOcorrenciaAnimal(Integer animalID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Ocorrencia.TABLE_NAME +
                " WHERE " + Ocorrencia.COLUMN_NAME_ANIMAL +
                " = ? ORDER BY " + Ocorrencia.COLUMN_NAME_CRIACAO +
                " DESC",new String[] { animalID.toString() });
    }

    public Ocorrencia getOcorrencia(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Ocorrencia.TABLE_NAME + " WHERE " + Ocorrencia._ID + " = ? LIMIT 1" ,new String[] { id.toString() });
        if(cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();
        return new Ocorrencia();
    }

    public boolean updateData(Pesagem pesagem) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Pesagem.COLUMN_NAME_ANIMAL,pesagem.getAnimalID().toString());
        contentValues.put(Pesagem.COLUMN_NAME_PESO,pesagem.getPeso());

        return updateDataIntoDatabase(
                        Pesagem.TABLE_NAME,
                        contentValues,
                        Pesagem._ID + " = ?",
                        new String[] { pesagem.getID().toString() });
    }

    public boolean updateData(Ocorrencia ocorrencia) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Ocorrencia.COLUMN_NAME_OCORRENCIA,ocorrencia.getAnimalID().toString());
        contentValues.put(Ocorrencia.COLUMN_NAME_OCORRENCIA,ocorrencia.getOcorrencia());

        return updateDataIntoDatabase(
                Ocorrencia.TABLE_NAME,
                contentValues,
                Ocorrencia._ID + " = ?",
                new String[] { Integer.toString(ocorrencia.getID()) });
    }

    public boolean removeData(Ocorrencia ocorrencia) {
        SQLiteDatabase db = this.getWritableDatabase();

        return getReturn(
                db.delete(
                        Ocorrencia.TABLE_NAME,
                        Ocorrencia._ID + " = ?",
                        new String[]{Integer.toString(ocorrencia.getID())}));
    }
    //! OCORRENCIA

    // PUBLICO
    protected boolean updateDataIntoDatabase(String table_name,ContentValues contentValues,String where, String[] whereArgs) {

        SQLiteDatabase db = this.getWritableDatabase();

        return getReturn(
                db.update(
                        table_name,
                        contentValues,
                        where,
                        whereArgs));
    }

    public boolean getReturn(long result) {
        if(result == -1)
            return false;
        else
            return true;
    }
}

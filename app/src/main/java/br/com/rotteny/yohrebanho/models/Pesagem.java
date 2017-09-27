package br.com.rotteny.yohrebanho.models;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ENCBACK on 03/07/2017.
 */

public class Pesagem implements BaseColumns {

    private Integer ID;
    private Integer animalID;
    private double peso = 0;
    private Integer transmitido = 0;
    private String data;
    private String dataCriacao;

    public static final String TABLE_NAME = "pesagens";
    public static final String COLUMN_NAME_ANIMAL = "animalID";
    public static final String COLUMN_NAME_PESO = "peso";
    public static final String COLUMN_NAME_DATA = "data";
    public static final String COLUMN_NAME_TRANSMITIDO = "transmitido";
    public static final String COLUMN_NAME_CRIACAO = "dataCriacao";

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_ANIMAL + " INTEGER, " +
                    COLUMN_NAME_PESO  + " REAL, " +
                    COLUMN_NAME_DATA  + " TEXT, " +
                    COLUMN_NAME_TRANSMITIDO  + " INTEGER DEFAULT 0, " +
                    COLUMN_NAME_CRIACAO + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public String toString() {
        return _ID + ": " + ID + " - " +
                COLUMN_NAME_ANIMAL + ": " + animalID + " - " +
                COLUMN_NAME_PESO + ": " + peso + " - " +
                COLUMN_NAME_DATA  + ": " + data +
                COLUMN_NAME_CRIACAO + ": " + dataCriacao;
    }

    public Pesagem() {}

    public Pesagem(Cursor cursor) {
        this.ID = cursor.getInt(0);
        this.animalID = cursor.getInt(1);
        this.peso = cursor.getDouble(2);
        this.data = cursor.getString(3);
        this.transmitido = cursor.getInt(4);
        this.dataCriacao = cursor.getString(5);
    }

    public Pesagem(Integer animalID, double peso, String data) {
        this.animalID = animalID;
        this.peso = peso;
        this.data = data;
    }

    public Integer getID() {
        return ID;
    }

    public Integer getAnimalID() { return animalID; }

    public void setAnimalID(Integer animalID) { this.animalID = animalID; }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) { this.data = data; }

    public Integer getTransmitido() { return transmitido; }

    public void setTransmitido(Integer transmitido) {
        this.transmitido = transmitido;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}

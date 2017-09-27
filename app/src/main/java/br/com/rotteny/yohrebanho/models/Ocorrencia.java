package br.com.rotteny.yohrebanho.models;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ENCBACK on 03/07/2017.
 */

public class Ocorrencia implements BaseColumns {

    private Integer ID;
    private Integer animalID;
    private String ocorrencia;
    private String data;
    private Integer transmitido = 0;
    private String dataCriacao;

    public static final String TABLE_NAME = "ocorrencias";
    public static final String COLUMN_NAME_ANIMAL = "animalID";
    public static final String COLUMN_NAME_OCORRENCIA = "ocorrencia";
    public static final String COLUMN_NAME_DATA = "data";
    public static final String COLUMN_NAME_TRANSMITIDO = "transmitido";
    public static final String COLUMN_NAME_CRIACAO = "dataCriacao";

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_ANIMAL + " INTEGER, " +
                    COLUMN_NAME_OCORRENCIA  + " TEXT, " +
                    COLUMN_NAME_DATA  + " TEXT, " +
                    COLUMN_NAME_TRANSMITIDO  + " INTEGER DEFAULT 0, " +
                    COLUMN_NAME_CRIACAO + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public String toString() {
        return _ID + ": " + ID + " - " +
                COLUMN_NAME_ANIMAL + ": " + animalID + " - " +
                COLUMN_NAME_OCORRENCIA + ": " + ocorrencia + " - " +
                COLUMN_NAME_DATA  + ": " + data +
                COLUMN_NAME_CRIACAO + ": " + dataCriacao;
    }

    public Ocorrencia() {}

    public Ocorrencia(Cursor cursor) {
        this.ID = cursor.getInt(0);
        this.animalID = cursor.getInt(1);
        this.ocorrencia = cursor.getString(2);
        this.data = cursor.getString(3);
        this.transmitido = cursor.getInt(4);
        this.dataCriacao = cursor.getString(5);
    }

    public Ocorrencia(Integer animalID, String ocorrencia, String data) {
        this.animalID = animalID;
        this.ocorrencia = ocorrencia;
        this.data = data;
    }

    public Ocorrencia(Integer ID,Integer animalID, String ocorrencia, String data, String dataCriacao)
    {
        this.ID = ID;
        this.animalID = animalID;
        this.ocorrencia = ocorrencia;
        this.data = data;
        this.dataCriacao = dataCriacao;
    }

    public Integer getID() {
        return ID;
    }

    public Integer getAnimalID() { return animalID; }

    public void setAnimalID(Integer animalID) { this.animalID = animalID; }

    public String getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(String ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) { this.data = data; }

    public Integer getTransmitido() {
        return transmitido;
    }

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

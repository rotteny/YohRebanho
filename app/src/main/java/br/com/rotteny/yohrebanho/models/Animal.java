package br.com.rotteny.yohrebanho.models;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ENCBACK on 03/07/2017.
 */

public class Animal implements BaseColumns {

    private Integer ID;
    private String brinco;
    private Integer usuarioID;
    private String sexo;
    private String dataNascimento;
    private double peso = 0;
    private Integer animalPaiID = 0;
    private Integer animalMaeID = 0;
    private byte[] imagem;
    private Integer transmitido = 0;
    private String dataCriacao;

    public static final Integer REQUEST_CODE_GALLERY = 1888;
    public static final Integer REQUEST_IMAGE_CAPTURE = 1;

    public static final String SEXO_DISPLAY_MACHO = "MACHO";
    public static final String SEXO_DISPLAY_FEMEA = "FÊMEA";

    public static final String TABLE_NAME = "animais";
    public static final String COLUMN_NAME_BRINCO = "brinco";
    public static final String COLUMN_NAME_USUARIO = "usuarioID";
    public static final String COLUMN_NAME_SEXO = "sexo";
    public static final String COLUMN_NAME_NASCIMENTO = "dataNascimento";
    public static final String COLUMN_NAME_PESO = "peso";
    public static final String COLUMN_NAME_PAI = "animalPaiID";
    public static final String COLUMN_NAME_MAE = "animalMaeID";
    public static final String COLUMN_NAME_IMAGEM = "imagem";
    public static final String COLUMN_NAME_TRANSMITIDO = "transmitido";
    public static final String COLUMN_NAME_CRIACAO = "dataCriacao";

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_BRINCO + " TEXT UNIQUE," +
                    COLUMN_NAME_USUARIO + " INTEGER," +
                    COLUMN_NAME_SEXO + " TEXT, " +
                    COLUMN_NAME_NASCIMENTO + " TEXT, " +
                    COLUMN_NAME_PESO + " REAL, " +
                    COLUMN_NAME_PAI+ " INTEGER, " +
                    COLUMN_NAME_MAE+ " INTEGER, " +
                    COLUMN_NAME_IMAGEM  + " BLOB, " +
                    COLUMN_NAME_TRANSMITIDO + " INTEGER DEFAULT 0," +
                    COLUMN_NAME_CRIACAO + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String SEXO_MACHO = "M";
    public static final String SEXO_FEMEA = "F";

    public Animal(Cursor cursor) {
        ID              = cursor.getInt(0);
        brinco          = cursor.getString(1);
        usuarioID       = cursor.getInt(2);
        sexo            = cursor.getString(3);
        dataNascimento  = cursor.getString(4);
        peso            = cursor.getDouble(5);
        animalPaiID     = cursor.getInt(6);
        animalMaeID     = cursor.getInt(7);
        imagem          = cursor.getBlob(8);
        transmitido     = cursor.getInt(9);
        dataCriacao     = cursor.getString(10);
    }

    public Animal(String brinco, Integer usuarioID, String sexo, String dataNascimento, Integer animalPaiID, Integer animalMaeID, byte[] imagem) {
        this.brinco         = brinco;
        this.usuarioID      = usuarioID;
        this.sexo           = sexo;
        this.dataNascimento = dataNascimento;
        this.animalPaiID    = animalPaiID;
        this.animalMaeID    = animalMaeID;
        this.imagem         = imagem;
    }

    public String toString() {
        return _ID + ": " + ID + " - " +
                COLUMN_NAME_BRINCO + ": " + brinco + " - " +
                COLUMN_NAME_NASCIMENTO + ": " + dataNascimento + " - " +
                COLUMN_NAME_SEXO + ": " + sexo + " - " +
                COLUMN_NAME_USUARIO + ": " + usuarioID + " - " +
                COLUMN_NAME_PAI + ": " + animalPaiID + " - " +
                COLUMN_NAME_MAE + ": " + animalMaeID;
    }

    public Integer getID() { return ID; }

    public void setID(Integer ID) { this.ID = ID; }

    public String getBrinco() {
        return brinco;
    }

    public void setBrinco(String brinco) {
        this.brinco = brinco;
    }

    public Integer getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Integer getAnimalPaiID() { return animalPaiID; }

    public void setAnimalPaiID(Integer animalPaiID) { this.animalPaiID = animalPaiID; }

    public Integer getAnimalMaeID() { return animalMaeID; }

    public void setAnimalMaeID(Integer animalMaeID) { this.animalMaeID = animalMaeID; }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

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


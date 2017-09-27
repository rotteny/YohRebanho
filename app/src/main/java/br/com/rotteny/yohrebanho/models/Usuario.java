package br.com.rotteny.yohrebanho.models;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ENCBACK on 19/07/2017.
 */

public class Usuario implements BaseColumns {

    private Integer ID;
    private Integer principal = 0;
    private Integer transmitido = 0;
    private String nome;
    private String telefone;
    private String email;
    private String dataCriacao;

    public static final String TABLE_NAME = "usuarios";
    public static final String COLUMN_NAME_NOME = "nome";
    public static final String COLUMN_NAME_TELEFONE = "telefone";
    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_PRINCIPAL = "principal";
    public static final String COLUMN_NAME_TRANSMITIDO = "transmitido";
    public static final String COLUMN_NAME_CRIACAO = "dataCriacao";

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_NOME + " TEXT, " +
                    COLUMN_NAME_TELEFONE + " TEXT, " +
                    COLUMN_NAME_EMAIL + " TEXT, " +
                    COLUMN_NAME_PRINCIPAL + " INTEGER, " +
                    COLUMN_NAME_TRANSMITIDO + " INTEGER DEFAULT 0, " +
                    COLUMN_NAME_CRIACAO + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public Usuario(String nome, String telefone, String email) {
        this.nome           = nome;
        this.telefone       = telefone;
        this.email          = email;
    }

    public Usuario(Cursor cursor) {
        cursor.moveToFirst();
        this.ID             = cursor.getInt(0);
        this.nome           = cursor.getString(1);
        this.telefone       = cursor.getString(2);
        this.email          = cursor.getString(3);
        this.principal      = cursor.getInt(4);
        this.transmitido    = cursor.getInt(5);
        this.dataCriacao    = cursor.getString(6);
    }

    public Usuario(String nome, String telefone, String email, Integer principal) {
        this.nome           = nome;
        this.telefone       = telefone;
        this.email          = email;
        this.principal      = principal;
    }

    public String toString() {
        return _ID + ": " + Integer.toString(ID) + " - " +
                COLUMN_NAME_NOME + ": " + nome + " - " +
                COLUMN_NAME_TELEFONE + ": " + telefone + " - " +
                COLUMN_NAME_EMAIL + ": " + email + " - " +
                COLUMN_NAME_PRINCIPAL + ": " + Integer.toString(principal);
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getPrincipal() {
        return principal;
    }

    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }

    public Integer getTransmitido() {
        return transmitido;
    }

    public void setTransmitido(Integer transmitido) {
        this.transmitido = transmitido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}

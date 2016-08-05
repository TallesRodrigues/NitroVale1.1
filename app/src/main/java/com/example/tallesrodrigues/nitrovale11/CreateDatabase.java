package com.example.tallesrodrigues.nitrovale11;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by allanromanato on 5/27/15.
 */
public class CreateDatabase extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "nitrovale.db";
    private static final String ID = "_id";


    private static final int VERSION = 1;
    public static final String SPAD ="SPAD" ;
    public static final String TABELA ="datapic" ;
    public static final String Red = "R";
    public static final String Green ="G";
    public static final String Blue = "B";
    public static final String Intensity ="Intensity";


    public CreateDatabase(Context context) {
        super(context, NOME_BANCO, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS \"datapic\"(\n" +
                "  \"id\" INTEGER PRIMARY KEY NOT NULL,\n" +
                "  \"R\" REAL NOT NULL,\n" +
                "  \"G\" REAL NOT NULL,\n" +
                "  \"B\" REAL NOT NULL,\n" +
                "  \"Intensity\" REAL NOT NULL,\n" +
                "  \"SPAD\" REAL NOT NULL\n" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABELA);
        onCreate(db);
    }
}
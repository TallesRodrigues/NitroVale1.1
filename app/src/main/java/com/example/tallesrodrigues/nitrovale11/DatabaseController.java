package com.example.tallesrodrigues.nitrovale11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by TallesRodrigues on 7/26/2016.
 */
public class DatabaseController  {
    private SQLiteDatabase db;
    private CreateDatabase database;

    public DatabaseController(Context context){
        database = new CreateDatabase(context);
    }

    public String insertData(float r, float g, float b,float intensity, float SPAD){
        ContentValues values;
        long result;

        db = database.getWritableDatabase();
        values = new ContentValues();
        values.put(CreateDatabase.Red, r);
        values.put(CreateDatabase.Green, g);
        values.put(CreateDatabase.Blue, b);
        values.put(CreateDatabase.Intensity, intensity);
        values.put(CreateDatabase.SPAD, SPAD);

        result = db.insert(CreateDatabase.TABELA,null,values);
        db.close();

        if (result ==-1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso";

    }

    public Cursor loadData(){
        Cursor cursor;
        String []columns = {"id as _id","R","G","B","Intensity","SPAD"};
        db = database.getReadableDatabase();
        cursor = db.query("datapic",columns,null,null,null,null,null,null);

        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            Log.e("Cursor not zero","Aew carai");
        }
        db.close();
        return cursor;
    }

}

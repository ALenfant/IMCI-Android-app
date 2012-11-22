package com.imci.registerpatients;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PatientsSQLiteHelper extends SQLiteOpenHelper {

	 
    //Sentencia SQL para crear la tabla de Patients
    String sqlCreate = "CREATE TABLE Patients (name TEXT, family_name TEXT," +
    		"idcard TEXT, birth TEXT, address TEXT, town TEXT, postcode TEXT)";
 
    public PatientsSQLiteHelper(Context context, String name,
                               CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
    	db.execSQL(sqlCreate);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.
 
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Patients");
 
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}

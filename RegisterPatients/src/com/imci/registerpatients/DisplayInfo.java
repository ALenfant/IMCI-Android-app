package com.imci.registerpatients;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayInfo extends Activity {
	
	String name;
    String family_name;
    String idcard;
    String birth;
    String address;
    String town;
    String postcode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_display_info);

        name = intent.getStringExtra(NewPatient.EXTRA_NAME);
        family_name = intent.getStringExtra(NewPatient.EXTRA_FAMILY);
        idcard = intent.getStringExtra(NewPatient.EXTRA_IDCARD);
        birth = intent.getStringExtra(NewPatient.EXTRA_BIRTH);
        address = intent.getStringExtra(NewPatient.EXTRA_ADDRESS);
        town = intent.getStringExtra(NewPatient.EXTRA_TOWN);
        postcode = intent.getStringExtra(NewPatient.EXTRA_POSTCODE);

        TextView textName = (TextView) findViewById(R.id.TextName);
        textName.setText(name);
        
        TextView textFamilyName = (TextView) findViewById(R.id.TextFamilyName);
        textFamilyName.setText(family_name);
        
        TextView textIDcard = (TextView) findViewById(R.id.TextIDcard);
        textIDcard.setText(idcard);
        
        TextView textBirth = (TextView) findViewById(R.id.TextBirth);
        textBirth.setText(birth);
        
        TextView textAddress = (TextView) findViewById(R.id.TextAddress);
        textAddress.setText(address);
        
        TextView textTown = (TextView) findViewById(R.id.TextTown);
        textTown.setText(town);
        
        TextView textPostCode = (TextView) findViewById(R.id.TextPostcode);
        textPostCode.setText(postcode);        
        
    }

    public void confirmInfo(View view) {
//        Intent intentAct = getIntent();
    	Intent intent = new Intent(this, DoneRegister.class);
    	
    	/*
    	String name = intentAct.getStringExtra(NewPatient.EXTRA_NAME);
        String family_name = intentAct.getStringExtra(NewPatient.EXTRA_FAMILY);
        String idcard = intentAct.getStringExtra(NewPatient.EXTRA_IDCARD);
        String birth = intentAct.getStringExtra(NewPatient.EXTRA_BIRTH);
        String address = intentAct.getStringExtra(NewPatient.EXTRA_ADDRESS);
        String town = intentAct.getStringExtra(NewPatient.EXTRA_TOWN);
        String postcode = intentAct.getStringExtra(NewPatient.EXTRA_POSTCODE);
		*/
    	//Abrimos la base de datos 'DBPatients' en modo escritura
        PatientsSQLiteHelper usdbh =
        		new PatientsSQLiteHelper(this, "DBPatients.db", null, 1);
      		
        SQLiteDatabase db = usdbh.getWritableDatabase();
        
        //Si hemos abierto correctamente la base de datos
        if(db != null) {
        
        	//Insertamos los datos en la tabla Usuarios
				db.execSQL("INSERT INTO Patients (name, family_name," +
						"idcard, birth, address, town, postcode ) " +
						"VALUES ('" + name +"', '" + family_name +
						"', '" + idcard +"', '"+ birth +"', '" + address +
						"', '" + town +"', '" + postcode + "')");
		
		//Cerramos la base de datos
		db.close();
        }
        
        startActivity(intent);
        finish();
        
    }
    
    public void modifyInfo(View view) {
    	finish();
    }    

}

package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class RegisterPatientActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_patient);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register_patient, menu);
		return true;
	}

	
	//Answer to Create button click
	public void createPatient(View view) {
		Intent intent = new Intent(this, NewPatientActivity.class);
		startActivity(intent);
		finish();
	}
	
    //Answer to Cancel button
    public void cancel(View view) {
    	finish();
    }
 
}

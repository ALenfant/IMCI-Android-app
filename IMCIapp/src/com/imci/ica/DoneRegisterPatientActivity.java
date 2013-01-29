package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activity to show if a register of a new patient was successfull
 * 
 * @author Miguel
 *
 */
public class DoneRegisterPatientActivity extends Activity {

	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";
	
	int patient_id;

	/**
	 * Show the Activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_done_register_patient);
		
		Intent intent = getIntent();
		patient_id = intent.getIntExtra(EXTRA_ID_PATIENT, -1);
	}
	
	/**
	 * Finish this activity and return to main menu
	 * 
	 * @param view
	 */
	//Finishing activity when Return button is pressed
	public void onClick(View view){
		Intent intent = new Intent(this, InfoPatientActivity.class);
		intent.putExtra(InfoPatientActivity.EXTRA_ID_PATIENT, patient_id);
		startActivity(intent);
    	finish();
	}
}

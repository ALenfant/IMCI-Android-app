package com.imci.ica;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * Activity to show if a register of a new patient was successfull
 * 
 * @author Miguel
 *
 */
public class DoneRegisterPatientActivity extends Activity {

	/**
	 * Show the Activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_done_register_patient);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_done_register_patient, menu);
		return true;
	}
	
	/**
	 * Finish this activity and return to main menu
	 * 
	 * @param view
	 */
	//Finishing activity when Return button is pressed
	public void onClick(View view){
    	finish();
	}
}

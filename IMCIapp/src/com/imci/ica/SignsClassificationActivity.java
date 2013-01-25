package com.imci.ica;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Class responsible for the Signs Classification activity. Uses the answers
 * from GetsignsActivity and data about the child to compute the equations
 * stored in the database's classifications table to see if there is a problem
 * or not
 * 
 * @author Antonin
 * 
 */
public class SignsClassificationActivity extends Activity {
	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";
	public final static String EXTRA_HASHMAP_DATA = "com.imci.ica.HASHMAP_DATA";

	private int patient_id; // the patient's id
	
	/**
	 * Get data of previous activity and do the diagnostic in background
	 */
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signs_classification);

		@SuppressWarnings("unchecked")
		// We load the data of the answers given by the previous activity
		HashMap<String, Object> data = (HashMap<String, Object>) getIntent()
				.getSerializableExtra(EXTRA_HASHMAP_DATA);

		// And the patient id
		patient_id = getIntent().getIntExtra(EXTRA_ID_PATIENT, -1);
		
		// Do the diagnostic task in backgroun
		GetDiagnostic diagnostic = new GetDiagnostic(this, patient_id, data);
		diagnostic.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_signs_classification, menu);
		return true;
	}

}

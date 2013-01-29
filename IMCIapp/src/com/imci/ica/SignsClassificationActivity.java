package com.imci.ica;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ListView;

import com.imci.ica.utils.ClassificationAdapter;
import com.imci.ica.utils.GetDiagnostic;

/**
 * Class responsible for the Signs Classification activity. Uses the answers
 * from GetsignsActivity and data about the child to compute the equations
 * stored in the database's classifications table to see if there is a problem
 * or not
 * 
 * @author Antonin
 * 
 */
public class SignsClassificationActivity extends MyActivity {
	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";
	public final static String EXTRA_HASHMAP_DATA = "com.imci.ica.HASHMAP_DATA";

	private int patient_id; // the patient's id
	private ArrayList<String> resultsArray;
	
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
		
		
		Log.w("PatientId: ", ((Integer) patient_id).toString());
		Log.w("data", data.toString());
		
		// Do the diagnostic task in background
		GetDiagnostic diagnostic = new GetDiagnostic(this, patient_id, data);
		diagnostic.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_signs_classification, menu);
		return true;
	}

	@Override
	public void setResutls(ArrayList<Integer> results) {
		
		resultsArray = new ArrayList<String>();		
		for(int i=0; i<results.size(); i++) 
			resultsArray.add(results.get(i).toString());
		
		// We now put the results inside a ListView
		ClassificationAdapter arrayAdapter = new ClassificationAdapter(
				this, R.layout.layout_classifications, resultsArray);
		((ListView) findViewById(R.id.listView_classifications))
				.setAdapter(arrayAdapter);


	}	
	
	/**
	 * Check result of diagnostic (Debugging purposes)
	 * 
	 * @return true if there is any diagnostic
	 */
	public boolean checkResult() {
		if(resultsArray.size() > 0) {
			return true;
		} else
			return false;
	}
	
	/**
	 * Answer to back key pressing
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, InfoPatientActivity.class);
			intent.putExtra(InfoPatientActivity.EXTRA_ID_PATIENT, patient_id);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

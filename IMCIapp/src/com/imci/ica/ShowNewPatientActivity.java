package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.imci.ica.utils.Database;

/**
 * Class to show the information of a new patient, to check if all data is
 * right.
 * 
 * @author Miguel
 * 
 */
public class ShowNewPatientActivity extends Activity {

	public String first_name;
	public String last_name;
	public String gender;
	public String born_on;
	public int village_id;
	public int zone_id;

	/**
	 * Show all data in screen
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_show_new_patient);

		// Catch of the data from NewPatientActivity
		first_name = intent
				.getStringExtra(SearchPatientActivity.EXTRA_FIRST_NAME);
		last_name = intent
				.getStringExtra(SearchPatientActivity.EXTRA_LAST_NAME);
		gender = intent.getStringExtra(SearchPatientActivity.EXTRA_GENDER);
		born_on = intent.getStringExtra(SearchPatientActivity.EXTRA_BORN_ON);
		village_id = intent.getIntExtra(SearchPatientActivity.EXTRA_VILLAGE_ID,
				0);
		zone_id = intent.getIntExtra(SearchPatientActivity.EXTRA_ZONE_ID, 0);

		// Putting in TextView data of new patient
		TextView textName = (TextView) findViewById(R.id.TextFirstName);
		textName.setText(first_name);

		TextView textLastName = (TextView) findViewById(R.id.TextLastName);
		textLastName.setText(last_name);

		TextView textGender = (TextView) findViewById(R.id.TextGender);

		// Creating Male or Female text
		if (gender != null) {
			if (gender.equals("t"))
				textGender.setText(R.string.male);
			else
				textGender.setText(R.string.female);
		}
		
		TextView textBornOn = (TextView) findViewById(R.id.TextBirth);
		textBornOn.setText(born_on);

		Database db = new Database(this);
		String villageName = db.getNameOfZone(village_id);

		TextView textVillage = (TextView) findViewById(R.id.TextVillage);
		textVillage.setText(villageName);
		String zoneName = db.getNameOfZone(zone_id);

		TextView textZone = (TextView) findViewById(R.id.TextZone);
		textZone.setText(zoneName);
	}

	/**
	 * Answer to Confirm button click
	 * 
	 * @param view
	 */
	public boolean confirmInfo(View view) {

		/**
		 * Register in Database
		 */
		Database db = new Database(this);

		int patient_id = db.insertNewPatient(village_id, first_name, last_name, gender,
				born_on);
		if (patient_id > 0) {
			// Closing previus activity
			Intent intentPrev = getIntent();
			intentPrev.putExtra(SearchPatientActivity.EXTRA_FINISH_ACTIVITY,
					true);
			setResult(Activity.RESULT_OK, intentPrev);
			
			
			
			// Creating DoneRegisterActivity
			Intent intentNew = new Intent(this,
					DoneRegisterPatientActivity.class);
			intentNew.putExtra(DoneRegisterPatientActivity.EXTRA_ID_PATIENT, patient_id);
			startActivity(intentNew);
			finish();
			db.close();
		} else {
			Toast.makeText(this, R.string.databaseError, Toast.LENGTH_LONG)
					.show();
			db.close();
			return false;
		}
		return true;
	}

	/**
	 * Answer to Modify button
	 * 
	 * @param view
	 */
	public void modifyInfo(View view) {
		Intent intent = getIntent();
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	/**
	 * Answer to back key pressing
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = getIntent();
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
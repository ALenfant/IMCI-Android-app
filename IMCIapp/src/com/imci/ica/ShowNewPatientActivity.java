package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowNewPatientActivity extends Activity {

	String first_name;
	String last_name;
	String gender;
	String born_on;
	int village_id;
	int zone_id;

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
		born_on = intent
				.getStringExtra(SearchPatientActivity.EXTRA_BORN_ON);
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
		if (gender.equals("t"))
			textGender.setText(R.string.male);
		else
			textGender.setText(R.string.female);

		TextView textBornOn = (TextView) findViewById(R.id.TextBirth);
		textBornOn.setText(born_on);

		Database db = new Database(this);
		String villageName = db.getNameOfZone(village_id);
//		
//		Cursor villageCursor = db.getZone(village_id);
//		if (villageCursor.getCount() > 0) {
//			String villageName = villageCursor.getString(1);

			TextView textVillage = (TextView) findViewById(R.id.TextVillage);
			textVillage.setText(villageName);
//		}
//
//		villageCursor = db.getZone(zone_id);
//		if (villageCursor.getCount() > 0) {
//			String zoneName = villageCursor.getString(1);
//
			String zoneName = db.getNameOfZone(zone_id);
			
			TextView textZone = (TextView) findViewById(R.id.TextZone);
			textZone.setText(zoneName);
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_new_patient, menu);
		return true;
	}

	// Answer to Confirm button click
	public void confirmInfo(View view) {

		// Register in Database
		Database db = new Database(this);

		if (db.insertNewPatient(village_id, first_name, last_name, gender,
				born_on)) {
			// Closing previus activity
			Intent intentPrev = getIntent();
			intentPrev.putExtra(SearchPatientActivity.EXTRA_FINISH_ACTIVITY, true);
			setResult(Activity.RESULT_OK, intentPrev);

			// Creating DoneRegisterActivity
			Intent intentNew = new Intent(this,
					DoneRegisterPatientActivity.class);
			startActivity(intentNew);
			finish();
		} else {
			Toast.makeText(this, R.string.databaseError, Toast.LENGTH_LONG)
					.show();
		}

	}

	// Answer to Modify button
	public void modifyInfo(View view) {
		Intent intent = getIntent();
		//intent.putExtra(NewPatientActivity.EXTRA_FINISH_ACTIVITY, false);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	// Creating a string with Date format
	public String dateString(Integer day, Integer month, Integer year) {
		String strDate = twoDigitsString(year) + "-"
				+ twoDigitsString(month + 1) + "-" + twoDigitsString(day);

		return strDate;
	}

	// Creating a string from a Integer with two digits,
	// even if number is less than 10.
	public String twoDigitsString(Integer number) {
		String str;

		if (number <= 9 & number >= 0)
			str = "0" + number.toString();
		else
			str = number.toString();

		return str;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = getIntent();
			//intent.putExtra(NewPatientActivity.EXTRA_FINISH_ACTIVITY, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
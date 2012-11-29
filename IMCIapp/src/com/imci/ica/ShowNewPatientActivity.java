package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowNewPatientActivity extends Activity {

	String first_name;
	String last_name;
	boolean gender;
	String born_on;
	int village_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_show_new_patient);

		Integer day;
		Integer month;
		Integer year;

		// Catch of the data from NewPatientActivity
		first_name = intent.getStringExtra(NewPatientActivity.EXTRA_FIRST_NAME);
		last_name = intent.getStringExtra(NewPatientActivity.EXTRA_LAST_NAME);
		gender = intent.getBooleanExtra(NewPatientActivity.EXTRA_GENDER, true);
		day = intent.getIntExtra(NewPatientActivity.EXTRA_DAY, 0);
		month = intent.getIntExtra(NewPatientActivity.EXTRA_MONTH, 0);
		year = intent.getIntExtra(NewPatientActivity.EXTRA_YEAR, 0);
		village_id = intent.getIntExtra(NewPatientActivity.EXTRA_VILLAGE, 0);

		// Putting in TextView data of new patient
		TextView textName = (TextView) findViewById(R.id.TextFirstName);
		textName.setText(first_name);

		TextView textLastName = (TextView) findViewById(R.id.TextLastName);
		textLastName.setText(last_name);

		TextView textGender = (TextView) findViewById(R.id.TextGender);
		// Creating Male or Female text
		if (gender == true)
			textGender.setText(R.string.male);
		else
			textGender.setText(R.string.female);

		born_on = dateString(day, month, year);

		TextView textBornOn = (TextView) findViewById(R.id.TextBirth);
		textBornOn.setText(born_on);

		TextView textVillage = (TextView) findViewById(R.id.TextVillage);
		textVillage.setText(Integer.toString(village_id));

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
		Database dbah = new Database(this);

		if (dbah.insertNewPatient(village_id, first_name, last_name, gender,
				born_on)) {
			// Closing previus activity
			Intent intentPrev = new Intent();
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
		finish();
	}

	// Creating a string with Date format
	public String dateString(Integer day, Integer month, Integer year) {
		String strDate = twoDigitsString(year) + "-"
				+ twoDigitsString(month + 1) + "-" + twoDigitsString(day);

		return strDate;
	}

	// Creating a string with DateTime format
	public String dateTimeString(Integer day, Integer month, Integer year,
			Integer hour, Integer min, Integer sec) {

		String strDateTime = twoDigitsString(year) + "-"
				+ twoDigitsString(month + 1) + "-" + twoDigitsString(day) + " "
				+ twoDigitsString(hour) + ":" + twoDigitsString(min) + ":"
				+ twoDigitsString(sec);

		return strDateTime;
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
}

package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SearchPatientActivity extends Activity {

	public final static String EXTRA_FIRST_NAME = "com.imci.ica.FIRST_NAME";
	public final static String EXTRA_LAST_NAME = "com.imci.ica.FAMILY_NAME";
	public final static String EXTRA_GENDER = "com.imci.ica.GENDER";
	public final static String EXTRA_BORN_ON = "com.imci.ica.BORN_ON";
	public final static String EXTRA_VILLAGE_ID = "com.imci.ica.VILLAGE";
	public final static String EXTRA_ZONE_ID = "com.imci.ica.ZONE";
	public final static String EXTRA_MODE_CREATE = "com.imci.ica.MODE_CREATE";
	public final static String EXTRA_FINISH_ACTIVITY = "com.imci.ica.FINISH_ACTIVITY";
	
	public final static int SEARCH = 0;
	public final static int CREATE = 1;
	int mode = SEARCH;

	boolean CHECK_VILLAGE = false;
	boolean SEND_DATE = false;

	int village_id = -1;
	int zone_id = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_patient);

		// We hide the picker
		findViewById(R.id.datePicker).setVisibility(View.GONE);

		Button villageButton = ((Button) findViewById(R.id.buttonVillage));
		villageButton.setText(R.string.select_village);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search_patient, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data.getBooleanExtra(EXTRA_FINISH_ACTIVITY, false)) {
			finish();
		}
		if (CHECK_VILLAGE) {

			super.onActivityResult(requestCode, resultCode, data);

			village_id = data.getIntExtra(
					ZoneChoiceActivity.EXTRA_RETURNED_ZONE_ID, -1);
			zone_id = data.getIntExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID,
					-1);

			Database db = new Database(this);
			Cursor villageCursor = db.getZone(village_id);
			if (villageCursor.getCount() > 0) {
				String villageName = villageCursor.getString(1);

				Button villageButton = ((Button) findViewById(R.id.buttonVillage));
				villageButton.setText(villageName);
			}
			CHECK_VILLAGE = false;
			return;
		}
		if (data.getBooleanExtra(EXTRA_MODE_CREATE, false)) {
			mode = CREATE;
			showPicker(findViewById(R.id.linearLayoutSearch));

		}
	}

	public void selectVillage(View view) {

		// check_result = true;

		// We start the zone selection activity
		Intent i = new Intent(SearchPatientActivity.this,
				ZoneChoiceActivity.class);
		i.putExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID, 0);

		// Activity not finish at return
		// i.putExtra(EXTRA_MODE_RESULT, GET_VILLAGE);
		CHECK_VILLAGE = true;
		startActivityForResult(i, 0);
	}

	// Answer to Cancel button
	public void cancel(View view) {
		finish();
	}

	// Answer to Send button click
	public void sendInfo(View view) {
		switch (mode) {
		case SEARCH:
			searchPatient();
			break;
		case CREATE:
			createPatient();
			break;
		}
		;
	}

	public void searchPatient() {
		Intent intent = new Intent(this, FoundPatientsActivity.class);

		// Passing data to next activity
		EditText editFirstName = (EditText) findViewById(R.id.edit_first_name);
		if (editFirstName.getText().length() == 0) {
			intent.putExtra(EXTRA_FIRST_NAME, "");
		} else {
			String first_name = editFirstName.getText().toString();
			intent.putExtra(EXTRA_FIRST_NAME, first_name);
		}

		EditText editLastName = (EditText) findViewById(R.id.edit_last_name);
		if (editLastName.getText().length() == 0) {
			Toast.makeText(this, R.string.invalidFamilyName, Toast.LENGTH_LONG)
					.show();
			return;
		} else {
			String last_name = editLastName.getText().toString();
			intent.putExtra(EXTRA_LAST_NAME, last_name);
		}

		// Check value of Gender Radio Group
		RadioButton maleButton = (RadioButton) findViewById(R.id.radio0);
		RadioButton femaleButton = (RadioButton) findViewById(R.id.radio1);

		if (maleButton.isChecked()) {
			intent.putExtra(EXTRA_GENDER, "t");
		} else if (femaleButton.isChecked()) {
			intent.putExtra(EXTRA_GENDER, "f");
		} else {
			intent.putExtra(EXTRA_GENDER, "");
		}

		if (SEND_DATE) {
			// Getting date of birth
			DatePicker birth = (DatePicker) findViewById(R.id.datePicker);
			String born_on = dateString(birth.getDayOfMonth(),
					birth.getMonth(), birth.getYear());

			intent.putExtra(EXTRA_BORN_ON, born_on);
		} else {
			intent.putExtra(EXTRA_BORN_ON, "");
		}

		intent.putExtra(EXTRA_VILLAGE_ID, village_id);

		// Avoid the Activity finish when back key is pressed in next Activity
		intent.putExtra(EXTRA_FINISH_ACTIVITY, false);

		// Starting new activity and hoping a result for finish by itself
		startActivityForResult(intent, 0);
	}

	public void createPatient() {
		Intent intent = new Intent(this, ShowNewPatientActivity.class);

		// Passing data to next activity
		EditText editFirstName = (EditText) findViewById(R.id.edit_first_name);
		if (editFirstName.getText().length() == 0) {
			Toast.makeText(this, R.string.invalidFirstName, Toast.LENGTH_LONG)
					.show();
			return;
		} else {
			String first_name = editFirstName.getText().toString();
			intent.putExtra(EXTRA_FIRST_NAME, first_name);
		}

		EditText editLastName = (EditText) findViewById(R.id.edit_last_name);
		if (editLastName.getText().length() == 0) {
			Toast.makeText(this, R.string.invalidFamilyName, Toast.LENGTH_LONG)
					.show();
			return;
		} else {
			String last_name = editLastName.getText().toString();
			intent.putExtra(EXTRA_LAST_NAME, last_name);
		}

		// Check value of Gender Radio Group
		RadioButton maleButton = (RadioButton) findViewById(R.id.radio0);
		RadioButton femaleButton = (RadioButton) findViewById(R.id.radio1);

		if (maleButton.isChecked()) {
			intent.putExtra(EXTRA_GENDER, "t");
		} else if (femaleButton.isChecked()) {
			intent.putExtra(EXTRA_GENDER, "f");
		} else {
			Toast.makeText(this, R.string.pressGender, Toast.LENGTH_LONG)
			.show();
			return;
		}

		// Getting date of birth
		DatePicker birth = (DatePicker) findViewById(R.id.datePicker);

		String born_on = dateString(birth.getDayOfMonth(), birth.getMonth(),
				birth.getYear());

		intent.putExtra(EXTRA_BORN_ON, born_on);

		if (village_id == -1 || zone_id == -1) {
			Toast.makeText(this, R.string.invalidVillage, Toast.LENGTH_LONG)
					.show();
			return;
		} else {
			intent.putExtra(EXTRA_VILLAGE_ID, village_id);
			intent.putExtra(EXTRA_ZONE_ID, zone_id);
		}
		intent.putExtra(EXTRA_FINISH_ACTIVITY, false);

		// Starting new activity and hoping a result for finish by itself
		startActivityForResult(intent, 0);

	}

	// If the date is be sent
	public void showPicker(View view) {
		// We hide the button
		findViewById(R.id.buttonDate).setVisibility(View.GONE);

		// And show the next step
		findViewById(R.id.datePicker).setVisibility(View.VISIBLE);

		SEND_DATE = true;
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
}

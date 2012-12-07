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

public class NewPatientActivity extends Activity {

	public final static String EXTRA_FIRST_NAME = "com.imci.ica.FIRST_NAME";
	public final static String EXTRA_LAST_NAME = "com.imci.ica.FAMILY_NAME";
	public final static String EXTRA_GENDER = "com.imci.ica.GENDER";
	public final static String EXTRA_BORN_ON = "com.imci.ica.BORN_ON";
	public final static String EXTRA_VILLAGE_ID = "com.imci.ica.VILLAGE";
	public final static String EXTRA_ZONE_ID = "com.imci.ica.ZONE";
	public final static String EXTRA_FINISH_ACTIVITY = "com.imci.ica.EXTRA_FINISH_ACTIVITY";

	boolean check_result = false;

	int village_id = -1;
	int zone_id = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_patient);

		Button villageButton = ((Button) findViewById(R.id.buttonVillage));
		villageButton.setText(R.string.select_village);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_new_patient, menu);
		return true;
	}

	// Answer to Send button click
	public void sendInfo(View view) {

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
			intent.putExtra(EXTRA_GENDER, true);
		} else if (femaleButton.isChecked()) {
			intent.putExtra(EXTRA_GENDER, false);
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

	// Answer to Cancel button
	public void cancel(View view) {
		finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data.getBooleanExtra(EXTRA_FINISH_ACTIVITY, false)) {
			finish();
		} else if (check_result) {

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

			check_result = false;
		}
	}

	public void selectVillage(View view) {

		check_result = true;

		// We start the zone selection activity
		Intent i = new Intent(NewPatientActivity.this, ZoneChoiceActivity.class);
		i.putExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID, 0); // Parent:0(none)
		i.putExtra(ZoneChoiceActivity.EXTRA_PARENT_TWO_ZONE_ID, -1); // Parent:0(none)

		// Activity not finish at return
		i.putExtra(EXTRA_FINISH_ACTIVITY, false);
		startActivityForResult(i, 0);
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

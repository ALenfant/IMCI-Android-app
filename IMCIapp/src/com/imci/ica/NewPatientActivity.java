package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class NewPatientActivity extends Activity {

	public final static String EXTRA_FIRST_NAME = "com.imci.ica.FIRST_NAME";
	public final static String EXTRA_LAST_NAME = "com.imci.ica.FAMILY_NAME";
	public final static String EXTRA_GENDER = "com.imci.ica.GENDER";
	public final static String EXTRA_DAY = "com.imci.ica.DAY";
	public final static String EXTRA_MONTH = "com.imci.ica.MONTH";
	public final static String EXTRA_YEAR = "com.imci.ica.YEAR";
	public final static String EXTRA_VILLAGE_ID = "com.imci.ica.VILLAGE";
	public final static String EXTRA_ZONE_ID = "com.imci.ica.ZONE";
	public final static String EXTRA_FINISH_ACTIVITY = "com.imci.ica.EXTRA_FINISH_ACTIVITY";

	boolean check_result = false;

	int village_id, zone_id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_patient);
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
			String name = editFirstName.getText().toString();
			intent.putExtra(EXTRA_FIRST_NAME, name);
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
		DatePicker birth = (DatePicker) findViewById(R.id.datePicker1);
		intent.putExtra(EXTRA_DAY, birth.getDayOfMonth());
		intent.putExtra(EXTRA_MONTH, birth.getMonth());
		intent.putExtra(EXTRA_YEAR, birth.getYear());

		/*
		 * EditText editVillage = (EditText) findViewById(R.id.edit_village); if
		 * (editVillage.getText().length() == 0) { Toast.makeText(this,
		 * R.string.invalidVillage, Toast.LENGTH_LONG).show(); return; } else {
		 * //Checking field Village is a number try { Integer villageID =
		 * Integer.parseInt(editVillage.getText().toString());
		 * intent.putExtra(EXTRA_VILLAGE_ID, villageID); } catch
		 * (NumberFormatException e) { Toast.makeText(this,
		 * R.string.invalidVillage, Toast.LENGTH_LONG).show(); return; } }
		 */

		intent.putExtra(EXTRA_VILLAGE_ID, village_id); // Parent:0(none)
		intent.putExtra(EXTRA_ZONE_ID, zone_id); // Parent:0(none)

		
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
			zone_id = data.getIntExtra(
					ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID, -1);

			check_result = false;
		}
	}

	public void selectVillage(View view) {

		check_result = true;
		
		// We start the zone selection activity
		Intent i = new Intent(NewPatientActivity.this, ZoneChoiceActivity.class);
		i.putExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID, 0); // Parent:0(none)

		// Activity not finish at return
		i.putExtra(EXTRA_FINISH_ACTIVITY, false); // Parent:0(none)
		startActivityForResult(i, 0);
	}
}

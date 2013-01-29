package com.imci.ica;

import com.imci.ica.utils.Database;
import com.imci.ica.utils.DateUtils;

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

/**
 * Activity to search a Patient into the system
 * This activity get the search criteria
 * 
 * If patient we search doesn't exist, it's possible to create
 * in this activity.
 * 
 * @author Miguel
 *
 */
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
	
	/**
	 * Show the interface
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_patient);

		// Hiding the picker
		findViewById(R.id.datePicker).setVisibility(View.GONE);

		// Initialization of village button text
		Button villageButton = ((Button) findViewById(R.id.buttonVillage));
		villageButton.setText(R.string.select_village);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search_patient, menu);
		return true;
	}

	/**
	 * Treat the result of next activity
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// If it's requested to finish the activity
		if (data.getBooleanExtra(EXTRA_FINISH_ACTIVITY, false)) {
			finish();
		}
		
		// If it's requested to check the entry of a village in search
		if (CHECK_VILLAGE) {

			super.onActivityResult(requestCode, resultCode, data);

			village_id = data.getIntExtra(
					ZoneChoiceActivity.EXTRA_RETURNED_ZONE_ID, -1);
			zone_id = data.getIntExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID,
					-1);

			Database db = new Database(this);
			Cursor villageCursor = db.getZoneById(village_id);
			if (villageCursor.getCount() > 0) {
				String villageName = villageCursor.getString(1);

				Button villageButton = ((Button) findViewById(R.id.buttonVillage));
				villageButton.setText(villageName);
			}
			CHECK_VILLAGE = false;
			return;
		}
		
		// If it's requested to create a new patient
		if (data.getBooleanExtra(EXTRA_MODE_CREATE, false)) {
			mode = CREATE;
			showPicker(findViewById(R.id.linearLayoutSearch));

		}
	}

	/**
	 * Answer to select village button
	 * 
	 * @param view
	 */
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

	/**
	 * Answer to Cancel button
	 * 
	 * @param view
	 */
	public void cancel(View view) {
		finish();
	}

	/**
	 * Answer to Send button click
	 * 
	 * @param view
	 */
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

	/**
	 * Answer to search button, go to next activity to do a search
	 */
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
			String born_on = DateUtils.dateString(birth.getDayOfMonth(),
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

	/**
	 * Answer to create button, go to next activity to create a new patient
	 */
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
		int day = birth.getDayOfMonth();
		int month = birth.getMonth();
		int year = birth.getYear();
		
		if(DateUtils.differenceWithCurrentDate(year, month, day) < 0) {
			Toast.makeText(this, R.string.invalidDate, Toast.LENGTH_LONG)
			.show();
			return;
		} else {
		String born_on = DateUtils.dateString(birth.getDayOfMonth(), birth.getMonth(),
				birth.getYear());
		intent.putExtra(EXTRA_BORN_ON, born_on);
		}

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

	/**
	 * If the date goes to be sent
	 * 
	 * @param view
	 */
	public void showPicker(View view) {
		// We hide the button
		findViewById(R.id.buttonDate).setVisibility(View.GONE);

		// And show the next step
		findViewById(R.id.datePicker).setVisibility(View.VISIBLE);

		SEND_DATE = true;
	}
}

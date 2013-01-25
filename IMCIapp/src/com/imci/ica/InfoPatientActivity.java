package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Activity to show the information of a Patient
 * 
 * @author Miguel
 *
 */
public class InfoPatientActivity extends Activity {

	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";

	public int id_patient;
	public String born_on;
	public final static int POS_BIRTH = 4;

	/**
	 * Show the data in fields of layout
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_patient);

		Intent intent = getIntent();
		id_patient = intent.getIntExtra(EXTRA_ID_PATIENT, 0);

		Database db = new Database(this);
		Cursor patient = db.getPatientById(id_patient);
		born_on = patient.getString(POS_BIRTH);
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);

		PatientInflater inflater = new PatientInflater(this);
		inflater.showPatientInScreen(id_patient, mainLayout, patient, db);
		
		Cursor cursor = db.getPatientDiagnostics(id_patient);
		if (cursor.getCount() != 0) {
			View view = getLayoutInflater().inflate(
					R.layout.layout_measures, mainLayout, false);

			mainLayout.addView(view);

			TextView textWeight = (TextView) findViewById(R.id.textWeight);
			textWeight.setText(((Float) cursor.getFloat(cursor.getColumnIndex("weight"))).toString());
			TextView textHeight = (TextView) findViewById(R.id.textHeight);
			textHeight.setText(((Float) cursor.getFloat(cursor.getColumnIndex("height"))).toString());
			TextView textTemp = (TextView) findViewById(R.id.textTemp);
			textTemp.setText(((Float) cursor.getFloat(cursor.getColumnIndex("temperature"))).toString());
			TextView textMuac = (TextView) findViewById(R.id.textMuac);
			textMuac.setText(((Float) cursor.getFloat(cursor.getColumnIndex("mac"))).toString());
		} else {
			TextView text = new TextView(this);
			mainLayout.addView(text);
			text.setText(R.string.noMeasures);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_info_patient, menu);
		return true;
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
	
	/**
	 * Answer to button to create a new diagnostic
	 * 
	 * @param view
	 */
	public void newDiagnostic(View view) {
		Intent prevIntent = getIntent();
		prevIntent.putExtra(FoundPatientsActivity.EXTRA_FINISH_ACTIVITY, true);
		setResult(Activity.RESULT_OK, prevIntent);
		
		Intent newIntent = new Intent(this, GetSignsActivity.class);
		newIntent.putExtra(GetSignsActivity.EXTRA_ID_PATIENT, id_patient);
		newIntent.putExtra(GetSignsActivity.EXTRA_AGE_GROUP, DateUtils.getAgeGroup(born_on));
		startActivity(newIntent);
	}

}

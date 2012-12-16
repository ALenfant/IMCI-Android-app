package com.imci.ica;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

public class InfoPatientActivity extends Activity {

	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";

	public int id_patient;
	public String born_on;
	public final static int POS_BIRTH = 4;

	
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_info_patient, menu);
		return true;
	}

	public void newDiagnostic(View view) {
		Intent intent = new Intent(this, GetSignsActivity.class);
		intent.putExtra(GetSignsActivity.EXTRA_ID_PATIENT, id_patient);
		intent.putExtra(GetSignsActivity.EXTRA_AGE_GROUP,
				getAgeGroup());
		startActivityForResult(intent, 0);
	}
	
	public int getAgeGroup() {

		int age_group;
//		int months;
		int days;

		String[] birthArray = born_on.split("\\-");
		Integer bDay = Integer.parseInt(birthArray[0]);
		Integer bMonth = Integer.parseInt(birthArray[1]) - 1;
		Integer bYear = Integer.parseInt(birthArray[2]);
		GregorianCalendar birth = new GregorianCalendar(bYear, bMonth, bDay);
		GregorianCalendar current = new GregorianCalendar();
		int yearRange = current.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
//		months = current.get(Calendar.MONTH) - birth.get(Calendar.MONTH)
//				+ yearRange * 12;
//		if (months > 1)
//			days = -1;
//		else
			days = current.get(Calendar.DAY_OF_YEAR)
					- birth.get(Calendar.DAY_OF_YEAR) + yearRange * 365;

//		if(months>=2) {
//			age_group = 2;
//		} else 
			if (days >= 0 && days <= 7) {
			age_group = 0;
		} else if (days >= 8 && days <= 60){
			age_group = 1;
		} else {
			age_group = 2;
		}
		
		return age_group;
	}
}

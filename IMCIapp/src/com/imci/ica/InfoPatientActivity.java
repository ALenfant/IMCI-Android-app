package com.imci.ica;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class InfoPatientActivity extends Activity {

	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_patient);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_info_patient, menu);
		return true;
	}

}

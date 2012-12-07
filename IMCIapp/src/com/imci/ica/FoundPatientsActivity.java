package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class FoundPatientsActivity extends Activity {

	String first_name;
	String last_name;
	boolean gender;
	String born_on;
	int village_id;
//	Cursor c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_found_patients);

		// Catch of the data from SearchPatientActivity
		first_name = intent
				.getStringExtra(SearchPatientActivity.EXTRA_FIRST_NAME);
		last_name = intent
				.getStringExtra(SearchPatientActivity.EXTRA_LAST_NAME);
		gender = intent.getBooleanExtra(SearchPatientActivity.EXTRA_GENDER,
				true);
		born_on = intent.getStringExtra(NewPatientActivity.EXTRA_BORN_ON);
		village_id = intent.getIntExtra(SearchPatientActivity.EXTRA_VILLAGE_ID,
				0);

		Database db = new Database(this);
		Cursor c = db.getPatients(first_name, last_name, gender, born_on, village_id);
		startManagingCursor(c);
		if (c == null) {
			Toast.makeText(this, R.string.noPatientsFound, Toast.LENGTH_LONG)
			.show();
			finish();
			/* please ignore this block */
		} else {
			c.moveToFirst();

			ListView listView = (ListView) findViewById(R.id.listViewPatients);
			listView.setAdapter(new CursorPatientAdapter(this, c));

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_found, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = getIntent();
			intent.putExtra(SearchPatientActivity.EXTRA_FINISH_ACTIVITY, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}


package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FoundPatientsActivity extends Activity {

	public final static String EXTRA_FINISH_ACTIVITY = "com.imci.ica.FINISH_ACTIVITY";
	
	String first_name;
	String last_name;
	String gender;
	String born_on;
	int village_id;

	// Cursor c;

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
		gender = intent.getStringExtra(SearchPatientActivity.EXTRA_GENDER);
		born_on = intent.getStringExtra(SearchPatientActivity.EXTRA_BORN_ON);
		village_id = intent.getIntExtra(SearchPatientActivity.EXTRA_VILLAGE_ID,
				0);

		Database db = new Database(this);
		Cursor mCursor = db.getPatients(first_name, last_name, gender, born_on, village_id);
		startManagingCursor(mCursor);
		if (mCursor.getCount()==0) {
			Toast.makeText(this, R.string.noPatientsFound, Toast.LENGTH_LONG)
			.show();
			/* please ignore this block */
		} else {
			mCursor.moveToFirst();

			ListView listView = (ListView) findViewById(R.id.listViewPatients);
			listView.setAdapter(new CursorPatientAdapter(this, mCursor));

			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long id) {
					Intent prevIntent = getIntent();
					prevIntent.putExtra(SearchPatientActivity.EXTRA_FINISH_ACTIVITY, true);
					setResult(Activity.RESULT_OK, prevIntent);

					Cursor selectItem = (Cursor) adapter.getItemAtPosition(position);
				    int idItem = Integer.parseInt(selectItem.getString(0));
					
					Intent newIntent = new Intent(FoundPatientsActivity.this, InfoPatientActivity.class);
					newIntent.putExtra(InfoPatientActivity.EXTRA_ID_PATIENT, idItem);
					newIntent.putExtra(EXTRA_FINISH_ACTIVITY, false);

					startActivityForResult(newIntent, 0);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_found_patients, menu);
		return true;
	}	
   	
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data.getBooleanExtra(EXTRA_FINISH_ACTIVITY, false)) {
			finish();
		}
	}

	
	//Answer to Create Patient button click
    public void createPatient(View view) {
    	Intent intent = getIntent();
    	intent.putExtra(SearchPatientActivity.EXTRA_MODE_CREATE, true);
		setResult(Activity.RESULT_OK, intent);
    	finish();
    }
}

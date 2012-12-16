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

	String first_name;
	String last_name;
	boolean gender;
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
		gender = intent.getBooleanExtra(SearchPatientActivity.EXTRA_GENDER,
				true);
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
//					int selected_id = (int) id;
//
//					Toast.makeText(getApplicationContext(),
//							"selected: pos" + position + " id:" + selected_id,
//							Toast.LENGTH_SHORT).show();

					Cursor selectItem = (Cursor) adapter.getItemAtPosition(position);
				    int idItem = Integer.parseInt(selectItem.getString(0));
					
					Intent i = new Intent(FoundPatientsActivity.this, InfoPatientActivity.class);
					i.putExtra(InfoPatientActivity.EXTRA_ID_PATIENT, idItem);
																				
					startActivity(i);
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
//			intent.putExtra(SearchPatientActivity.EXTRA_MODE_RESULT, 
//					SearchPatientActivity.DO_NOTHING);
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//Answer to Create Patient button click
    public void createPatient(View view) {
    	Intent intent = getIntent();
    	intent.putExtra(SearchPatientActivity.EXTRA_CHANGE_MODE, true);
		setResult(Activity.RESULT_OK, intent);
    	finish();
    }
}

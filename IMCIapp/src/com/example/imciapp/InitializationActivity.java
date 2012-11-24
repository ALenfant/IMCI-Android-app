package com.example.imciapp;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InitializationActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initialization);

		// Initialization of the buttons
		findViewById(R.id.button_newcenter).setOnClickListener( // New center
				new OnClickListener() {
					public void onClick(View v) {
						// We hide the buttons
						findViewById(R.id.linearlayout_initbuttons)
								.setVisibility(View.GONE);

						// And show the next step
						findViewById(R.id.linearlayout_zoneselection)
								.setVisibility(View.VISIBLE);
					}
				});

		findViewById(R.id.button_zoneselection).setOnClickListener(
				new OnClickListener() { // Select a location
					public void onClick(View v) {
						// We start the zone selection activity
						Intent i = new Intent(InitializationActivity.this,
								ZoneChoiceActivity.class);
						i.putExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID, 0); // Parent:0(none)
						startActivityForResult(i, 0);
					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		int parent_zone_id = data.getIntExtra(ZoneChoiceActivity.EXTRA_RETURNED_ZONE_ID,-1);
		Toast.makeText(this, "Returned result!" + parent_zone_id, Toast.LENGTH_SHORT).show();
		
		Database db = new Database(this);
		Cursor parentCursor = db.getZone(parent_zone_id);
		if (parentCursor.getCount() > 0) {
			String zone_name = parentCursor.getString(1);
			((TextView)(findViewById(R.id.textView_selectedzone))).setText(zone_name);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_initialization, menu);
		return true;
	}
}
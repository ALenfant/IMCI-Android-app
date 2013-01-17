package com.imci.ica;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Class responsible for the Zone Choice activity. Called when initializing a
 * new center, displays a list of the zones, and then calls itself each time a
 * zone is clicked to show its children zones. The current zone is selected when
 * clicking on a button.
 * 
 * @author Antonin
 * 
 */
public class ZoneChoiceActivity extends Activity {
	public final static String EXTRA_PARENT_ZONE_ID = "com.imci.ica.PARENT_ZONE_ID";
	public final static String EXTRA_PARENT_TWO_ZONE_ID = "com.imci.ica.PARENT_TWO_ZONE_ID";
	public final static String EXTRA_PARENT_ZONES = "com.imci.ica.PARENT_ZONES";
	public final static String EXTRA_RETURNED_ZONE_ID = "com.imci.ica.RETURNED_ZONE_ID";
	public final static String EXTRA_CHECK_RESULT = "com.imci.ica.CHECK_RESULT";

	private int parent_zone_id; // The parent zone's id
	private int parent_two_zone_id; // The parent's parent zone's id

	private ArrayList<String> parentZones;

	private Database db; // Database adapter

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zone_choice);

		// We get the information provided when the activity was called
		Intent intent = getIntent();
		parent_zone_id = intent.getIntExtra(
				ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID, -1); // -1
		parent_two_zone_id = intent.getIntExtra(
				ZoneChoiceActivity.EXTRA_PARENT_TWO_ZONE_ID, -1); // -1
		// default
		// value
		parentZones = intent
				.getStringArrayListExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONES);

		if (parentZones == null) {
			parentZones = new ArrayList<String>();
		} else {
			TextView textView = (TextView) findViewById(R.id.textView);
			textView.setText(implode(">", parentZones));
		}

		/*
		 * Toast.makeText(getApplicationContext(), "parent_id:" +
		 * parent_zone_id, Toast.LENGTH_SHORT).show();
		 */

		db = new Database(this);
		// We get data about the parent zone
		Cursor parentCursor = db.getZone(parent_zone_id);
		if (parentCursor.getCount() > 0) {
			// If there is a parent, we create the button and put its name on it
			String parent_name = parentCursor.getString(1);
			Button selectButton = ((Button) findViewById(R.id.button));
			selectButton.setText(parent_name);
			selectButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// Button clicked, we return the selected zone
					Intent i = new Intent();
					i.putExtra(ZoneChoiceActivity.EXTRA_RETURNED_ZONE_ID,
							parent_zone_id);
					i.putExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID,
							parent_two_zone_id);
					i.putExtra(ZoneChoiceActivity.EXTRA_CHECK_RESULT, true);
					setResult(0, i);// Here I am Requestcode 0
					finish();
				}
			});

		}

		// We get all the zones to display
		Cursor cursor = db.getZones(parent_zone_id);
		startManagingCursor(cursor);

		// We create a ListView to display the data
		ListView listView = (ListView) findViewById(R.id.listView);
		String columns[] = new String[] { "name" };
		int[] to = new int[] { android.R.id.text1 };

		// We use a CursorAdapter to get the data from the cursor to the
		// ListView
		SimpleCursorAdapter myAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor, columns, to);
		listView.setAdapter(myAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				// Code managing when a zone is clicked
				// We call the same activity to display its children
				int selected_id = (int) id;

				/*
				 * Toast.makeText(getApplicationContext(), "selected: pos" +
				 * position + " id:" + selected_id, Toast.LENGTH_SHORT).show();
				 */

				Cursor mycursor = (Cursor) adapter.getItemAtPosition(position);
				String item = mycursor.getString(1);
				parentZones.add(item);

				Intent i = new Intent(ZoneChoiceActivity.this,
						ZoneChoiceActivity.class);
				i.putExtra(ZoneChoiceActivity.EXTRA_PARENT_TWO_ZONE_ID,
						parent_zone_id);
				i.putExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID, selected_id); // New
																					// parent
				i.putExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONES, parentZones); // List
																				// of
																				// parent
																				// zones

				startActivityForResult(i, 0);
			}
		});
	}

	/**
	 * Called when a zone is selected on one of the child activities
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Toast.makeText(this, "Returned result!", Toast.LENGTH_SHORT).show();
		setResult(0, data);// We transmit the intent
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_zone_choice, menu);
		return true;
	}

	/**
	 * Implodes a string
	 * 
	 * @param glue
	 * @param strArray
	 * @return
	 */
	public static String implode(String glue, ArrayList<String> strArray) {
		String ret = "";
		for (int i = 0; i < strArray.size(); i++) {
			ret += (i == strArray.size() - 1) ? strArray.get(i) : strArray
					.get(i) + glue;
		}
		return ret;
	}

	/**
	 * Called when a key is pressed
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = getIntent();
			intent.putExtra(EXTRA_CHECK_RESULT, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

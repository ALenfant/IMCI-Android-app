package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imci.ica.utils.ApplicationPreferences;
import com.imci.ica.utils.Database;
import com.imci.ica.utils.Login;

/**
 * Class responsible for the Initialization activity Used to initialize a
 * medical center (when the application is executed for the first time)
 * 
 * @author Antonin
 * 
 */
public class InitializationActivity extends Activity {
	protected int selected_zone_id = -1;
	protected String selected_zone_name = "";

	boolean check_result;

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
		findViewById(R.id.button_restorecenter).setOnClickListener(// Restore
																	// center
				new OnClickListener() {

					public void onClick(View arg0) {
						// TODO Do something here...
						Toast.makeText(InitializationActivity.this,
								"Unimplemented function", Toast.LENGTH_LONG)
								.show();
					}
				});
		findViewById(R.id.button_zoneselection).setOnClickListener( // Select a
																	// location
				new OnClickListener() {
					public void onClick(View v) {
						// We start the zone selection activity
						Intent i = new Intent(InitializationActivity.this,
								ZoneChoiceActivity.class);
						i.putExtra(ZoneChoiceActivity.EXTRA_PARENT_ZONE_ID, 0); // Parent:0(none)
						startActivityForResult(i, 0);
					}
				});
		findViewById(R.id.button_setupcenter).setOnClickListener( // Setup the
																	// center
				new OnClickListener() {
					public void onClick(View v) {
						setupCenter();
					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data.getBooleanExtra(ZoneChoiceActivity.EXTRA_CHECK_RESULT, true)) {
			super.onActivityResult(requestCode, resultCode, data);

			int parent_zone_id = data.getIntExtra(
					ZoneChoiceActivity.EXTRA_RETURNED_ZONE_ID, -1);

			Database db = new Database(this);
			Cursor parentCursor = db.getZoneById(parent_zone_id);
			if (parentCursor.getCount() > 0) {
				selected_zone_name = parentCursor.getString(1);
				((TextView) (findViewById(R.id.textView_selectedzone)))
						.setText(selected_zone_name);
				selected_zone_id = parent_zone_id; // We set the selected zone
													// id

				// And show the next step
				findViewById(R.id.linearLayout_logindetails).setVisibility(
						View.VISIBLE);
			}
		}
	}

	/**
	 * Called when the button Setup Center is pressed
	 */
	protected void setupCenter() {
		String name = ((EditText) (findViewById(R.id.editText_fullname)))
				.getText().toString();
		String password = ((EditText) (findViewById(R.id.editText_password)))
				.getText().toString();
		String password2 = ((EditText) (findViewById(R.id.editText_password2)))
				.getText().toString();

		if (selected_zone_id == -1) {
			Toast.makeText(this, R.string.error_empty_location,
					Toast.LENGTH_LONG).show();
			return;
		}

		if (name.length() == 0) {
			Toast.makeText(this, R.string.error_empty_fullname,
					Toast.LENGTH_LONG).show();
			return;
		}

		if (password.length() == 0) {
			Toast.makeText(this, R.string.error_empty_password,
					Toast.LENGTH_LONG).show();
			return;
		}

		if (!password.equals(password2)) {
			Toast.makeText(this, R.string.error_different_passwords,
					Toast.LENGTH_LONG).show();
			return;
		}

		Database db = new Database(this);
		if (!db.addUser(name, password, true, selected_zone_id)) {
			Toast.makeText(this, R.string.error_impossible_create_user,
					Toast.LENGTH_LONG).show();
			return;
		}

		ApplicationPreferences.initializeCenter(this, selected_zone_id);

		if (Login.login(this, name, password, false)) {

			Intent intent = new Intent(this, MenuActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
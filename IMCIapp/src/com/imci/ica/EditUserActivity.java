package com.imci.ica;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.imci.ica.utils.ApplicationPreferences;
import com.imci.ica.utils.Database;

/**
 * Class responsible for the Edit User activity
 * 
 * @author Antonin
 * 
 */
public class EditUserActivity extends Activity {
	public final static String EXTRA_USER_ID = "com.imci.ica.USER_ID";
	private int edit_userId = -1; // The id of the user we're editing
	private Boolean editMode = false; // If we are editing or creating a new
										// user

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_user);

		// Check if we edit a user...
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.getInt(EXTRA_USER_ID, -1) != -1) {
			int userId = extras.getInt(EXTRA_USER_ID, -1);
			Database db = new Database(this);
			Cursor userCursor = db.getUserById(userId);
			if (userCursor.getCount() != 0) {
				((EditText) findViewById(R.id.editText_fullname))
						.setText(userCursor.getString(1));
				if (userCursor.getString(2).equals("t")) {
					((RadioButton) findViewById(R.id.radioButton_yes))
							.setChecked(true);
				}

				// We set the labels correctly
				((TextView) findViewById(R.id.textView_title))
						.setText(R.string.edituser_title);
				((TextView) findViewById(R.id.textView_description))
						.setText(R.string.edituser_description);
				((Button) findViewById(R.id.button_edituser))
						.setText(R.string.edituser_title);

				edit_userId = userId;
				editMode = true;
			}
		}

		// Create code for the button
		((Button) findViewById(R.id.button_edituser))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						editUser();
					}
				});
	}

	/**
	 * Called when the button Edit User (or Create User when creating) is
	 * clicked
	 * 
	 * @return if the operation worked successfully
	 */
	public boolean editUser() {
		String name = ((EditText) findViewById(R.id.editText_fullname))
				.getText().toString();
		String password = ((EditText) findViewById(R.id.editText_password))
				.getText().toString();
		String password2 = ((EditText) findViewById(R.id.editText_password2))
				.getText().toString();
		Boolean admin = false;

		// We get if the user is admin or not
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup_admin);
		int selectedRadioButton = radioGroup.getCheckedRadioButtonId();
		if (selectedRadioButton == R.id.radioButton_yes) {
			admin = true;
		}

		// We do the basic checks...
		if (name.length() == 0) {
			Toast.makeText(this, R.string.error_empty_fullname,
					Toast.LENGTH_LONG).show();
			return false;
		}

		if (!editMode && password.length() == 0) {
			Toast.makeText(this, R.string.error_empty_password,
					Toast.LENGTH_LONG).show();
			return false;
		}

		if (!password.equals(password2)) {
			Toast.makeText(this, R.string.error_different_passwords,
					Toast.LENGTH_LONG).show();
			return false;
		}

		if (!editMode) {
			// We want to add a new user
			Database db = new Database(this);
			int zoneId = ApplicationPreferences.getCenterZoneId(this);
			if (db.addUser(name, password, admin, zoneId)) {
				finish(); // Done, we close it
			} else {
				Toast.makeText(this, R.string.error_impossible_create_user,
						Toast.LENGTH_LONG).show();
			}
		} else {
			// We want to edit an existing user
			Database db = new Database(this);
			int zoneId = ApplicationPreferences.getCenterZoneId(this);
			if (db.editUser(edit_userId, name, password, admin, zoneId, zoneId
					+ "/1")) {
				finish(); // Done, we close it
			} else {
				Toast.makeText(this, R.string.error_impossible_create_user,
						Toast.LENGTH_LONG).show();
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Get if the activity is in edit or create mode. Used for testing
	 * 
	 * @return if the activity is in edit mode (else it's in create mode)
	 */
	public boolean getEditMode() {
		return editMode;
	}

}

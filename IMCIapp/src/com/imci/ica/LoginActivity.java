package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.imci.ica.utils.ApplicationPreferences;
import com.imci.ica.utils.Database;

/**
 * Class responsible for the Login activity
 * 
 * @author Antonin
 * 
 */
public class LoginActivity extends Activity {
	public final static String EXTRA_DEBUG_DISABLE_INITIALIZATION = "com.imci.ica.DEBUG_DISABLE_INITIALIZATION";
	private Boolean user_remembered = false; // If a there is a remembered user

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		boolean disableInitialization = false;
		if (getIntent().getExtras() != null) {
			disableInitialization = getIntent().getExtras().getBoolean(
					EXTRA_DEBUG_DISABLE_INITIALIZATION, false);
		}

		if (!ApplicationPreferences.isCenterInitialized(this)
				&& !disableInitialization) {
			// Center not initialized
			startActivity(new Intent(this, InitializationActivity.class));
			finish();
			return;
		}

		if (ApplicationPreferences.loggedin_user != null) {
			// User already logged in
			// We display the main menu
			Intent intent = new Intent(this, MenuActivity.class);
			startActivity(intent);

			// And close this activity
			finish();
			return;
			
		}

		((Button) findViewById(R.id.button_login))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						if (login()) {
							// IF the login is successful
							// We then display the main menu
							Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
							startActivity(intent);

							// And close this activity
							finish();
						}
					}
				});

		// After everything is loaded, we check if there is a remembered user
		if (ApplicationPreferences.isUserRemembered(this)) {
			// The user checked "remember me"
			user_remembered = true;
			if (login()) {
				// IF the login is successful
				// We then display the main menu
				Intent intent = new Intent(this, MenuActivity.class);
				startActivity(intent);

				// And close this activity
				finish();
				return;
			}
		}
	}

	/**
	 * Method for connect without login, for debugging purposes
	 * 
	 * @param view
	 */
	public void directConnection(View view) {
		
	}
	
	/**
	 * Called when the Login button is clicked
	 * 
	 * @return if the login was successful
	 */
	public boolean login() {
		String name = "", password = "";
		if (!user_remembered) {
			// If there isn't a remembered user, we get the values from the form
			name = ((EditText) findViewById(R.id.editText_fullname)).getText()
					.toString();
			password = ((EditText) findViewById(R.id.editText_password))
					.getText().toString();
		} else {
			// If there is a remembered user, we load the values from the
			// application parameters
			name = ApplicationPreferences.getRememberedUserName(this);
			password = ApplicationPreferences.getRememberedUserPassword(this);
		}

		if (name.length() == 0) {
			// Empty name
			Toast.makeText(this, R.string.error_empty_fullname,
					Toast.LENGTH_LONG).show();
			return false;
		}

		if (password.length() == 0) {
			// Empty password
			Toast.makeText(this, R.string.error_empty_password,
					Toast.LENGTH_LONG).show();
			return false;
		}

		Database db = new Database(this);
		if (!db.LoginUser(name, password)) {
			// User not found
			Toast.makeText(this, R.string.error_invalid_login,
					Toast.LENGTH_LONG).show();
			user_remembered = false; // If it was a "remember me" user, we
										// disable it
			db.close();
			return false;
		} else {
			// If everything worked
			if (ApplicationPreferences.loggedin_user != null) {
				// User really logged in

				if (((CheckBox) findViewById(R.id.checkBox_remember))
						.isChecked()) {
					// Remember me option selected!
					// We store the login data
					ApplicationPreferences.rememberUser(this, name, password);
				}
				db.close();
				return true;
			}
		}
		return false;
	}

}

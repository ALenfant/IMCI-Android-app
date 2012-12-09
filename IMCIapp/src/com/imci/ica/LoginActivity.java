package com.imci.ica;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private Boolean user_remembered = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (!ApplicationPreferences.isCenterInitialized(this)) {
			// Center not initialized
			startActivity(new Intent(this, InitializationActivity.class));
		}

		if (ApplicationPreferences.loggedin_user != null) {
			// User already logged in
			// TODO : redirect to main activity
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		((Button) findViewById(R.id.button_login))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						login();
					}
				});
		
		//After everything is loaded, we check if there is a remembered user
		if (ApplicationPreferences.isUserRemembered(this)) {
			// The user checked "remember me"
			user_remembered = true;
			login(); //We login directly
		}
	}

	protected void login() {
		String name = "", password = "";
		if (!user_remembered) {
		name = ((EditText) findViewById(R.id.editText_fullname))
				.getText().toString();
		password = ((EditText) findViewById(R.id.editText_password))
				.getText().toString();
		} else {
			name = ApplicationPreferences.getRememberedUserName(this);
			password = ApplicationPreferences.getRememberedUserPassword(this);
		}

		if (name.length() == 0) {
			// Empty name
			Toast.makeText(this, R.string.error_empty_fullname,
					Toast.LENGTH_LONG).show();
			return;
		}

		if (password.length() == 0) {
			// Empty password
			Toast.makeText(this, R.string.error_empty_password,
					Toast.LENGTH_LONG).show();
			return;
		}

		Database db = new Database(this);
		if (!db.LoginUser(name, password)) {
			// User not found
			Toast.makeText(this, R.string.error_invalid_login,
					Toast.LENGTH_LONG).show();
			user_remembered = false; //If it was a "remember me" user, we disable it
			return;
		} else {
			if (ApplicationPreferences.loggedin_user != null) {
				Toast.makeText(this, ApplicationPreferences.loggedin_user.name,
						Toast.LENGTH_LONG).show();
				
				if (((CheckBox)findViewById(R.id.checkBox_remember)).isChecked()) {
					//Remember me option selected!
					ApplicationPreferences.rememberUser(this, name, password); //We store the login data
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

}

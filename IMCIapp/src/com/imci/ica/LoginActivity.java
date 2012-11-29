package com.imci.ica;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (!ApplicationPreferences.getInstance().isCenterInitialized(this)) {
			startActivity(new Intent(this, InitializationActivity.class));
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		((Button)findViewById(R.id.button_login)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				login();
			}
		});
	}
	
	protected void login() {
		String name = ((EditText)findViewById(R.id.editText_fullname)).getText().toString();
		String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();
		
		if (name.length() == 0) {
			Toast.makeText(this, R.string.error_empty_fullname, Toast.LENGTH_LONG).show();
			return;
		}
		
		if (password.length() == 0) {
			Toast.makeText(this, R.string.error_empty_password, Toast.LENGTH_LONG).show();
			return;
		}
		
		Database db = new Database(this);
		if (!db.LoginUser(name, password)) {
			Toast.makeText(this, R.string.error_invalid_login, Toast.LENGTH_LONG).show();
			return;
		} else {
			Toast.makeText(this, "Login OK!!!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

}

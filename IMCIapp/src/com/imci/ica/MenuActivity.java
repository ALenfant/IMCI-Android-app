package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.imci.ica.utils.ApplicationPreferences;

/**
 * Main Activity of the Application.
 * To login to the system and manage existing users
 * 
 * @author Miguel Navarro & Antonin Lenfant
 *
 */
public class MenuActivity extends Activity {

	/*
	 * COMMENTS: BY THE MOMENT WE DISPLAY A BUTTON FOR ANY FUNCTIONALITY, TO
	 * TEST THEM BY SEPARATE. AFTER IMPLEMENTATION, WE'LL CREATE THE RELATIONS
	 * BETWEEN THEM
	 */

	/**
	 * Set the view
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}


	/**
	 * Answer to Search Patient button click
	 * 
	 * @param view
	 */
	public void searchPatient(View view) {
		Intent intent = new Intent(this, SearchPatientActivity.class);
		startActivity(intent);
	}

	/**
	 * Go to Login Activity
	 * @param view
	 */
	public void login(View view) {
		Intent intent = new Intent(this, IMCIAppActivity.class);
		startActivity(intent);
	}

	/**
	 * Go to Manage Users Activity
	 * @param view
	 */
	public void manageUsers(View view) {
		Intent intent = new Intent(this, ManageUsersActivity.class);
		startActivity(intent);
	}
	
	//Debug button
	public void debugClassifications(View view) {
		Intent intent = new Intent(this, SignsClassificationActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Logout button
	 * 
	 * @param view
	 */
	public void logout(View view) {
		//We disable the "remember me" feature if it's activated...
		if (ApplicationPreferences.isUserRemembered(this)) {
			ApplicationPreferences.setUserRemembered(this, false);
		}
		
		//We log out the current user...
		ApplicationPreferences.loggedin_user = null;
		
		//And we go to the Login activity!
		Intent intent = new Intent(this, IMCIAppActivity.class);
		startActivity(intent);
		
		//Without forgetting to close this one
		finish();
	}
}

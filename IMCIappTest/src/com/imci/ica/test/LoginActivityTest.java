package com.imci.ica.test;

import com.imci.ica.ApplicationPreferences;
import com.imci.ica.Database;
import com.imci.ica.LoginActivity;
import com.imci.ica.R;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.EditText;

/**
 * Tests the LoginActivity class
 * 
 * @author Antonin
 * 
 */
public class LoginActivityTest extends
		ActivityInstrumentationTestCase2<LoginActivity> {
	private LoginActivity loginActivity;

	public LoginActivityTest() {
		super("com.imci.ica", LoginActivity.class); // Initialize the activity
	}

	@Override
	/**
	 * Sets everything up, for example, open a network connection.
	 * Runs before the tests
	 */
	protected void setUp() throws Exception {
		super.setUp();
		loginActivity = getActivity(); // We load the activity
		// We add a fake user
		Database db = new Database(loginActivity);
		db.addUser("testUser", "testPassword", true, 1, "1/1");
		db.close();
	}

	@UiThreadTest
	// Tells Android we're going to use the UI, avoids thread errors.
	/**
	 * Tests a correct login
	 */
	public void testCorrectLogin() {
		// We input the login data
		((EditText) loginActivity.findViewById(R.id.editText_fullname))
				.setText("testUser");
		((EditText) loginActivity.findViewById(R.id.editText_password))
				.setText("testPassword");
		
		// And check if the login is successful
		assertEquals(true, loginActivity.login());
		
		//We force-legout the user to avoid problems
		ApplicationPreferences.loggedin_user = null;
	}
	
	@UiThreadTest
	// Tells Android we're going to use the UI, avoids thread errors.
	/**
	 * Tests an incorrect login (wrong username and password)
	 */
	public void testIncorrectLoginData() {
		// We input the login data with the wrong username and password
		((EditText) loginActivity.findViewById(R.id.editText_fullname))
				.setText("_testUser");
		((EditText) loginActivity.findViewById(R.id.editText_password))
				.setText("_testPassword");
		
		// And check if the login is unsuccessful
		assertEquals(false, loginActivity.login());
	}
	
	@UiThreadTest
	// Tells Android we're going to use the UI, avoids thread errors.
	/**
	 * Tests an incorrect login (wrong username)
	 */
	public void testIncorrectLoginUsername() {
		// We input the login data with the wrong username
		((EditText) loginActivity.findViewById(R.id.editText_fullname))
				.setText("_testUser");
		((EditText) loginActivity.findViewById(R.id.editText_password))
				.setText("testPassword");
		
		// And check if the login is unsuccessful
		assertEquals(false, loginActivity.login());
	}
	
	@UiThreadTest
	// Tells Android we're going to use the UI, avoids thread errors.
	/**
	 * Tests an incorrect login (wrong password)
	 */
	public void testIncorrectLoginPassword() {
		// We input the login data with the wrong password
		((EditText) loginActivity.findViewById(R.id.editText_fullname))
				.setText("testUser");
		((EditText) loginActivity.findViewById(R.id.editText_password))
				.setText("_testPassword");
		
		// And check if the login is unsuccessful
		assertEquals(false, loginActivity.login());
	}

	@Override
	/**
	 * Finish everything, for example, close a network connection.
	 * Runs after the tests
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
package com.imci.ica.test;

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
	}

	@UiThreadTest
	// Tells Android we're going to use the UI, avoids thread errors.
	public void testCorrectLogin() {
		// We add a fake user
		Database db = new Database(getActivity());
		db.addUser("testUser", "testPassword", true, 1, "1/1");

		// We input its data
		((EditText) loginActivity.findViewById(R.id.editText_fullname))
				.setText("testUser");
		((EditText) loginActivity.findViewById(R.id.editText_password))
				.setText("testPassword");

		// And check if the login is successful
		assertEquals(true, loginActivity.login());
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

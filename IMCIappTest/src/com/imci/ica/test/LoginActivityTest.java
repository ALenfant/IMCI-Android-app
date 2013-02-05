package com.imci.ica.test;

import com.imci.ica.IMCIAppActivity;
import com.imci.ica.R;
import com.imci.ica.utils.ApplicationPreferences;
import com.imci.ica.utils.Database;

import android.content.Intent;
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
		ActivityInstrumentationTestCase2<IMCIAppActivity> {
	private IMCIAppActivity loginActivity;

	public LoginActivityTest() {
		super("com.imci.ica", IMCIAppActivity.class); // Initialize the activity
	}

	@Override
	/**
	 * Sets everything up, for example, open a network connection.
	 * Runs before the tests
	 */
	protected void setUp() throws Exception {
		super.setUp();
		// We create the activity with an intent
		// to force it to display the login
		// even if the center hasn't been initialized
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				IMCIAppActivity.class);
		intent.putExtra(IMCIAppActivity.EXTRA_DEBUG_DISABLE_INITIALIZATION, true);
		setActivityIntent(intent);
		loginActivity = getActivity(); // We load the activity

		// We add a fake user to the database
		Database db = new Database(loginActivity);
		db.addUser("testUser", "testPassword", true, 1);
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

		// We force-legout the user to avoid problems
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

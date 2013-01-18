package com.imci.ica.test;

import com.imci.ica.ApplicationPreferences;

import android.test.AndroidTestCase;

/**
 * Tests the ApplicationPreferences class
 * 
 * @author Antonin
 * 
 */
public class ApplicationPreferencesTest extends AndroidTestCase {
	@Override
	/**
	 * Sets everything up, for example, open a network connection.
	 * Runs before the tests
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testSetRememberMe() {
		// We do exactly as if we wanted to remember a user, then we chack if
		// everything works as expected
		ApplicationPreferences.rememberUser(getContext(), "testUser",
				"testPassword");
		assertEquals(true,
				(boolean) ApplicationPreferences.isUserRemembered(getContext()));
		assertEquals("testUser",
				ApplicationPreferences.getRememberedUserName(getContext()));
		assertEquals("testPassword",
				ApplicationPreferences.getRememberedUserPassword(getContext()));

		// We disable the setting to not confuse the application
		ApplicationPreferences.setUserRemembered(getContext(), false);
		assertEquals(false,
				(boolean) ApplicationPreferences.isUserRemembered(getContext()));
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

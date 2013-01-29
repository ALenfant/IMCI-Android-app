package com.imci.ica.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.imci.ica.R;
import com.imci.ica.ShowNewPatientActivity;

public class ShowNewPatientActivityTest extends
		ActivityInstrumentationTestCase2<ShowNewPatientActivity> {

	private ShowNewPatientActivity showNewPatientActivity;

	public ShowNewPatientActivityTest() {
		super("com.imci.ica", ShowNewPatientActivity.class); // Initialize the
																// activity
	}

	@Override
	/**
	 * Sets everything up, for example, open a network connection.
	 * Runs before the tests
	 */
	protected void setUp() throws Exception {
		super.setUp();
		showNewPatientActivity = getActivity();
	}

	@UiThreadTest
	// Tells Android we're going to use the UI, avoids thread errors.
	/**
	 * Tests a insert of a patient
	 */
	public void testInsertPatient() {

		// // We input the search data
		showNewPatientActivity.first_name = "One";
		showNewPatientActivity.last_name = "Patient";
		showNewPatientActivity.gender = "t";
		showNewPatientActivity.born_on = "2012-10-06";
		showNewPatientActivity.village_id = 20;
		showNewPatientActivity.zone_id = 18;

		assertEquals(true, showNewPatientActivity.confirmInfo(
				showNewPatientActivity.findViewById(R.id.buttonConfirm)));
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
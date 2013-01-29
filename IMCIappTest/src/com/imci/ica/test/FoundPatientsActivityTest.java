package com.imci.ica.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.imci.ica.FoundPatientsActivity;

public class FoundPatientsActivityTest extends
		ActivityInstrumentationTestCase2<FoundPatientsActivity> {

	private FoundPatientsActivity foundPatientActivity;

	public FoundPatientsActivityTest() {
		super("com.imci.ica", FoundPatientsActivity.class); // Initialize the activity
	}

	@Override
	/**
	 * Sets everything up, for example, open a network connection.
	 * Runs before the tests
	 */
	protected void setUp() throws Exception {
		super.setUp();
		foundPatientActivity = getActivity();

	}

	@UiThreadTest
	// Tells Android we're going to use the UI, avoids thread errors.
	/**
	 * Tests a patient exists in system
	 * 
	 * (First execution, patient doesn't exists, because is created
	 * in later test) 
	 */
	public void testSearchPatientExists() {
		// We input the search data
		foundPatientActivity.first_name = "";
		foundPatientActivity.last_name = "Patient";
		foundPatientActivity.gender = "";
		foundPatientActivity.born_on = "";
		foundPatientActivity.village_id = -1;

		assertEquals(true, foundPatientActivity.searchPatients() > 0);
	}

	@UiThreadTest
	// Tells Android we're going to use the UI, avoids thread errors.
	/**
	 * Tests a patient not exists in system
	 */
	public void testSearchPatientNotExists() {
		// We input the search data
		foundPatientActivity.first_name = "";
		foundPatientActivity.last_name = "Nopatient";
		foundPatientActivity.gender = "";
		foundPatientActivity.born_on = "";
		foundPatientActivity.village_id = -1;

		assertEquals(false, foundPatientActivity.searchPatients() > 0);
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

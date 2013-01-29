package com.imci.ica.test;

import java.util.HashMap;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.imci.ica.SignsClassificationActivity;

public class SignsClassificationActivityTest extends
		ActivityInstrumentationTestCase2<SignsClassificationActivity> {

	SignsClassificationActivity signsClassificationActivity;
	HashMap<String, Object> data;

	public SignsClassificationActivityTest() {
		super("com.imci.ica", SignsClassificationActivity.class);
	}

	@Override
	/**
	 * Sets everything up, for example, open a network connection.
	 * Runs before the tests
	 */
	protected void setUp() throws Exception {
		super.setUp();
		// We create the HashMap designed to trigger the diagnostic...
		int muac = 100;
		float weight = 4;
		float height = 56;
		float temp = 36;

		data = new HashMap<String, Object>();
		data.put("enfant.muac", muac);
		data.put("enfant.weight", weight);
		data.put("enfant.height", height);
		data.put("enfant.temperature", temp);

		data.put("danger.boire", false);
		data.put("danger.vomit", true);
		data.put("danger.convulsions_passe", false);
		data.put("danger.lethargie", true);
		data.put("danger.convulsions_present", false);

		// We create the activity with the intent
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				SignsClassificationActivity.class);
		intent.putExtra(SignsClassificationActivity.EXTRA_HASHMAP_DATA, data);
		intent.putExtra(SignsClassificationActivity.EXTRA_ID_PATIENT, 1);
		setActivityIntent(intent);
		signsClassificationActivity = getActivity();
	}

	@UiThreadTest
	/**
	 * Tests a basic diagnostic
	 */
	public void testDiagnostic() {

		// We check if we have a result (the diagnostic worked)
		assertEquals(true, signsClassificationActivity.checkResult());

	}
}

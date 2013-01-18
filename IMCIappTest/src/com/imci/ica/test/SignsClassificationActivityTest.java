package com.imci.ica.test;

import java.util.HashMap;
import com.imci.ica.SignsClassificationActivity;
import com.imci.ica.R;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ListView;

public class SignsClassificationActivityTest extends
		ActivityInstrumentationTestCase2<SignsClassificationActivity> {
	SignsClassificationActivity signsClassificationActivity;

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
	}

	@UiThreadTest
	/**
	 * Tests a basic diagnostic
	 */
	public void testDiagnostic() {
		// We create the HashMap designed to trigger the diagnostic...
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("danger.boire", false);
		data.put("danger.vomit", true);
		data.put("danger.convulsions_passe", false);
		data.put("danger.lethargie", true);
		data.put("danger.convulsions_present", false);

		// We create the activity with the intent
		//ERROR : data is always empty??? Check the activity
		Intent intent = new Intent();
		intent.putExtra(signsClassificationActivity.EXTRA_HASHMAP_DATA, data);
		intent.putExtra(signsClassificationActivity.EXTRA_ID_PATIENT, 1);
		setActivityIntent(intent);
		signsClassificationActivity = getActivity();

		// We check if we have a result
		assertEquals(true,
				((ListView) signsClassificationActivity
						.findViewById(R.id.listView_classifications))
						.getAdapter().getCount() > 0);

	}
}

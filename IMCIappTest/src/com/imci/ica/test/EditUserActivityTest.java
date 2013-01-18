package com.imci.ica.test;

import com.imci.ica.EditUserActivity;
import android.test.ActivityInstrumentationTestCase2;

public class EditUserActivityTest extends
		ActivityInstrumentationTestCase2<EditUserActivity> {
	EditUserActivity mActivity;

	public EditUserActivityTest() {
		super("com.imci.ica", EditUserActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
	}

	public void testTest() {
		//TODO : create real useful tests :°
		assertEquals(true, false);
	}
}

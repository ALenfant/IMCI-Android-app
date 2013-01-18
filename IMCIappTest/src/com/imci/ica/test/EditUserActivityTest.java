package com.imci.ica.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;

import com.imci.ica.EditUserActivity;
import com.imci.ica.R;

public class EditUserActivityTest extends
		ActivityInstrumentationTestCase2<EditUserActivity> {
	private EditUserActivity editUserActivity;

	public EditUserActivityTest() {
		super("com.imci.ica", EditUserActivity.class);
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
	 * Test the addition of a new application user
	 */
	public void testAddUser() {
		editUserActivity = getActivity();

		// We check if we are in Add User mode
		assertEquals(false, editUserActivity.getEditMode());

		// We change part of the data
		Log.d("Testing Debug", "UI thread");
		((EditText) editUserActivity.findViewById(R.id.editText_fullname))
				.setText("testUser_");
		((EditText) editUserActivity.findViewById(R.id.editText_password))
				.setText("testPassword_");
		((EditText) editUserActivity.findViewById(R.id.editText_password2))
				.setText("testPassword_");
		((RadioButton) editUserActivity.findViewById(R.id.radioButton_yes))
				.setChecked(false);
		((RadioButton) editUserActivity.findViewById(R.id.radioButton_no))
				.setChecked(true);

		// And then we save it!
		assertEquals(true, editUserActivity.editUser());

	}
}

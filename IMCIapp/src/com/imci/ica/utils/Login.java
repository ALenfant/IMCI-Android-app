package com.imci.ica.utils;

import android.content.Context;
import android.widget.Toast;

import com.imci.ica.R;

/**
 * Class to keep a login user in the system.
 * 
 * @author Miguel
 *
 */
public class Login {

	/**
	 * Register in preferences a logined user
	 * 
	 * @param context
	 * 				current activity
	 * @param name
	 * 				name of user
	 * @param password
	 * 				password of user
	 * @return true if process was successful
	 */
	public static boolean login(Context context, String name, String password, boolean checkBox) {
		
		
		Database db = new Database(context);
		if (!db.LoginUser(name, password)) {
			// User not found
			Toast.makeText(context, R.string.error_invalid_login,
					Toast.LENGTH_LONG).show();
						
			db.close();
			return false;
		} else {
			// If everything worked
			if (ApplicationPreferences.loggedin_user != null) {
				// User really logged in

				if (checkBox) {
					// Remember me option selected!
					// We store the login data
					ApplicationPreferences.rememberUser(context, name, password);
				}
				db.close();
				return true;
			}
		}
		return false;

	}
}

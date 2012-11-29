package com.imci.ica;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationPreferences {
	private static final String PREFERENCES_FILE = "Preferences";
	
	private static ApplicationPreferences instance = null;
	
	private ApplicationPreferences() {
	}

	public Boolean isCenterInitialized(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCES_FILE, 0);
		return preferences.getBoolean("center_initialized", false);
	}
	
	public int getCenterZoneId(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCES_FILE, 0);
		return preferences.getInt("center_zone_id", -1);
	}

	public void initializeCenter(Context context, int zone_id) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCES_FILE, 0);

		// We get the preferences editor
		SharedPreferences.Editor editor = preferences.edit();
		// We set the values
		editor.putBoolean("center_initialized", true);
		editor.putInt("center_zone_id", zone_id);
		// And then commit the changes
		editor.commit();
	}
	
	public static ApplicationPreferences getInstance() {
		if (instance == null)
			instance = new ApplicationPreferences();
		return instance;
	}
}

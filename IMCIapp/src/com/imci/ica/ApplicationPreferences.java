package com.imci.ica;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ApplicationPreferences {
	public static User loggedin_user = null;
	
	public static Boolean isCenterInitialized(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getBoolean("center_initialized", false);
	}
	
	public static int getCenterZoneId(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getInt("center_zone_id", -1);
	}

	public static void initializeCenter(Context context, int zone_id) {
		// We get the preferences editor
		SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
		editor.clear(); //We clear all the existing preferences
		// We set the values
		editor.putBoolean("center_initialized", true);
		editor.putInt("center_zone_id", zone_id);
		// And then commit the changes
		editor.commit();
	}
	
	public static void rememberUser(Context context, String name, String password) {
		// We get the preferences editor
		SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
		// We set the values
		editor.putBoolean("remembereduser", true);
		editor.putString("remembereduser_name", name);
		editor.putString("remembereduser_password", password);
		// And then commit the changes
		editor.commit();
	}
	
	public static Boolean isUserRemembered(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getBoolean("remembereduser", false);
	}
	
	public static String getRememberedUserName(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getString("remembereduser_name", "");
	}
	
	public static String getRememberedUserPassword(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getString("remembereduser_password", "");
	}
	
	// Private helper functions
	private static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	private static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
		return getSharedPreferences(context).edit();
	}
}

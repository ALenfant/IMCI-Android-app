package com.imci.ica.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Class managing the application's preferences
 * 
 * @author Antonin
 * 
 */
public class ApplicationPreferences {
	public static User loggedin_user = null; // Stores the logged in user, null
												// otherwise

	/**
	 * Check if the center is initialized
	 * 
	 * @param context
	 *            the current context
	 * @return if the center is initialized
	 */
	public static Boolean isCenterInitialized(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getBoolean("center_initialized", false);
	}

	/**
	 * Get the center zone's id
	 * 
	 * @param context
	 *            the current context
	 * @return the center's zone id
	 */
	public static int getCenterZoneId(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getInt("center_zone_id", -1);
	}

	/**
	 * Initializes a center for the first time
	 * 
	 * @param context
	 *            the current context
	 * @param zone_id
	 *            the center zone's id
	 */
	public static void initializeCenter(Context context, int zone_id) {
		// We get the preferences editor
		SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
		editor.clear(); // We clear all the existing preferences
		// We set the values
		editor.putBoolean("center_initialized", true);
		editor.putInt("center_zone_id", zone_id);
		// And then commit the changes
		editor.commit();
	}

	/**
	 * Remembers a user (used with the "remember me" checkbox)
	 * 
	 * @param context
	 *            the current context
	 * @param name
	 *            the user's name
	 * @param password
	 *            the user's password
	 */
	public static void rememberUser(Context context, String name,
			String password) {
		// We get the preferences editor
		SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
		// We set the values
		editor.putBoolean("remembereduser", true);
		editor.putString("remembereduser_name", name);
		editor.putString("remembereduser_password", password);
		// And then commit the changes
		editor.commit();
	}

	/**
	 * Check sif there is a remembered user
	 * 
	 * @param context
	 *            the current context
	 * @return if there is a remembered user
	 */
	public static Boolean isUserRemembered(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getBoolean("remembereduser", false);
	}

	/**
	 * Set if there is a remembered user (used to disable the "remember me"
	 * setting)
	 * 
	 * @param context
	 *            the current context
	 * @param userRemembered
	 *            the new value of the remembereduser parameter
	 */
	public static void setUserRemembered(Context context, Boolean userRemembered) {
		// We get the preferences editor
		SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
		// We set the value
		editor.putBoolean("remembereduser", userRemembered);
		// And then commit the changes
		editor.commit();
	}

	/**
	 * Get the remembered user's name
	 * 
	 * @param context
	 *            the current context
	 * @return the remembered user's name
	 */
	public static String getRememberedUserName(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getString("remembereduser_name", "");
	}

	/**
	 * Get the remembered user's password
	 * 
	 * @param context
	 *            the current context
	 * @return the remembered user's password
	 */
	public static String getRememberedUserPassword(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getString("remembereduser_password", "");
	}

	// Private helper functions
	/**
	 * Get the shared preferences
	 * 
	 * @param context
	 *            the current context
	 * @return the shared preferences for the application
	 */
	private static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * Get the shared preferences editor
	 * 
	 * @param context
	 *            the current context
	 * @return the shared preferences editor for the application
	 */
	private static SharedPreferences.Editor getSharedPreferencesEditor(
			Context context) {
		return getSharedPreferences(context).edit();
	}
}

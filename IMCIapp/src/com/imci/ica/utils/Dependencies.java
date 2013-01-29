package com.imci.ica.utils;

import java.util.ArrayList;
import java.util.HashMap;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;

/**
 * Class to manage dependencies 
 * 
 * @author Miguel
 *
 */
public class Dependencies {

	boolean enable;  // if enable or disable view
	int age_group;
	int illness_id;

	Context mContext;
	TableLayout tableQuestions;
	Cursor mCursor;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param age_group
	 * 				age group of patient
	 * @param illness_id
	 * 				id of current illness to diagnostic
	 * @param tableQuestions
	 * 				contains current layout of questions
	 * @param cursor
	 * 				all questions for this illness
	 */
	public Dependencies(Context context, int age_group, int illness_id,
			TableLayout tableQuestions, Cursor cursor) {
		this.mContext = context;
		this.age_group = age_group;
		this.illness_id = illness_id;
		this.tableQuestions = tableQuestions;
		this.mCursor = cursor;
	}

	/**
	 * Check dependency for Boolean question
	 * 
	 * @param key
	 * 			key of question answered
	 * @param value
	 * 			value caught of question
	 */
	public void dependencyBoolean(String key, Object value) {

		HashMap<String, Object> oneEquation = new HashMap<String, Object>();
		String jsDataVar = "";
		ArrayList<Integer> results = null;

		// Particular case for fever illness
		if (key.equals("fievre.fievre_presence") && !((Boolean) value)) {
			enable = false;
			results = new ArrayList<Integer>();
			for (int i = 19; i <= 31; i++) {
				results.add(i);
			}

		// Particular case for ear illness
		} else if (key.equals("oreille.oreille_ecoulement")) {
			if ((Boolean) value) {
				ViewGroup layout = (ViewGroup) tableQuestions
						.findViewWithTag("oreille.oreille_ecoulement_duree");
				enableDisableInteger(true, layout);
			} else {
				ViewGroup layout = (ViewGroup) tableQuestions
						.findViewWithTag("oreille.oreille_ecoulement_duree");
				enableDisableInteger(false, layout);
			}
			return;
			
		// Default case
		} else {
			enable = (Boolean) value;
			oneEquation.put(key, true);
			jsDataVar = JSUtils.hashMapToJavascript(oneEquation, "data");

			// Do evaluation of question
			results = JSUtils.evaluation(age_group, jsDataVar, mCursor);

			if (key.equals("oreille.oreille_probleme") && !((Boolean) value)) {
				results.add(35);
			}

		}

		readResults(results);
	}

	/**
	 * Check dependency for Integer question
	 * 
	 * @param value
	 * 			value introduced in EditText.
	 */
	public void dependencyInteger(Object value) {
		// Just illness with id = 4 has Integer dependency.
		if (illness_id == 4) {
			if ((Integer) value < 7) {
				ViewGroup layout = (ViewGroup) tableQuestions
						.findViewWithTag("fievre.fievre_presence_longue");
				enableDisableBoolean(false, layout);
			} else {
				ViewGroup layout = (ViewGroup) tableQuestions
						.findViewWithTag("fievre.fievre_presence_longue");
				enableDisableBoolean(true, layout);
			}
		}
	}

	/**
	 * Check dependency for List question
	 * 
	 * @param value
	 * @param map
	 */
	public void dependencyList(Object value, HashMap<String, Object> map) {
		// Just illness with id = 4 has List dependency.
		if (illness_id == 4) {
			ArrayList<Integer> results = new ArrayList<Integer>();

			if (((String) value).equals("aucun")) {
				enable = false;
				results.add(29);
				results.add(30);
				results.add(31);
			} else {
				enable = true;
				String jsDataVar = JSUtils.hashMapToJavascript(map, "data");

				// Do evaluation of question
				results = JSUtils.evaluation(age_group, jsDataVar, mCursor);

			}

			readResults(results);
		}

	}

	/**
	 * Read result ids of questions and set them enabled or disabled
	 * 
	 * @param results
	 * 				contains ids of questions
	 */
	public void readResults(ArrayList<Integer> results) {
		Database db = new Database(mContext);
		String illness_key = db.getIllnessKey(illness_id);

		for (int i = 0; i < results.size(); i++) {
			Cursor question = db.getQuestionById(results.get(i));
			String tag = illness_key + "."
					+ question.getString(question.getColumnIndex("key"));
			String typeToED = question.getString(question
					.getColumnIndex("type"));
			ViewGroup layout = (ViewGroup) tableQuestions.findViewWithTag(tag);

			if (typeToED.equals("BooleanSign")) {
				enableDisableBoolean(enable, layout);

			} else if (typeToED.equals("IntegerSign")) {
				enableDisableInteger(enable, layout);
			} else {
				enableDisableList(enable, layout);
			}
		}

		db.close();

	}
	
	/**
	 * Particular case for malnutrition illness
	 * 
	 * @param map
	 * 			map with patient data
	 */
	public void dependencyMalnutrition(HashMap<String, Object> map) {
		enable = true;
		
		String jsDataVar = JSUtils.hashMapToJavascript(map, "data");

		// Do evaluation of question
		ArrayList<Integer> results = JSUtils.evaluation(age_group, jsDataVar, mCursor);
		
		readResults(results);

	}

	/**
	 * Enable or Disable a layout of boolean question
	 * 
	 * @param enable
	 *            true if enable, false if disable
	 * @param group
	 *            view group contains element to E/D
	 */
	public void enableDisableBoolean(boolean enable, ViewGroup group) {
		ViewGroup radioGroup = (ViewGroup) group.getChildAt(1);
		RadioButton buttonYes = (RadioButton) radioGroup.getChildAt(0);
		RadioButton buttonNo = (RadioButton) radioGroup.getChildAt(1);

		if (enable) {
			group.setBackgroundColor(Color.WHITE);
			buttonYes.setEnabled(true);
			buttonNo.setEnabled(true);
		} else {
			group.setBackgroundColor(Color.GRAY);
			buttonYes.setChecked(false);
			buttonNo.setChecked(false);

			buttonYes.setEnabled(false);
			buttonNo.setEnabled(false);

		}
	}

	/**
	 * Enable or Disable a layout of integer question
	 * 
	 * @param enable
	 *            true if enable, false if disable
	 * @param group
	 *            view group contains element to E/D
	 */
	public void enableDisableInteger(boolean enable, ViewGroup group) {
		EditText editText = (EditText) group.getChildAt(1);

		if (enable) {
			group.setBackgroundColor(Color.WHITE);
			editText.setEnabled(true);
		} else {
			group.setBackgroundColor(Color.GRAY);
			editText.setText("");
			editText.setEnabled(false);
		}
	}

	/**
	 * Enable or Disable a layout of list question
	 * 
	 * @param enable
	 *            true if enable, false if disable
	 * @param group
	 *            view group contains element to E/D
	 */
	public void enableDisableList(boolean enable, ViewGroup group) {
		Spinner spinner = (Spinner) group.getChildAt(1);

		if (enable) {
			group.setBackgroundColor(Color.WHITE);
			spinner.setEnabled(true);
		} else {
			group.setBackgroundColor(Color.GRAY);
			spinner.setSelection(0);
			spinner.setEnabled(false);
		}
	}

}

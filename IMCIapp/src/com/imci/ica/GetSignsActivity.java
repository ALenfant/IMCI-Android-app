package com.imci.ica;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imci.ica.utils.CursorQuestionsAdapter;
import com.imci.ica.utils.Database;
import com.imci.ica.utils.DateUtils;
import com.imci.ica.utils.Dependencies;

/**
 * Class to take data to do a diagnostic Questions are shown in screen by
 * illness group of questions.
 * 
 * @author Miguel
 * 
 */
public class GetSignsActivity extends MyActivity {

	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";
	public final static String EXTRA_AGE_GROUP = "com.imci.ica.AGE_GROUP";
	public final static String EXTRA_HASHMAP = "com.imci.ica.HASHMAP";

	private final int TYPE_BOOLEAN = 1;
	private final int TYPE_INTEGER = 2;

	int id_patient, age_group;
	String born_on;
	int illness_id;
	int index;
	boolean end = false;
	String prevDep = null;
	String illness_key;

	Cursor illnesses, questions;
	CursorQuestionsAdapter mAdapter;
	Cursor mCursor;
	TableLayout tableQuestions;

	HashMap<String, Integer> types; // Map containing types of answer of
									// questions
	HashMap<String, Object> answersAllQuestions; // Map for all questions
													// answered correctly
	HashMap<String, Object> answersOneIllness; // Aux Map to answer of one
												// illness

	/**
	 * Show interface and initialize all variables
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_signs);

		ScrollView view = (ScrollView) findViewById(R.id.scrollViewQuestions);
		view.setVisibility(View.GONE);

		Intent mIntent = getIntent();
		id_patient = mIntent.getIntExtra(EXTRA_ID_PATIENT, 0);
		age_group = mIntent.getIntExtra(EXTRA_AGE_GROUP, 0);

		Database db = new Database(this);
		illnesses = db.getIllnesses();
		questions = db.getQuestions(age_group);
		startManagingCursor(illnesses);
		startManagingCursor(questions);

		mAdapter = new CursorQuestionsAdapter(this, questions);

		index = 0;

		types = new HashMap<String, Integer>();
		answersAllQuestions = new HashMap<String, Object>();
		answersOneIllness = new HashMap<String, Object>();

		tableQuestions = null;
	}

	/**
	 * Check dependencies of answers.
	 * 
	 * @param key
	 *            the key of question
	 * @param value
	 *            the value inserted
	 */
	@Override
	public void checkDependencies(String key, Object value) {

		String[] keyParts = key.split("\\.");

		Database db = new Database(this);
		Cursor questionForIllness = db.getQuestionsByIllnessId(age_group,
				illness_id);

		Dependencies dep = new Dependencies(this, age_group, illness_id, tableQuestions, questionForIllness);

		if(keyParts[1].equals("malnutrition")) {
			dep.dependencyMalnutrition(answersAllQuestions);
			return;
		}
		
		Cursor thisQuestion = db.getQuestionByKey(keyParts[1]);
		String typeCurrent = thisQuestion.getString(thisQuestion
				.getColumnIndex("type"));

		answersOneIllness.put(key, value);

		if (typeCurrent.equals("BooleanSign")) {
			dep.dependencyBoolean(key, value);
			
		} else if (typeCurrent.equals("IntegerSign")) {
			dep.dependencyInteger(value);
			
		} else {
			dep.dependencyList(value, answersOneIllness);
		}
	}

	/**
	 * Answer to Next button. Save answers in current screen if all them are
	 * answered
	 * 
	 * @param view
	 */
	public void saveAnswers(View view) {

		View mView;
		String key;
		int type;

		// If is the first entry, hide measures layout
		if (index == 0) {
			if (!takeMeasures()) {
				answersOneIllness.clear();
				return;
			}
			ScrollView scrollMeasures = (ScrollView) findViewById(R.id.scrollViewMeasures);
			scrollMeasures.setVisibility(View.GONE);

			ScrollView scrollQuestions = (ScrollView) findViewById(R.id.scrollViewQuestions);
			scrollQuestions.setVisibility(View.VISIBLE);

		} else {
			Iterator<Entry<String, Integer>> iterator = types.entrySet()
					.iterator();

			while (iterator.hasNext()) {
				Entry<String, Integer> entry = iterator.next();
				key = entry.getKey();
				type = entry.getValue();

				mView = (View) tableQuestions.findViewWithTag(key);

				switch (type) {
				case TYPE_BOOLEAN: {
					if (!checkAnswersBoolean(mView, key)) {
						answersOneIllness.clear();
						return;
					}
				}
					break;
				case TYPE_INTEGER: {
					if (!checkAnswersInteger(mView, key)) {
						answersOneIllness.clear();
						return;
					}
				}
					break;
				default:
					checkAnswersList(mView, key);
					break;
				}

			}

			tableQuestions.setVisibility(View.GONE);
		}

		registerAnswersOneIllness(answersOneIllness);

		types.clear();
		answersOneIllness.clear();

		// If it isn't the end, show next questions
		if (!end) {
			showNextIllness();
		} else {
			// Finishing previous Activity
			Intent prevIntent = getIntent();
			prevIntent.putExtra(InfoPatientActivity.EXTRA_FINISH_ACTIVITY, true);
			setResult(Activity.RESULT_OK, prevIntent);

			// Go to next activity
			Intent intent = new Intent(this, SignsClassificationActivity.class);
			intent.putExtra(SignsClassificationActivity.EXTRA_HASHMAP_DATA,
					answersAllQuestions); // The answers
			intent.putExtra(SignsClassificationActivity.EXTRA_ID_PATIENT,
					id_patient); // The patient's id
			startActivity(intent);
			finish();

		}
	}

	/**
	 * Show new illnesses group of questions
	 */
	public void showNextIllness() {
		LinearLayout mLayout = (LinearLayout) findViewById(R.id.layoutQuestions);

		mCursor = mAdapter.getCursor();

		illness_id = Integer.parseInt(mAdapter.getIllnessId(mCursor));

		Database db = new Database(this);
		illness_key = db.getIllnessKey(illness_id);
		String illness_name = db.getIllnessName(illness_id);
		db.close();
		
		((TextView) findViewById(R.id.textTitle)).setText(illness_name);
		
		tableQuestions = new TableLayout(GetSignsActivity.this);
		mLayout.addView(tableQuestions);

		String key;

		while (illness_id == Integer.parseInt(mAdapter.getIllnessId(mCursor))
				&& !end) {
			View view = mAdapter.getView(index, null, tableQuestions);

			key = mAdapter.createKey(illness_key);
			view.setTag(key);

			types.put(key, mAdapter.getTypeQuestion(mCursor));

			tableQuestions.addView(view);
			if (!mAdapter.isLast()) {
				mAdapter.moveToNext();
				mCursor = mAdapter.getCursor();
				index++;
			} else {
				end = true;
			}
		}
		
		if(illness_id == 7) {
			checkDependencies("malnutrition.malnutrition", null);
		}
	}

	/**
	 * Get answers from a question of boolean type
	 * 
	 * @param view
	 *            the layout of the question
	 * @param key
	 *            the key of the question
	 * @return true if lecture was successful or element is disable
	 */
	public boolean checkAnswersBoolean(View view, String key) {
		RadioButton yesButton = (RadioButton) view.findViewById(R.id.buttonYes);
		RadioButton noButton = (RadioButton) view.findViewById(R.id.buttonNo);

		if (yesButton.isEnabled()) {
			if (!yesButton.isChecked() && !noButton.isChecked()) {
				Toast.makeText(this, R.string.allQuestionsMarked,
						Toast.LENGTH_LONG).show();
				return false;
			} else {
				// if (yesButton.isChecked()) {
				// answersOneIllness.put(key, true);
				// } else if (noButton.isChecked()) {
				// answersOneIllness.put(key, false);
				// }
				return true;
			}
		} else {
			answersAllQuestions.remove(key);
			return true;
		}
	}

	/**
	 * Get answers from a question of integer type
	 * 
	 * @param view
	 *            the layout of the question
	 * @param key
	 *            the key of the question
	 * @return true if lecture was successful or element is disable
	 */
	public boolean checkAnswersInteger(View view, String key) {
		EditText entry = (EditText) view.findViewById(R.id.editValue);
		String number = entry.getText().toString();

		if (entry.isEnabled()) {
			if (number.length() == 0) {
				Toast.makeText(this, R.string.allQuestionsMarked,
						Toast.LENGTH_LONG).show();
				return false;
			} else {
				// answersOneIllness.put(key, number);
				return true;
			}
		} else {
			answersAllQuestions.remove(key);
			return true;
		}
	}

	/**
	 * Get answers from a question of list type
	 * 
	 * @param view
	 *            the layout of the question
	 * @param key
	 *            the key of the question
	 * @return true if lecture was successful or element is disable
	 */
	public boolean checkAnswersList(View view, String key) {
		Spinner list = (Spinner) view.findViewById(R.id.spinner);
		String election = list.getSelectedItem().toString();

		if (list.isEnabled() && !answersOneIllness.containsKey(key)) {
			answersOneIllness.put(key, election);
		} else {
			answersAllQuestions.remove(key);
		}
		return true;
	}

	/**
	 * Register answers of a illness group of questions, in final result hashmap
	 * 
	 * @param answersOneIllness2
	 */
	public void registerAnswersOneIllness(
			HashMap<String, Object> answersOneIllness2) {
		String key;
		Object value;
		Iterator<Entry<String, Object>> iterator = answersOneIllness2
				.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, Object> e = iterator.next();
			key = e.getKey();
			value = e.getValue();

			answersAllQuestions.put(key, value);
		}
	}

	/**
	 * Get the measures in the first group of questions
	 * 
	 * @return true if process was successful
	 */
	@SuppressLint("SimpleDateFormat")
	public boolean takeMeasures() {
		EditText editHeight = (EditText) findViewById(R.id.editHeight);
		String heightStr = editHeight.getText().toString();
		EditText editWeight = (EditText) findViewById(R.id.editWeight);
		String weightStr = editWeight.getText().toString();
		EditText editTemp = (EditText) findViewById(R.id.editTemp);
		String tempStr = editTemp.getText().toString();
		EditText editMuac = (EditText) findViewById(R.id.editMuac);
		String muacStr = editMuac.getText().toString();

		if (heightStr.length() == 0 || weightStr.length() == 0 || tempStr.length() == 0
				|| muacStr.length() == 0) {
			Toast.makeText(this, R.string.allQuestionsMarked, Toast.LENGTH_LONG)
					.show();
			return false;
		} else {

			Database db = new Database(this);
			Cursor patient = db.getPatientById(id_patient);
			Date birthdate;
			try {
				birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(patient
						.getString(patient.getColumnIndex("born_on")));
			} catch (Exception ex) {
				ex.printStackTrace();
				birthdate = new Date();
			}

			Integer months = DateUtils.getMonthsDifference(birthdate,
					new Date());

			int muac;
			float temp;
			float weight;
			float height;
			float wfh;
			float wfa;
			
			try {
				muac = Integer.parseInt(muacStr);
				temp = Float.parseFloat(tempStr);
				
				weight = Float.parseFloat(weightStr);
				height = Float.parseFloat(heightStr);
				
				wfh = weight / height;
				wfa = weight / months;
				
				answersOneIllness.put("enfant.months", months);
				answersOneIllness.put("enfant.height", height);
				answersOneIllness.put("enfant.weight", weight);
				answersOneIllness.put("enfant.temperature", temp);
				answersOneIllness.put("enfant.muac", muac);
				answersOneIllness.put("enfant.wfh", wfh);
				answersOneIllness.put("enfant.wfa", wfa);
				return true;

			} catch (NumberFormatException e) {
				Toast.makeText(this, R.string.invalidNumbers, Toast.LENGTH_LONG)
				.show();
				e.printStackTrace();
				return false;
			}

		}

	}
	
	/**
	 * Answer to back key pressing
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = getIntent();
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

package com.imci.ica;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;

public class GetSignsActivity extends Activity {

	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";
	public final static String EXTRA_AGE_GROUP = "com.imci.ica.AGE_GROUP";

	private final int TYPE_BOOLEAN = 1;
	private final int TYPE_INTEGER = 2;
//	private final int TYPE_LIST = 3;

	int id_patient, age_group;
	int illness_id, count;
	int index;
	boolean end = false;

	Cursor illnesses, questions;
	CursorQuestionsAdapter mAdapter;
	TableLayout tableQuestions;

	HashMap<String, String> types;
	// HashMap<String, View> views;
	HashMap<String, String> answers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_signs);

		Intent mIntent = getIntent();
		id_patient = mIntent.getIntExtra(EXTRA_ID_PATIENT, 0);
		age_group = mIntent.getIntExtra(EXTRA_AGE_GROUP, 0);

		Database db = new Database(this);
		illnesses = db.getIllnesses();
		questions = db.getQuestions(age_group);
		startManagingCursor(illnesses);
		startManagingCursor(questions);

		mAdapter = new CursorQuestionsAdapter(this, questions);
		count = mAdapter.getCount();

		index = 0;

		types = new HashMap<String, String>();
		// views = new HashMap<String, View>();
		answers = new HashMap<String, String>();

		tableQuestions = null;

		showNextIllness();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_get_signs, menu);
		return true;
	}

	public void showNextIllness() {
		String key;

		LinearLayout mLayout = (LinearLayout) findViewById(R.id.layoutQuestions);

		illness_id = Integer.parseInt(mAdapter.getIllnessId(mAdapter
				.getCursor()));

		Database db = new Database(this);
		String illness_key = db.getIllnessKey(illness_id);

		tableQuestions = new TableLayout(GetSignsActivity.this);
		mLayout.addView(tableQuestions);

		while (illness_id == Integer.parseInt(mAdapter.getIllnessId(mAdapter
				.getCursor())) && !end) {
			View view = mAdapter.getView(index, null, tableQuestions);

			key = mAdapter.createKey(illness_key);
			view.setTag(key);

			// views.put(key, view);
			types.put(key, mAdapter.getType(mAdapter.getCursor()));

			tableQuestions.addView(view);
			if (!mAdapter.isLast()) {
				mAdapter.moveToNext();
				index++;
			} else {
				end = true;
			}
		}

	}

	public void saveAnswers(View view) {
		if (!end) {
			View mView;
			String key;
			String type;

			// Iterator<View> itViews = views.values().iterator();
			Iterator<Entry<String, String>> iterator = types.entrySet().iterator();
//			Iterator<String> itTypes = types.values().iterator();

			// while (itViews.hasNext() && itTypes.hasNext()) {
			// mView = (View) itViews.next();
			while (iterator.hasNext()){ //&& itTypes.hasNext()) {

//				key = itKeys.next();
				Entry<String,String> e = iterator.next();
				key = e.getKey();
				type = e.getValue();//(String) itTypes.next();
				Log.w(key, type);
				
				mView = (View) tableQuestions.findViewWithTag(key);
				switch (mAdapter.getTypeQuestion(type)) {
				case TYPE_BOOLEAN:
					takeAnswersBoolean(mView, key);
					break;
				case TYPE_INTEGER:
					takeAnswersInteger(mView, key);
					break;
				default:
					takeAnswersList(mView, key);
					break;
				}
				
			}
			
			types.clear();
			// Hiding previous illnesses questions
			tableQuestions.setVisibility(View.GONE);

			showNextIllness();
		} else {
			//Only for check result of data entry
			Iterator<String> itKey = answers.keySet().iterator();
			String key;
			String value;
			while(itKey.hasNext()){
				key = itKey.next();
				value = answers.get(key);
				
				Log.w(key, value);
			}

		}
	}

	public void takeAnswersBoolean(View view, String key) {
		RadioButton yesButton = (RadioButton) view.findViewById(R.id.buttonYes);
		RadioButton noButton = (RadioButton) view.findViewById(R.id.buttonNo);
		String value="nada";
		if (yesButton.isChecked()) {
			answers.put(key, "true");
			value = "true";
		} else if (noButton.isChecked()) {
			answers.put(key, "false");
			value = "false";
		}
		Log.w(key, value);

	}

	public void takeAnswersInteger(View view, String key) {
		EditText entry = (EditText) view.findViewById(R.id.editValue);
		String number = entry.getText().toString();

		answers.put(key, number);
	}

	public void takeAnswersList(View view, String key) {
		Spinner list = (Spinner) view.findViewById(R.id.spinner);
		String election = list.getSelectedItem().toString();

		answers.put(key, election);
	}
}

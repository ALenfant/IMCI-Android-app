package com.imci.ica;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class responsible for the Questions activity Not used any more, was for a
 * test at the start of the development. Replaced by GetSignsActivity
 * 
 * @author Antonin
 * 
 */
public class QuestionsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);

		Database db = new Database(this);
		Cursor cursor = db.getSigns();

		// call the main layout from xml
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.layoutIllnessContainer);

		String last_illness = new String("");
		LinearLayout illnessQuestionsLayout = null;
		if (cursor.moveToFirst()) { // If there's any data
			while (!cursor.isAfterLast()) {
				String illness = cursor
						.getString(cursor.getColumnIndex("name"));
				String question = cursor.getString(cursor
						.getColumnIndex("question"));
				String type = cursor.getString(cursor.getColumnIndex("type"));
				String values = cursor.getString(cursor
						.getColumnIndex("values"));
				System.out.println("ILLNESS:" + illness);
				System.out.println("QUESTION:" + question);

				if (!last_illness.equals(illness)) {
					// New illness
					last_illness = illness;

					LinearLayout.LayoutParams layoutParams = null;
					if (illnessQuestionsLayout != null) {
						layoutParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.FILL_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);

						layoutParams.setMargins(0, 8, 0, 0);
					}

					// we create another container
					View view_illness = getLayoutInflater().inflate(
							R.layout.layout_illness, mainLayout, false);

					((TextView) view_illness.findViewById(R.id.textIllness))
							.setText(Html.fromHtml(illness));

					// add the view to the main layout
					if (layoutParams == null)
						mainLayout.addView(view_illness);
					else
						mainLayout.addView(view_illness, layoutParams);

					// Keep the layout
					illnessQuestionsLayout = (LinearLayout) view_illness
							.findViewById(R.id.layoutIllnessQuestionsContainer);
				}

				// Depending on the type, we create the new questions
				View view_question = null;
				if (type.equals("BooleanSign")) {
					view_question = getLayoutInflater().inflate(
							R.layout.layout_illness_question_boolean,
							illnessQuestionsLayout, false);
				} else if (type.equals("IntegerSign")) {
					view_question = getLayoutInflater().inflate(
							R.layout.layout_illness_question_integer,
							illnessQuestionsLayout, false);
				} else if (type.equals("ListSign")) {
					view_question = getLayoutInflater().inflate(
							R.layout.layout_illness_question_list,
							illnessQuestionsLayout, false);

					Spinner spinner = (Spinner) view_question
							.findViewById(R.id.spinner);
					ArrayList<String> value = new ArrayList<String>(
							Arrays.asList(values.split(";")));

					// Create an ArrayAdapter using the string array and a
					// default spinner layout
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							this, android.R.layout.simple_spinner_item, value);
					// Specify the layout to use when the list of choices
					// appears
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					// Apply the adapter to the spinner
					spinner.setAdapter(adapter);

					/*
					 * for(String val : value) { list. }
					 */
				}

				TextView textQuestion = (TextView) view_question
						.findViewById(R.id.textQuestion);
				textQuestion.setText(Html.fromHtml(question));

				// add the view to the main layout
				illnessQuestionsLayout.addView(view_question);

				cursor.moveToNext();
			}
			// System.out.println(cursor.getString(cursor.getColumnIndex("title"));
			for (String name : cursor.getColumnNames())
				System.out.println(name);
		}
		cursor.close(); // Important to avoid memory leaks
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void MessageBox(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}

class CustomExpandable extends BaseExpandableListAdapter {
	private ArrayList<String> mItems;

	public CustomExpandable(Context context, ArrayList<String> items) {
		mItems = items;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return mItems.get(groupPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getChildrenCount(int groupPosition) {
		return 0;
	}

	public Object getGroup(int groupPosition) {
		return mItems.get(groupPosition);
	}

	public int getGroupCount() {
		return mItems.size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
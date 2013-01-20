package com.imci.ica;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Custom cursor adapter to inflate questions in display
 * @author Miguel Navarro & Antonin Lenfant
 *
 */
public class CursorQuestionsAdapter extends CursorAdapter {

	private final int TYPE_BOOLEAN = 1;
	private final int TYPE_INTEGER = 2;
	private final int TYPE_LIST = 3;

	private Cursor mCursor;
	private Context mContext;
	private final LayoutInflater mInflater;

	/**
	 * Constructor that fix current context and cursor with info
	 * and create a new inflater
	 * @param context
	 * @param c
	 */
	public CursorQuestionsAdapter(Context context, Cursor c) {
		super(context, c);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mCursor = c;
	}

	/**
	 * Fix all data in view fields
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		switch (getTypeQuestion(cursor)) {
		case TYPE_BOOLEAN:
			putBooleanLayout(view, cursor);
			break;
		case TYPE_INTEGER:
			putIntegerLayout(view, cursor);
			break;
		default:
			putListLayout(view, context, cursor);
			break;
		}
	}
	
	/**
	 * Create a new view with layout for show questions
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view;

		switch (getTypeQuestion(cursor)) {
		case TYPE_BOOLEAN:
			view = mInflater.inflate(R.layout.layout_illness_question_boolean,
					parent, false);
			break;
		case TYPE_INTEGER:
			view = mInflater.inflate(R.layout.layout_illness_question_integer,
					parent, false);
			break;
		// default option is for case TYPE_LIST
		default:
			view = mInflater.inflate(R.layout.layout_illness_question_list,
					parent, false);
			break;
		}
		return view;
	}

	/**
	 * Get info of a question in cursor position
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (!mCursor.moveToPosition(position)) {
			throw new IllegalStateException("couldn't move cursor to position "
					+ position);
		}
		View v;
		if (convertView == null) {
			v = newView(mContext, mCursor, parent);
		} else {
			v = convertView;
		}
		bindView(v, mContext, mCursor);
		return v;
	}

	@Override
	public Cursor getCursor() {
		return mCursor;
	}

	/**
	 *  
	 * @param question
	 * @return the type of question since a cursor
	 */
	public int getTypeQuestion(Cursor question) {
		String type = question.getString(question.getColumnIndex("type"));
		int option = getTypeQuestion(type);

		return option;
	}
	
	/**
	 * 
	 * @param type
	 * @return the type of question since a String
	 */
	public int getTypeQuestion(String type) {
		int option;
		if (type.equals("BooleanSign"))
			option = TYPE_BOOLEAN;
		else if (type.equals("IntegerSign"))
			option = TYPE_INTEGER;
		else
			option = TYPE_LIST;

		return option;
	}
	
	/**
	 * Fix layout for boolean question
	 * @param view
	 * @param cursor
	 */
	public void putBooleanLayout(View view, Cursor cursor) {
		ViewHolderBoolean holder = new ViewHolderBoolean();
		holder.textQuestion = (TextView) view.findViewById(R.id.textQuestion);
		holder.buttonYes = (RadioButton) view.findViewById(R.id.buttonYes);
		holder.buttonNo = (RadioButton) view.findViewById(R.id.buttonNo);
		
		holder.textQuestion.setText(cursor.getString(cursor.getColumnIndex("question")));
		holder.buttonYes.setText(R.string.yes);
		holder.buttonNo.setText(R.string.no);
	}

	/**
	 * Fix layout for integer question
	 * @param view
	 * @param cursor
	 */
	public void putIntegerLayout(View view, Cursor cursor) {
		ViewHolderInteger holder = new ViewHolderInteger();
		holder.textQuestion = (TextView) view.findViewById(R.id.textQuestion);
//		holder.editValue = (EditText) view.findViewById(R.id.editValue);
		
		holder.textQuestion.setText(cursor.getString(cursor.getColumnIndex("question")));
	}
	
	/**
	 * Fix layout for list question
	 * @param view
	 * @param cursor
	 */
	public void putListLayout(View view, Context context, Cursor cursor) {
		ViewHolderList holder = new ViewHolderList();
		holder.textQuestion = (TextView) view.findViewById(R.id.textQuestion);
		holder.spinner = (Spinner) view.findViewById(R.id.spinner);
		
		holder.textQuestion.setText(cursor.getString(cursor.getColumnIndex("question")));
		
		String values = cursor.getString(cursor.getColumnIndex("values"));
		String[] valuesArray = values.split("\\;");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, valuesArray);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		holder.spinner.setAdapter(adapter);
	}
	
	/**
	 * 
	 * @param cursor
	 * @return the id of illness associed to question
	 */
	public String getIllnessId(Cursor cursor){
		String id = cursor.getString(cursor.getColumnIndex("illness_id"));
		return id;
	}
	
	/**
	 * 
	 * @param cursor
	 * @return the type of question
	 */
	public String getType(Cursor cursor){
		String type = cursor.getString(cursor.getColumnIndex("type"));
		return type;
	}
	
	/**
	 * 
	 * @return true if it's the last position of cursor
	 */
	public boolean isLast() {
		return (mCursor.isLast());
	}
	
	/**
	 * 
	 * @return true if there is more illnesses in cursor
	 */
	public boolean moveToNext() {
		return (mCursor.moveToNext());
	}
	
	/**
	 * Create a key to compare with equation of diagnostics in database
	 * @param illness_key
	 * @return the created key
	 */
	public String createKey(String illness_key) {
		String key = illness_key + "." + mCursor.getString(mCursor.getColumnIndex("key"));
		return key;
	}
	
	/**
	 * Include all fields in a layout to show a  boolean question
	 * @author Miguel
	 *
	 */
	static class ViewHolderBoolean {
		private TextView textQuestion;
		private RadioButton buttonYes;
		private RadioButton buttonNo;
	}

	/**
	 * Include all fields in a layout to show a  integer question
	 * @author Miguel
	 *
	 */
	static class ViewHolderInteger {
		private TextView textQuestion;
	}

	/**
	 * Include all fields in a layout to show a  list question
	 * @author Miguel
	 *
	 */
	static class ViewHolderList {
		private TextView textQuestion;
		private Spinner spinner;
	}
}

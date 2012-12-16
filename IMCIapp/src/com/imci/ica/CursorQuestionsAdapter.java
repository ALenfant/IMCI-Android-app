package com.imci.ica;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class CursorQuestionsAdapter extends CursorAdapter {

//	private final int POS_TYPE = 2;
//	private final int POS_QUESTION = 4;
//	private final int POS_VALUES = 5;
//	private final int POS_TYPE = 2;

	private final int TYPE_BOOLEAN = 1;
	private final int TYPE_INTEGER = 2;
	private final int TYPE_LIST = 3;

	private Cursor mCursor;
	private Context mContext;
	private final LayoutInflater mInflater;

	public CursorQuestionsAdapter(Context context, Cursor c) {
		super(context, c);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mCursor = c;
	}

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

	public int getTypeQuestion(Cursor question) {
		String type = question.getString(question.getColumnIndex("type"));
		int option = getTypeQuestion(type);

		return option;
	}
	
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
	
	public void putBooleanLayout(View view, Cursor cursor) {
		ViewHolderBoolean holder = new ViewHolderBoolean();
		holder.textQuestion = (TextView) view.findViewById(R.id.textQuestion);
		holder.buttonYes = (RadioButton) view.findViewById(R.id.buttonYes);
		holder.buttonNo = (RadioButton) view.findViewById(R.id.buttonNo);
		
		holder.textQuestion.setText(cursor.getString(cursor.getColumnIndex("question")));
		holder.buttonYes.setText(R.string.yes);
		holder.buttonNo.setText(R.string.no);
	}

	public void putIntegerLayout(View view, Cursor cursor) {
		ViewHolderInteger holder = new ViewHolderInteger();
		holder.textQuestion = (TextView) view.findViewById(R.id.textQuestion);
		holder.editValue = (EditText) view.findViewById(R.id.editValue);
		
		holder.textQuestion.setText(cursor.getString(cursor.getColumnIndex("question")));
	}
	
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
	
	public String getIllnessId(Cursor cursor){
		String id = cursor.getString(cursor.getColumnIndex("illness_id"));
		return id;
	}
	
	public String getType(Cursor cursor){
		String type = cursor.getString(cursor.getColumnIndex("type"));
		return type;
	}
	
	public boolean isLast() {
		return (mCursor.isLast());
	}
	
	public boolean moveToNext() {
		return (mCursor.moveToNext());
	}
	
	public String createKey(String illness_key) {
		String key = "data['" + illness_key + ".";
		String add = mCursor.getString(mCursor.getColumnIndex("key"));
		
		key = key + add + "']";
		return key;
	}
	
	static class ViewHolderBoolean {
		private TextView textQuestion;
		private RadioButton buttonYes;
		private RadioButton buttonNo;
	}

	static class ViewHolderInteger {
		private TextView textQuestion;
		private EditText editValue;
	}

	static class ViewHolderList {
		private TextView textQuestion;
		private Spinner spinner;
	}
}

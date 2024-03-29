package com.imci.ica.utils;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.imci.ica.R;

/**
 * Custom cursor adapter to insert all patients found with request entries
 * 
 * @author Miguel Navarro & Antonin Lenfant
 * 
 */
public class CursorPatientAdapter extends CursorAdapter {
	private Cursor mCursor;
	private Context mContext;
	private final LayoutInflater mInflater;

	/**
	 * Constructor that fix current context and info cursor
	 * 
	 * @param context
	 * @param c
	 */
	public CursorPatientAdapter(Context context, Cursor c) {
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

		ViewHolder holder = new ViewHolder();
		holder.firstName = (TextView) view.findViewById(R.id.textFirst);
		holder.lastName = (TextView) view.findViewById(R.id.textLast);
		holder.gender = (TextView) view.findViewById(R.id.textGender);
		holder.bornOn = (TextView) view.findViewById(R.id.textDate);
		holder.village = (TextView) view.findViewById(R.id.textVillage);

		holder.firstName.setText(cursor.getString(
				cursor.getColumnIndex("first_name")).toString());
		holder.lastName.setText(cursor.getString(
				cursor.getColumnIndex("last_name")).toString());
		String genderValue = cursor.getString(cursor.getColumnIndex("gender")).toString();
		if (genderValue.equals("t")) {
			holder.gender.setText(R.string.male);
		} else {
			holder.gender.setText(R.string.female);
		}

		String date = cursor
				.getString(cursor.getColumnIndex("born_on")).toString();
		holder.bornOn.setText(date);

		Database db = new Database(this.mContext);
		int village_id = Integer.parseInt(cursor.getString(
				cursor.getColumnIndex("village_id")).toString());
		Cursor villageCursor = db.getZoneById(village_id);
		if (villageCursor.getCount() > 0) {
			holder.village.setText(villageCursor.getString(villageCursor
					.getColumnIndex("name")));
		}
	}

	/**
	 * Create a new view with layout for show patients
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = mInflater.inflate(R.layout.layout_list_patients,
				parent, false);
		return view;
	}

	/**
	 * Get info of a patient in cursor position
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

	/**
	 * Include all fields in a layout to show patients
	 * 
	 * @author Miguel Navarro & Antonin Lenfant
	 * 
	 */
	static class ViewHolder {
		private TextView firstName;
		private TextView lastName;
		private TextView gender;
		private TextView bornOn;
		private TextView village;

	}
}

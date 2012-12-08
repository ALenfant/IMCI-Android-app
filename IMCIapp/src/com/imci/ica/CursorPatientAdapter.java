package com.imci.ica;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CursorPatientAdapter extends CursorAdapter {
	private Cursor mCursor;
	private Context mContext;
	private final LayoutInflater mInflater;

	public CursorPatientAdapter(Context context, Cursor c) {
		super(context, c);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mCursor = c;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		ViewHolder holder = new ViewHolder();
		holder.firstName = (TextView) view.findViewById(R.id.textFirst);
		holder.lastName = (TextView) view.findViewById(R.id.textLast);
		holder.gender = (TextView) view.findViewById(R.id.textGender);
		holder.bornOn = (TextView) view.findViewById(R.id.textDate);
		holder.village = (TextView) view.findViewById(R.id.textVillage);

		holder.firstName.setText(cursor.getString(1).toString());
		holder.lastName.setText(cursor.getString(2).toString());
		String genderValue = cursor.getString(3).toString();
		if (genderValue.equals("t")) {
			holder.gender.setText(R.string.male);
		} else {
			holder.gender.setText(R.string.female);
		}
		holder.bornOn.setText(cursor.getString(4).toString());

		Database db = new Database(this.mContext);
		int village_id = Integer.parseInt(cursor.getString(5).toString());
		Cursor villageCursor = db.getZone(village_id);
		if (villageCursor.getCount() > 0) {
			holder.village.setText(villageCursor.getString(1));
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = mInflater.inflate(R.layout.layout_list_patients, parent,
				false);
		return view;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (!mCursor.moveToPosition(position)) {
	        throw new IllegalStateException("couldn't move cursor to position " + position);
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
	
	static class ViewHolder {
		private TextView firstName;
		private TextView lastName;
		private TextView gender;
		private TextView bornOn;
		private TextView village;

	}
}

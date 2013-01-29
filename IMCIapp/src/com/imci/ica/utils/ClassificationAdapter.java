package com.imci.ica.utils;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.imci.ica.R;

public class ClassificationAdapter extends ArrayAdapter<String>{

	int layoutId;
	Context mContext;
	List<String> mList;
	
	public ClassificationAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		layoutId = textViewResourceId;
		mContext = context;
		mList = objects;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup group) {

		LayoutInflater inf = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View lView = inf.inflate(layoutId, null);
		
		int classifId = Integer.parseInt(mList.get(position));
		Database db = new Database(mContext);
		int illnessId = db.getIllnessIdByClassification(classifId);
		String illness = db.getIllnessName(illnessId);
		String text = db.getClassificationText(classifId);
		
		((TextView) lView.findViewById(R.id.textIllness)).setText(illness);
		((TextView) lView.findViewById(R.id.textClassif)).setText(text);
		
		return lView;
	}
}

package com.imci.ica.utils;

import android.app.Activity;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MyOnCheckedChangeListener implements OnCheckedChangeListener{

	public Activity mActivity;
	
	public MyOnCheckedChangeListener(Activity activity) {
		mActivity = activity;
	}
	
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
	}

}

package com.imci.registerpatients;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}
	   	
	//Answer to Create button click
	public void pushCreate(View view) {
		Intent intent = new Intent(this, NewPatientActivity.class);
		startActivity(intent);
	}
		
}
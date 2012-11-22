package com.imci.registerpatients;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DoneRegister extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_done_register);
	}

	public void onClick(View view){
    	Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    	finish();
	}
}

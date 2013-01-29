package com.imci.ica;

import com.imci.ica.utils.Database;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Class responsible for the Manage Users activity
 * 
 * @author Antonin
 * 
 */
public class ManageUsersActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_users);

		ListView listCurrentCenterUsers = (ListView) findViewById(R.id.listView_currentcenterusers);

		Database db = new Database(this);
		Cursor usersCursor = db.getAllUsers();
		startManagingCursor(usersCursor);

		String[] from = new String[] { "name" };
		int[] to = new int[] { android.R.id.text1 };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, usersCursor, from, to);
		listCurrentCenterUsers.setAdapter(adapter);

		// Make the button work...
		((Button) findViewById(R.id.button_adduser))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(ManageUsersActivity.this,
								EditUserActivity.class);
						startActivity(intent);
					}
				});

		// Make a list click work...
		((ListView) findViewById(R.id.listView_currentcenterusers))
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> adapter, View view,
							int position, long id) {
						int selected_id = (int) id;

						// Cursor mycursor = (Cursor)
						// adapter.getItemAtPosition(position);

						Intent i = new Intent(ManageUsersActivity.this,
								EditUserActivity.class);
						i.putExtra(EditUserActivity.EXTRA_USER_ID, selected_id);

						startActivity(i);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_manage_users, menu);
		return true;
	}

}

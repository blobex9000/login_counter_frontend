package com.example.logincounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class DisplayUserCountActivity extends Activity {

	public final static String EXTRA_USERNAME = "com.example.logincounter.USERNAME";
	public final static String EXTRA_PASSWORD = "com.example.logincounter.PASSWORD";
	public final static String EXTRA_COUNT = "com.example.logincounter.COUNT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_user_count);

		Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.EXTRA_USERNAME);
        String count = intent.getStringExtra(MainActivity.EXTRA_COUNT);
        
        //final LayoutInflater factory = getLayoutInflater();
        //final View welcomeView = factory.inflate(R.layout.activity_display_user_count, null);
        
        //Resources res = getResources();
        String welcomeText = "Welcome " + username + "\nYou have logged in " + count + " times.";
        TextView welcomeTxtView = (TextView) findViewById(R.id.WelcomeCount);
        
        welcomeTxtView.setText(welcomeText);
        /***
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(username);

        // Set the text view as the activity layout
        setContentView(textView);
        ***/
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_user_count, menu);
		return true;
	}

	public void logout(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
}

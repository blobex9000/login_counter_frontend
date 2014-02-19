package com.example.logincounter;

import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String EXTRA_USERNAME = "com.example.logincounter.USERNAME";
	public final static String EXTRA_PASSWORD = "com.example.logincounter.PASSWORD";
	public final static String EXTRA_COUNT = "com.example.logincounter.COUNT";
	public final static String EXTRA_MESSAGE = "com.example.logincounter.MESSAGE";
	
	public final static int SUCCESS = 1;
	public final static int ERR_BAD_CREDENTIALS = -1;
	public final static int ERR_USER_EXISTS = -2;
	public final static int ERR_BAD_USERNAME = -3;
	public final static int ERR_BAD_PASSWORD = -4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		if (message != null) {
			TextView messageTxtView = (TextView) findViewById(R.id.Message);
			messageTxtView.setText(message);
		} else {
			String messageText = "Please enter your credentials below";
			TextView messageTxtView = (TextView) findViewById(R.id.Message);
			messageTxtView.setText(messageText);	
		}
		// Code from http://stackoverflow.com/questions/19266553/android-caused-by-android-os-networkonmainthreadexception
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void login(View view) {
		EditText usernameTxt = (EditText) findViewById(R.id.username);
		EditText passwordTxt = (EditText) findViewById(R.id.password);
		
		String username = usernameTxt.getText().toString();
		String password = passwordTxt.getText().toString();
		
		// Code based on: http://stackoverflow.com/questions/3027066/how-to-send-a-json-object-over-request-with-android
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 10000);
		HttpClient httpClient = new DefaultHttpClient(params);
		
		HttpPost request;
		HttpResponse response = null;
		JSONObject jsonArgs = new JSONObject();
		
		try {
			request = new HttpPost("http://afternoon-plains-5626.herokuapp.com/users/login");
			jsonArgs.put("user", username);
			jsonArgs.put("password", password);
			System.out.println("usernameTxt: " + username);
			System.out.println("passwordTxt: " + password);
			StringEntity jsonStringEntity = new StringEntity(jsonArgs.toString());
			jsonStringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			request.setEntity(jsonStringEntity);
			//request.setEntity(new ByteArrayEntity(jsonArgs.toString().getBytes("UTF8")));
			response = httpClient.execute(request);
		} catch(Exception e) {
			System.out.println("Connection had a problem:");
			e.printStackTrace();
		}
		
		// Code based on http://stackoverflow.com/questions/14812560/how-to-parse-the-json-response-in-android
		String responseString = null;
		JSONObject responseJson = null;
		String errCode = null;
		try {
			responseString = EntityUtils.toString(response.getEntity());
			responseJson = new JSONObject(responseString);
		    errCode = responseJson.getString("errCode");
			System.out.println(responseJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int errCodeInt = Integer.valueOf(errCode);
		System.out.println("errCodeInt -----> " + errCodeInt);
		if (errCodeInt == SUCCESS) {
			System.out.println("How did I get here?");
			Intent intent = new Intent(this, DisplayUserCountActivity.class);
			intent.putExtra(EXTRA_USERNAME, username);
			try {
				intent.putExtra(EXTRA_COUNT, responseJson.getString("count"));
			} catch (Exception e) {
				System.out.println("There is no count:");
				e.printStackTrace();
			}
			intent.putExtra(EXTRA_PASSWORD, password);
			//Resources res = getResources();
			//String text = String.format(res.getString(R.string.welcome), 1);
			startActivity(intent);
		} else if (errCodeInt == ERR_BAD_CREDENTIALS) {
			String messageText = "Invalid username and password combination. Please try again.";
	        TextView messageTxtView = (TextView) findViewById(R.id.Message);
	        messageTxtView.setText(messageText);
	        usernameTxt.setText("");
	        passwordTxt.setText("");
		}
	}
	
	public void addUser(View view) {
		EditText usernameTxt = (EditText) findViewById(R.id.username);
		EditText passwordTxt = (EditText) findViewById(R.id.password);
		
		String username = usernameTxt.getText().toString();
		String password = passwordTxt.getText().toString();
		
		//System.out.println("hello--------> " + (username.equals("")));
		
		// Code based on: http://stackoverflow.com/questions/3027066/how-to-send-a-json-object-over-request-with-android
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 10000);
		HttpClient httpClient = new DefaultHttpClient(params);
		
		HttpPost request;
		HttpResponse response = null;
		JSONObject jsonArgs = new JSONObject();
		
		try {
			request = new HttpPost("http://afternoon-plains-5626.herokuapp.com/users/add");
			jsonArgs.put("user", username);
			jsonArgs.put("password", password);
			System.out.println("usernameTxt: " + username);
			System.out.println("passwordTxt: " + password);
			StringEntity jsonStringEntity = new StringEntity(jsonArgs.toString());
			jsonStringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			request.setEntity(jsonStringEntity);
			//request.setEntity(new ByteArrayEntity(jsonArgs.toString().getBytes("UTF8")));
			response = httpClient.execute(request);
		} catch(Exception e) {
			System.out.println("Connection had a problem:");
			e.printStackTrace();
		}
		
		// Code based on http://stackoverflow.com/questions/14812560/how-to-parse-the-json-response-in-android
		String responseString = null;
		JSONObject responseJson = null;
		String errCode = null;
		try {
			responseString = EntityUtils.toString(response.getEntity());
			responseJson = new JSONObject(responseString);
		    errCode = responseJson.getString("errCode");
			System.out.println(responseJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int errCodeInt = Integer.valueOf(errCode);
		System.out.println("errCodeInt -----> " + errCodeInt);
		if (errCodeInt == SUCCESS) {
			System.out.println("How did I get here?");
			Intent intent = new Intent(this, DisplayUserCountActivity.class);
			intent.putExtra(EXTRA_USERNAME, username);
			try {
				intent.putExtra(EXTRA_COUNT, responseJson.getString("count"));
			} catch (Exception e) {
				System.out.println("There is no count:");
				e.printStackTrace();
			}
			intent.putExtra(EXTRA_PASSWORD, password);
			//Resources res = getResources();
			//String text = String.format(res.getString(R.string.welcome), 1);
			startActivity(intent);
		} else if (errCodeInt == ERR_BAD_USERNAME) {
			String messageText = "The user name should be non-empty and at most 128 characters long. Please try again.";
	        TextView messageTxtView = (TextView) findViewById(R.id.Message);
	        messageTxtView.setText(messageText);
	        usernameTxt.setText("");
	        passwordTxt.setText("");
		} else if (errCodeInt == ERR_BAD_PASSWORD) {
			String messageText = "The password should be at most 128 characters long. Please try again.";
	        TextView messageTxtView = (TextView) findViewById(R.id.Message);
	        messageTxtView.setText(messageText);
	        usernameTxt.setText("");
	        passwordTxt.setText("");
		} else if (errCodeInt == ERR_USER_EXISTS) {
			String messageText = "This user name already exists. Please try again.";
	        TextView messageTxtView = (TextView) findViewById(R.id.Message);
	        messageTxtView.setText(messageText);
	        usernameTxt.setText("");
	        passwordTxt.setText("");
			//Intent intent = new Intent(this, MainActivity.class);
			//intent.putExtra(EXTRA_MESSAGE, "This user name already exists. Please try again.");
			//startActivity(intent);
		}
	}
	
	/**
	private class getUserCount extends AsyncTask<URL, Integer, Integer> {
		protected Integer doInBackground(URL... urls) {
			EditText usernameTxt = (EditText) findViewById(R.id.username);
			EditText passwordTxt = (EditText) findViewById(R.id.password);
			
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);
			HttpClient httpClient = new DefaultHttpClient(params);
			
			HttpPost request;
			HttpResponse response = null;
			JSONObject jsonArgs = new JSONObject();
			
			try {
				request = new HttpPost(urls[0].toString());
				jsonArgs.put("user", usernameTxt);
				jsonArgs.put("password", passwordTxt);
				StringEntity jsonStringEntity = new StringEntity(jsonArgs.toString());
				jsonStringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				request.setEntity(jsonStringEntity);
				//request.setEntity(new ByteArrayEntity(jsonArgs.toString().getBytes("UTF8")));
				System.out.println("ajsldkfjsjlf");
				response = httpClient.execute(request);
			
			} catch(Exception e) {
				System.out.println("Connection had a problem:");
				e.printStackTrace();
			}
			
			// Code based on http://stackoverflow.com/questions/14812560/how-to-parse-the-json-response-in-android
			String responseString = null;
			JSONObject responseJson = null;
			Integer errCode = null;
			try {
				responseString = EntityUtils.toString(response.getEntity());
				responseJson = new JSONObject(responseString);
				errCode = responseJson.getInt("errCode");
				System.out.println("ajsldkfjsjlf");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return errCode;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			//do nothing
		}
		
		protected void onPostExecute(Integer result) {
			//do nothing
		}
		
	}
	**/
}

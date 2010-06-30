package com.android.airpic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


public class SettingsActivity extends PreferenceActivity
{
	private static final String TAG = "Airpic.settings";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try{
			addPreferencesFromResource(R.xml.preferences);
		}
		catch(ClassCastException e)
		{
			Log.e(TAG, "Shared preferences are corrupt! Resetting to default values", e);
			
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			
			//Blow away all the preferences
			SharedPreferences.Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
			
			PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
			
			addPreferencesFromResource(R.xml.preferences);
		}
		
	}
}

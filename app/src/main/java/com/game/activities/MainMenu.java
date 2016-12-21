package com.game.activities;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.R;
import com.game.util.Constants;

public class MainMenu extends Activity {

	public static int level;
	private HashMap<Integer, String> levelMap;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_menu);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		levelMap = new HashMap<Integer, String>();

		levelMap.put(1, Constants.EASIEST_NAME);
		levelMap.put(2, Constants.EASY_NAME);
		levelMap.put(3, Constants.NORMAL_NAME);
		levelMap.put(4, Constants.HARD_NAME);
		levelMap.put(5, Constants.HARDEST_NAME);

		Button start = (Button) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ResourceLoaderAsync resourceLoaderAsync = new ResourceLoaderAsync();
				resourceLoaderAsync.execute();
			}
		});

		// Button options = (Button)findViewById(R.id.options);
		// options.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// startActivity(new Intent(getBaseContext(), Options.class));
		// }
		// });

	}

	protected void onPause() {
		super.onPause();

		SharedPreferences preferences = getSharedPreferences(Constants.GAME, 0);
		Editor editor = preferences.edit();

		editor.putInt(Constants.LEVEL, level);
		editor.commit();
	}

	protected void onResume() {
		super.onResume();
		final SharedPreferences preferences = getSharedPreferences(
				Constants.GAME, 0);
		final TextView score = (TextView) findViewById(R.id.score);
		final TextView levelText = (TextView) findViewById(R.id.level);
		final ImageView imageView = (ImageView) findViewById(R.id.android);

		level = preferences.getInt(Constants.LEVEL, Constants.NORMAL);
		String levelName = levelMap.get(level).toString();

		switch (level) {
		case Constants.EASIEST:
			levelText.setText(levelName);
			score.setText(Long.toString(preferences.getLong(levelName, 0)));
			imageView.setImageResource(R.drawable.easiest);
			break;
		case Constants.EASY:
			levelText.setText(levelName);
			score.setText(Long.toString(preferences.getLong(levelName, 0)));
			imageView.setImageResource(R.drawable.easy);
			break;
		case Constants.NORMAL:
			levelText.setText(levelName);
			score.setText(Long.toString(preferences.getLong(levelName, 0)));
			imageView.setImageResource(R.drawable.normal);
			break;
		case Constants.HARD:
			levelText.setText(levelName);
			score.setText(Long.toString(preferences.getLong(levelName, 0)));
			imageView.setImageResource(R.drawable.hard);
			break;
		case Constants.HARDEST:
			levelText.setText(levelName);
			score.setText(Long.toString(preferences.getLong(levelName, 0)));
			imageView.setImageResource(R.drawable.hardest);
			break;

		default:
			levelText.setText(levelName);
			score.setText(Long.toString(preferences.getLong(levelName, 0)));
			imageView.setImageResource(R.drawable.normal);
			break;
		}

		LinearLayout inc = (LinearLayout) findViewById(R.id.inc);
		LinearLayout dec = (LinearLayout) findViewById(R.id.dec);

		inc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				level++;
				if (level > Constants.HARDEST) {
					level = Constants.EASIEST;
				}
				String l = levelMap.get(level).toString();
				levelText.setText(l);
				score.setText(Long.toString(preferences.getLong(l, 0)));
				changeImage();
			}

		});

		dec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				level--;
				if (level < Constants.EASIEST) {
					level = Constants.HARDEST;
				}
				String levelName = levelMap.get(level).toString();
				levelText.setText(levelName);
				score.setText(Long.toString(preferences.getLong(levelName, 0)));
				changeImage();
			}
		});

	}

	private void changeImage() {
		ImageView levelImage = (ImageView) findViewById(R.id.android);
		switch (level) {
		case Constants.EASIEST:
			levelImage.setImageResource(R.drawable.easiest);
			break;
		case Constants.EASY:
			levelImage.setImageResource(R.drawable.easy);
			break;
		case Constants.NORMAL:
			levelImage.setImageResource(R.drawable.normal);
			break;
		case Constants.HARD:
			levelImage.setImageResource(R.drawable.hard);
			break;
		case Constants.HARDEST:
			levelImage.setImageResource(R.drawable.hardest);
			break;

		default:
			levelImage.setImageResource(R.drawable.normal);
			break;
		}
	}
	
	class ResourceLoaderAsync extends AsyncTask<Void, Void, Void>{
		
		ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainMenu.this);
			progressDialog.setMessage("Loading...");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Constants.loadResources(getApplicationContext());
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			startActivity(new Intent(getBaseContext(), Game.class));
		}
		
	}

}

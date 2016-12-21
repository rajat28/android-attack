package com.game.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;

import com.game.elements.Player;
import com.game.listeners.GameStateListener;
import com.game.util.Constants;
import com.game.util.Panel;
import com.game.util.ViewThread;

public class Game extends Activity {
	Activity a;
	public static int extraScore = 0;
	public int bckgScore = 0;
	private long remTime;
	private boolean isPaused = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		extraScore = 0;
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		Panel panel = new Panel(getApplicationContext());

		LinearLayout linearLayout = new LinearLayout(getApplicationContext());
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		linearLayout.addView(panel);

		setContentView(linearLayout);
		a = Game.this;

		panel.setGameStateListener(gameStateListener);
	}

	GameStateListener gameStateListener = new GameStateListener() {

		@Override
		public void gameFinished(boolean isFinished) {
			try {
				if (isFinished) {
					Panel.viewThread.setRunning(false);

					Player.isTouching = false;
					Long score = (long) (System.currentTimeMillis() - ViewThread.score) / 100;
					score = (score / 10) * 10 + extraScore + Panel.bonus;
					SharedPreferences preferences = getSharedPreferences(
							Constants.GAME, 0);
//					Panel.bonus = 0;
					switch (MainMenu.level) {
					case 1:
						if (score > preferences.getLong(Constants.EASIEST_NAME,
								0)) {
							Editor editor = preferences.edit();
							editor.putLong(Constants.EASIEST_NAME, score);
							editor.commit();
						}
						break;
					case 2:
						if (score > preferences.getLong(Constants.EASY_NAME, 0)) {
							Editor editor = preferences.edit();
							editor.putLong(Constants.EASY_NAME, score);
							editor.commit();
						}
						break;
					case 3:
						if (score > preferences.getLong(Constants.NORMAL_NAME,
								0)) {
							Editor editor = preferences.edit();
							editor.putLong(Constants.NORMAL_NAME, score);
							editor.commit();
						}
						break;
					case 4:
						if (score > preferences.getLong(Constants.HARD_NAME, 0)) {
							Editor editor = preferences.edit();
							editor.putLong(Constants.HARD_NAME, score);
							editor.commit();
						}
						break;
					case 5:
						if (score > preferences.getLong(Constants.HARDEST_NAME,
								0)) {
							Editor editor = preferences.edit();
							editor.putLong(Constants.HARDEST_NAME, score);
							editor.commit();
						}
						break;

					default:
						break;
					}

					AlertDialog.Builder builder = new AlertDialog.Builder(a);
					builder.setCancelable(false);
					builder.setMessage("Score: " + score);
					builder.setTitle("Game Over");
					builder.setPositiveButton("Play Again",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// finish();
									// startActivity(new Intent(Game.this,
									// Game.class));
									restartGame();
								}
							});
					builder.setNegativeButton("Main Menu",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									Panel.viewThread.setRunning(false);
									Panel.count = 0;
									finish();
								}
							});

					AlertDialog alert = builder.create();
					alert.show();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void restartGame() {

		resetValues();

		Panel.viewThread = new ViewThread(Panel.panel);
		Panel.viewThread.setRunning(true);
		Panel.viewThread.start();
		Panel.panel.addEnemy();
	}

	public void onDestroy() {
		super.onDestroy();
		Constants.releaseResources();
		resetValues();
	}

	private void resetValues() {
		Panel.enemyList.clear();
		Panel.panel.resetPlayerPostion();
		Panel.count = 2;
		Player.isTouching = false;
		Panel.powerUp = null;
		Player.isShielded = false;
		Panel.bonus = 0;
		extraScore = 0;
	}

	public void onPause() {
		super.onPause();
		if (Panel.viewThread != null) {
			Panel.viewThread.setRunning(false);
		}
		if (Player.isShielded) {
			remTime = System.currentTimeMillis() - Player.shieldTime;
		}
		bckgScore += (int) (((System.currentTimeMillis() - ViewThread.score) / 100) / 10) * 10;
	}

	@Override
	public void onRestart() {
		super.onRestart();
		if (Player.isShielded) {
			Player.shieldTime = System.currentTimeMillis() - remTime;
		}
		System.out.println("IS PAUSED? : " + isPaused);
		if (!isPaused) {
			extraScore = bckgScore;
			Panel.viewThread = new ViewThread(Panel.panel);
			Panel.viewThread.setRunning(true);
			Panel.viewThread.start();
		} else {
			Panel.viewThread.setRunning(false);
		}
	}

	public void onBackPressed() {

		isPaused = true;

		if (Player.isShielded) {
			remTime = System.currentTimeMillis() - Player.shieldTime;
		}
		Panel.viewThread.setRunning(false);
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		bckgScore += (int) (((System.currentTimeMillis() - ViewThread.score) / 100) / 10) * 10;
		builder.setCancelable(false);
		builder.setMessage("Are you sure you want to quit this turn?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Panel.viewThread.setRunning(false);
				Panel.count = 0;
				finish();
				isPaused = false;
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Panel.viewThread.stop();
				extraScore = bckgScore;
				Panel.viewThread = new ViewThread(Panel.panel);
				Panel.viewThread.setRunning(true);
				Panel.viewThread.start();
				if (Player.isShielded) {
					Player.shieldTime = System.currentTimeMillis() - remTime;
				}
				isPaused = false;
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
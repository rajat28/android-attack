package com.game.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import com.game.R;

public class Constants {
	
	public static float DENSITY;
	
	public static final int GAME_OVER = 123;

	// Level Values
	public static final int EASIEST = 1;
	public static final int EASY = 2;
	public static final int NORMAL = 3;
	public static final int HARD = 4;
	public static final int HARDEST = 5;

	// Power-Ups
	public static final int POINTS = 1;
	public static final int SHIELD = 2;
	public static final int NUKE = 3;

	// Level Strings
	public static final String EASIEST_NAME = "Easiest";
	public static final String EASY_NAME = "Easy";
	public static final String NORMAL_NAME = "Normal";
	public static final String HARD_NAME = "Hard";
	public static final String HARDEST_NAME = "Hardest";

	// Preferences
	public static final String GAME = "game";
	public static final String LEVEL = "level";

	// Related to explosion sprite
	public static final int FPS = 20;
	public static final int EXPLOSION_SPRITE_COUNT = 18;

	// Bitmaps
	public static Bitmap ENEMY_BMP;
	public static Bitmap PLAYER_BMP;
	public static Bitmap PLAYER_SHIELD_BMP;
	public static Bitmap POINTS_BMP;
	public static Bitmap SHIELD_BMP;
	public static Bitmap NUKE_BMP;
	public static Bitmap EXP_SPRITE_SHEET_BMP;
	
	//Player for sound effects
	public static MediaPlayer EXPLOSION_EFFECT;
	public static MediaPlayer COIN_EFFECT;
	public static MediaPlayer SHIELD_EFFECT;
	
	
	public static final void loadResources(Context context) {
		DENSITY = (float) (context.getResources().getDisplayMetrics().density/1.5);
		
		ENEMY_BMP = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.android);
		PLAYER_BMP = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.apple);
		PLAYER_SHIELD_BMP = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.shield);
		POINTS_BMP = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.points);
		SHIELD_BMP = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.shield_power_up);
		NUKE_BMP = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.nuclear);
		EXP_SPRITE_SHEET_BMP = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.explosion_spritesheet);
		
		EXPLOSION_EFFECT = MediaPlayer.create(context, R.raw.explosion);
		COIN_EFFECT = MediaPlayer.create(context, R.raw.coin);
		SHIELD_EFFECT = MediaPlayer.create(context, R.raw.shield);
	}

	public static final void releaseResources() {
		ENEMY_BMP.recycle();
		PLAYER_BMP.recycle();
		PLAYER_SHIELD_BMP.recycle();
		POINTS_BMP.recycle();
		SHIELD_BMP.recycle();
		NUKE_BMP.recycle();
		EXP_SPRITE_SHEET_BMP.recycle();
		EXPLOSION_EFFECT.stop();
		EXPLOSION_EFFECT.release();
		COIN_EFFECT.stop();
		COIN_EFFECT.release();
		SHIELD_EFFECT.stop();
		SHIELD_EFFECT.release();
		
		ENEMY_BMP = null;
		PLAYER_BMP = null;
		PLAYER_SHIELD_BMP = null;
		POINTS_BMP = null;
		SHIELD_BMP = null;
		NUKE_BMP = null;
		EXP_SPRITE_SHEET_BMP = null;
		EXPLOSION_EFFECT = null;
		COIN_EFFECT = null;
		SHIELD_EFFECT = null;
	}
}

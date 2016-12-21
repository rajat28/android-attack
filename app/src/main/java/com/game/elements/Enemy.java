package com.game.elements;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.game.activities.MainMenu;
import com.game.util.Constants;
import com.game.util.Panel;

public class Enemy {

	private float xCoor;
	private float yCoor;

	public float xSpeed;
	public float ySpeed;

	public static Bitmap bitmap;
	public static Bitmap explodeSpriteSheet;

	public Rect sourceRect;
	public static int spriteWidth;
	public static int spriteHeight;

	public int[] xBounds;
	public int[] yBounds;

	private Paint paint;

	public static boolean isExploding = false;

	private long frameTicker;
	private long frameTime;

	private int currentFrame;
	private int offset = 45;

	public Enemy(Resources resources, int speedX, int speedY, int corner) {
		Random random = new Random();
		
		offset *= Constants.DENSITY;
		
		paint = new Paint();
		paint.setColor(Color.RED);

		while (xSpeed == 0) {
			xSpeed = random.nextInt((speedX));
		}
		while (ySpeed == 0) {
			ySpeed = random.nextInt((speedY));
		}
		
		xSpeed *= Constants.DENSITY;
		ySpeed *= Constants.DENSITY;
		
		currentFrame = 0;
		frameTime = 1000 / Constants.FPS;
		frameTicker = 0l;

		sourceRect = new Rect(offset, 0, Enemy.spriteWidth - offset,
				Enemy.spriteHeight);

		switch (corner) {
		case 1:
			xCoor = 0;
			yCoor = 0;
			break;

		case 2:
			xCoor = Panel.canvasWidth - Enemy.bitmap.getWidth();
			yCoor = 0;
			break;

		case 3:
			xCoor = 0;
			yCoor = Panel.canvasHeight - Enemy.bitmap.getHeight();
			break;

		case 4:
			xCoor = Panel.canvasWidth - Enemy.bitmap.getWidth();
			yCoor = Panel.canvasHeight - Enemy.bitmap.getHeight();
			break;

		default:
			xCoor = 0;
			yCoor = 0;
			break;
		}

		xBounds = new int[2];
		yBounds = new int[2];

		xBounds[0] = (int) xCoor;
		xBounds[1] = (int) xCoor + Enemy.bitmap.getWidth();

		yBounds[0] = (int) yCoor;
		yBounds[1] = (int) yCoor + Enemy.bitmap.getHeight();
	}

	public void incSpeed() {

		switch (MainMenu.level) {

		case Constants.EASIEST:
			xSpeed *= 1.2;
			ySpeed *= 1.2;
			break;
		case Constants.EASY:
			xSpeed *= 1.25;
			ySpeed *= 1.25;
			break;
		case Constants.NORMAL:
			xSpeed *= 1.5;
			ySpeed *= 1.5;
			break;
		case Constants.HARD:
			xSpeed *= 1.75;
			ySpeed *= 1.75;
			break;
		case Constants.HARDEST:
			xSpeed *= 2;
			ySpeed *= 2;
			break;

		default:
			xSpeed *= 1.5;
			ySpeed *= 1.5;
			break;
		}
		
		xSpeed *= Constants.DENSITY;
		ySpeed *= Constants.DENSITY;
	}

	public void doDraw(Canvas canvas) {
		if (!isExploding) {
			canvas.drawBitmap(Enemy.bitmap, xCoor, yCoor, null);
		} else {
			update(System.currentTimeMillis());
			if (isExploding) {
				Rect destRect = new Rect((int) xCoor + (offset / 2),
						(int) yCoor, (int) xCoor + Enemy.spriteWidth
								- (offset / 2), (int) yCoor
								+ Enemy.spriteHeight);
				canvas.drawBitmap(Enemy.explodeSpriteSheet, sourceRect,
						destRect, null);
			}
		}
	}

	public void update(long gameTime) {
		if (gameTime > frameTicker + frameTime) {
			frameTicker = gameTime;
			currentFrame++;
			if (currentFrame >= Constants.EXPLOSION_SPRITE_COUNT) {
				isExploding = false;
				currentFrame = 0;
				Panel.clearList = true;
			}
		}
		sourceRect.left = currentFrame * spriteWidth + offset;
		sourceRect.right = sourceRect.left + spriteWidth - offset;
	}

	public void animate() {
		if (!isExploding) {
			xCoor += xSpeed;
			yCoor += ySpeed;
		}
		xBounds[0] = (int) xCoor;
		xBounds[1] = (int) xCoor + Enemy.bitmap.getWidth();

		yBounds[0] = (int) yCoor;
		yBounds[1] = (int) yCoor + Enemy.bitmap.getHeight();

		checkBorders();
	}

	public int[] getXBounds() {
		return xBounds;
	}

	public int[] getYBounds() {
		return yBounds;
	}

	public void checkBorders() {

		if (xCoor <= 0) {
			xCoor = 0;
			xSpeed = -xSpeed;
		} else if (xCoor + Enemy.bitmap.getWidth() >= Panel.canvasWidth) {
			xCoor = Panel.canvasWidth - Enemy.bitmap.getWidth();
			xSpeed = -xSpeed;
		}

		if (yCoor <= 0) {
			yCoor = 0;
			ySpeed = -ySpeed;
		} else if (yCoor + Enemy.bitmap.getHeight() >= Panel.canvasHeight) {
			yCoor = Panel.canvasHeight - Enemy.bitmap.getHeight();
			ySpeed = -ySpeed;
		}

	}

}

package com.game.elements;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.game.util.Constants;
import com.game.util.Panel;

public class PowerUp {

	private float xCoor;
	private float yCoor;

	private int ySpeed;

	private Bitmap bitmap;

	private int[] xBounds;
	private int[] yBounds;

	private int bonusType;

	public PowerUp(Resources resources) {

		Random random = new Random();

		if (!Player.isShielded) {

			int bonus = random.nextInt(20000);
			if (bonus < 10000) {
				bonusType = Constants.POINTS;
			} else if (bonus >= 10000 && bonus < 17500) {
				bonusType = Constants.SHIELD;
			} else {
				bonusType = Constants.NUKE;
			}
		} else {
			bonusType = Constants.POINTS;
		}

		switch (bonusType) {

		case Constants.POINTS:
			bitmap = Constants.POINTS_BMP;
			break;

		case Constants.NUKE:
			bitmap = Constants.NUKE_BMP;
			break;

		case Constants.SHIELD:
			bitmap = Constants.SHIELD_BMP;
			break;

		default:
			bitmap = Constants.POINTS_BMP;
			break;
		}

		yCoor = 0;
		xCoor = random.nextInt(Panel.canvasWidth);

		if (xCoor > Panel.canvasWidth - bitmap.getWidth()) {
			xCoor = Panel.canvasWidth - bitmap.getWidth();
		}

		while (ySpeed <(int) (5 * Constants.DENSITY)) {
			ySpeed = random.nextInt((int) (10* Constants.DENSITY));
		}

		xBounds = new int[2];
		yBounds = new int[2];

	}

	public int getBonusType() {
		return bonusType;
	}

	public void doDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, xCoor, yCoor, null);
	}

	public void animate() {
		yCoor += ySpeed;
		if (yCoor >= Panel.canvasHeight) {
			Player.isPowerUp = true;
		}
		xBounds[0] = (int) xCoor;
		xBounds[1] = (int) xCoor + bitmap.getWidth();

		yBounds[0] = (int) yCoor;
		yBounds[1] = (int) yCoor + bitmap.getHeight();

	}

	public int[] getXBounds() {
		return xBounds;
	}

	public int[] getYBounds() {
		return yBounds;
	}
}

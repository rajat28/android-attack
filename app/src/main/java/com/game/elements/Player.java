package com.game.elements;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.game.util.Constants;
import com.game.util.Panel;

public class Player {

	private float xCoor;
	private float yCoor;
	public static Bitmap playerBitmap;

	// private int centerX;
	// private int centerY;

	public static int width;
	public static int height;

	public int[] xBounds;
	public int[] yBounds;

	public int[] xBoundsAct;
	public int[] yBoundsAct;

	public static boolean isTouching = false;
	public static boolean isPowerUp = false;
	public static boolean isShielded = false;
	private boolean alt = false;

	public static long shieldTime;

	public Player() {
		if (!isShielded) {
			playerBitmap = Constants.PLAYER_BMP;
		} else {
			playerBitmap = Constants.PLAYER_SHIELD_BMP;
		}
		width = playerBitmap.getWidth();
		height = playerBitmap.getHeight();
		xBounds = new int[2];
		yBounds = new int[2];

		xBoundsAct = new int[2];
		yBoundsAct = new int[2];

	}

	public void doDraw(Canvas canvas, int x, int y) {

		xCoor = x - playerBitmap.getWidth() / 2;
		yCoor = y - playerBitmap.getHeight() / 2;
		// centerX = x;
		// centerY = y;

		setBounds();
		setBoundsAct();
		canvas.drawBitmap(playerBitmap, xCoor, yCoor, null);
	}

	public void setBounds() {
		xBounds[0] = (int) xCoor + 22;
		xBounds[1] = (int) xCoor + playerBitmap.getWidth() - 22;

		yBounds[0] = (int) yCoor + 25;
		yBounds[1] = (int) yCoor + playerBitmap.getHeight() - 25;

	}

	public void setBoundsAct() {
		xBoundsAct[0] = (int) xCoor;
		xBoundsAct[1] = (int) xCoor + playerBitmap.getWidth();

		yBoundsAct[0] = (int) yCoor;
		yBoundsAct[1] = (int) yCoor + playerBitmap.getHeight();

	}

	public void checkBorders() {
		if (!isShielded && !Enemy.isExploding) {

			checkElementTouch();
		} else {
			// elementBounceBack();
			long time = System.currentTimeMillis() - shieldTime;
			if (time > 4000 && time < 5000) {
				if (alt) {
					playerBitmap = Constants.PLAYER_SHIELD_BMP;
					alt = false;
				} else {
					playerBitmap = Constants.PLAYER_BMP;
					alt = true;
				}
			}

			if (System.currentTimeMillis() - shieldTime > 5000) {
				shieldTimer.run();
			}
		}
		checkPowerUpTouch();

		if (xCoor <= 0) {
			xCoor = 0;
		} else if (xCoor + playerBitmap.getWidth() >= Panel.canvasWidth) {
			xCoor = Panel.canvasWidth - playerBitmap.getWidth();
		}

		if (yCoor <= 0) {
			yCoor = 0;
		} else if (yCoor + playerBitmap.getHeight() >= Panel.canvasHeight) {
			yCoor = Panel.canvasHeight - playerBitmap.getHeight();
		}
	}

	private void checkPowerUpTouch() {
		if (Panel.powerUp != null) {
			if ((Panel.powerUp.getXBounds()[0] >= xBoundsAct[0])
					&& (Panel.powerUp.getXBounds()[0] <= xBoundsAct[1])) {
				if ((Panel.powerUp.getYBounds()[0] <= yBoundsAct[1] && Panel.powerUp
						.getYBounds()[0] > yBoundsAct[0])
						|| (Panel.powerUp.getYBounds()[1] >= yBoundsAct[0] && Panel.powerUp
								.getYBounds()[1] < yBoundsAct[1])) {
					isPowerUp = true;
					powerUps();
				}
			} else if ((Panel.powerUp.getXBounds()[1] >= xBoundsAct[0])
					&& (Panel.powerUp.getXBounds()[1] <= xBoundsAct[1])) {
				if ((Panel.powerUp.getYBounds()[0] <= yBoundsAct[1] && Panel.powerUp
						.getYBounds()[0] > yBoundsAct[0])
						|| (Panel.powerUp.getYBounds()[1] >= yBoundsAct[0] && Panel.powerUp
								.getYBounds()[1] < yBoundsAct[1])) {
					isPowerUp = true;
					powerUps();
				}
			}
			if ((Panel.powerUp.getYBounds()[0] >= yBoundsAct[0])
					&& (Panel.powerUp.getYBounds()[0] <= yBoundsAct[1])) {
				if ((Panel.powerUp.getXBounds()[0] <= xBoundsAct[1] && Panel.powerUp
						.getXBounds()[0] > xBoundsAct[0])
						|| (Panel.powerUp.getXBounds()[1] >= xBoundsAct[0] && Panel.powerUp
								.getXBounds()[1] < xBoundsAct[1])) {
					isPowerUp = true;
					powerUps();
				}
			} else if ((Panel.powerUp.getYBounds()[1] >= yBoundsAct[0])
					&& (Panel.powerUp.getYBounds()[1] <= yBoundsAct[1])) {
				if ((Panel.powerUp.getXBounds()[0] <= xBoundsAct[1] && Panel.powerUp
						.getXBounds()[0] > xBoundsAct[0])
						|| (Panel.powerUp.getXBounds()[1] >= xBoundsAct[0] && Panel.powerUp
								.getXBounds()[1] < xBoundsAct[1])) {
					isPowerUp = true;
					powerUps();
				}
			}
		}
	}

	private void checkElementTouch() {
		for (Enemy elements : Panel.enemyList) {
			// if (((centerX > elements.getXBounds()[0]) && (centerX <
			// elements.getXBounds()[1]))
			// && ((centerY > elements.getYBounds()[0]) && (centerY <
			// elements.getYBounds()[1]))) {
			// isTouching = true;
			// }

			if (xBounds[0] > elements.getXBounds()[0]
					&& xBounds[0] < elements.getXBounds()[1]) {
				if ((yBounds[0] < elements.getYBounds()[1] && yBounds[0] > elements
						.getYBounds()[0])
						|| (yBounds[1] < elements.getYBounds()[1] && yBounds[1] > elements
								.getYBounds()[0])) {
					isTouching = true;
				}
			} else if (xBounds[1] > elements.getXBounds()[0]
					&& xBounds[1] < elements.getXBounds()[1]) {
				if ((yBounds[0] < elements.getYBounds()[1] && yBounds[0] > elements
						.getYBounds()[0])
						|| (yBounds[1] < elements.getYBounds()[1] && yBounds[1] > elements
								.getYBounds()[0])) {
					isTouching = true;
				}
			}
			if (yBounds[0] > elements.getYBounds()[0]
					&& yBounds[0] < elements.getYBounds()[1]) {
				if ((xBounds[0] == elements.getXBounds()[1])
						|| (xBounds[1] == elements.getXBounds()[0])) {
					isTouching = true;
				}
			} else if (yBounds[1] > elements.getYBounds()[0]
					&& yBounds[1] < elements.getYBounds()[1]) {
				if ((xBounds[0] == elements.getXBounds()[1])
						|| (xBounds[1] == elements.getXBounds()[0])) {
					isTouching = true;
				}
			}
		}
	}

	private void powerUps() {
		switch (Panel.powerUp.getBonusType()) {
		case Constants.POINTS:
			Constants.COIN_EFFECT.start();
			Panel.bonus += 50;
			break;
		case Constants.SHIELD:
			Constants.SHIELD_EFFECT.start();
			playerBitmap = Constants.PLAYER_SHIELD_BMP;
			isShielded = true;
			shieldTime = System.currentTimeMillis();
			break;
		case Constants.NUKE:
			if (Panel.enemyList.size()>0) {
				Constants.EXPLOSION_EFFECT.start();
				Enemy.isExploding = true;
			}
			break;
		default:
			Constants.COIN_EFFECT.start();
			Panel.bonus += 50;
			break;
		}
	}

	Runnable shieldTimer = new Runnable() {

		@Override
		public void run() {
			isShielded = false;
			playerBitmap = Constants.PLAYER_BMP;
		}
	};
}

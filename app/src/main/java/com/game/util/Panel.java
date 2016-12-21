package com.game.util;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.game.activities.Game;
import com.game.activities.MainMenu;
import com.game.elements.Enemy;
import com.game.elements.Player;
import com.game.elements.PowerUp;
import com.game.listeners.GameStateListener;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {

	public static int canvasWidth;
	public static int canvasHeight;

	private Player player;
	private Enemy elements;
	public static ViewThread viewThread;
	public static ArrayList<Enemy> enemyList;
	public static PowerUp powerUp;

	public static Panel panel;
	public static int bonus;

	public static int count = 0;
	public int nextIndex = 0;
	public int corner = 1;

	private boolean isFirstTime = true;

	private int xCoor;
	private int yCoor;

	private long score;

	public static boolean clearList = false; // For clearing the enemies when
												// nuked

	int maxEnemy = 6;
	GameStateListener gameStateListener;

	Paint paint = new Paint();

	public Panel(Context context) {
		super(context);
		getHolder().addCallback(this);

		Enemy.bitmap = Constants.ENEMY_BMP;
		Enemy.explodeSpriteSheet = Constants.EXP_SPRITE_SHEET_BMP;

		Enemy.spriteHeight = Enemy.explodeSpriteSheet.getHeight();
		Enemy.spriteWidth = (int) (Enemy.explodeSpriteSheet.getWidth() / (Constants.EXPLOSION_SPRITE_COUNT + 2));

		panel = this;
		viewThread = new ViewThread(panel);
		paint.setColor(Color.WHITE);
		Random random = new Random();
		corner = random.nextInt(5);
		while (corner < 1 && corner > 4) {
			corner = random.nextInt(5);
		}
		elements = new Enemy(getResources(), 2, 2, corner);
		count = 2;
		enemyList = new ArrayList<Enemy>();
		enemyList.add(elements);

		switch (MainMenu.level) {
		case Constants.EASIEST:
			maxEnemy = 2;
			break;
		case Constants.EASY:
			maxEnemy = 4;
			break;
		case Constants.NORMAL:
			maxEnemy = 6;
			break;
		case Constants.HARD:
			maxEnemy = 8;
			break;
		case Constants.HARDEST:
			maxEnemy = 10;
			break;

		default:
			maxEnemy = 6;
			break;
		}
	}

	public void setGameStateListener(GameStateListener listener) {
		gameStateListener = listener;
	}

	Handler gameHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Constants.GAME_OVER) {
				gameStateListener.gameFinished(true);
			}

			super.handleMessage(msg);
		}

	};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		canvasHeight = height;
		canvasWidth = width;

		if (isFirstTime) {
			xCoor = width / 2;
			yCoor = height / 2;
			isFirstTime = false;
		}

		player = new Player();
		// powerUp = new PowerUp(getResources());
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!viewThread.isAlive()) {
			viewThread = new ViewThread(panel);
			viewThread.setRunning(true);
			viewThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (viewThread.isAlive()) {
			viewThread.setRunning(false);
		}
	}

	public void animate() {
		if (player != null) {
			player.checkBorders();
			if (Player.isTouching) {
				gameHandler.obtainMessage(Constants.GAME_OVER).sendToTarget();
			}
		}
		if (enemyList != null) {
			for (Enemy elements : enemyList) {
				elements.animate();
			}
		}
		if (powerUp != null) {
			powerUp.animate();
		}
	}

	public void addEnemy() {

		if (!Enemy.isExploding) {
			if (enemyList.size() >= maxEnemy) {
				Enemy elements = enemyList.get(nextIndex);
				nextIndex++;
				if (nextIndex >= maxEnemy) {
					nextIndex = 0;
				}
				elements.incSpeed();
			} else {
				count++;
				Random random = new Random();
				corner = random.nextInt(5);
				while (corner < 1 && corner > 4) {
					corner = random.nextInt(5);
				}
				Enemy elements = new Enemy(getResources(), count, count, corner);
				enemyList.add(elements);
			}
		}
	}
	
	public void resetPlayerPostion(){
		xCoor = Panel.canvasWidth / 2;
		yCoor = Panel.canvasHeight / 2; 
	}

	public void doDraw(Canvas canvas) {

		if (powerUp == null) {
			Random random = new Random();
			if (random.nextInt(200) == 3) {
				powerUp = new PowerUp(getResources());
			}
		}
		canvas.drawColor(Color.DKGRAY);
		score = (((System.currentTimeMillis() - ViewThread.score) / 100) / 10)
				* 10 + Game.extraScore + bonus;

		// canvas.drawText("Score: " + score, 0, 20, paint);

		if (!Player.isTouching) {
			canvas.drawText("Score: " + score, 0, 20, paint);
			if (player != null) {

				if (xCoor - Player.width / 2 <= 0) {
					xCoor = Player.width / 2;
				} else if (xCoor + Player.width / 2 >= Panel.canvasWidth) {
					xCoor = Panel.canvasWidth - Player.width / 2;
				}

				if (yCoor - Player.height / 2 <= 0) {
					yCoor = Player.height / 2;
				} else if (yCoor + Player.height / 2 >= Panel.canvasHeight) {
					yCoor = Panel.canvasHeight - Player.height / 2;
				}

				player.doDraw(canvas, xCoor, yCoor);
			}
			if (enemyList != null) {
				if (clearList) {
					enemyList.clear();
					clearList = false;
				}
				for (Enemy elements : enemyList) {
					elements.doDraw(canvas);
				}
			}
			if (powerUp != null && !Player.isPowerUp) {
				powerUp.doDraw(canvas);
			} else {
				powerUp = null;
				Player.isPowerUp = false;
			}

		}
	}

	static boolean down = false;

	public boolean onTouchEvent(MotionEvent motionEvent) {

		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

			if ((motionEvent.getX() > xCoor - Player.width / 2 - 20)
					&& (motionEvent.getX() < xCoor + Player.width / 2 + 20)
					&& (motionEvent.getY() > yCoor - Player.height / 2 - 20)
					&& (motionEvent.getY() < yCoor + Player.height / 2 + 20)) {
				xCoor = (int) motionEvent.getX();
				yCoor = (int) motionEvent.getY();
				down = true;
			} else {
				down = false;
			}
		} else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
			if (down) {
				xCoor = (int) motionEvent.getX();
				yCoor = (int) motionEvent.getY();
			}
		}

		return true;
	}

}

package com.game.util;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ViewThread extends Thread{
	
	public Panel panel;
	public SurfaceHolder holder;
	public boolean isRunning;
	Canvas canvas;
	
	public static long score;
	
	private long startTime;
	private long elapsedTime;
	
	public ViewThread(Panel p){
		panel = p;
		holder = panel.getHolder();
		startTime = System.currentTimeMillis();
		score = System.currentTimeMillis();
	}
	
	public void setRunning(boolean b){
		isRunning = b;
	}
	
	public void run(){
		Canvas canvas = null;
		while(isRunning){
			canvas = holder.lockCanvas();
			if(canvas!=null){
				elapsedTime = System.currentTimeMillis();
				if(elapsedTime - startTime >= 3000){
					panel.addEnemy();
					startTime = elapsedTime;
				}
				panel.animate();
				panel.doDraw(canvas);
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
}

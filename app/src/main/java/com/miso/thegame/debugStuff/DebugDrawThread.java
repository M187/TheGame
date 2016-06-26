package com.miso.thegame.debugStuff;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by michal.hornak on 26.06.2016.
 */
public class DebugDrawThread extends Thread {

    public static Canvas canvas;
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private DebugSurfaceView gamePanel;
    private boolean running;
    private int logIterator = 0;

    public DebugDrawThread(SurfaceHolder surfaceHolder, DebugSurfaceView gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        long frameCount = 0;
        long targetTime = 1000 / FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch (Exception e) {
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            logIterator++;
            if (frameCount == FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;

                if (logIterator == FPS * 10) {
                    System.out.println(averageFPS);
                    logIterator = 0;
                }
            }

        }
    }

    public void setRunning(boolean b) {
        running = b;
    }
}

package com.miso.thegame.gameMechanics;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.miso.thegame.gameViews.GameView2;

/**
 * Created by Miso on 8.10.2015.
 */
public class MainGameThread extends Thread {

    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GameView2 gamePanel;
    private boolean running;
    public static Canvas canvas;
    private int logIterator = 0;

    public MainGameThread(SurfaceHolder surfaceHolder, GameView2 gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        long frameCount = 0;
        long targetTime = 1000/FPS;

        while(running)
        {
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    long startTime2, finishTime, updateFinished, drawFinished;

                    //System.out.println("  --  --  --  --  --  -- START --  --  --  --  --  --  ");
                    //startTime2 = System.nanoTime();
                    //System.out.println("Before update: " + startTime2);

                    this.gamePanel.update();

                    //updateFinished = System.nanoTime();
                    //System.out.println("Update duration: " +  (updateFinished - startTime2));

                    this.gamePanel.draw(canvas);

                    //drawFinished = System.nanoTime();
                    //System.out.println("Draw duration: " + (drawFinished - updateFinished));

                    this.gamePanel.postDrawTasks();

                    //finishTime = System.nanoTime();
                    //System.out.println("PostDraw duration: " + (finishTime - drawFinished));
                    //System.out.println("Cycle ends: " + (finishTime - startTime2));
                    //System.out.println("  --  --  --  --  --  --  --  --  --  --  --  --  ");
                }
            }catch(Exception e){
                //System.out.println(e);
                e.printStackTrace();
            }
            finally {
                if(canvas != null)
                {
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch(Exception e){e.printStackTrace();}
                }
            }

            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;

            try{
                this.sleep(waitTime);
            }catch(Exception e){}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            logIterator++;
            if(frameCount == FPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;

                if (logIterator == FPS * 10){
                    System.out.println(averageFPS);
                    logIterator = 0;
                }
            }

        }
    }

    public void setRunning(boolean b)
    {
        running = b;
    }
}

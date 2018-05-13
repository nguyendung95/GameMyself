package com.example.hoshiko.gamemyself;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.webkit.WebHistoryItem;

class MainThread extends Thread {

    /**
     * What is FPS ?
     * FPS = Frames per second
     * A unit that measures display device performance.
     * It consists of the number of complete scans of the display screen that occur each second.
     * A good thread runs between 30 and 60 FPS
     * more FPS more cpu demanding.
     */
    private int FPS = 30;

    /**
     * Every second the thread is going to run 30 frames.
     * What if we need play the game for 3 mins or 2 hours ?
     */
    private double averageFPS;

    private SurfaceHolder surfaceHolder;

    private GamePanel gamePanel;

    private boolean running;

    /**
     * What is Canvas ?
     * The Canvas class holds the "draw" calls.
     * To draw something, you need 4 basic components: A Bitmap to hold the pixels, a Canvas
     * to host the draw calls (writing into the bitmap),
     * a drawing primitive (e.g. Rect, Path, text, Bitmap), and
     * a paint (to describe the colors and styles for the drawing).
     */
    public static Canvas canvas;


    /**
     * For our thread constructor we need references of our Content view Object
     *
     * @param surfaceHolder
     * @param gamePanel
     */
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {

        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

    }

    @Override
    public void run() {
        /**
         * Inside the run method, we want every second to catch the 30 frames
         */
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;

        /**
         * What is target time?
         * We have 30 frames but how long a frames is going to last in our screen?
         * 1000 millis / 30 frames = 33.3 millis per frame.
         */
        long targetTime = 1000 / FPS;


        while (running) {

            /**
             * SET THE TIME
             * Now our system timer start to count in nanos
             *1 millis  = 1.000.000 nanos
             */
            startTime = System.nanoTime();

            canvas = null;

            try{
                //Lock canvas to our content view
                canvas = this.surfaceHolder.lockCanvas();

                /**
                 * Synchronous every time we update or draw anything on our screen in every frame
                 * so our game will flow naturally
                 */
                synchronized (surfaceHolder){
                    /**
                     * This is the game data update as for example the x & y position coordinates
                     * for a little character (Sprite position, Score base in time ...)
                     *
                     */
                    this.gamePanel.update();

                    /** This is about drawing the picture you see in the screen.
                     * When this method is called repeatedly it gives you perception of a movie
                     * or of an animation.
                     */

                    this.gamePanel.draw(canvas);
                }

            }catch (Exception exception) {

            }
            finally {

                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime)/1000000;

            /**
             * What is wait time ?
             * The time we will wait till the next frame enter the loop
             */
            waitTime = timeMillis - targetTime;

            try {
                this.sleep(waitTime);

            } catch (Exception e){

            }

            totalTime +=  System.nanoTime() - startTime;
            frameCount++;


            /**
             * Every game loop is for 1 sec. We need do calculate the averrageFPS
             * We know that if all are perfect in our system
             * every frame gonna last for 33.33 millis. But not all cpu's are
             * working at the same or all have the same power.
             */
            if (frameCount == FPS){
                averageFPS =  1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0 ;
                Log.i("MAIN THREAD", "Average FPS is: "+ averageFPS);
            }


        }

    }

    public void setRunning(boolean run){
        this.running = run;

    }
}

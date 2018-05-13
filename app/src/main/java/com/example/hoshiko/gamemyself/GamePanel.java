package com.example.hoshiko.gamemyself;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static com.example.hoshiko.gamemyself.MainThread.canvas;

class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Random random = new Random();

    //Set game screen width & height
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;

    public static final int MOVESPEED = -5;

    // An object reference of the Background class
    private Background bg;

    private Hero hero;

    /**
     * List for our bullet
     */
    private ArrayList<Bullet> bullets;
    private long bulletStartTime;


    //set refs for an ArrayList  and the Enemy Timers
    private ArrayList<Enemy> alien;
    private long alienStartTime;

    // set refs for an Obstacle
    private ArrayList<Obstacle> obstacles;
    private long obstacleStartTime;

    // add border bottom
    private ArrayList<BorderBottom> borderBottoms;
    private long borderStartTime;

    //Create explosion
    private Explosion explosion;
    private  int best;


    //Create heart
    Bitmap heartA;
    Bitmap heartB;
    Bitmap heartC;
    private int hearts = 3;

    // music and sound
    MediaPlayer mp;
    SoundPool exSound ;
    private int exSoundID;

    // Reset the game
    private boolean newGameCreated;

    private long startReset;

    private boolean reset;

    // to Dissapear the hero when we lose
    // and appear again when we reset the newgame()
    private boolean dissapear;

    private boolean started;

    private Context context;

    private MainThread thread;
    // Create constructor of our new class,
    // that is helping us calling objects and methods.


    public GamePanel(Context context) {
        /**
         * What is Context?
         * As a name suggests, it's the Context of the current state of the Application/Objects
         * It lets newly-created object understand.
         * what has been going on. Typically you call it to get information regarding another part of program.
         */
        super(context);

        // game music
        mp = MediaPlayer.create(context, R.raw.digital);
        exSound = new SoundPool(99, AudioManager.STREAM_MUSIC,0);
        exSoundID =  exSound.load(context, R.raw.gun, 1);;


        this.context = context;

        // Add the callback to surfaceholder to intercept events
        getHolder().addCallback(this);

        // Make GamePanel focusable so it can handle events
        setFocusable(true);
    }


    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should starts up whatever rendering code they desire
     * Note that only one thread can ever draw into Surface
     * so you should not draw into the Surface here if you normal rendering will be in another thread.
     *
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        //Initialize new thread object
        thread = new MainThread(getHolder(), this);

        // Draw background
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));

        //Create my hero
        hero = new Hero(BitmapFactory.decodeResource(getResources(), R.drawable.hero), 60, 90, 5);

        //Create bullets
        bullets = new ArrayList<Bullet>();
        bulletStartTime = System.nanoTime();

        //create the enemy obj
        alien = new ArrayList<Enemy>();
        alienStartTime = System.nanoTime();


        //create Obstacle obj
        obstacles = new ArrayList<Obstacle>();
        obstacleStartTime = System.nanoTime();

        // create object bottom  border
        borderBottoms = new ArrayList<BorderBottom>();
        borderStartTime = System.nanoTime();

        // Safely start game loop
        thread.setRunning(true);
        thread.start();

    }

    /**
     * This is called immediately after any structural changes (format or size)
     * have been made to the surface.
     *
     * @param surfaceHolder
     * @param i
     * @param i1
     * @param i2
     */

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    /**
     * This is called immediately before a surface being destroyed.
     * After returning from this call, you  should no longer try to access this Surface.
     * If you have a rendering thread that directly accesses the surface, you must ensure
     * that thread is no longer touching the Surface before returning this function.
     *
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        int counter = 0 ;
        while (retry && counter < 1000 ) {
            try {

                thread.setRunning(false);
                /**
                 * The join() method is used to hold the execution of currently running
                 * thread until the specified thread is dead (finished execution).
                 */
                thread.join();

                // we need set thread to null
                // so that the garbage collector can pick the object
                retry = false;
                thread = null;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Also we need override onTouchEvent method to know if we touch or not the screen.
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (!hero.getPlaying() && newGameCreated && reset) {

                hero.setPlaying(true);
                hero.setUp(true);

            }

            if (hero.getPlaying()) {
                if (!started) started = true;

                reset = false;
                hero.setUp(true);
            }
            return true;

        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            hero.setUp(false);
            Toast.makeText(context, "up touch ", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onTouchEvent(event);
    }


    public void update() {
        //Update hero action every second
        if (hero.getPlaying()) {

            //mp.start();

            bg.update();

            hero.update();

            ////////////
            /////////////////
            //add bottom border behavior
/*            long borderElapsed = (System.nanoTime() - borderStartTime) / 1000000;

            if(borderElapsed > 100){
                borderBottoms.add(new BorderBottom(BitmapFactory.decodeResource(getResources(),
                        R.drawable.border_bottom), WIDTH+10, (HEIGHT-80)+random.nextInt(10)));

                //reset Timer
                obstacleStartTime = System.nanoTime();
            }

            //Loop thought every alien and check collision and remove
            for (int i=0; i<borderBottoms.size(); i++) {

                //update obstacle
                borderBottoms.get(i).update();

                // Check collision between Obstacles and Hero
                if (isCollision(borderBottoms.get(i), hero)) {

                    hero.setPlaying(false);
                    break;
                }

                //remove border if is of the limits of screen
                if(borderBottoms.get(i).getX() < 10){
                    borderBottoms.remove(i);
                }
            }*/

            ////////////////
            ////////////////////

            //Obstacle update
            long obstacleElapsed = (System.nanoTime() - obstacleStartTime) / 1000000;

            if (obstacleElapsed > (15000 - hero.getScore() / 4)) {

                // Bot obstacle apprear
                obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.obstacle),
                        WIDTH + 10, HEIGHT - 290 + random.nextInt(150),
                        90, 300, hero.getScore(), 1));

                // reset timer
                obstacleStartTime = System.nanoTime();

            }

            //Loop thought every alien and check collision and remove
            for (int i = 0; i < obstacles.size(); i++) {

                //update obstacle
                obstacles.get(i).update();

                // Check collision between Obstacles and Hero
                if (isCollision(obstacles.get(i), hero)) {

                    hero.setPlaying(false);
                    break;
                }
            }


            /**
             * Bullet on timer
             */
            long bulletTimer = (System.nanoTime() - bulletStartTime) / 1000000;
            // check the delay among bullets fired from the hero
            // in simple words when a bullet will appear on the screen
            // every sec, our bullet to be faster than the previous one.
            if (bulletTimer > (2500 - hero.getScore() / 4)) {
                bullets.add(new Bullet((BitmapFactory.decodeResource(getResources(), R.drawable.bullet)),
                        hero.getX() + 60, hero.getY() + 55, 15, 7, 3));
                bulletStartTime = System.nanoTime();
            }

            // Loop to animate & update the frame of the bullet image
            for (int j = 0; j < bullets.size(); j++) {
                bullets.get(j).update();

                //Remove the bullet if  is of the screen limit
                if (bullets.get(j).getX() < -10) {
                    bullets.remove(j);
                }
            }

            // Behavior / movement of the enemy

            // Set alien timer to millis
            long alienElapsed = (System.nanoTime() - alienStartTime) / 1000000;

            // the first enemy appear after 10 sec
            // then more often by the time
            if (alienElapsed > (10000 - hero.getScore()) / 4) {

                alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemy),
                        WIDTH + 10, (int) (random.nextDouble() * (HEIGHT - 50)),
                        60, 100, hero.getScore(), 4));

                // reset timer
                alienStartTime = System.nanoTime();
            }


            // loop thought every alien
            for (int i = 0; i < alien.size(); i++) {

                //update alien
                alien.get(i).update();

                if (isCollision(alien.get(i), hero)) {

                    explosion = new Explosion(BitmapFactory.decodeResource(getResources(),
                            R.drawable.explosion), hero.getX(), hero.getY(),
                            200, 200, 15);

                    alien.remove(i);

                    //Lose a life if collide
                    hearts --;
                   // hero.setPlaying(false);
                    break;
                }

                //remove alien if it is way off the screen
                if (alien.get(i).getX() < -100) {
                    alien.remove(i);
                    break;
                }

                // collision alien with bullet (fire)
                for (int j = 0; j < bullets.size(); j++) {

                    if (isCollision(bullets.get(j), alien.get(i))) {
                        // Explosion if we hit an alien

                        //play sound of explosion
                        exSound.play(exSoundID, 5,5,1,0,1);

                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(),
                                R.drawable.explosion), alien.get(i).getX(), alien.get(i).getY(),
                                200, 200, 14);


                        alien.remove(i);
                        bullets.remove(j);


                        best+=30;
                        break;
                    }
                    bullets.get(j).update();
                }
            }


        } // end if playing

        else {

            hero.resetDYA();
            if (!reset) {

                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;

                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),
                        R.drawable.explosion), hero.getX(), hero.getY(),
                        200, 200, 15);
            }

            explosion.update();

            long resetElapsed = (System.nanoTime() - startReset) / 1000000;
            // reset game
            if (resetElapsed > 2500 && !newGameCreated) {
                newGame();
            }

        }


    } // end update

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            bg.draw(canvas);
            if (!dissapear) {

            }
            //Draw hero
            hero.draw(canvas);

            // Draw Bullet
            for (Bullet bullet : bullets) {
                bullet.draw(canvas);
            }


            // draw enemy
            for (Enemy alien : alien) {
                alien.draw(canvas);
            }

            // draw obstacle
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(canvas);
            }

            // draw bottom borders
            for (BorderBottom borderBottom : borderBottoms) {
                borderBottom.draw(canvas);
            }

            //draw explosion
            if(started){

                explosion.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState);
        }

    }

    // check collision
    public boolean isCollision(GameObject a, GameObject b) {

        if (Rect.intersects(a.getRectagle(), b.getRectagle())) {
            return true;
        }
        return false;

    }


    // New game method
    public void newGame() {

        dissapear = false;
        alien.clear();
        obstacles.clear();
        bullets.clear();

        hero.resetDYA();
        hero.resetScore();

        hero.setY(HEIGHT / 2);


        //we also need the newgamcreated var to be true so we can play
        newGameCreated = true;

    }

    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Distance: " + (hero.getScore()*2), 10, HEIGHT-10, paint);
        canvas.drawText("Score: " + best, WIDTH-150, HEIGHT-10, paint);

        //update hearts
        if(hearts == 3){
            heartA = BitmapFactory.decodeResource(getResources(), R.drawable.lifea);
            canvas.drawBitmap(heartA, WIDTH/2 - 120, 0, null);

            heartB = BitmapFactory.decodeResource(getResources(), R.drawable.lifeb);
            canvas.drawBitmap(heartA, WIDTH/2 - 80, 0, null);

            heartC = BitmapFactory.decodeResource(getResources(), R.drawable.lifec);
            canvas.drawBitmap(heartA, WIDTH/2 - 40, 0, null);

        }

        else if(hearts == 2){
            heartA = BitmapFactory.decodeResource(getResources(), R.drawable.lifea);
            canvas.drawBitmap(heartA, WIDTH/2 - 120, 0, null);

            heartB = BitmapFactory.decodeResource(getResources(), R.drawable.lifeb);
            canvas.drawBitmap(heartA, WIDTH/2 - 80, 0, null);

        }
        else if (hearts == 1){

            heartA = BitmapFactory.decodeResource(getResources(), R.drawable.lifea);
            canvas.drawBitmap(heartA, WIDTH/2 - 120, 0, null);
        }
        else {
            hero.setPlaying(false);
            hearts = 3;
        }
    }
}

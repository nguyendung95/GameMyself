package com.example.hoshiko.gamemyself;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.net.PortUnreachableException;

public class Hero extends GameObject {

    public static final String TAG = "HERO";

    // Player image is gonna be 2 images actually
    private Bitmap spriteSheet ;

    private int score ;

    // y-cord value every time we touch in the screen
    private double dya;

    // To know hero is going up or down
    private boolean up ;

    // To know if we start the game ?
    private  boolean playing ;


    private Animation animation = new Animation();
    private long startTime;

    public Hero(Bitmap heroRes, int width, int height, int numberFrames){

        //Inherited from ObjectGame Class
        x = 100;
        y = GamePanel.HEIGHT/2;


        // Initiate the dy position
        // Our hero is going to move up or down
        // press the screen the hero will go up
        dy = 0;
        //dya = 0 ;

        // Score var in the beginning  is going to be 0
        score = 0;

        // Width and height of the image so we can create a bitmap
        // Inherited from GameObject class
        this.width = width;
        this.height = height;

        Bitmap[] images = new Bitmap[numberFrames];
        spriteSheet = heroRes;

        for(int i=0; i<images.length; i++){
            // Hero image has 3 frames so [0] is the first frame, [1] is the second
            // And [2] is the third frame
            images[i] = Bitmap.createBitmap(spriteSheet, i*width, 0, width, height);
        }

        //Set animation & delay time
        animation.setFrames(images);
        animation.setDelayTime(10);

        //Initiate the timer so we can use update method
        startTime = System.nanoTime();


    };

    //Create a boolean method to know if we press the screen and hero goes up
    public void setUp(boolean b){
        this.up = b;

    }


    public void update(){
        // The timer of our player millis
        long elapsed = (System.nanoTime() - startTime)/ 1000000;

        // When our timer get past 100 millis we want the score to auto increment by the time
        if(elapsed>100){
            score++;
            startTime = System.nanoTime();
        }

        animation.update();

        // Set the speed of our hero when goes up are falling with help of dyTouch variable
        if(up == true){
            dy = (int) (dya -= 0.1);
            Log.d(TAG, "Now, this is dya (event touch of user)  = " + dya);
            Log.d(TAG, "Now, this is dy = " + dy);
        }
        else if(up == false){
            dy = (int) (dya += 0.1);
            Log.d(TAG, "Now, this is dya (event touch of user)  = " + dya);
            Log.d(TAG, "Now, this is dy = " + dy);
        }

        //Set the limit if the player press all the time in screen (Speed Limitation)
        if(dy > 3) {
            Log.d(TAG, "IF DY > 14");
            dy = 3;
        }
        if (dy < -3) {
            Log.d(TAG, "IF DY < -14");
            dy = -3;
        }

        if(y < 50){
            y = 50;
        }
        else if(y>430){
            y = 430;
        }
        y += dy*2;
        Log.d(TAG, "this is y-cord value: " + y);
        dy = 0;

     } // end update;



    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    };


    public int getScore(){return this.score;}
    public boolean getPlaying (){return  this.playing; }
    public void setPlaying(boolean play) {this.playing = play; }
    public void resetDYA () { this.dya = 0;  }
    public void resetScore() {this.score = 0 ;}
}

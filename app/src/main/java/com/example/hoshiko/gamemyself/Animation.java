package com.example.hoshiko.gamemyself;

import android.graphics.Bitmap;

/**
 * This class will help us to animate the images through their sprite sheet
 */

public class Animation {

    // A bitmap table keep the numbers of the image frames [0]frame to [n]frame
    private Bitmap[] frames;

    // To know in witch frame of the image we're every time
    private int currentFrame;

    // Timer for animation
    private long startTime;

    // Delay between the frames (How fast animation is gonna be)
    private long delayTime;

    // Boolean for the images that are going to animate once in our screen
    // Ex: the explosion images we want to happened once when we kill an enemy
    private boolean isOnce;


    // Constructor for Animation class
    public void setFrames(Bitmap[] frames) {

        this.frames = frames;

        // Every image we'll start from 1st image/sprite
        currentFrame = 0;

        // We set the time of our animation to our system timer
        startTime = System.nanoTime();
    }

    // Set delay time
    public void setDelayTime(long delayTime) {

        this.delayTime = delayTime;
    }

    // Set current frame
    public void setCurrentFrame(int currFrame) {
        this.currentFrame = currFrame;
    }

    // Get current frame
    public int getCurrentFrame(){
        return  currentFrame;
    }

    public boolean getIsOnce(){
        return  this.isOnce;
    }

    public void update() {
        /**
         * This timer determines witch frame of the images is gonna be return every time
         * In General we use Timers in our game to determine what time an object will appear
         * in our screen and the meantime what actions will do
         * All objects (hero, enemy...) have a timer
         */
        long elapsedTimer = (System.nanoTime() - startTime) / 1000000;


        /**
         * Set the delay of animation hero to 10 millis
         * Want to start animation after 10 millis when we start the game.
         * If delayTime value is big then our animation is gonna be very slow
         */
        if (elapsedTimer > delayTime) {
            currentFrame++;
            startTime = System.nanoTime();
        }

        if (currentFrame == frames.length) {
            currentFrame = 0;
            isOnce = true;
        }
    }


    public Bitmap getImage(){
        return frames[currentFrame];

    }




}

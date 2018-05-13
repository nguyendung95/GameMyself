package com.example.hoshiko.gamemyself;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Background {
    /**
     * Use the Bitmap class along side with Canvas
     */
    private Bitmap bitmapImage;
    private int x, y, dx;


    public Background(Bitmap res) {
        this.bitmapImage = res;
        this.dx = GamePanel.MOVESPEED ;
    };

    public void update(){
        /**
         * Suppose we want to our bg image scroll to the left every time
         * with that sceptic we will give the illusion of a continuous bg and movements
         */

        //Change the x-cord of our image
        x += dx;

        /**
         * If the bg start to move out of the screen
         */
        if(x <- GamePanel.WIDTH){
            x = 0 ;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmapImage, x, y, null);

         //If the bg is of the limits of screen screen
        if( x<0 ) {
            canvas.drawBitmap(bitmapImage, x+GamePanel.WIDTH, y, null);
        }

    }

    // New method for dx
    // Need a method to check every time the new value of x-cord
    /*public void setVectorX (int dx) {
        this.dx = dx;
    }*/


}

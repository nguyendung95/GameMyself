package com.example.hoshiko.gamemyself;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Bullet extends GameObject {

    private int speed;

    //the animation to animate the bullet image
    private Animation animation = new Animation();

    // a bitmap reference of the image
    Bitmap spriteSheet;

    public Bullet(Bitmap res, int x, int y, int w, int h, int numberFrames){
        super.x = x;
        super.y = y;
        width = w;
        height = h;

        //the first bullet is gonna havev 13 speed
        speed = 13;

        //We need all the frames of our bullet
        Bitmap[] images = new Bitmap[numberFrames];
        spriteSheet = res;

        for(int i=0; i<images.length; i++){
            // Hero image has 3 frames so [0] is the first frame, [1] is the second
            // And [2] is the third frame
            images[i] = Bitmap.createBitmap(spriteSheet, 0, i*height, width, height);
        }


        // then we have all the info of the image and we can do the animation
        animation.setFrames(images);

        // we set delay of the animation between the frames
        animation.setDelayTime(120-speed);

    } // end constructor

    public void update(){
        //every sec we want to change the speed of bullet
        x+= speed-4;

        animation.update();

    } //end update

    public void draw(Canvas canvas){

        //draw the bullet on the screen
        try{
            canvas.drawBitmap(animation.getImage(), x-30, y, null);

        }catch (Exception e){

        }

    }// end draw

}

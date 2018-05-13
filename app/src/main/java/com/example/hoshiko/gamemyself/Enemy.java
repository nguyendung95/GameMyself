package com.example.hoshiko.gamemyself;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Enemy extends GameObject {

    //hit an enemy increase the score
    private int score;

    // spreed of enemy
    private int speed;

    //random clsss (enemy have different speeeds)
    private Random random = new Random();

    //animation classs
    Animation animation = new Animation();

    //bitmap ref of the image
    private Bitmap spriteSheet;


    public Enemy(Bitmap res, int x, int y, int w, int h, int s, int numberFrames) {

        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        //we set every game loop  a  diff speed for the enemy
        speed = 4 + (int)  (random.nextDouble()*score/30);

        // Limit for the max speed
        if(speed > 40) speed =40;

        //
        Bitmap[] images = new Bitmap[numberFrames];
        spriteSheet = res;

        for(int i=0; i<images.length; i++){
            images[i] = Bitmap.createBitmap(spriteSheet, i*width, 0, width, height);
        }

        animation.setFrames(images);
        // Set the delay of animation
        animation.setDelayTime(100-speed);


    }

    public void update() {

        // The enemy will move closer every sec to the hero
        x-=speed;

        animation.update();

    }

    public void draw(Canvas canvas) {

        //Draw the enemy
        try{

            canvas.drawBitmap(animation.getImage(), x, y, null);

        }catch (Exception e){};

    }

    @Override
    public int getWidth() {

        //offset slightly for more realistic collision detection
        return  width -10;
    }
}

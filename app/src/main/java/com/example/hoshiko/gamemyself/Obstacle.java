package com.example.hoshiko.gamemyself;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

public class Obstacle extends GameObject {

    private int score;
    private int speed;
    private Random random = new Random();
    private Animation animation = new Animation();
    private Bitmap spriteSheet;

    public Obstacle(Bitmap res, int x, int y, int w, int h, int s, int numberFrames){

        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        speed = 11;

        Bitmap[] images = new Bitmap[numberFrames];
        spriteSheet = res;


        for(int i=0; i<images.length; i++){
            images[i] = Bitmap.createBitmap(spriteSheet, i*width, 0, width, height);
        }

        animation.setFrames(images);
        animation.setDelayTime(100-speed);
    };

    public void update(){
        x -=speed;
        animation.update();

    };

    public void draw(Canvas canvas){

        try{
            canvas.drawBitmap(animation.getImage(), x, y, null);


        }catch (Exception e){};

    }


    @Override
    public int getWidth() {
        return width -10;
    }
}

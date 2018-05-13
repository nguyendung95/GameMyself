package com.example.hoshiko.gamemyself;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Explosion {

    private int x;
    private  int y;
    private int width;
    private int height;
    private int row;

    private Animation animation = new Animation();
    private Bitmap spriteSheet;

    public Explosion (Bitmap res, int x, int y, int w, int h, int numberFrames){

        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        Bitmap[] images = new Bitmap[numberFrames];
        spriteSheet = res;

        for(int i=0; i<images.length; i++){
            if(i%5==0 && i>0) row++;
            images[i] = Bitmap.createBitmap(spriteSheet, (i-(5*row))*width, row*height, width, height);
        }
        animation.setFrames(images);
        animation.setDelayTime(7);
    }


    public void update(){

        if(!animation.getIsOnce()){
            animation.update();
        }
    }

    public void draw(Canvas canvas){

        if(!animation.getIsOnce()){

            canvas.drawBitmap(animation.getImage(), x, y, null);
        }
    }


    public int getHeight(){
        return  height;
    }

}

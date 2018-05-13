package com.example.hoshiko.gamemyself;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BorderTop extends GameObject {

    private Bitmap image;

    public BorderTop(Bitmap res, int x, int y, int h){

        width = 20;

        //get the cords from superclass GameObject
        height = h;
        this.x = x;
        this.y = y;

        //set the dx var to have the movespeed value
        dx = GamePanel.MOVESPEED;

        //creating the image
        image = Bitmap.createBitmap(res, 0,0, width, height);
    }

    public void update(){
        x+=dx;

    };
    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    };
}


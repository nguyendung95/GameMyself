package com.example.hoshiko.gamemyself;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BorderBottom extends GameObject {

    private Bitmap image;

    public BorderBottom(Bitmap res, int x, int y){
        width = 200;
        height = 100;


        //get the cords from superclass GameObject
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

package com.arkumbra.examplegame.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.arkumbra.examplegame.drawable.GameDrawable;


/**
 * Created by lukegardener on 2017/07/28.
 */

public class DummyGameObject implements GameEntity, GameDrawable {

    private static final float UNITS_PER_SECOND_MOVE = 200;
    private static final float SIZE = 20;

    private Paint paint;

    private float screenWidth;
    private float screenHeight;

    private float xPos;
    private float yPos;
    private float angle;

    private int xDir = 1;
    private int yDir = 1;

    public DummyGameObject(float screenWidth, float screenHeight, float angle) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        paint = new Paint();
        paint.setColor(Color.WHITE);

        this.xPos = 300;
        this.yPos = 300;
        this.angle = angle;
    }

    @Override
    public void update(long msSinceLastUpdate) {
        if (xPos + SIZE >= screenWidth || xPos - SIZE <= 0)
            xDir *= -1;
        if (yPos + SIZE >= screenHeight || yPos - SIZE <= 0)
            yDir *= -1;


        float unitsToMove = UNITS_PER_SECOND_MOVE * ((float)msSinceLastUpdate/1000f);

        xPos += unitsToMove * xDir * angle;
        yPos += unitsToMove * yDir;
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawRect(xPos, yPos, xPos + SIZE, yPos + SIZE, paint);
    }
}

package com.arkumbra.brickout.view.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.arkumbra.brickout.engine.GameEngine;
import com.arkumbra.brickout.engine.collision.AABB;
import com.arkumbra.brickout.engine.entity.GameDrawable;
import com.arkumbra.brickout.engine.touch.MotionEventHandler;

/**
 * Created by lukegardener on 2017/08/10.
 */

public class PowerupButton implements GameDrawable, MotionEventHandler {

    // 00E6F0
    private Paint paint = new Paint();
    private Paint outlinePaint = new Paint();
    private AABB aabb;
    private GameEngine gameEngine;

    public PowerupButton(GameEngine gameEngine, AABB aabb) {
        this.gameEngine = gameEngine;
        this.aabb = aabb;

//        this.paint.setColor(Color.rgb(0,230,240));
        this.outlinePaint.setColor(Color.rgb(255, 45, 62));
        this.paint.setColor(Color.rgb(59, 67, 80));
    }

    @Override
    public void draw(Canvas canvas) {

        //
        canvas.drawCircle(aabb.getXMidpoint(),
                aabb.getYMidpoint(),
                (aabb.getMaxX() - aabb.getMinX()) / 2,
                outlinePaint);

        canvas.drawCircle(aabb.getXMidpoint(),
                aabb.getYMidpoint(),
                ((aabb.getMaxX() - aabb.getMinX()) / 2) - 15,
                paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

}

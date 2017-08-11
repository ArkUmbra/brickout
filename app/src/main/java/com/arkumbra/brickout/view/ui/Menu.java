package com.arkumbra.brickout.view.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.arkumbra.brickout.R;
import com.arkumbra.brickout.engine.GameEngine;
import com.arkumbra.brickout.engine.GameState;
import com.arkumbra.brickout.engine.LevelStateHandler;
import com.arkumbra.brickout.engine.collision.AABB;
import com.arkumbra.brickout.engine.entity.GameDrawable;
import com.arkumbra.brickout.engine.entity.Position;
import com.arkumbra.brickout.engine.touch.MotionEventHandler;
import com.arkumbra.brickout.view.FontUtils;

/**
 * Created by lukegardener on 2017/08/10.
 */

public class Menu implements GameDrawable, MotionEventHandler {

    private final Paint menuPaint = new Paint();

    private final GameEngine gameEngine;
    private final AABB menuButtonBounding;
    private final Position menuBtnDrawPos;
    private final float screenWidth;
    private final float screenHeight;

    private boolean isPaused;

    public Menu(GameEngine gameEngine, AABB menuButtonBounding, Position menuBtnDrawPos,
                float screenWidth, float screenHeight) {

        this.gameEngine = gameEngine;
        this.menuButtonBounding = menuButtonBounding;
        this.menuBtnDrawPos = menuBtnDrawPos;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;


        this.menuPaint.setColor(Color.rgb(255, 45, 62));
        this.menuPaint.setTextAlign(Paint.Align.LEFT);
        this.menuPaint.setTypeface(FontUtils.getAppTypeface());
        int screenScaledFontSize = gameEngine.getContext().getResources().getDimensionPixelSize(R.dimen.scoreFontSize);
        this.menuPaint.setTextSize(screenScaledFontSize);
    }

    // TODO move all bounding and draw definitions outside to ControlsUI class
    @Override
    public void draw(Canvas canvas) {
        canvas.drawText("MENU", menuBtnDrawPos.getX(), menuBtnDrawPos.getY(), menuPaint);

//        canvas.drawText("---", menuBtnDrawPos.getX(), menuBtnDrawPos.getY(), menuPaint);
//        canvas.drawText("---", menuBtnDrawPos.getX(), menuBtnDrawPos.getY() - 15, menuPaint);
//        canvas.drawText("---", menuBtnDrawPos.getX(), menuBtnDrawPos.getY() - 30, menuPaint);


//        canvas.drawRect(menuButtonBounding.getMinX(), menuButtonBounding.getMinY(),
//                menuButtonBounding.getMaxX(), menuButtonBounding.getMaxY(), p
//                ausePaint);
//
        if (isPaused) {
//            drawFullMenuOptions();
        }
        // TODO bitmap
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP)
            return false;


        if (touchEventIsOnThisButton(motionEvent)) {
            togglePauseButton();
            return true;
        }

        return false;
    }

    private boolean touchEventIsOnThisButton(MotionEvent motionEvent) {
        return ClickDetector.isTouchPointOverBoundingBox(
                menuButtonBounding, motionEvent.getX(), motionEvent.getY());
    }

    private void togglePauseButton() {
        if (isPaused) {
            this.isPaused = false;
            this.gameEngine.onStateChange(GameState.UNPAUSE);
        } else {
            this.isPaused = true;
            this.gameEngine.onStateChange(GameState.PAUSE);
        }
    }
}

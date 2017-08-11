package com.arkumbra.brickout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.arkumbra.brickout.engine.GameEngine;
import com.arkumbra.brickout.engine.LevelStateHandler;
import com.arkumbra.brickout.engine.collision.AABB;
import com.arkumbra.brickout.engine.entity.GameDrawable;
import com.arkumbra.brickout.engine.entity.Position;
import com.arkumbra.brickout.engine.touch.MotionEventHandler;
import com.arkumbra.brickout.view.ui.Menu;
import com.arkumbra.brickout.view.ui.PowerupButton;
import com.arkumbra.brickout.view.ui.Score;
import com.arkumbra.brickout.view.ui.ScoreBackground;

/**
 * Created by lukegardener on 2017/08/06.
 */

public class ControlsUI implements GameDrawable, MotionEventHandler {

    // 9DBDC6
//    private final Paint uiBackgroundOutlinePainter = new Paint();


    private final GameEngine gameEngine;

    private final ScoreBackground scoreBackground; // Move inside score?
    private final Score score;
    private final Menu menu; // TODO change to menu
    private final PowerupButton powerupButton;

//    private final float xMin;
//    private final float yMin;
//    private final float xMax;
//    private final float yMax;

    private final float canvasWidth;
    private final float canvasHeight;

    public ControlsUI(Context context, GameEngine gameEngine, float canvasWidth, float canvasHeight) {
        this.gameEngine = gameEngine;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
//        this.xMin = xMin;
//        this.yMin = yMin;
//        this.xMax = xMax;
//        this.yMax = yMax;

        this.scoreBackground = new ScoreBackground(canvasWidth, canvasHeight);
        this.score = new Score(canvasWidth, canvasHeight, gameEngine);
        this.menu = createMenuButton(gameEngine, canvasWidth, canvasHeight);
        this.powerupButton = createPowerupButton(gameEngine, canvasWidth, canvasHeight);

    }

    private Menu createMenuButton(GameEngine gameEngine,
                                   float canvasWidth, float canvasHeight) {

        Position min = new Position(canvasWidth * 0.0f, canvasHeight * 0.925f);
        Position max = new Position(canvasWidth * 0.25f, canvasHeight * 1.00f);
        AABB pauseBoundingBox = new AABB(min, max);

        Position drawPosition = new Position(canvasWidth * 0.02f, canvasHeight * 0.982f);

        return new Menu(gameEngine, pauseBoundingBox, drawPosition, canvasWidth, canvasHeight);
    }


    private PowerupButton createPowerupButton(GameEngine gameEngine,
                                              float canvasWidth, float canvasHeight) {

        Position min = new Position(canvasWidth * 0.43f, canvasHeight * 0.92f);
        Position max = new Position(canvasWidth * 0.57f, canvasHeight * 1.00f);
        AABB boundingBox = new AABB(min, max);

        return new PowerupButton(gameEngine, boundingBox);
    }


    @Override
    public void draw(Canvas canvas) {
        this.scoreBackground.draw(canvas);
        this.score.draw(canvas);
        this.menu.draw(canvas);
        this.powerupButton.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (menu.onTouchEvent(motionEvent)) {
            return true;
        }

        if (powerupButton.onTouchEvent(motionEvent)) {
            return true;
        }

        return false;
    }
}

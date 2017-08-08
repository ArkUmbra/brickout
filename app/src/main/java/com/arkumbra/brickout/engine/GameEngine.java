package com.arkumbra.brickout.engine;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.arkumbra.brickout.engine.level.LevelProgress;
import com.arkumbra.brickout.engine.touch.TouchEventHandler;

/**
 * Created by lukegardener on 2017/07/27.
 */

public interface GameEngine {

    void initialise(int canvasWidth, int canvasHeight);
    boolean isInitialisedYet();


    // Lock this so only activity can activate?
    void startEngine();

    void update(long msDelta);

    void draw(Canvas canvas);

    void notifyLevelProgress(LevelProgress levelProgress);

    int getScore();


    boolean onTouchEvent(MotionEvent motionEvent);
}

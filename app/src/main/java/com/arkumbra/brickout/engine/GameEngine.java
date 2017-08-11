package com.arkumbra.brickout.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.arkumbra.brickout.engine.level.LevelProgress;
import com.arkumbra.brickout.engine.touch.MotionEventHandler;
import com.arkumbra.brickout.engine.touch.TouchEventHandler;

/**
 * Created by lukegardener on 2017/07/27.
 */

public interface GameEngine extends MotionEventHandler, LevelStateHandler {

    void initialise(int canvasWidth, int canvasHeight);
    boolean isInitialisedYet();


    // Lock this so only activity can activate?
    void startEngine();

    void update(long msDelta);

    void draw(Canvas canvas);

    void notifyLevelProgress(LevelProgress levelProgress);

    int getScore();

    Context getContext();

}

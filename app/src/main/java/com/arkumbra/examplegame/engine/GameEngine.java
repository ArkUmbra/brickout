package com.arkumbra.examplegame.engine;

import android.graphics.Canvas;

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
}

package com.arkumbra.examplegame;

import android.view.MotionEvent;

import com.arkumbra.examplegame.engine.GameEngine;

/**
 * Created by lukegardener on 2017/07/27.
 */

public class TouchEventHandler {

    private GameEngine gameEngine;

    public TouchEventHandler(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        // TODO
//        gameEngine.doThing
        return true;
    }
}

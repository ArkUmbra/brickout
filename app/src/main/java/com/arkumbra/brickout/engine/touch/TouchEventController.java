package com.arkumbra.brickout.engine.touch;

import android.view.MotionEvent;

import com.arkumbra.brickout.engine.GameEngine;
import com.arkumbra.brickout.engine.touch.TouchEventType;

/**
 * Created by lukegardener on 2017/07/27.
 */

public class TouchEventController {

    private GameEngine gameEngine;

    public TouchEventController(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

//    public boolean onTouchEvent(MotionEvent motionEvent) {
//        float xPress = motionEvent.getRawX();
//        float yPress = motionEvent.getRawY();
//        TouchEventType touchEventType = convertTouchEventType(motionEvent);
//
//        return gameEngine.touch(xPress, yPress, touchEventType);
//    }
//
//    private TouchEventType convertTouchEventType(MotionEvent motionEvent) {
//        int eventAction = motionEvent.getAction();
//
//        switch (eventAction) {
//            case MotionEvent.ACTION_UP:
//                return TouchEventType.MOVE_BAT_KEY_UP;
//            default:
//                return TouchEventType.MOVE_BAT_KEY_DOWN;
//        }
//
//    }
}

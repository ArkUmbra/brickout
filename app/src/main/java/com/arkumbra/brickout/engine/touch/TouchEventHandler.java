package com.arkumbra.brickout.engine.touch;

/**
 * Created by lukegardener on 2017/07/30.
 */

public interface TouchEventHandler {

    boolean touch(float x, float y, TouchEventType touchEventType);
}

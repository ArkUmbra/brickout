package com.arkumbra.brickout.engine.collision;

/**
 * Created by lukegardener on 2017/08/09.
 */

public enum Direction {

    LEFT,
    RIGHT,
    NONE;

    public boolean isLeft() {
        return this == LEFT;
    }

    public boolean isRight() {
        return this == RIGHT;
    }

    public boolean isNone() {
        return this == NONE;
    }
}

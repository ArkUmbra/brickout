package com.arkumbra.brickout.engine.collision;

/**
 * Created by lukegardener on 2017/08/05.
 */

public enum Axis {
    X,
    Y;

    public boolean isX() {
        return this == Axis.X;
    }

    public boolean isY() {
        return this == Axis.Y;
    }
}

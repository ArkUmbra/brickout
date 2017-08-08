package com.arkumbra.brickout.engine.entity;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by lukegardener on 2017/07/29.
 */

public interface Brick extends CollisionBox {

    boolean isDestroyed();
    boolean isBreakable();
    boolean hit(int damage);
    int getColour();
    Bitmap getBitmap();
    UnitSize getUnitSize();
    Position getTopLeftPosition();
}

package com.arkumbra.brickout.view.ui;

import com.arkumbra.brickout.engine.collision.AABB;

/**
 * Created by lukegardener on 2017/08/10.
 */

public class ClickDetector {

    public static boolean isTouchPointOverBoundingBox(AABB aabb, float touchX, float touchY) {
        if (touchX < aabb.getMinX() || touchX > aabb.getMaxX()) return false;
        if (touchY < aabb.getMinY() || touchY > aabb.getMaxY()) return false;

        return true;
    }
}

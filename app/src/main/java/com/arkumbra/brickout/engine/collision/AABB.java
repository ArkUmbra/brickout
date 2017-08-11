package com.arkumbra.brickout.engine.collision;

import com.arkumbra.brickout.engine.entity.Position;
import com.arkumbra.brickout.engine.entity.UnitSize;

/**
 * Axis-aligned Bounding Box
 *
 * Created by lukegardener on 2017/08/04.
 */
public class AABB {
    private Position min; // topLeft
    private Position max; // bottomRight

    public AABB(Position min, Position max) {
        this.min = min;
        this.max = max;
    }

    public float getMinX() {
        return min.getX();
    }

    public float getMinY() {
        return min.getY();
    }

    public float getMaxX() {
        return max.getX();
    }

    public float getMaxY() {
        return max.getY();
    }

    public float getXMidpoint() {
        return min.getX() + ((max.getX() - min.getX()) / 2f);
    }

    public float getYMidpoint() {
        return min.getY() + ((max.getY() - min.getY()) / 2f);
    }
}

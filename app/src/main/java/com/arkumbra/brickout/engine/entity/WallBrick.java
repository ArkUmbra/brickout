package com.arkumbra.brickout.engine.entity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import com.arkumbra.brickout.engine.collision.AABB;

/**
 * Created by lukegardener on 2017/07/29.
 */

public class WallBrick implements Brick {

    @Override
    public boolean isDestroyed() {
        return false;
    }

    @Override
    public boolean isBreakable() {
        return false;
    }

    @Override
    public boolean hit(int damage) {
        return false;
    }

    @Override
    public int getColour() {
        return Color.DKGRAY;
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public UnitSize getUnitSize() {
        return null;
    }

    @Override
    public Position getTopLeftPosition() {
        return null;
    }

    @Override
    public AABB getLastUpdatesAABB() {
        return null;
    }

    @Override
    public AABB getAABB() {
        return null;
    }
}

package com.arkumbra.brickout.engine.entity;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.arkumbra.brickout.engine.collision.AABB;

/**
 * Created by lukegardener on 2017/07/29.
 */

public class StandardBrick implements Brick {

    private UnitSize unitSize = new UnitSize(2, 1);
    private BrickType brickType;
    private Position topLeftPositionInUnits;
    private AABB axisAlignedBoundingBox;
    private boolean isDestroyed;
    private int hp;

    public StandardBrick(BrickType brickType, Position topLeftPositionInUnits) {
        this.brickType = brickType;
        this.topLeftPositionInUnits = topLeftPositionInUnits;
        this.axisAlignedBoundingBox = createBoundingBox();
        this.hp = brickType.getHitsToBreak();
    }

    private AABB createBoundingBox() {
        return new AABB(topLeftPositionInUnits,
                new Position(topLeftPositionInUnits.getX() + unitSize.getWidthInUnits(),
                        topLeftPositionInUnits.getY() + unitSize.getHeightInUnits()));
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    // TODO actually required?
    @Override
    public boolean isBreakable() {
        return true;
    }

    @Override
    public boolean hit(int damage) {
        this.hp -= damage;

        if (this.hp <= 0) {
            this.isDestroyed = true;
        }

        this.brickType = BrickType.getByHits(this.hp);
        return isDestroyed;
    }

    @Override
    public int getColour() {
        return brickType.getBrickCanvasColour();
    }

    @Override
    public Bitmap getBitmap() {
        return brickType.getBitmap();
    }

    @Override
    public UnitSize getUnitSize() {
        return unitSize;
    }

    @Override
    public Position getTopLeftPosition() {
        return topLeftPositionInUnits;
    }

    @Override
    public AABB getLastUpdatesAABB() {
        // Bricks don't move, so keep the same
        return axisAlignedBoundingBox;
    }

    @Override
    public AABB getAABB() {
        return axisAlignedBoundingBox;
    }
}

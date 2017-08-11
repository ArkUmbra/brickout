package com.arkumbra.brickout.engine.entity;

import android.graphics.Color;
import android.util.Log;

import com.arkumbra.brickout.engine.collision.AABB;
import com.arkumbra.brickout.engine.collision.Direction;

/**
 * Created by lukegardener on 2017/07/29.
 */
public class PlayerBat implements GameEntity, CollisionBox{
    public static final float MAX_MOVE_UNIT_AMOUNT_PER_SECOND = 14f;

    private static final String LOG_TAG = "PlayerBat";
    private static final UnitSize size = new UnitSize(4.2f, 0.8f); // TODO can do power to make bat wider?

    private final Position pos;

    private AABB aabb;
    private AABB lastAabb;

    private int colour = Color.WHITE;
    private float nextXPosToMoveTowards = -1.0f;
    private float velocityThisTick;
    private boolean moveOnUpdate;

    private float canvasWidthPixels;

    public PlayerBat(Position pos, float canvasWidthPixels) {
        this.pos = pos;
        this.canvasWidthPixels = canvasWidthPixels;

        updateAlisAlignedBoundingBox();
    }

    @Override
    public void update(long msSinceLastUpdate) {
        if (! moveOnUpdate) {
            this.velocityThisTick = 0f;
            return; // Nothing to do
        }

        float distanceToTarget = distanceToTarget(nextXPosToMoveTowards);

        float maxUnitsToMoveInThisTick = ((float)msSinceLastUpdate/1000f) * MAX_MOVE_UNIT_AMOUNT_PER_SECOND;
        if (maxUnitsToMoveInThisTick > distanceToTarget) {
            maxUnitsToMoveInThisTick = distanceToTarget;
        }

        if (! targetToTheRightOfCurrentPosition(nextXPosToMoveTowards)) {
            maxUnitsToMoveInThisTick *= -1;
        }

        this.pos.addX(maxUnitsToMoveInThisTick);
        this.velocityThisTick = maxUnitsToMoveInThisTick;

        updateAlisAlignedBoundingBox();
    }

    private void updateAlisAlignedBoundingBox() {
        this.lastAabb = this.aabb;

        Position min = pos;
        Position max = new Position(pos.getX() + size.getWidthInUnits(),
                                    pos.getY() + size.getHeightInUnits());
        aabb = new AABB(min, max);
    }

    private boolean targetToTheRightOfCurrentPosition(float nextXPosToMoveTowards) {
        return (nextXPosToMoveTowards - this.pos.getX() > 0);
    }

    public Position getTopLeftPosition() {
        return pos;
    }

    public int getColour() {
        return colour;
    }

    public UnitSize getSize() {
        return size;
    }


    public void stopMovingToDestination() {
        this.moveOnUpdate = false;
    }

    public void proceedToDestination(float xUnitPosition) {
        Log.d(LOG_TAG, "tap pos " + xUnitPosition);
        Log.d(LOG_TAG, "canvas unit width " + canvasWidthPixels);

        if (xUnitPosition >= canvasWidthPixels / 2f) {
            this.nextXPosToMoveTowards = canvasWidthPixels;
        } else {
            this.nextXPosToMoveTowards = 0;
        }

//        if (touchPositionIsOverPlayerBat(xUnitPosition))
//            return;
//
        this.nextXPosToMoveTowards = calculateIntendedPositionByAccountingForBatSize(nextXPosToMoveTowards);

//        this.nextXPosToMoveTowards = xUnitPosition;
        this.moveOnUpdate = true;
    }

    private float calculateIntendedPositionByAccountingForBatSize(float xUnitPosition) {
        if (targetToTheRightOfCurrentPosition(xUnitPosition)) {
            return xUnitPosition - (this.getSize().getWidthInUnits());
        }

        return xUnitPosition;
    }

    private boolean touchPositionIsOverPlayerBat(float targetUnitPosition) {
        return (targetUnitPosition >= pos.getX() &&
                targetUnitPosition <= pos.getX() + size.getWidthInUnits());
    }

    private float distanceToTarget(float targetUnitPosition) {
        return Math.abs(targetUnitPosition - this.pos.getX());
    }


    @Override
    public AABB getLastUpdatesAABB() {
        return aabb;
    }

    @Override
    public AABB getAABB() {
        return aabb;
    }

    public float getVelocityThisTick() {
        return velocityThisTick;
    }

    public Direction getDirectionOfBatMovement() {
        if (this.velocityThisTick == 0) {
            return Direction.NONE;
        } else if (targetToTheRightOfCurrentPosition(this.nextXPosToMoveTowards)) {
            return Direction.RIGHT;
        } else {
            return Direction.LEFT;
        }
    }
}

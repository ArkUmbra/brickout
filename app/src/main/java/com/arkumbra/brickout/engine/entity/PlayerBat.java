package com.arkumbra.brickout.engine.entity;

import android.graphics.Color;

import com.arkumbra.brickout.engine.collision.AABB;

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

    public PlayerBat(Position pos) {
        this.pos = pos;

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
        if (touchPositionIsOverPlayerBat(xUnitPosition))
            return;

        xUnitPosition = calculateIntendedPositionByAccountingForBatSize(xUnitPosition);

        this.nextXPosToMoveTowards = xUnitPosition;
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
}

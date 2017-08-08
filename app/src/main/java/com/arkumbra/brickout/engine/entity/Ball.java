package com.arkumbra.brickout.engine.entity;

import android.graphics.Color;

import com.arkumbra.brickout.engine.collision.AABB;
import com.arkumbra.brickout.engine.collision.Axis;

/**
 * Created by lukegardener on 2017/08/01.
 */

public class Ball implements GameEntity, CollisionBox {

    private static final float UNIT_PER_SECOND_MOVE_SPEED = 10f;

    // TODO use actual angles...

    private float xSpeed = 8f;
    private float ySpeed = -8f; // Start as negative, because coordinate system has 0,0 at top-left

    private int colour = Color.MAGENTA;
    private Position centrePointPosition;
    private float radius = 0.5f;

    // Used so we can calculate bounds... temporary
    private UnitSize levelDimensions;
    private AABB lastAabb;
    private AABB aabb;

    private boolean launched;
    private boolean inDeadBallZone;

    public Ball(Position startPosition, UnitSize levelDimensions) {
        this.centrePointPosition = startPosition;
        this.levelDimensions = levelDimensions;

        updateAxisAlignedBoundingBox();
    }

    private void updateAxisAlignedBoundingBox() {
        this.lastAabb = this.aabb;

        Position min = new Position(centrePointPosition.getX() - radius,
                                    centrePointPosition.getY() - radius);
        Position max = new Position(centrePointPosition.getX() + radius,
                                    centrePointPosition.getY() + radius);
        this.aabb = new AABB(min, max);
    }

    @Override
    public void update(long msSinceLastUpdate) {
        if (! launched)
            return;

        if (isBallOffBottomOfScreen()) {
            inDeadBallZone = true;
            return;
        }

        float xUnitsToMoveInThisUpdate = ((float)msSinceLastUpdate / 1000f) * xSpeed;
        float yUnitsToMoveInThisUpdate = ((float)msSinceLastUpdate / 1000f) * ySpeed;

        ifBallIsOffNonBottomSideOfScreenThenCorrectDirection();

        this.centrePointPosition.addX(xUnitsToMoveInThisUpdate);
        this.centrePointPosition.addY(yUnitsToMoveInThisUpdate);

        ifBallIsOffNonBottomSideOfScreenThenCorrectDirection();

        updateAxisAlignedBoundingBox();

        if (isBallOffBottomOfScreen()) {
            inDeadBallZone = true;
            return;
        }
    }

    // TODO move to level, make some AABBs around outside of level to bounce off
    private void ifBallIsOffNonBottomSideOfScreenThenCorrectDirection() {
        float x = centrePointPosition.getX();
        float y = centrePointPosition.getY();


        if (x - radius <= 0) {
            xSpeed = forcePositive(xSpeed);
        } else if (x + radius >= levelDimensions.getWidthInUnits()) {
            xSpeed = forceNegative(xSpeed);
        }

        if (y - radius <= 0) {
            ySpeed = forcePositive(ySpeed);
        }
    }

    private boolean isBallOffBottomOfScreen() {
        float y = centrePointPosition.getY();

        return (y + radius >= levelDimensions.getHeightInUnits());
    }

    private float forceNegative(float num) {
        return -(Math.abs(num));
    }

    private float forcePositive(float num) {
        return Math.abs(num);
    }


    public void launch() {
        this.launched = true;
    }

    public boolean hasLaunched() {
        return launched;
    }

    public Position getCentrePointPosition() {
        return centrePointPosition;
    }

    public float getRadius() {
        return radius;
    }

    public int getColour() {
        return colour;
    }

    @Override
    public AABB getLastUpdatesAABB() {
        return lastAabb;
    }

    @Override
    public AABB getAABB() {
        return aabb;
    }

    public void bounce(Axis bounceAxis) {
        if (bounceAxis == Axis.X) {
            this.xSpeed *= -1;
        } else {
            this.ySpeed *= -1;
        }
    }

    public boolean isInDeadBallZone() {
        return inDeadBallZone;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }
}

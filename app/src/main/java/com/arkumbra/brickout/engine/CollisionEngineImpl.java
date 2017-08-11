package com.arkumbra.brickout.engine;

import android.util.Log;

import com.arkumbra.brickout.engine.collision.AABB;
import com.arkumbra.brickout.engine.collision.Axis;
import com.arkumbra.brickout.engine.collision.Direction;
import com.arkumbra.brickout.engine.entity.Ball;
import com.arkumbra.brickout.engine.entity.CollisionBox;
import com.arkumbra.brickout.engine.entity.PlayerBat;
import com.arkumbra.brickout.engine.entity.Position;

/**
 * Created by lukegardener on 2017/08/04.
 */

public class CollisionEngineImpl implements CollisionEngine {
    private static final String LOG_TAG = "CollisionEngine";

    private static final float REBOUND_OFFSET = 0.05f;


    @Override
    public boolean isOverlapping(AABB a, AABB b) {
        if (a.getMaxX() < b.getMinX() || a.getMinX() > b.getMaxX()) return false;
        if (a.getMaxY() < b.getMinY() || a.getMinY() > b.getMaxY()) return false;

        return true;
    }


    // TODO Will this automatically work for non-square bounding boxes? Think so, but probably
    // TODO will break if the ball ever changes size.
    @Override
    public Axis givenOverlapWhichAxisShouldBallBounceOn(AABB brick, AABB ball) {
        float xOverlap = calculateXOverlap(brick, ball);
        float yOverlap = calculateYOverlap(brick, ball);

        // Bounce on axis opposite to most overlap
        return (xOverlap > yOverlap) ? Axis.Y : Axis.X;
    }

    private float calculateXOverlap(AABB brick, AABB ball) {
        float overlap = 0.0f;
        if (isWithin(ball.getMinX(), brick.getMinX(), brick.getMaxX())) {
            overlap = brick.getMaxX() - ball.getMinX();
        } else if (isWithin(ball.getMaxX(), brick.getMinX(), brick.getMaxX())) {
            overlap = ball.getMaxX() - brick.getMinX();
        }
        return overlap;
    }

    private float calculateYOverlap(AABB brick, AABB ball) {
        float overlap = 0.0f;
        if (isWithin(ball.getMinY(), brick.getMinY(), brick.getMaxY())) {
            overlap = brick.getMaxY() - ball.getMinY();
        } else if (isWithin(ball.getMaxY(), brick.getMinY(), brick.getMaxY())) {
            overlap = ball.getMaxY() - brick.getMinY();
        }
        return overlap;
    }


    private boolean isWithin(float insideCheckPoint, float minCheck, float maxCheck) {
        return (insideCheckPoint > minCheck && insideCheckPoint < maxCheck);
    }


    // FIXME still not perfect. Really do need to do two frames worth of data to get more
    // FIXME accurate comparison, but that's for another day
    @Override
    public void moveBallSlightlySoNoLongerCollides(Ball ball, AABB collidingEntity, Axis bounceAxis) {
        Position ballPos = ball.getCentrePointPosition();


        // TODO FIX

        if (bounceAxis.isX()) {
            // Ball to the right -- any of the ball is outside
            if (ballPos.getX() + ball.getRadius() > collidingEntity.getMaxX()) {
                ballPos.setX(collidingEntity.getMaxX() + ball.getRadius() + REBOUND_OFFSET);
            // Ball to the left
            } else {
                ballPos.setX(collidingEntity.getMinX() - ball.getRadius() - REBOUND_OFFSET);
            }

        } else {
            // Ball above
            if (ballPos.getY() - ball.getRadius() < collidingEntity.getMinY()) {
                ballPos.setY(collidingEntity.getMinY() - ball.getRadius() - REBOUND_OFFSET);
            // Ball below
            } else {
                ballPos.setY(collidingEntity.getMaxY() + ball.getRadius() + REBOUND_OFFSET);
            }
        }
    }

    private static final double MAX_DEFLECT_ANGLE_DEGREES = 88d;
    private double degreesPerRadian = 180 / Math.PI;

    @Override
    public void alterSpeedAndAngleOfBallBasedOnSpeedBatHitsBall(Ball ball, PlayerBat playerBat) {
//
        // TODO is this faster to do via tan (toh), then sub 180 - 90 - theta?
        // cosh = adj / hyp


        // FIXME Always at max speed, though?????? Either should accelerate, or calculate angle
        // FIXME based on initial ball angle or...
//        float fractionOfMaxSpeed = Math.abs(playerBat.getVelocityThisTick() /
//                    PlayerBat.MAX_MOVE_UNIT_AMOUNT_PER_SECOND);
//

        // bounce the ball, then adjust the angle slightly

        ball.bounce(Axis.Y);

        Direction batDirection = playerBat.getDirectionOfBatMovement();
        if (batDirection.isNone()) {
            return;
        } else if (batDirection.isLeft()) {
            ball.appendDeflectionAngleDueToBatWithinCertainRange(18);
        } else {
            ball.appendDeflectionAngleDueToBatWithinCertainRange(-18);
        }


//        if (batMovingLeft(playerBat) && ballMovingLeft(ball)) {
//            angleDegreesToAdd = 20;
//        } else if (batMovingLeft(playerBat) && !ballMovingLeft(ball)) {
//            angleDegreesToAdd = 20;
//        } else if (!batMovingLeft(playerBat) && ballMovingLeft(ball)) {
//            angleDegreesToAdd
//        } else if (!batMovingLeft(playerBat) && !ballMovingLeft(ball)) {
//
//        } else {
//            Log.d(LOG_TAG, "This should not occur. Resulting angle unchanged");
//        }
    }

//    private boolean ballMovingLeft(Ball ball) {
//
//    }

}

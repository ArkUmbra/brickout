package com.arkumbra.brickout.engine.entity;

import android.graphics.Color;
import android.util.Log;

import com.arkumbra.brickout.engine.collision.AABB;
import com.arkumbra.brickout.engine.collision.Axis;

/**
 * Created by lukegardener on 2017/08/01.
 */

public class Ball implements GameEntity, CollisionBox {

    private static final String LOG_TAG = "Ball";
    private static final double degreesPerRadian = 180 / Math.PI;
    private static final double RADIANS_PER_DEGREE = Math.PI / 180;

    public static final double RAD_0_DEG = 0;
    public static final double RAD_90_DEG = 90 * RADIANS_PER_DEGREE;
    public static final double RAD_180_DEG = 180 * RADIANS_PER_DEGREE;
    public static final double RAD_270_DEG = 270 * RADIANS_PER_DEGREE;
    public static final double RAD_360_DEG = 360 * RADIANS_PER_DEGREE;

    private static final double RAD_MAX_RIGHT_DEFLECT_ANGLE_FROM_BAT = 60 * RADIANS_PER_DEGREE;
    private static final double RAD_MAX_LEFT_DEFLECT_ANGLE_FROM_BAT = (60 + 180) * RADIANS_PER_DEGREE;

    private static final float UNIT_PER_SECOND_MOVE_SPEED = 11f;

    private double angle = RAD_90_DEG / 2;

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

    // slow?
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


        ifBallIsOffNonBottomSideOfScreenThenFixAngle();
        float distancePerThisTick = ((float)msSinceLastUpdate / 1000f) * UNIT_PER_SECOND_MOVE_SPEED;
        updateToNewPositionBasedOnSpeedAndAngle(angle, distancePerThisTick);
//        ifBallIsOffNonBottomSideOfScreenThenFixAngle();


        updateAxisAlignedBoundingBox();

        if (isBallOffBottomOfScreen()) {
            inDeadBallZone = true;
            return;
        }
    }

    private void updateToNewPositionBasedOnSpeedAndAngle(double angleRadians, float distance) {
        double xAdd = 0f;
        double yAdd = 0f;

        System.out.println("Distance to travel directly " + distance);
        // Take degrees off the real angle so we can just use the same trig functions for each
        // Only difference is whether we add or remove that amount
        // Having 4 conditions here may (or may not) make it too slow
        if (angleRadians >= RAD_0_DEG && angleRadians < RAD_90_DEG){
            xAdd += calculateX(angleRadians, distance);
            yAdd -= calculateY(angleRadians, distance);

        } else if (angleRadians >= RAD_90_DEG && angleRadians < RAD_180_DEG) {
            xAdd += calculateX(angleRadians - RAD_90_DEG, distance);
            yAdd += calculateY(angleRadians - RAD_90_DEG, distance);

        } else if (angleRadians >= RAD_180_DEG && angleRadians < RAD_270_DEG) {
            xAdd -= calculateX(angleRadians - RAD_180_DEG, distance);
            yAdd += calculateY(angleRadians - RAD_180_DEG, distance);

        } else if (angleRadians >= RAD_270_DEG && angleRadians <= RAD_360_DEG) {
            xAdd -= calculateX(angleRadians - RAD_270_DEG, distance);
            yAdd -= calculateY(angleRadians - RAD_270_DEG, distance);
        }

        System.out.println("Adding x pos " + xAdd);
        System.out.println("Adding y pos " + yAdd);

        centrePointPosition.addX((float)xAdd);
        centrePointPosition.addY((float)yAdd);
    }

    private double calculateX(double angleRadians, float hypotenuse) {
        return Math.sin(angleRadians) * hypotenuse;
    }

    private double calculateY(double angleRadians, float hypotenuse) {
        return Math.cos(angleRadians) * hypotenuse;
    }

    // TODO move to level, make some AABBs around outside of level to bounce off
    private void ifBallIsOffNonBottomSideOfScreenThenFixAngle() {
        float x = centrePointPosition.getX();
        float y = centrePointPosition.getY();

        // If off either of 3 sides of screen AND travelling that direction, then just reverse angle
        if (x - radius <= 0 &&  angleBetween(RAD_180_DEG, RAD_360_DEG)) {
            System.out.println("Branch 1 " + angle/RADIANS_PER_DEGREE);
            bounce(Axis.X);

        } else if (x + radius >= levelDimensions.getWidthInUnits()
                && angleBetween(RAD_0_DEG, RAD_180_DEG)) {
            System.out.println("Branch 2 " + angle/RADIANS_PER_DEGREE);
            bounce(Axis.X);

        } else if (y - radius <= 0
                && (angleBetween(RAD_0_DEG, RAD_90_DEG) || angleBetween(RAD_270_DEG, RAD_360_DEG))) {
            System.out.println("Branch 3 " + angle/RADIANS_PER_DEGREE);
            bounce(Axis.Y);
        }
    }

    private boolean angleBetween(double min, double max) {
        return (this.angle >= min && this.angle <= max);
    }

    private boolean isBallOffBottomOfScreen() {
        float y = centrePointPosition.getY();

        return (y + radius >= levelDimensions.getHeightInUnits());
    }

    public void launch(Position positionToLaunchTowards) {
        float opposite = positionToLaunchTowards.getX() - centrePointPosition.getX();
        float adjacent = -(positionToLaunchTowards.getY() - centrePointPosition.getY());

//        float opposite = centrePointPosition.getX() - positionToLaunchTowards.getX();
//        float adjacent = centrePointPosition.getY() - positionToLaunchTowards.getY();


//        double angleInRads = Math.tanh(opposite / adjacent);
//
//        Log.d(LOG_TAG, "Opp " + opposite + ", adj " + adjacent + ", angle " + angleInRads / RADIANS_PER_DEGREE);
//
////        this.angle = angleInRads;
////        setNewAngle(angleInRads);
//        setAngleToDeflectConsideringMinMaxBounds(angleInRads);
//
//        Log.d(LOG_TAG, "Angle is now" + this.angle / RADIANS_PER_DEGREE);

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

    public void bounce(Axis axis) {
        double angleToAdd = RAD_0_DEG;

        if (angle >= RAD_0_DEG && angle < RAD_90_DEG){
            angleToAdd = (axis.isX()) ? -RAD_90_DEG : RAD_90_DEG;

        } else if (angle >= RAD_90_DEG && angle < RAD_180_DEG) {
            angleToAdd = (axis.isX()) ? RAD_90_DEG : -RAD_90_DEG;

        } else if (angle >= RAD_180_DEG && angle < RAD_270_DEG) {
            angleToAdd = (axis.isX()) ? -RAD_90_DEG : RAD_90_DEG;

        } else if (angle >= RAD_270_DEG && angle <= RAD_360_DEG) {
            angleToAdd = (axis.isX()) ? RAD_90_DEG : -RAD_90_DEG;

        }

        addToAngle(angleToAdd);
    }

    private void addToAngle(double angleToAddInRads) {
        setNewAngle(this.angle + angleToAddInRads);

        System.out.println("Angle is now " + this.angle/RADIANS_PER_DEGREE);
    }

    private void setNewAngle(double newAngleInRads) {
        this.angle = newAngleInRads;

        if (angle > RAD_360_DEG) {
            this.angle -= RAD_360_DEG;
        } else if (angle < RAD_0_DEG) {
            this.angle += RAD_360_DEG;
        }
    }

    /**
     * Ball can only bounce off the bat at certain angles. For example, if it bounced totally
     * horizontally then the bounce would never come back 'down'. Not fun to play!
     * @param
     */
    public void appendDeflectionAngleDueToBatWithinCertainRange(double angleToAppend) {
        // Only allow 80~ degrees either side of vertical

        double angleToAppendRads = angleToAppend * RADIANS_PER_DEGREE;
        addToAngle(angleToAppendRads);

        setAngleToDeflectConsideringMinMaxBounds(angleToAppendRads);
    }

    private void setAngleToDeflectConsideringMinMaxBounds(double angleInRads) {
        if (this.angleBetween(RAD_MAX_RIGHT_DEFLECT_ANGLE_FROM_BAT, RAD_180_DEG)) {
            setNewAngle(RAD_MAX_RIGHT_DEFLECT_ANGLE_FROM_BAT - 0.005);

        } else if(this.angleBetween(RAD_180_DEG, RAD_MAX_LEFT_DEFLECT_ANGLE_FROM_BAT)) {
            setNewAngle(RAD_MAX_LEFT_DEFLECT_ANGLE_FROM_BAT + 0.005);
        }
    }

    public void moveForwardAtCurrentAngle(float unitsToMoveForward) {
        updateToNewPositionBasedOnSpeedAndAngle(angle, unitsToMoveForward);
    }

    public boolean isInDeadBallZone() {
        return inDeadBallZone;
    }

    public double getAngle() {
        return angle;
    }
}

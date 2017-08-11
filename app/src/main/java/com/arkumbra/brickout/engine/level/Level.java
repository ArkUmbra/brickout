package com.arkumbra.brickout.engine.level;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.arkumbra.brickout.engine.CollisionEngine;
import com.arkumbra.brickout.engine.CollisionEngineImpl;
import com.arkumbra.brickout.engine.GameEngine;
import com.arkumbra.brickout.engine.collision.Axis;
import com.arkumbra.brickout.engine.entity.Ball;
import com.arkumbra.brickout.engine.entity.GameDrawable;
import com.arkumbra.brickout.engine.entity.Brick;
import com.arkumbra.brickout.engine.entity.GameEntity;
import com.arkumbra.brickout.engine.entity.PlayerBat;
import com.arkumbra.brickout.engine.entity.Position;
import com.arkumbra.brickout.engine.entity.UnitRadius;
import com.arkumbra.brickout.engine.entity.UnitSize;
import com.arkumbra.brickout.engine.touch.TouchEventHandler;
import com.arkumbra.brickout.engine.touch.TouchEventType;
import com.arkumbra.brickout.view.SurfaceDimensions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukegardener on 2017/07/29.
 */

public class Level implements GameDrawable, GameEntity, TouchEventHandler {
    private static final String LOG_TAG = "Level";

    private static final Paint paint = new Paint();
    private float LEVEL_X_DIMENSION_UNITS;
    private float LEVEL_Y_DIMENSION_UNITS;

    private float UNIT_WIDTH_PIXELS;
    private float UNIT_HEIGHT_PIXELS;

    private GameEngine gameEngine;
    private CollisionEngine collisionEngine = new CollisionEngineImpl();

    private List<Brick> bricks;
    private List<Brick> destructableBricks;

    private PlayerBat playerBat;
    private Ball ball;
    private int score = 0;


    public Level(GameEngine gameEngine,
                 float levelWidthUnits, float levelHeightUnits, List<Brick> bricks,
                 SurfaceDimensions surfaceDimensions) {

        this.gameEngine = gameEngine;

        this.LEVEL_X_DIMENSION_UNITS = levelWidthUnits;
        this.LEVEL_Y_DIMENSION_UNITS = levelHeightUnits;
        this.bricks = bricks;
        this.destructableBricks = filterDestructableBricks(bricks);

        UNIT_WIDTH_PIXELS = surfaceDimensions.getWidthPixels() / LEVEL_X_DIMENSION_UNITS;
        UNIT_HEIGHT_PIXELS = surfaceDimensions.getHeightPixels() / LEVEL_Y_DIMENSION_UNITS;

        Position batStartPosition = new Position(LEVEL_X_DIMENSION_UNITS / 2, LEVEL_Y_DIMENSION_UNITS - 5);
        playerBat = new PlayerBat(batStartPosition, LEVEL_X_DIMENSION_UNITS);

        // Put ball above bat
        Position ballStartPosition = batStartPosition.getNewPositionWithOffset(2, -1);
        ball = new Ball(ballStartPosition, new UnitSize(LEVEL_X_DIMENSION_UNITS, LEVEL_Y_DIMENSION_UNITS));
    }

    private List<Brick> filterDestructableBricks(List<Brick> allBricks) {
        List<Brick> destructableBricks = new ArrayList<Brick>();

        for (Brick brick: allBricks) {
            if (brick.isBreakable()) {
                destructableBricks.add(brick);
            }
        }

        return destructableBricks;
    }


    @Override
    public void draw(Canvas canvas) {
        // Bricks ---------
        for (Brick brick: bricks) {
            Position pos = brick.getTopLeftPosition();
            UnitSize size = brick.getUnitSize();
            paint.setColor(brick.getColour());

//            canvas.drawRect((pos.getX() - 1) * UNIT_WIDTH_PIXELS,
//                    (pos.getY() - 1) * UNIT_HEIGHT_PIXELS,
//                        ((pos.getX() -1) + size.getWidthInUnits()) * UNIT_WIDTH_PIXELS - 6,
//                        ((pos.getY() -1) + size.getHeightInUnits()) * UNIT_HEIGHT_PIXELS - 6,
//                        paint
//                    );

            canvas.drawRect((pos.getX()) * UNIT_WIDTH_PIXELS,
                    (pos.getY()) * UNIT_HEIGHT_PIXELS,
                    ((pos.getX()) + size.getWidthInUnits()) * UNIT_WIDTH_PIXELS - 6,
                    ((pos.getY()) + size.getHeightInUnits()) * UNIT_HEIGHT_PIXELS - 6,
                    paint
            );
        }

        // Bat ---------------
        Position pos = playerBat.getTopLeftPosition();
        UnitSize size = playerBat.getSize();
        paint.setColor(playerBat.getColour());

        canvas.drawRect(pos.getX() * UNIT_WIDTH_PIXELS,
                pos.getY() * UNIT_HEIGHT_PIXELS,
                (pos.getX() + size.getWidthInUnits()) * UNIT_WIDTH_PIXELS - 2,
                (pos.getY() + size.getHeightInUnits()) * UNIT_HEIGHT_PIXELS - 2,
                paint
        );

        // Ball -----------------
        pos = ball.getCentrePointPosition();
        paint.setColor(ball.getColour());

        canvas.drawCircle(pos.getX() * UNIT_WIDTH_PIXELS,
                            pos.getY() * UNIT_HEIGHT_PIXELS,
                ball.getRadius() * UNIT_WIDTH_PIXELS, paint);
    }

    @Override
    public void update(long msSinceLastUpdate) {
        if (ball.isInDeadBallZone()) {
            gameEngine.notifyLevelProgress(LevelProgress.FAILED);
            return;
        }

        if (destructableBricks.isEmpty()) {
            gameEngine.notifyLevelProgress(LevelProgress.CLEARED);
            return;
        }

        // Do collision detection
        doCollisionChecks();

        ball.update(msSinceLastUpdate);
        playerBat.update(msSinceLastUpdate);
    }

    private void doCollisionChecks() {
        checkBatCollisionAgainstBall();
        checkBrickCollisionsAgainstBall();
    }

    private void checkBrickCollisionsAgainstBall() {
        List<Brick> destroyedBricks = new ArrayList<>();

        for (Brick brick : bricks) {
            if (collisionEngine.isOverlapping(brick.getAABB(), ball.getAABB())) {

                if (brick.isBreakable()) {
                    this.score = score + 1;

                    if (brick.hit(1)) {
                        destroyedBricks.add(brick);
                    }
                }

                // TODO FIXME not always bouncing at right angle
                Axis bounceAxis = collisionEngine.givenOverlapWhichAxisShouldBallBounceOn(brick.getAABB(), ball.getAABB());
                ball.bounce(bounceAxis);

                // Nudge position away so that ball is not overlapping any more
                collisionEngine.moveBallSlightlySoNoLongerCollides(ball, brick.getAABB(), bounceAxis);

                // Quit ball collision detection after one bounce...
                break;
            }
        }

        bricks.removeAll(destroyedBricks);
        destructableBricks.removeAll(destroyedBricks);
    }

    private void checkBatCollisionAgainstBall() {
        if (collisionEngine.isOverlapping(playerBat.getAABB(), ball.getAABB())) {
            Axis bounceAxis = collisionEngine.
                    givenOverlapWhichAxisShouldBallBounceOn(playerBat.getAABB(), ball.getAABB());
//            ball.bounce(bounceAxis);

            collisionEngine.alterSpeedAndAngleOfBallBasedOnSpeedBatHitsBall(ball, playerBat);

            // Nudge position away so that ball is not overlapping any more
            collisionEngine.moveBallSlightlySoNoLongerCollides(ball, playerBat.getAABB(), bounceAxis);
        }
    }

    @Override
    public boolean touch(float xPixel, float yPixel, TouchEventType touchEventType) {
        Log.d(LOG_TAG, "Touching at x " + xPixel);

        if (! ball.hasLaunched()) {
            Position positionToLaunchTowards = new Position(xPixel / UNIT_WIDTH_PIXELS,
                    yPixel / UNIT_HEIGHT_PIXELS);

            ball.launch(positionToLaunchTowards);
            return true;
        }

        switch (touchEventType) {
            case MOVE_BAT_KEY_DOWN:
                playerBat.proceedToDestination(xPixel / UNIT_WIDTH_PIXELS);
                break;
            case MOVE_BAT_KEY_UP:
                playerBat.stopMovingToDestination();
                break;
        }

        return true;
    }

    public int getScore() {
        return score;
    }
}

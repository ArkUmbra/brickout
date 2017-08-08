package com.arkumbra.brickout.engine;

import com.arkumbra.brickout.engine.collision.AABB;
import com.arkumbra.brickout.engine.collision.Axis;
import com.arkumbra.brickout.engine.entity.Ball;
import com.arkumbra.brickout.engine.entity.CollisionBox;
import com.arkumbra.brickout.engine.entity.PlayerBat;

/**
 * Created by lukegardener on 2017/08/04.
 */

public interface CollisionEngine {

    boolean isOverlapping(AABB aabb1, AABB aabb2);
    Axis givenOverlapWhichAxisShouldBallBounceOn(AABB brick, AABB ball);
    void moveBallSlightlySoNoLongerCollides(Ball ball, AABB collidingEntity, Axis bounceAxis);
    void alterSpeedAndAngleOfBallBasedOnSpeedBatHitsBall(Ball ball, PlayerBat playerBat);

}

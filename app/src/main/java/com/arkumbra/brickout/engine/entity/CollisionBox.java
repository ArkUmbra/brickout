package com.arkumbra.brickout.engine.entity;

import com.arkumbra.brickout.engine.collision.AABB;

/**
 * Created by lukegardener on 2017/08/04.
 */

public interface CollisionBox {

    AABB getLastUpdatesAABB();
    AABB getAABB();
}

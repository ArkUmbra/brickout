package com.arkumbra.examplegame.entity;

/**
 * Created by lukegardener on 2017/07/28.
 */

public interface GameEntity {

    void update(long msSinceLastUpdate);

    // add something to store last position so can track difference between this tick and last tick for hit detection
}
